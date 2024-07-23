package com.vasnatech.mando.command;

import com.vasnatech.commons.function.CheckedSupplier;
import com.vasnatech.mando.model.FileNode;
import com.vasnatech.mando.model.GlobalVariables;
import com.vasnatech.mando.model.Session;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.standard.ShellComponent;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@ShellComponent
public class AbstractCommands {

    static final AttributedStyle SUCCESS_STYLE = AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE);
    static final AttributedStyle FAIL_STYLE = AttributedStyle.DEFAULT.foreground(AttributedStyle.RED);

    final Session session;
    final Path workspaceDirectory;

    AbstractCommands(Session session, Path workspaceDirectory) {
        this.session = session;
        this.workspaceDirectory = workspaceDirectory;
    }

    public Session session() {
        return session;
    }

    <T> AttributedString execute(CheckedSupplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            return fail(e);
        }
    }

    AttributedString success(CharSequence message) {
        return message(message, SUCCESS_STYLE);
    }

    AttributedString success(Object message) {
        return success(message == null ? "" : message.toString());
    }

    AttributedString fail(CharSequence message) {
        return message(message, FAIL_STYLE);
    }

    AttributedString fail(Exception e) {
        return fail("Unexpected error: " + e.getMessage() + (debugEnabled() ? stackTrace(e) : ""));
    }

    AttributedString message(CharSequence message, AttributedStyle style) {
        return new AttributedStringBuilder()
                .style(style)
                .append(message)
                .toAttributedString();
    }

    String stackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    GlobalVariables globalVariables() {
        return session.globalVariables();
    }

    boolean debugEnabled() {
        return Optional.ofNullable(session().get("debugEnabled"))
                .map(Boolean.class::cast)
                .orElse(globalVariables().debugEnabled());
    }

    FileNode loadTree() throws IOException {
        return loadDirectory(FileNode.directory("ROOT", ""), workspaceDirectory, "");
    }

    FileNode loadDirectory(FileNode parent, Path parentPath, String prefix) throws IOException {
        Files.list(parentPath)
                .map(childPath -> {
                    String name = childPath.getFileName().toString();
                    String fullName = prefix + name;
                    if (Files.isDirectory(childPath)) {
                        try {
                            return loadDirectory(FileNode.directory(name, fullName + "/"), childPath, prefix + name + "/");
                        } catch (IOException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    } else {
                        return FileNode.file(name, fullName);
                    }
                })
                .forEach(parent::add);
        return parent;
    }

    String listOfTree(FileNode root, String filter) {
        List<String> list = listOfTree(new LinkedList<>(), filter(root, filter));
        return String.join("\n", list);
    }

    FileNode filter(FileNode root, String filter) {
        Pattern pattern = Pattern.compile(filter.replace("%", ".*"));
        FileNode newRoot = root.clone();
        newRoot.addAll(filter(root, pattern));
        return newRoot;
    }

    List<FileNode> filter(FileNode parent, Pattern pattern) {
        List<FileNode> filteredChildren = new LinkedList<>();
        for (FileNode child : parent.children()) {
            if (child.isDirectory()) {
                List<FileNode> childFilteredChildren = filter(child, pattern);
                if (childFilteredChildren.isEmpty()) {
                    if (pattern.matcher(child.fullName()).matches())
                        filteredChildren.add(child.clone());
                } else {
                    FileNode newChild = child.clone();
                    filteredChildren.add(newChild);
                    newChild.addAll(childFilteredChildren);
                }
            } else {
                if (pattern.matcher(child.fullName()).matches())
                    filteredChildren.add(child.clone());
            }
        }
        return filteredChildren;
    }

    List<String> listOfTree(List<String> result, FileNode parent) {
        for (FileNode child : parent.children()) {
            result.add(child.fullName());
            if (child.isDirectory()) {
                listOfTree(result, child);
            }
        }
        return result;
    }

    String treeOfTree(FileNode root, String filter) {
        return String.join("\n", treeOfTree(new LinkedList<>(), filter(root, filter), ""));
    }

    List<String> treeOfTree(List<String> result, FileNode parent, String prefix) {
        int index = 0;
        for (FileNode child : parent.children()) {
            boolean isLast = index == parent.children().size() - 1;
            String infix = isLast ? "└─" : "├─";
            if (child.isDirectory()) {
                result.add(prefix + infix + "[" + child.name() + "]");
                treeOfTree(result, child, prefix + (isLast ? "  " : "| "));
            } else {
                result.add(prefix + infix + " " + child.name());
            }
            ++index;
        }
        return result;
    }
}
