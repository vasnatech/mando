package com.vasnatech.mando.config;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;

@Configuration
public class VersionConfig {

    @Bean
    public BuildProperties buildProperties() {
        Properties properties = new Properties();
        properties.putAll(
                Map.of(
                        "group", "com.vasnatech",
                        "artifact", "mando",
                        "name", "mando",
                        "version", "0.1"
                )
        );
        return new BuildProperties(properties);
    }
}
