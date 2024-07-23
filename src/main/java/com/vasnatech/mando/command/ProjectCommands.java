package com.vasnatech.mando.command;

import com.vasnatech.mando.model.Project;
import com.vasnatech.mando.model.Session;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ShellComponent
public class ProjectCommands extends AbstractCommands {

    final Path projectsDirectory;

    public ProjectCommands(Session session, Path workspaceDirectory, Path projectsDirectory) {
        super(session, workspaceDirectory);
        this.projectsDirectory = projectsDirectory;
    }

    @ShellMethod(group = "Project", key = {"project list", "project ls", "prj list", "prj ls"}, prefix = "-")
    public AttributedString projectList(
            @ShellOption(value = {"l"}) boolean eachLine
    ) {
        return execute(() -> projectNames().collect(Collectors.joining(eachLine ? "\n" : ", ")));
    }

    @ShellMethod(group = "Project", key = {"project", "prj"}, prefix = "-")
    public AttributedString project(
            @ShellOption(value = {"name", "n"}, defaultValue = ShellOption.NULL) String projectName
    ) {
        return execute(() -> {
                if (projectName == null) {
                    return session.currentProject() == null
                            ? "No current project"
                            : "Current project: " + session.currentProject().name();
                }
                return loadProject(projectName)
                        .map(project -> {
                            session.currentProject(project);
                            return "Current project: " + projectName;
                        })
                        .orElse("Unable to find project: " + projectName);
        });
    }

    @ShellMethod(group = "Project", key = {"project environment", "project env", "prj environment", "prj env"}, prefix = "-")
    public AttributedString environmentList(
            @ShellOption(value = {"l"}) boolean eachLine
    ) {
        return execute(() -> {
            if (session.currentProject() == null) {
                return "No current project";
            }
            return execute(() -> environmentNames(session.currentProject()).collect(Collectors.joining(eachLine ? "\n" : ", ")));
        });
    }

    Stream<Path> projectPaths() throws IOException {
        return Files.list(projectsDirectory)
                .filter(Files::isDirectory);
    }

    Stream<String> projectNames() throws IOException {
        return projectPaths()
                .map(Path::getFileName)
                .map(Path::toString);
    }

    Optional<Project> loadProject(String name) throws IOException {
        return projectPaths()
                .filter(path -> path.getFileName().toString().equals(name))
                .findFirst()
                .map(Project::new);
    }

    Stream<String> environmentNames(Project project) throws IOException {
        return environmentPaths(project)
                .map(Path::getFileName)
                .map(Path::toString);
    }

    Stream<Path> environmentPaths(Project project) throws IOException {
        Path environmentsDirectory = project.path().resolve("environments");
        return Files.list(environmentsDirectory)
                .filter(Files::isRegularFile);
    }
}
