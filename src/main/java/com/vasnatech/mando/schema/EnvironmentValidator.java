package com.vasnatech.mando.schema;

import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.ValidationInfo;

import java.util.List;

public class EnvironmentValidator implements SchemaValidator<Environment> {

    @Override
    public List<ValidationInfo> validate(Environment schemas) {
        return List.of();
    }
}
