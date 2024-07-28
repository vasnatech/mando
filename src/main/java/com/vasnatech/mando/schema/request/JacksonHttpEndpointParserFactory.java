package com.vasnatech.mando.schema.request;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonHttpEndpointParserFactory extends HttpEndpointParserFactory {

    @Override
    public HttpEndpointParser create(String version) {
        return create(new JsonFactory());
    }

    public HttpEndpointParser create(JsonFactory jsonFactory) {
        return new JacksonHttpEndpointParser(jsonFactory);
    }
}
