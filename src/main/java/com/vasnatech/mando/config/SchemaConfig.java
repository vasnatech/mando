package com.vasnatech.mando.config;

import com.vasnatech.commons.schema.Module;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.commons.schema.load.SchemaLoaderFactory;
import com.vasnatech.mando.schema.environment.Environment;
import com.vasnatech.mando.schema.environment.EnvironmentValidatorFactory;
import com.vasnatech.mando.schema.environment.JacksonEnvironmentParserFactory;
import com.vasnatech.mando.schema.request.JacksonHttpEndpointParserFactory;
import com.vasnatech.mando.schema.request.HttpEndpoint;
import com.vasnatech.mando.schema.request.HttpEndpointValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SchemaConfig {

    @Bean
    public SchemaLoader schemaLoader() {
        Modules.add(
                Module.of(
                        "mando-environment",
                        Environment.class,
                        Map.of(SupportedMediaTypes.JSON, new JacksonEnvironmentParserFactory()),
                        new EnvironmentValidatorFactory()
                )
        );
        Modules.add(
                Module.of(
                        "mando-http-endpoint",
                        HttpEndpoint.class,
                        Map.of(SupportedMediaTypes.JSON, new JacksonHttpEndpointParserFactory()),
                        new HttpEndpointValidatorFactory()
                )
        );

        SchemaLoaderFactory schemaLoaderFactory = SchemaLoaderFactories.get(SupportedMediaTypes.JSON);
        return schemaLoaderFactory.create(Map.of(
                "normalize", false,
                "validate", false
        ));
    }
}
