package com.vasnatech.mando.model;

import java.nio.file.Path;

public class Project {

    final String name;
    final Path path;

    public Project(Path path) {
        this.name = path.getFileName().toString();
        this.path = path;
    }

    public String name() {
        return name;
    }

    public Path path() {
        return path;
    }
}
