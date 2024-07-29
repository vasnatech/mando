package com.vasnatech.mando.command;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.mando.model.TreeNode;
import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.service.FileSystemService;
import com.vasnatech.mando.service.FormatService;
import com.vasnatech.mando.service.TreeService;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class FileSystemCommands extends AbstractCommands {

    final FileSystemService fileSystemService;
    final TreeService treeService;

    public FileSystemCommands(
            Session session,
            FormatService formatService,
            FileSystemService fileSystemService,
            TreeService treeService
    ) {
        super(session, formatService);
        this.fileSystemService = fileSystemService;
        this.treeService = treeService;
    }

    @ShellMethod(group = "File System", key = "ls", prefix = "-")
    public AttributedString ls(
            @ShellOption(value = "l") boolean list,
            @ShellOption(value = "t") boolean tree,
            @ShellOption(value = "r") boolean recursive,
            @ShellOption(value = "f", defaultValue = "%") String filter
    ) throws Exception {
        return execute(() -> {
            TreeNode root = fileSystemService.treeOfCurrentDirectory(recursive);
            return tree ? treeService.treeOfTree(root, filter) : treeService.listOfTree(root, list, filter);
        });
    }

    @ShellMethod(group = "File System", key = "pwd", prefix = "-")
    public AttributedString pwd() throws Exception {
        return execute(() -> session.currentDirectory().toString());
    }

    @ShellMethod(group = "File System", key = "cd", prefix = "-")
    public AttributedString cd(String relativePath) throws Exception {
        return execute(() ->
            switch (relativePath) {
                case "."  -> fileSystemService.currentDirectory();
                case ".." -> fileSystemService.navigateToParentDirectory();
                default   -> fileSystemService.navigateToChildDirectory(relativePath);
            }
        );
    }

    @ShellMethod(group = "File System", key = "flush", prefix = "-")
    public AttributedString flush(String relativePath) throws Exception {
        return execute(() -> Resources.asString(fileSystemService.childFile(relativePath)));
    }
}
