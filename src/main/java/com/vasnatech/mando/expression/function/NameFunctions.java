package com.vasnatech.mando.expression.function;

import com.vasnatech.commons.type.Name;

import java.util.Locale;

public interface NameFunctions {

    static Name fromSnakeCase(String str) {
        return fromSnakeCaseForLocale(str, Locale.ENGLISH);
    }

    static Name fromSnakeCaseForLocale(String str, Locale locale) {
        if (str == null)
            return null;
        if (locale == null)
            locale = Locale.ENGLISH;
        return Name.fromSnakeCase(str, locale);
    }

    static String toSnakeCase(Name name) {
        return toSnakeCaseForLocale(name, Locale.ENGLISH);
    }

    static String toSnakeCaseForLocale(Name name, Locale locale) {
        return name == null ? null : name.toSnakeCase(locale);
    }

    static Name fromKebabCase(String str) {
        return fromKebabCaseForLocale(str, Locale.ENGLISH);
    }

    static Name fromKebabCaseForLocale(String str, Locale locale) {
        if (str == null)
            return null;
        if (locale == null)
            locale = Locale.ENGLISH;
        return Name.fromKebabCase(str, locale);
    }

    static String toKebabCase(Name name) {
        return toKebabCaseForLocale(name, Locale.ENGLISH);
    }

    static String toKebabCaseForLocale(Name name, Locale locale) {
        return name == null ? null : name.toKebabCase(locale);
    }

    static Name fromPascalCase(String str) {
        return fromPascalCaseForLocale(str, Locale.ENGLISH);
    }

    static Name fromPascalCaseForLocale(String str, Locale locale) {
        if (str == null)
            return null;
        if (locale == null)
            locale = Locale.ENGLISH;
        return Name.fromPascalCase(str, locale);
    }

    static String toPascalCase(Name name) {
        return toPascalCaseForLocale(name, Locale.ENGLISH);
    }

    static String toPascalCaseForLocale(Name name, Locale locale) {
        return name == null ? null : name.toPascalCase(locale);
    }

    static Name fromCamelCase(String str) {
        return fromCamelCaseForLocale(str, Locale.ENGLISH);
    }

    static Name fromCamelCaseForLocale(String str, Locale locale) {
        if (str == null)
            return null;
        if (locale == null)
            locale = Locale.ENGLISH;
        return Name.fromCamelCase(str, locale);
    }

    static String toCamelCase(Name name) {
        return toCamelCaseForLocale(name, Locale.ENGLISH);
    }

    static String toCamelCaseForLocale(Name name, Locale locale) {
        return name == null ? null : name.toCamelCase(locale);
    }
}
