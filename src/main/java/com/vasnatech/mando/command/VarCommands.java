package com.vasnatech.mando.command;

import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.mando.expression.ExpressionResolver;
import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.schema.environment.Environment;
import com.vasnatech.mando.service.FileSystemService;
import com.vasnatech.mando.service.FormatService;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
public class VarCommands extends AbstractCommands {

    final FileSystemService fileSystemService;
    final SchemaLoader schemaLoader;
    final ExpressionResolver expressionResolver;

    public VarCommands(
            Session session,
            FormatService formatService,
            FileSystemService fileSystemService,
            SchemaLoader schemaLoader,
            ExpressionResolver expressionResolver
    ) {
        super(session, formatService);
        this.fileSystemService = fileSystemService;
        this.schemaLoader = schemaLoader;
        this.expressionResolver = expressionResolver;
    }

    @ShellMethod(group = "Var", key = "var ls", prefix = "-")
    public AttributedString varList(
            @ShellOption(value = "l") boolean eachLine,
            @ShellOption(value = "c") boolean type,
            @ShellOption(value = "f", defaultValue = ShellOption.NULL) String filter
    ) throws Exception {
        return execute(() -> session.scope().flattenAsMap().entrySet().stream()
                .filter(entry -> filter == null || entry.getKey().matches(filter.replace("%", ".*")))
                .map(entry -> formatService.formatVariable(type, entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(eachLine ? "\n" : ", "))
        );
    }

    @ShellMethod(group = "Var", key = "var", prefix = "-")
    public AttributedString var(
            @ShellOption(value = "n") String name,
            @ShellOption(value = "v", defaultValue = ShellOption.NULL) String expression,
            @ShellOption(value = "c") boolean type,
            @ShellOption(value = "d") boolean delete
    ) throws Exception {
        return execute(() -> {
            if (delete) {
                Object value = session.scope().remove(name);
                return "Variable deleted. " + formatService.formatVariable(type, name, value);
            } else if (expression == null ) {
                return Optional.ofNullable(session.scope().get(name))
                        .map(val -> formatService.formatVariable(type, name, val))
                        .orElse("No variable named " + name);
            } else {
                Object value = expressionResolver.resolve(expression.replace('`', '\''));
                session.scope().put(name, value);
                return formatService.formatVariable(type, name, value);
            }
        });
    }

    @ShellMethod(group = "Var", key = "var load", prefix = "-")
    public AttributedString varLoad(String relativePath) throws Exception {
        return execute(() -> {
            Path childFile = fileSystemService.childFile(relativePath);
            Environment environment = schemaLoader.load(Files.newBufferedReader(childFile));
            environment.variables().forEach(session.scope()::put);
            return environment.variables();
        });
    }

    @ShellMethod(group = "Var", key = "eval", prefix = "-")
    public AttributedString evaluate(String expression) throws Exception {
        return execute(() -> expressionResolver.resolve(expression.replace('`', '\'')));
    }
}
