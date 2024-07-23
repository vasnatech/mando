package com.vasnatech.mando.schema;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonEnvironmentParserFactory extends EnvironmentParserFactory {

    @Override
    public EnvironmentParser create(String version) {
        return create(new JsonFactory());
    }

    public EnvironmentParser create(JsonFactory jsonFactory) {
        return new JacksonEnvironmentParser(jsonFactory);
    }
}
