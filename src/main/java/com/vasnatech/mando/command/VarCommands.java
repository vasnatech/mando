package com.vasnatech.mando.command;

import com.vasnatech.mando.expression.ExpressionResolver;
import com.vasnatech.mando.model.Session;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
public class VarCommands extends AbstractCommands {

    final ExpressionResolver expressionResolver;

    public VarCommands(Session session, Path workspaceDirectory, ExpressionResolver expressionResolver) {
        super(session, workspaceDirectory);
        this.expressionResolver = expressionResolver;
    }

    @ShellMethod(group = "Var", key = {"var list", "var ls"}, prefix = "-")
    public AttributedString varList(
            @ShellOption(value = {"l"}) boolean eachLine,
            @ShellOption(value = {"t", "type"}) boolean type,
            @ShellOption(value = {"n", "name"}, defaultValue = ShellOption.NULL) String name,
            @ShellOption(value = {"f", "filter"}, defaultValue = ShellOption.NULL) String filter
    ) {
        return execute(() -> allVariables().entrySet().stream()
                .filter(entry -> filter == null || entry.getKey().matches(filter.replace("%", ".*")))
                .map(entry -> formatVariable(type, entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(eachLine ? "\n" : ", "))
        );
    }

    @ShellMethod(group = "Var", key = {"var"}, prefix = "-")
    public AttributedString var(
            @ShellOption(value = {"n", "name"}) String name,
            @ShellOption(value = {"v", "value"}, defaultValue = ShellOption.NULL) String expression,
            @ShellOption(value = {"t", "type"}) boolean type,
            @ShellOption(value = {"d", "delete"}) boolean delete
    ) {
        return execute(() -> {
            if (delete) {
                Object value = session.remove(name);
                return "Variable deleted. " + formatVariable(type, name, value);
            } else if (expression == null ) {
                return Optional.ofNullable(session().get(name))
                        .map(val -> formatVariable(type, name, val))
                        .orElse("No variable named " + name);
            } else {
                Object value = expressionResolver.resolve(expression.replace('`', '\''));
                session().put(name, value);
                return formatVariable(type, name, value);
            }
        });
    }


    String formatVariable(boolean type, String name, Object value) {
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

    String formatValue(Object value) {
        if (value == null) {
            return "<null>";
        }
        if (value instanceof Collection<?> collection) {
            return formatValue(collection);
        }
        if (value instanceof Map<?, ?> map) {
            return formatValue(map);
        }
        String stringValue = value.toString();
        return (value instanceof CharSequence) ? "`" + stringValue + "`" : stringValue;
    }

    String formatValue(Collection<?> collection) {
        return collection.stream().map(this::formatValue).collect(Collectors.joining(", ", "[", "]"));
    }

    String formatValue(Map<?, ?> map) {
        return map.entrySet().stream().map(entry -> entry.getKey() + " = " + formatValue(entry.getValue())).collect(Collectors.joining(", ", "{", "}"));
    }

    Map<String, Object> allVariables() {
        return session().flattenAsMap();
    }
}
