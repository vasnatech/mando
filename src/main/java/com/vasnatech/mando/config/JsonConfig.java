package com.vasnatech.mando.config;

import com.vasnatech.commons.json.Json;
import com.vasnatech.commons.json.jackson.Jackson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {

    @Bean
    public Json json() {
        Jackson.init();
        return Json.getJson();
    }
}
