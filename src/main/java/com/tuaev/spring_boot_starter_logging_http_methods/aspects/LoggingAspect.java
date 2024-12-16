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
        if (Level.INFO == propertiesConfiguration.getLvl()){
            logger.info("Вызван метод {} у класса {}: ", joinPoint.getSignature().getName(), joinPoint.getTarget());
        }
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.BeforeLoggingMethod * *(..))")
    public void beforeLogging(JoinPoint joinPoint){
        if (Level.INFO == propertiesConfiguration.getLvl()){
            logger.info("Метод {} у класса {} закончил свою работу: ", joinPoint.getSignature().getName(), joinPoint.getTarget());
        }
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.FullLoggingMethod * *(..))")
    public Object fullLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o;
        try {
            if (Level.INFO == propertiesConfiguration.getLvl()) {
                logger.info("Вызван метод {} у класса {}: ", joinPoint.getSignature().getName(),
                        joinPoint.getTarget());
            }
            o = joinPoint.proceed();
        } catch (Throwable e) {
            if (Level.INFO == propertiesConfiguration.getLvl()) {
                logger.info("Метод {} у класса {} выбросил исключение: {}", joinPoint.getSignature().getName(),
                        joinPoint.getTarget(), e.getClass());
            }
            throw e;
        } finally {
            if (Level.INFO == propertiesConfiguration.getLvl()) {
                logger.info("Метод {} у класса {} закончил свою работу", joinPoint.getSignature().getName(),
                        joinPoint.getTarget());
            }
        }
        return o;
    }

    @AfterReturning(pointcut = "@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.ResultHandlerMethod * *(..))", returning = "o")
    public void resultHandler(JoinPoint joinPoint, Object o){
        if (Level.INFO == propertiesConfiguration.getLvl()){
            logger.info("Метод {} у класса {} вернул следующий результат: {}", joinPoint.getSignature().getName(),
                    joinPoint.getTarget(), o);
        }
    }

    @AfterThrowing(pointcut = "@within(org.springframework.web.bind.annotation.RestController) && execution(@com.tuaev.spring_boot_starter_logging_http_methods.annotations.ExceptionHandlerMethod * *(..))", throwing = "throwable")
    public void exceptionHandler(JoinPoint joinPoint, Throwable throwable) throws Throwable {
        if (Level.INFO == propertiesConfiguration.getLvl()) {
            logger.info("Вызвано исключение {} у класса {} метода {}", throwable.getClass().getSimpleName(),
                    joinPoint.getTarget(), joinPoint.getSignature().getName());
        }
    }
}
