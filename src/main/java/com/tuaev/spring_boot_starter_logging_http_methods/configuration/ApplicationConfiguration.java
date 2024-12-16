package com.tuaev.spring_boot_starter_logging_http_methods.configuration;

import com.tuaev.spring_boot_starter_logging_http_methods.aspects.LoggingAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableConfigurationProperties(PropertiesConfiguration.class)
@EnableAspectJAutoProxy
public class ApplicationConfiguration {

    private final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Bean
    @ConditionalOnProperty(value = "logging.status", havingValue = "true")
    public LoggingAspect loggingAspect(PropertiesConfiguration propertiesConfiguration) {
        return new LoggingAspect(propertiesConfiguration);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PropertiesConfiguration propertiesConfiguration) {
        return args -> {
            if (!propertiesConfiguration.isStatus()) {
                logger.warn("Логирование отключено! Для включения логирования укажите свойство: logging.status=true");
            } else {
                logger.warn("""
                        Логирование включено!\s
                        Для отключения логирования укажите свойство: logging.status=false\s
                        Установлен уровень логирования: {}""", propertiesConfiguration.getLvl());
            }
        };
    }
}
