package com.vasnatech.mando.schema;

import com.vasnatech.commons.schema.schema.AbstractSchema;
import com.vasnatech.commons.type.VariableContainer;

import java.util.LinkedHashMap;
import java.util.Map;

public class Environment extends AbstractSchema implements VariableContainer {

    final LinkedHashMap<String, Object> variables;
    final LinkedHashMap<String, Object> headers;

    public Environment(LinkedHashMap<String, String> meta, String name, LinkedHashMap<String, Object> variables, LinkedHashMap<String, Object> headers) {
        super("name", "environment", meta);
        this.variables = variables;
        this.headers = headers;
    }

    public Map<String, Object> variables() {
        return variables;
    }

    public Map<String, Object> headers() {
        return headers;
    }

    @Override
    public boolean containsKey(String name) {
        return variables.containsKey(name);
    }

    @Override
    public Object get(String name) {
        return variables.get(name);
    }

    @Override
    public Object put(String name, Object value) {
        throw new UnsupportedOperationException("Environments are read only.");
    }

    @Override
    public Object remove(String name) {
        throw new UnsupportedOperationException("Environments are read only.");
    }

    @Override
    public Map<String, Object> flattenAsMap() {
        return Map.copyOf(variables);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        String name;
        LinkedHashMap<String, Object> variables = new LinkedHashMap<>();
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();

        public Builder meta(String metaKey, String metaValue) {
            this.meta.put(metaKey, metaValue);
            return this;
        }

        public Builder meta(Map<String, String> meta) {
            this.meta.putAll(meta);
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder variable(String name, Object value) {
            this.variables.put(name, value);
            return this;
        }

        public Builder variables(Map<String, Object> variables) {
            this.variables.putAll(variables);
            return this;
        }

        public Builder header(String name, Object value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Environment build() {
            return new Environment(meta, name, variables, headers);
        }
    }
}
