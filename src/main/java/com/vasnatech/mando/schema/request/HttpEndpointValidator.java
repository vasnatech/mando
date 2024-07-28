package com.vasnatech.mando.schema.request;

import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.ValidationInfo;

import java.util.List;

public class HttpEndpointValidator implements SchemaValidator<HttpEndpoint> {

    @Override
    public List<ValidationInfo> validate(HttpEndpoint schemas) {
        return List.of();
    }
}
