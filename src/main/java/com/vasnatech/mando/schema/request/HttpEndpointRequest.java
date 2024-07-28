package com.vasnatech.mando.schema.request;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HttpEndpointRequest implements Serializable {

    final HttpMethod method;
    final String url;
    final LinkedHashMap<String, String> headers;
    final HttpEndpointBody body;

    public HttpEndpointRequest(HttpMethod method, String url, LinkedHashMap<String, String> headers, HttpEndpointBody body) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod method() {
        return method;
    }

    public String url() {
        return url;
    }

    public LinkedHashMap<String, String> headers() {
        return headers;
    }

    public HttpEndpointBody body() {
        return body;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        HttpMethod method = HttpMethod.GET;
        String url = null;
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        HttpEndpointBody body;

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder headers(LinkedHashMap<String, String> requestHeaders) {
            this.headers.putAll(requestHeaders);
            return this;
        }

        public Builder body(HttpEndpointBody body) {
            this.body = body;
            return this;
        }

        public HttpEndpointRequest build() {
            return new HttpEndpointRequest(method, url, headers, body);
        }
    }
}
