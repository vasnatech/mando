package com.vasnatech.mando.schema.request;

import java.io.Serializable;

public class HttpEndpointBody implements Serializable {

    final String variable;
    final String mimeType;
    final ValueType valueType;
    final boolean isArray;

    public HttpEndpointBody(
            String variable,
            String mimeType,
            ValueType valueType,
            boolean isArray
    ) {
        this.variable = variable;
        this.mimeType = mimeType;
        this.valueType = valueType;
        this.isArray = isArray;
    }

    public String variable() {
        return variable;
    }

    public String mimeType() {
        return mimeType;
    }

    public ValueType valueType() {
        return valueType;
    }

    public boolean isArray() {
        return isArray;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String variable;
        String mimeType;
        ValueType valueType;
        boolean isArray;

        public Builder variable(String variable) {
            this.variable = variable;
            return this;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder valueType(ValueType valueType) {
            this.valueType = valueType;
            return this;
        }

        public Builder isArray(boolean isArray) {
            this.isArray = isArray;
            return this;
        }

        public HttpEndpointBody build() {
            return new HttpEndpointBody(
                    variable,
                    mimeType,
                    valueType,
                    isArray
            );
        }
    }
}
