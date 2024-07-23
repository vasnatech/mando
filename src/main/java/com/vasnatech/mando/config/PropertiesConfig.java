package com.vasnatech.mando.config;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Bean
    public JavaPropsMapper javaPropsMapper() {
        return new JavaPropsMapper();
    }
}
