package com.vasnatech.mando.command;

import com.vasnatech.mando.model.FileNode;
import com.vasnatech.mando.model.Session;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Path;

@ShellComponent
public class EnvironmentCommands extends AbstractCommands {

    public EnvironmentCommands(Session session, Path workspaceDirectory) {
        super(session, workspaceDirectory);
    }

    @ShellMethod(group = "Environment", key = {"environment list", "environment ls", "env list", "env ls"}, prefix = "-")
    public AttributedString environmentList(
            @ShellOption(value = {"t", "tree"}) boolean tree
    ) {
        return execute(() -> {
            FileNode root = loadTree();
            return tree ? treeOfTree(root, "%.env") : listOfTree(root, "%.env");
        });
    }
}
