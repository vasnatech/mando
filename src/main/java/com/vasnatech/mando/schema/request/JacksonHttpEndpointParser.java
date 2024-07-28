package com.vasnatech.mando.schema.request;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.mando.schema.JacksonParser;

import java.io.IOException;
import java.util.Map;

public class JacksonHttpEndpointParser extends JacksonParser<HttpEndpoint> implements HttpEndpointParser {

    public JacksonHttpEndpointParser(JsonFactory jsonFactory) {
        super(jsonFactory);
    }

    protected HttpEndpoint parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            HttpEndpoint.Builder requestBuilder = HttpEndpoint.builder();
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("meta".equals(fieldName)) {
                    parseObjectAsStringMap(parser, requestBuilder::meta);
                } else if ("request".equals(fieldName)) {
                    requestBuilder.request(parseRequest(parser));
                } else if ("responses".equals(fieldName)) {
                    parseResponses(parser, requestBuilder);
                }
                parser.nextToken();
            }
            return requestBuilder.build();
        }
        return null;
    }

    @Override
    public HttpEndpoint continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        HttpEndpoint.Builder endpointBuilder = HttpEndpoint.builder();
        endpointBuilder.meta(meta);
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            if ("request".equals(fieldName)) {
                endpointBuilder.request(parseRequest(parser));
            } else if ("responses".equals(fieldName)) {
                parseResponses(parser, endpointBuilder);
            }
            parser.nextToken();
        }
        return endpointBuilder.build();
    }

    private HttpEndpointRequest parseRequest(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            HttpEndpointRequest.Builder requestBuilder = HttpEndpointRequest.builder();
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("method".equals(fieldName)) {
                    requestBuilder.method(HttpMethod.valueOf(parser.nextTextValue()));
                } else if ("url".equals(fieldName)) {
                    requestBuilder.url(parser.nextTextValue());
                } else if ("headers".equals(fieldName)) {
                    parseObjectAsStringMap(parser, requestBuilder::header);
                } else if ("body".equals(fieldName)) {
                    requestBuilder.body(parseBody(parser));
                }
                parser.nextToken();
            }
            return requestBuilder.build();
        }
        return null;
    }

    private void parseResponses(JsonParser parser, HttpEndpoint.Builder endpointBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String status = parser.currentName();
                HttpEndpointResponse.Builder responseBuilder = HttpEndpointResponse.builder();
                responseBuilder.status(status);
                parseResponse(parser, responseBuilder);
                endpointBuilder.response(status, responseBuilder.build());

                parser.nextToken();
            }
        }
    }

    private void parseResponse(JsonParser parser, HttpEndpointResponse.Builder responseBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("headers".equals(fieldName)) {
                    parseObjectAsStringMap(parser, responseBuilder::header);
                } else if ("body".equals(fieldName)) {
                    responseBuilder.body(parseBody(parser));
                }
                parser.nextToken();
            }
        }
    }

    private HttpEndpointBody parseBody(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            HttpEndpointBody.Builder bodyBuilder = HttpEndpointBody.builder();
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("variable".equals(fieldName)) {
                    bodyBuilder.variable(parser.nextTextValue());
                } else if ("mimeType".equals(fieldName)) {
                    bodyBuilder.mimeType(parser.nextTextValue());
                } else if ("valueType".equals(fieldName)) {
                    bodyBuilder.valueType(ValueType.valueOf(parser.nextTextValue()));
                } else if ("array".equals(fieldName)) {
                    bodyBuilder.isArray(parser.nextBooleanValue());
                }
                parser.nextToken();
            }
            return bodyBuilder.build();
        }
        return null;
    }
}
