package com.vasnatech.mando.schema.request;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HttpEndpointResponse implements Serializable {

    final String status;
    final LinkedHashMap<String, String> headers;
    final HttpEndpointBody body;

    public HttpEndpointResponse(String status, LinkedHashMap<String, String> headers, HttpEndpointBody body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public String status() {
        return status;
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
        String status;
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        HttpEndpointBody body;

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder headers(LinkedHashMap<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder body(HttpEndpointBody body) {
            this.body = body;
            return this;
        }

        public HttpEndpointResponse build() {
            return new HttpEndpointResponse(status, headers, body);
        }
    }
}
