package com.vasnatech.mando.command;

import com.vasnatech.mando.model.FileNode;
import com.vasnatech.mando.model.Session;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Path;

@ShellComponent
public class DirCommands extends AbstractCommands {

    public DirCommands(Session session, Path workspaceDirectory) {
        super(session, workspaceDirectory);
    }

    @ShellMethod(group = "Dir", key = {"dir", "list", "ls"}, prefix = "-")
    public AttributedString varList(
            @ShellOption(value = {"t", "tree"}) boolean tree,
            @ShellOption(value = {"f", "filter"}, defaultValue = "%") String filter
    ) {
        return execute(() -> {
            FileNode root = loadTree();
            return tree ? treeOfTree(root, filter) : listOfTree(root, filter);
        });
    }
}
