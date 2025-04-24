package com.vasnatech.mando.expression.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class Functions {
    
    private Functions() {
    }

    private static final Map<String, Method> METHODS = new HashMap<>(30);
    private static final Map<String, Map<String, Method>> METHODS_GROUPED_BY = new HashMap<>(6);

    static {
        addMethods("String", StringFunctions.class);
        addMethods("Name", NameFunctions.class);
        addMethods("DateTime", DateTimeFunctions.class);
        addMethods("Collection", CollectionFunctions.class);
        addMethods("Random", RandomFunctions.class);
        addMethods("Number", NumberFunctions.class);
    }

    public static void addMethod(String group, String name, Method method) {
        METHODS.put(name, method);
        METHODS_GROUPED_BY.computeIfAbsent(group, it -> new HashMap<>()).put(name, method);
    }

    public static void addMethods(String group, Class<?> type) {
        Stream.of(type.getDeclaredMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .forEach(method -> addMethod(group, method.getName(), method));
    }

    public static Map<String, Method> methods() {
        return METHODS;
    }

    public static Map<String, Map<String, Method>> methodsGroupedBy() {
        return METHODS_GROUPED_BY;
    }
}
