package com.vasnatech.mando.config;

import com.vasnatech.commons.json.Json;
import com.vasnatech.commons.json.jackson.JsonJackson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {

    @Bean
    public Json json() {
        JsonJackson.init();
        return Json.getJson();
    }
}
