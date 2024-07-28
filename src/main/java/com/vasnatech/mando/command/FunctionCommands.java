package com.vasnatech.mando.command;

import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.service.FormatService;
import com.vasnatech.mando.service.FunctionService;
import com.vasnatech.mando.service.TreeService;
import org.jline.utils.AttributedString;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class FunctionCommands extends AbstractCommands {

    private final FunctionService functionService;
    private final TreeService treeService;

    public FunctionCommands(Session session, FormatService formatService, FunctionService functionService, TreeService treeService) {
        super(session, formatService);
        this.functionService = functionService;
        this.treeService = treeService;
    }

    @ShellMethod(group = "Function", key = "fun ls", prefix = "-")
    public AttributedString functionList(
            @ShellOption(value = "l") boolean eachLine,
            @ShellOption(value = "t") boolean tree,
            @ShellOption(value = "c") boolean type,
            @ShellOption(value = "f", defaultValue = "%") String filter
    ) throws Exception {
        return execute(() -> {
            if (tree) {
                return treeService.treeOfTree(functionService.tree(type, filter), "%");
            } else {
                return String.join(eachLine ? "\n" : ", ", functionService.list(type, filter));
            }
        });
    }
}
