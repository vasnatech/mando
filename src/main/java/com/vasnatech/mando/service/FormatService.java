package com.vasnatech.mando.service;

import com.vasnatech.mando.exception.WrapperException;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FormatService {

    static final AttributedStyle SUCCESS_STYLE = AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN);
    static final AttributedStyle FAIL_STYLE = AttributedStyle.DEFAULT.foreground(AttributedStyle.RED);

    public AttributedString success(Object message) {
        return success(formatValue(message));
    }

    AttributedString success(CharSequence message) {
        return message(message, SUCCESS_STYLE);
    }

    public AttributedString fail(Exception e) throws Exception {
        if (e instanceof WrapperException wrapperException) {
            return fail(wrapperException.getCause());
        }
        throw e;
    }

    public AttributedString fail(CharSequence message) {
        return message(message, FAIL_STYLE);
    }

    AttributedString message(CharSequence message, AttributedStyle style) {
        return new AttributedStringBuilder()
                .style(style)
                .append(message)
                .toAttributedString();
    }

    public String formatValue(Object value) {
        if (value == null) {
            return "<null>";
        }
        if (value instanceof Collection<?> collection) {
            return formatValue(collection);
        }
        if (value instanceof Map<?, ?> map) {
            return formatValue(map);
        }
        if (value instanceof CharSequence charSequence) {
            return formatValue(charSequence);
        }
        return value.toString();
    }

    public String formatValue(Collection<?> collection) {
        return collection.stream().map(this::formatValue).collect(Collectors.joining(", ", "[", "]"));
    }

    public String formatValue(Map<?, ?> map) {
        return map.entrySet().stream().map(entry -> entry.getKey() + " = " + formatValue(entry.getValue())).collect(Collectors.joining(", ", "{", "}"));
    }

    public String formatValue(CharSequence charSequence) {
        return charSequence.toString();
    }

    public String formatVariable(boolean type, String name, Object value) {
        return name + (type ? " : " + typeOfValue(value) : "") + " = " + formatValue(value);
    }

    String typeOfValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof List<?>) {
            return "List";
        }
        if (value instanceof Set<?>) {
            return "Set";
        }
        if (value instanceof Map<?,?>) {
            return "Map";
        }
        return value.getClass().getSimpleName();
    }
}
