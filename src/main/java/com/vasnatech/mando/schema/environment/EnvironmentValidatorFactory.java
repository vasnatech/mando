package com.vasnatech.mando.schema.environment;

import com.vasnatech.commons.schema.validate.SchemaValidatorFactory;

public class EnvironmentValidatorFactory implements SchemaValidatorFactory<Environment, EnvironmentValidator> {

    @Override
    public EnvironmentValidator create(String version) {
        return new EnvironmentValidator();
    }
}
