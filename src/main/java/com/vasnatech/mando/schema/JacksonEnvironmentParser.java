package com.vasnatech.mando.schema;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.serialize.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class JacksonEnvironmentParser implements EnvironmentParser {

    final JsonFactory jsonFactory;

    public JacksonEnvironmentParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }

    @Override
    public Environment parse(InputStream in) throws IOException {
        return parse(jsonFactory.createParser(in));
    }

    private Environment parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            Environment.Builder schemasBuilder = Environment.builder();
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("meta".equals(fieldName)) {
                    parseMeta(parser, schemasBuilder);
                } else if ("variables".equals(fieldName)) {
                    parseMeta(parser, schemasBuilder);
                } else if ("headers".equals(fieldName)) {
                    parseMeta(parser, schemasBuilder);
                }
                parser.nextToken();
            }
            return schemasBuilder.build();
        }
        return null;
    }

    private void parseMeta(JsonParser parser, Environment.Builder schemasBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                schemasBuilder.meta(parser.currentName(), parser.nextTextValue());
                parser.nextToken();
            }
        }
    }

    @Override
    public Environment continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        Environment.Builder environmentBuilder = Environment.builder();
        environmentBuilder.meta(meta);
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            if ("variables".equals(fieldName)) {
                parseObjectAsMap(parser, environmentBuilder::variable);
            } else if ("headers".equals(fieldName)) {
                parseObjectAsMap(parser, environmentBuilder::header);
            }
            parser.nextToken();
        }
        return environmentBuilder.build();
    }

    void parseObjectAsMap(JsonParser parser, BiConsumer<String, Object> appender) throws IOException {
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

    Object parseValue(JsonParser parser) throws IOException {
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

    Map<String, ?> parseObject(JsonParser parser) throws IOException {
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

    List<?> parseArray(JsonParser parser) throws IOException {
        List<Object> list = new ArrayList<>();
        parser.nextToken();
        while (parser.currentToken() != JsonToken.END_ARRAY) {
            list.add(parseValue(parser));
            parser.nextToken();
        }
        return list;
    }

    @Override
    public Environment normalize(Environment schema) {
        return schema;
    }
}
