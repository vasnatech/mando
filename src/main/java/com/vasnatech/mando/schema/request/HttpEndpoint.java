package com.vasnatech.mando.schema.request;

import com.vasnatech.commons.schema.schema.AbstractSchema;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpEndpoint extends AbstractSchema implements Serializable {

    final HttpEndpointRequest request;
    final LinkedHashMap<String, HttpEndpointResponse> responses;

    public HttpEndpoint(
            LinkedHashMap<String, String> meta,
            HttpEndpointRequest request,
            LinkedHashMap<String, HttpEndpointResponse> responses

    ) {
        super("name", "mando-http-endpoint", meta);
        this.request = request;
        this.responses = responses;
    }

    public HttpEndpointRequest request() {
        return request;
    }

    public LinkedHashMap<String, HttpEndpointResponse> responses() {
        return responses;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        HttpEndpointRequest request;
        LinkedHashMap<String, HttpEndpointResponse> responses = new LinkedHashMap<>();

        public Builder meta(String metaKey, String metaValue) {
            this.meta.put(metaKey, metaValue);
            return this;
        }

        public Builder meta(Map<String, String> meta) {
            this.meta.putAll(meta);
            return this;
        }

        public Builder request(HttpEndpointRequest request) {
            this.request = request;
            return this;
        }

        public Builder response(String status, HttpEndpointResponse response) {
            this.responses.put(status, response);
            return this;
        }

        public Builder responses(LinkedHashMap<String, HttpEndpointResponse> responses) {
            this.responses = responses;
            return this;
        }

        public HttpEndpoint build() {
            return new HttpEndpoint(meta, request, responses);
        }
    }
}
