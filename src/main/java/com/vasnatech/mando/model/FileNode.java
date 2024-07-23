package com.vasnatech.mando.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class FileNode implements Comparable<FileNode> {

    final String name;
    final String fullName;
    final Set<FileNode> children;

    public FileNode(String name, String fullName, Set<FileNode> children) {
        this.name = name;
        this.fullName = fullName;
        this.children = children;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof FileNode fileNode)
            return this.name.equals(fileNode.name);
        return false;
    }

    @Override
    public int compareTo(FileNode that) {
        return this.name.compareTo(that.name);
    }

    public String name() {
        return name;
    }

    public String fullName() {
        return fullName;
    }

    public Set<FileNode> children() {
        return children;
    }

    public void add(FileNode child) {
        if (children == null) {
            throw new IllegalStateException("Can't add child to file: " + name);
        }
        children.add(child);
    }

    public void addAll(Collection<FileNode> children) {
        if (children == null) {
            throw new IllegalStateException("Can't add child to file: " + name);
        }
        this.children.addAll(children);
    }

    public boolean isFile() {
        return children == null;
    }

    public boolean isDirectory() {
        return children != null;
    }

    public FileNode clone() {
        return new FileNode(this.name, this.fullName, isDirectory() ? new TreeSet<>() : null);
    }

    public static FileNode file(String name, String fullName) {
        return new FileNode(name, fullName, null);
    }

    public static FileNode directory(String name, String fullName) {
        return new FileNode(name, fullName, new TreeSet<>());
    }
}
