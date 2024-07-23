package com.vasnatech.mando.schema;

import com.vasnatech.commons.schema.Module;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.serialize.MediaType;

import java.util.Map;

public class EnvironmentModule implements Module<Environment, EnvironmentParser, EnvironmentParserFactory, EnvironmentValidator, EnvironmentValidatorFactory> {

    private static final EnvironmentModule INSTANCE = new EnvironmentModule();
    public static EnvironmentModule instance() {
        return INSTANCE;
    }

    private final Module<Environment, EnvironmentParser, EnvironmentParserFactory, EnvironmentValidator, EnvironmentValidatorFactory> values;

    private EnvironmentModule() {
        values = Module.of(
                "mando-environment",
                Environment.class,
                Map.of(SupportedMediaTypes.JSON, new JacksonEnvironmentParserFactory()),
                new EnvironmentValidatorFactory()
        );
    }

    @Override
    public String type() {
        return values.type();
    }

    @Override
    public Class<Environment> schemaClass() {
        return values.schemaClass();
    }

    @Override
    public Map<MediaType, EnvironmentParserFactory> parserFactories() {
        return values.parserFactories();
    }

    @Override
    public EnvironmentValidatorFactory validatorFactory() {
        return values.validatorFactory();
    }
}
