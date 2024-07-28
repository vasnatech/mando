package com.vasnatech.mando.schema.environment;

import com.vasnatech.commons.schema.schema.AbstractSchema;

import java.util.LinkedHashMap;
import java.util.Map;

public class Environment extends AbstractSchema {

    final String name;
    final LinkedHashMap<String, Object> variables;

    public Environment(
            LinkedHashMap<String, String> meta,
            String name,
            LinkedHashMap<String, Object> variables
    ) {
        super(name, "mando-environment", meta);
        this.name = name;
        this.variables = variables;
    }

    public String name() {
        return name;
    }

    public Map<String, Object> variables() {
        return variables;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        String name;
        LinkedHashMap<String, Object> variables = new LinkedHashMap<>();

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

        public Environment build() {
            return new Environment(meta, name, variables);
        }
    }
}
