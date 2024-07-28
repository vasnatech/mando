package com.vasnatech.mando.schema;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.parse.SchemaParser;
import com.vasnatech.commons.schema.schema.Schema;
import com.vasnatech.commons.serialize.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class JacksonParser<S extends Schema> implements SchemaParser<S> {

    protected final JsonFactory jsonFactory;

    protected JacksonParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }

    @Override
    public S parse(InputStream in) throws IOException {
        return parse(jsonFactory.createParser(in));
    }

    protected abstract S parse(JsonParser parser) throws IOException;

    protected void parseObjectAsMap(JsonParser parser, BiConsumer<String, Object> appender) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                parser.nextToken();
                Object value = parseValue(parser);
                appender.accept(fieldName, value);
                parser.nextToken();
            }
        }
    }

    protected void parseObjectAsStringMap(JsonParser parser, BiConsumer<String, String> appender) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                String value = parser.nextTextValue();
                appender.accept(fieldName, value);
                parser.nextToken();
            }
        }
    }

    protected Object parseValue(JsonParser parser) throws IOException {
        JsonToken currentToken = parser.currentToken();
        return switch (currentToken) {
            case NOT_AVAILABLE -> null;
            case FIELD_NAME -> null;
            case VALUE_NULL -> null;
            case START_OBJECT -> parseObject(parser);
            case END_OBJECT -> null;
            case START_ARRAY -> parseArray(parser);
            case END_ARRAY -> null;
            case VALUE_EMBEDDED_OBJECT -> parser.currentValue();
            case VALUE_NUMBER_INT -> parser.getIntValue();
            case VALUE_NUMBER_FLOAT -> parser.getFloatValue();
            case VALUE_FALSE -> false;
            case VALUE_TRUE -> true;
            case VALUE_STRING -> parser.getText();
        };
    }

    protected Map<String, ?> parseObject(JsonParser parser) throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();
        parser.nextToken();
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            parser.nextToken();
            map.put(fieldName, parseValue(parser));
            parser.nextToken();
        }
        return map;
    }

    protected List<?> parseArray(JsonParser parser) throws IOException {
        List<Object> list = new ArrayList<>();
        parser.nextToken();
        while (parser.currentToken() != JsonToken.END_ARRAY) {
            list.add(parseValue(parser));
            parser.nextToken();
        }
        return list;
    }

    @Override
    public S normalize(S schema) {
        return schema;
    }
}
