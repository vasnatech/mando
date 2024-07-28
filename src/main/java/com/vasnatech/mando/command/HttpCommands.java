package com.vasnatech.mando.command;

import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.mando.expression.ExpressionResolver;
import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.schema.request.HttpEndpoint;
import com.vasnatech.mando.service.FileSystemService;
import com.vasnatech.mando.service.FormatService;
import com.vasnatech.mando.service.HttpService;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.nio.file.Files;
import java.nio.file.Path;

@ShellComponent
public class HttpCommands extends AbstractCommands {

    final FileSystemService fileSystemService;
    final HttpService httpService;
    final SchemaLoader schemaLoader;
    final ExpressionResolver expressionResolver;

    public HttpCommands(
            Session session,
            FormatService formatService,
            FileSystemService fileSystemService,
            HttpService httpService,
            SchemaLoader schemaLoader,
            ExpressionResolver expressionResolver
    ) {
        super(session, formatService);
        this.fileSystemService = fileSystemService;
        this.schemaLoader = schemaLoader;
        this.expressionResolver = expressionResolver;
        this.httpService = httpService;
    }

    @ShellMethod(group = "Http", key = "call", prefix = "-")
    public AttributedString call(String relativePath) throws Exception {
        return execute(() -> {
            Path childFile = fileSystemService.childFile(relativePath);
            HttpEndpoint httpEndpoint = schemaLoader.load(Files.newBufferedReader(childFile));
            return httpService.doHttpCall(httpEndpoint);
        });
    }
}
