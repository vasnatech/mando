package com.vasnatech.mando.expression.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class Functions {
    
    private Functions() {
    }

    private static final Map<String, Method> METHODS = new HashMap<>(22);

    static {
        addMethods(StringFunctions.class);
        addMethods(NameFunctions.class);
        addMethods(DateTimeFunctions.class);
        addMethods(CollectionFunctions.class);
    }

    public static void addMethod(String name, Method method) {
        METHODS.put(name, method);
    }

    public static void addMethods(Class<?> type) {
        Stream.of(type.getDeclaredMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .forEach(method -> addMethod(method.getName(), method));
    }
    public static Map<String, Method> methods() {
        return METHODS;
    }
}
