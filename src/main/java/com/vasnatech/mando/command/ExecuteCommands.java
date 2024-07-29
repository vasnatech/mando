package com.vasnatech.mando.command;

import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.service.FileSystemService;
import com.vasnatech.mando.service.FormatService;
import org.jline.reader.Parser;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.FileInputProvider;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

@ShellComponent
public class ExecuteCommands extends AbstractCommands {

    private final FileSystemService fileSystemService;
    private final ObjectProvider<Shell> shellProvider;
    private final Parser parser;

    public ExecuteCommands(
            Session session,
            FormatService formatService,
            FileSystemService fileSystemService,
            ObjectProvider<Shell> shellProvider,
            Parser parser
    ) {
        super(session, formatService);
        this.fileSystemService = fileSystemService;
        this.shellProvider = shellProvider;
        this.parser = parser;
    }

    @ShellMethod(group = "Script", key = "exe", prefix = "-")
    public void execute(String relativePath) throws Exception {
        Path path = fileSystemService.childFile(relativePath);
        BufferedReader reader = Files.newBufferedReader(path);
        shellProvider.getObject().run(new FileInputProvider(reader, parser));
    }
}
