package com.vasnatech.mando.expression.function;

import java.util.function.Function;

public interface NumberFunctions {

    static Byte toByte(Object source) {
        return to(source, Number::byteValue, Byte::valueOf);
    }

    static Short toShort(Object source) {
        return to(source, Number::shortValue, Short::valueOf);
    }

    static Integer toInt(Object source) {
        return to(source, Number::intValue, Integer::valueOf);
    }

    static Long toLong(Object source) {
        return to(source, Number::longValue, Long::valueOf);
    }

    static Float toFloat(Object source) {
        return to(source, Number::floatValue, Float::valueOf);
    }

    static Double toDouble(Object source) {
        return to(source, Number::doubleValue, Double::valueOf);
    }

    private static <T> T to(Object source, Function<Number, T> fromNumber, Function<String, T> fromString) {
        if (source == null) {
            return null;
        }
        try {
            if (source instanceof Number number) {
                return fromNumber.apply(number);
            }
            if (source instanceof CharSequence charSequence) {
                return fromString.apply(charSequence.toString());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
