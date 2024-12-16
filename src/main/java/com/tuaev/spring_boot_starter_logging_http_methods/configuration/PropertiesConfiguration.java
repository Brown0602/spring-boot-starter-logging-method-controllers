package com.tuaev.spring_boot_starter_logging_http_methods.configuration;

import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logging")
public class PropertiesConfiguration {

    private boolean status;
    private Level lvl = Level.INFO;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Level getLvl() {
        return lvl;
    }

    public void setLvl(Level lvl) {
        this.lvl = lvl;
    }
}
