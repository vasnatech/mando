package com.vasnatech.mando.schema.request;

import com.vasnatech.commons.schema.validate.SchemaValidatorFactory;

public class HttpEndpointValidatorFactory implements SchemaValidatorFactory<HttpEndpoint, HttpEndpointValidator> {

    @Override
    public HttpEndpointValidator create(String version) {
        return new HttpEndpointValidator();
    }
}
