package com.vasnatech.mando.config;

import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.mando.schema.environment.Environment;
import com.vasnatech.mando.model.GlobalVariables;
import com.vasnatech.mando.model.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Configuration
public class SessionConfig {

    public SessionConfig() {}

    @Bean
    public Session session(GlobalVariables globalVariables, Path workspaceDirectory, SchemaLoader schemaLoader) throws IOException {
        Session session = new Session(globalVariables, workspaceDirectory);
        session.scope().put("username", globalVariables.username());
        session.scope().put("email", globalVariables.email());

        return loadWorkspaceEnvironments(session, schemaLoader);
    }

    Session loadWorkspaceEnvironments(Session session, SchemaLoader schemaLoader) throws IOException {
        Path workspaceDirectory = Path.of(session.globalVariables().workspace());
        List<Path> environmentFiles = Files.list(workspaceDirectory)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".env"))
                .toList();
        for (Path environmentFile : environmentFiles) {
            Environment environment = schemaLoader.load(Files.newBufferedReader(environmentFile));
            environment.variables().forEach(session.scope()::put);
        }
        return session;
    }
}
