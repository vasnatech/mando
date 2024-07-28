package com.vasnatech.mando.service;

import com.vasnatech.commons.json.Json;
import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.text.ParametrizedString;
import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.schema.request.HttpEndpoint;
import com.vasnatech.mando.schema.request.HttpEndpointBody;
import com.vasnatech.mando.schema.request.HttpEndpointRequest;
import com.vasnatech.mando.schema.request.HttpEndpointResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class HttpService {

    final Session session;
    final HttpClient httpClient;
    final FormatService formatService;

    public HttpService(Session session, HttpClient httpClient, FormatService formatService) {
        this.session = session;
        this.httpClient = httpClient;
        this.formatService = formatService;
    }

    public Object doHttpCall(HttpEndpoint httpEndpoint) throws IOException, InterruptedException {
        HttpRequest httpRequest = createHttpRequest(httpEndpoint.request());
        HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        String statusCode = String.valueOf(httpResponse.statusCode());
        HttpEndpointResponse httpEndpointResponse = httpEndpoint.responses().get(statusCode);
        if (httpEndpointResponse == null) {
            httpEndpointResponse = httpEndpoint.responses().get("default");
        }
        if (httpEndpointResponse == null) {
            return Map.of(
                    "status", statusCode,
                    "body", Resources.asString(httpResponse.body())
            );
        } else {
            httpEndpointResponse.headers()
                    .forEach((variable, headerName) -> {
                        List<String> headerValues = httpResponse.headers().allValues(headerName);
                        if (headerValues.size() == 1) {
                            session.scope().put(variable, headerValues.get(0));
                        } else if (headerValues.size() > 1) {
                            session.scope().put(variable, headerValues);
                        }
                    });
            if (httpEndpointResponse.body() != null) {
                Object body = parseBody(httpResponse.body(), httpEndpointResponse.body());
                session.scope().put(httpEndpointResponse.body().variable(), body);
                return Map.of(
                        "status", statusCode,
                        "body", formatService.formatValue(body)
                );
            }
            return Map.of("status", statusCode);
        }
    }

    private HttpRequest createHttpRequest(HttpEndpointRequest httpEndpointRequest) {
        Map<String, Object> values = session.scope().flattenAsMap();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        //TODO request body
        requestBuilder.method(httpEndpointRequest.method().name(), HttpRequest.BodyPublishers.noBody());
        requestBuilder.uri(URI.create(evaluate(httpEndpointRequest.url(), values)));
        httpEndpointRequest.headers().forEach((name, value) -> requestBuilder.header(name, evaluate(value, values)));
        return requestBuilder.build();
    }

    private String evaluate(String template, Map<String, Object> values) {
        ParametrizedString parametrized = new ParametrizedString(template, "{{", "}}");
        return parametrized.toString(values);
    }

    private Object parseBody(InputStream in, HttpEndpointBody body) throws IOException {
        if ("application/json".equals(body.mimeType())) {
            return parseBodyAsJson(in, body);
        } else {
            return Resources.asString(in);
        }
    }

    private Object parseBodyAsJson(InputStream in, HttpEndpointBody body) throws IOException {
        return body.isArray()
                ? switch (body.valueType()) {
                    case OBJECT ->  Json.decoder().fromInputStream(in, List.class, Map.class);
                    case STRING ->  Json.decoder().fromInputStream(in, List.class, String.class);
                    case INTEGER -> Json.decoder().fromInputStream(in, List.class, Integer.class);
                    case DECIMAL -> Json.decoder().fromInputStream(in, List.class, Double.class);
                    case BOOLEAN -> Json.decoder().fromInputStream(in, List.class, Boolean.class);
                }
                : switch (body.valueType()) {
                    case OBJECT ->  Json.decoder().fromInputStream(in, Map.class, String.class, Object.class);
                    case STRING ->  Json.decoder().fromInputStream(in, String.class);
                    case INTEGER -> Json.decoder().fromInputStream(in, Integer.class);
                    case DECIMAL -> Json.decoder().fromInputStream(in, Double.class);
                    case BOOLEAN -> Json.decoder().fromInputStream(in, Boolean.class);
                };
    }
}
