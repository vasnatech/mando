package com.vasnatech.mando.config;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.vasnatech.mando.model.GlobalVariables;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Configuration
public class ApplicationConfig {

    private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

    @Bean
    public Path userHomeDirectory() {
        return Path.of(System.getProperty("user.home"));
    }

    @Bean
    public Path mandoDirectory(Path userHomeDirectory) throws IOException {
        Path mandoDirectory = userHomeDirectory.resolve(".mando");
        if (!Files.exists(mandoDirectory)) {
            log.info("Creating mando directory. Path: {}", mandoDirectory);
            Files.createDirectories(mandoDirectory);
            log.info("Created mando directory. Path: {}", mandoDirectory);
        }
        return mandoDirectory;
    }

    @Bean
    public Path configFile(Path mandoDirectory, JavaPropsMapper javaPropsMapper) throws IOException {
        Path configFile = mandoDirectory.resolve("config");
        if (!Files.exists(configFile)) {
            log.info("Unable to find config file. Path: {}", configFile);
            log.info("Creating config file with defaults. Path: {}", configFile);
            Files.createFile(configFile);
            BufferedWriter writer = Files.newBufferedWriter(configFile);
            javaPropsMapper.writeValue(writer, new GlobalVariables(mandoDirectory.resolve("workspace").toString(), "user", "user@domain", false));
            log.info("Created config file with defaults. Update file and restart. Path: {}", configFile);

            throw new IllegalStateException("Update config file and restart. Path: " + configFile);
        }
        return configFile;
    }

    @Bean
    public GlobalVariables globalVariables(Path configFile, JavaPropsMapper javaPropsMapper) throws IOException {
        BufferedReader reader = Files.newBufferedReader(configFile);
        GlobalVariables globalVariables = javaPropsMapper.readValue(reader, GlobalVariables.class);
        return globalVariables;
    }

    @Bean
    public Path workspaceDirectory(GlobalVariables globalVariables) throws IOException {
        Path workspaceDirectory = Path.of(globalVariables.workspace());
        if (!Files.exists(workspaceDirectory)) {
            log.info("Creating workspace. Path: {}", workspaceDirectory);
            Files.createDirectories(workspaceDirectory);
            log.info("Created workspace. Path: {}", workspaceDirectory);
        }
        return workspaceDirectory;
    }

    @Bean
    public Path projectsDirectory(Path workspaceDirectory) throws IOException {
        Path projectsDirectory = workspaceDirectory.resolve("projects");
        if (!Files.exists(projectsDirectory)) {
            log.info("Creating projects directory. Path: {}", projectsDirectory);
            Files.createDirectories(projectsDirectory);
            log.info("Created projects directory. Path: {}", projectsDirectory);
        }
        return projectsDirectory;
    }
}
