package com.vasnatech.mando.schema.environment;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.mando.schema.JacksonParser;

import java.io.IOException;
import java.util.Map;

public class JacksonEnvironmentParser extends JacksonParser<Environment> implements EnvironmentParser {

    public JacksonEnvironmentParser(JsonFactory jsonFactory) {
        super(jsonFactory);
    }

    protected Environment parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            Environment.Builder environmentBuilder = Environment.builder();
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("meta".equals(fieldName)) {
                    parseObjectAsStringMap(parser, environmentBuilder::meta);
                } else if ("variables".equals(fieldName)) {
                    parseObjectAsMap(parser, environmentBuilder::variable);
                }
                parser.nextToken();
            }
            return environmentBuilder.build();
        }
        return null;
    }

    @Override
    public Environment continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        Environment.Builder environmentBuilder = Environment.builder();
        environmentBuilder.meta(meta);
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            if ("variables".equals(fieldName)) {
                parseObjectAsMap(parser, environmentBuilder::variable);
            }
            parser.nextToken();
        }
        return environmentBuilder.build();
    }
}
