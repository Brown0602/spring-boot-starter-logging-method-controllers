package com.tuaev.spring_boot_starter_logging_http_methods.aspects;

import com.tuaev.spring_boot_starter_logging_http_methods.configuration.PropertiesConfiguration;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

@Aspect
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final PropertiesConfiguration propertiesConfiguration;

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
        if (Level.INFO.equals(propertiesConfiguration.getLvl())){
            logger.info(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }else if (Level.TRACE.equals(propertiesConfiguration.getLvl())){
            logger.trace(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }else if (Level.ERROR.equals(propertiesConfiguration.getLvl())){
            logger.error(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }else if (Level.DEBUG.equals(propertiesConfiguration.getLvl())){
            logger.debug(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }else if (Level.WARN.equals(propertiesConfiguration.getLvl())){
            logger.warn(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }
    }

    private void loggingEndCalsMethod(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint){
        String message = "Метод {} у класса {} закончил свою работу: ";
        if (Level.INFO.equals(propertiesConfiguration.getLvl())){
            logger.info(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }else if (Level.TRACE.equals(propertiesConfiguration.getLvl())){
            logger.trace(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }else if (Level.ERROR.equals(propertiesConfiguration.getLvl())){
            logger.error(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }else if (Level.DEBUG.equals(propertiesConfiguration.getLvl())){
            logger.debug(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }else if (Level.WARN.equals(propertiesConfiguration.getLvl())){
            logger.warn(message, joinPoint.getSignature().getName(), joinPoint.getTarget());
        }
    }

    private void loggingExceptionsMethod(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint, Throwable throwable){
        String message = "Метод {} у класса {} выбросил следующее исключение {}: ";
        if (Level.INFO.equals(propertiesConfiguration.getLvl())){
            logger.info(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), throwable.getClass().getName());
        }else if (Level.TRACE.equals(propertiesConfiguration.getLvl())){
            logger.trace(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), throwable.getClass().getName());
        }else if (Level.ERROR.equals(propertiesConfiguration.getLvl())){
            logger.error(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), throwable.getClass().getName());
        }else if (Level.DEBUG.equals(propertiesConfiguration.getLvl())){
            logger.debug(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), throwable.getClass().getName());
        }else if (Level.WARN.equals(propertiesConfiguration.getLvl())){
            logger.warn(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), throwable.getClass().getName());
        }
    }

    private void loggingResultMethod(PropertiesConfiguration propertiesConfiguration, JoinPoint joinPoint, Object o){
        String message = "Метод {} у класса {} вернул следующий результат: {}";
        if (Level.INFO.equals(propertiesConfiguration.getLvl())){
            logger.info(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), o);
        }else if (Level.TRACE.equals(propertiesConfiguration.getLvl())){
            logger.trace(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), o);
        }else if (Level.ERROR.equals(propertiesConfiguration.getLvl())){
            logger.error(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), o);
        }else if (Level.DEBUG.equals(propertiesConfiguration.getLvl())){
            logger.debug(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), o);
        }else if (Level.WARN.equals(propertiesConfiguration.getLvl())){
            logger.warn(message, joinPoint.getSignature().getName(), joinPoint.getTarget(), o);
        }
    }
}
