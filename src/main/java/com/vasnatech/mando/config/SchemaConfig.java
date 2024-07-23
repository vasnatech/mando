package com.vasnatech.mando.config;

import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.commons.schema.load.SchemaLoaderFactory;
import com.vasnatech.mando.schema.EnvironmentModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SchemaConfig {

    @Bean
    public SchemaLoader schemaLoader() {
        Modules.add(EnvironmentModule.instance());

        SchemaLoaderFactory schemaLoaderFactory = SchemaLoaderFactories.get(SupportedMediaTypes.JSON);
        return schemaLoaderFactory.create(Map.of(
                "normalize", false,
                "validate", false
        ));
    }
}
