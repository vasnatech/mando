package com.vasnatech.mando.service;

import com.vasnatech.mando.model.Session;
import com.vasnatech.mando.model.TreeNode;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileSystemService {

    final Session session;

    public FileSystemService(Session session) {
        this.session = session;
    }

    public Path childPath(String relativePath) throws FileNotFoundException {
        Path child = session.currentDirectory().resolve(relativePath);
        if (!Files.exists(child)) {
            throw new FileNotFoundException("Path: " + child + " does not exist.");
        }
        return child;
    }


    public Path childFile(String relativePath) throws FileNotFoundException {
        Path child = childPath(relativePath);
        if (!Files.isRegularFile(child)) {
            throw new FileNotFoundException("Path: " + child + " is not a file.");
        }
        return child;
    }

    public Path childDirectory(String relativePath) throws FileNotFoundException {
        Path child = childPath(relativePath);
        if (!Files.isDirectory(child)) {
            throw new FileNotFoundException("Path: " + child + " is not a directory.");
        }
        return child;
    }

    public TreeNode treeOfCurrentDirectory(boolean all) throws IOException {
        Path currentDirectory = session.currentDirectory();
        return treeOfDirectory(all, TreeNode.branch("ROOT", ""), currentDirectory, "");
    }

    TreeNode treeOfDirectory(boolean all, TreeNode parent, Path parentPath, String prefix) throws IOException {
        Files.list(parentPath)
                .map(childPath -> {
                    String name = childPath.getFileName().toString();
                    String fullName = prefix + name;
                    if (Files.isDirectory(childPath)) {
                        try {
                            TreeNode treeNode = TreeNode.branch(name, fullName + "/");
                            return all
                                    ? treeOfDirectory(all, treeNode, childPath, prefix + name + "/")
                                    : treeNode;
                        } catch (IOException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    } else {
                        return TreeNode.leaf(name, fullName);
                    }
                })
                .forEach(parent::add);
        return parent;
    }

    public Path currentDirectory() {
        return session.currentDirectory();
    }

    public Path navigateToParentDirectory() {
        Path parentDirectory = currentDirectory().getParent();
        if (parentDirectory != null)
            session.currentDirectory(parentDirectory);
        return currentDirectory();
    }

    public Path navigateToChildDirectory(String relativePath) throws FileNotFoundException {
        Path child = childDirectory(relativePath);
        session.currentDirectory(child);
        return currentDirectory();
    }
}
