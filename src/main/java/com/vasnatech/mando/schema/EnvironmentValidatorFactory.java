package com.vasnatech.mando.schema;

import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.SchemaValidatorFactory;
import com.vasnatech.commons.schema.validate.ValidationInfo;

import java.util.List;

public class EnvironmentValidatorFactory implements SchemaValidatorFactory<Environment, EnvironmentValidator> {

    @Override
    public EnvironmentValidator create(String version) {
        return new EnvironmentValidator();
    }
}
