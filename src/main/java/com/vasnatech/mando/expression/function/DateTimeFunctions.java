package com.vasnatech.mando.expression.function;

import java.time.Instant;

public interface DateTimeFunctions {

    static Instant now() {
        return Instant.now();
    }
}
