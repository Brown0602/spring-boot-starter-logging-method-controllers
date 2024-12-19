package com.tuaev.spring_boot_starter_logging_http_methods.aspects;

import com.tuaev.spring_boot_starter_logging_http_methods.configuration.PropertiesConfiguration;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

@Aspect
public class LoggingAspect {

    private final PropertiesConfiguration propertiesConfiguration;
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    public LoggingAspect(PropertiesConfiguration propertiesConfiguration) {
        this.propertiesConfiguration = propertiesConfiguration;
    }

    @After("@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.AfterLoggingMethod * *(..))")
    public void afterLogging(JoinPoint joinPoint){
        loggingCalsMethod(propertiesConfiguration, joinPoint);
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.BeforeLoggingMethod * *(..))")
    public void beforeLogging(JoinPoint joinPoint){
        loggingEndCalsMethod(propertiesConfiguration, joinPoint);
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.FullLoggingMethod * *(..))")
    public Object fullLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o;
        try {
            loggingCalsMethod(propertiesConfiguration, joinPoint);
            o = joinPoint.proceed();
        } catch (Throwable e) {
            loggingExceptionsMethod(propertiesConfiguration, joinPoint, e);
            throw e;
        } finally {
            loggingEndCalsMethod(propertiesConfiguration, joinPoint);
        }
        return o;
    }

    @AfterReturning(pointcut = "@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.ResultHandlerMethod * *(..))", returning = "o")
    public void resultHandler(JoinPoint joinPoint, Object o){
        loggingResultMethod(propertiesConfiguration, joinPoint, o);
    }

    @AfterThrowing(pointcut = "@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.ExceptionHandlerMethod * *(..))", throwing = "throwable")
    public void exceptionHandler(JoinPoint joinPoint, Throwable throwable){
        loggingExceptionsMethod(propertiesConfiguration, joinPoint, throwable);
    }

    private void loggingCalsMethod(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint){
        String message = "Вызван метод {} у класса {}: ";
        log(propertiesConfiguration, joinPoint, message);
    }

    private void log(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint, String message) {
        LoggingEventBuilder loggingEventBuilder = logger.atLevel(propertiesConfiguration.getLvl());
        loggingEventBuilder.log(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
    }

    private void log(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint, String message, Object o){
        LoggingEventBuilder loggingEventBuilder = logger.atLevel(propertiesConfiguration.getLvl());
        loggingEventBuilder.log(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), o);
    }

    private void loggingEndCalsMethod(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint){
        String message = "Метод {} у класса {} закончил свою работу: ";
        log(propertiesConfiguration, joinPoint, message);
    }

    private void loggingExceptionsMethod(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint, Throwable throwable){
        String message = "Метод {} у класса {} выбросил следующее исключение {}: ";
        log(propertiesConfiguration, joinPoint, message, throwable.getClass());
    }

    private void loggingResultMethod(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint, Object o){
        String message = "Метод {} у класса {} вернул следующий результат: {}";
        log(propertiesConfiguration, joinPoint, message, o);
    }
}
