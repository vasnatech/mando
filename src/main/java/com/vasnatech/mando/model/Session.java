package com.vasnatech.mando.model;

import com.vasnatech.commons.type.Scope;

import java.nio.file.Path;
import java.util.Map;

public class Session {

    final GlobalVariables globalVariables;

    final Scope scope;

    Path currentDirectory;


    public Session(GlobalVariables globalVariables, Path workspaceDirectory) {
        this.globalVariables = globalVariables;
        this.scope = new Scope(Map.of());
        this.currentDirectory = workspaceDirectory;
    }

    public GlobalVariables globalVariables() {
        return globalVariables;
    }

    public Scope scope() {
        return scope;
    }

    public Path currentDirectory() {
        return currentDirectory;
    }

    public void currentDirectory(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }
}
