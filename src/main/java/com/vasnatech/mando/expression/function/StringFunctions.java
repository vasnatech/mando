package com.vasnatech.mando.expression.function;

import com.vasnatech.commons.collection.Streams;
import com.vasnatech.commons.type.Name;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface StringFunctions {

    static String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    static String toUpperCase(Object obj) {
        return toUpperCaseForLocale(obj, Locale.ENGLISH);
    }

    static String toUpperCaseForLocale(Object obj, Locale locale) {
        if (obj == null)
            return null;
        if (locale == null)
            locale = Locale.ENGLISH;
        if (obj instanceof String str)
            return str.toUpperCase(locale);
        if (obj instanceof Name name)
            return name.toUpperCase(locale);
        return obj.toString().toUpperCase(locale);
    }

    static String toLowerCase(Object obj) {
        return toLowerCaseForLocale(obj, Locale.ENGLISH);
    }

    static String toLowerCaseForLocale(Object obj, Locale locale) {
        if (obj == null)
            return null;
        if (locale == null)
            locale = Locale.ENGLISH;
        if (obj instanceof String str)
            return str.toLowerCase(locale);
        if (obj instanceof Name name)
            return name.toLowerCase(locale);
        return obj.toString().toLowerCase(locale);
    }

    static String concat(Object... objects) {
        return Stream.of(objects).map(String::valueOf).collect(Collectors.joining());
    }

    static String replace(String source, String target, String replacement) {
        if (source == null) return null;
        if (target == null || target.isEmpty()) return source;
        if (replacement == null) {
            replacement = "";
        }
        return source.replace(target, replacement);
    }

    static String join(Object obj, String deliminator, String prefix, String suffix) {
        Stream<?> stream;
        if (obj == null) {
            stream = Stream.of();
        } else if (obj instanceof Collection<?> collection) {
            stream = collection.stream();
        } else if (obj instanceof Map<?, ?> map) {
            stream = map.entrySet().stream();
        } else if (obj instanceof Iterable<?> iterable) {
            stream = Streams.from(iterable);
        } else if (obj instanceof Iterator<?> iterator) {
            stream = Streams.from(iterator);
        } else if (obj.getClass().isArray()) {
            stream = Stream.of((Object[]) obj);
        } else {
            stream = Stream.of(obj);
        }
        return stream.map(String::valueOf).collect(Collectors.joining(deliminator, prefix, suffix));
    }
}
