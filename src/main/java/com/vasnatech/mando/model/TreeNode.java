package com.vasnatech.mando.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class TreeNode implements Comparable<TreeNode> {

    final String name;
    final String fullName;
    final Set<TreeNode> children;

    public TreeNode(String name, String fullName, Set<TreeNode> children) {
        this.name = name;
        this.fullName = fullName;
        this.children = children;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof TreeNode treeNode)
            return this.name.equals(treeNode.name);
        return false;
    }

    @Override
    public int compareTo(TreeNode that) {
        return this.name.compareTo(that.name);
    }

    public String name() {
        return name;
    }

    public String fullName() {
        return fullName;
    }

    public Set<TreeNode> children() {
        return children;
    }

    public void add(TreeNode child) {
        if (children == null) {
            throw new IllegalStateException("Can't add child to leaf: " + name);
        }
        children.add(child);
    }

    public void addAll(Collection<TreeNode> children) {
        if (children == null) {
            throw new IllegalStateException("Can't add child to leaf: " + name);
        }
        this.children.addAll(children);
    }

    public boolean isLeaf() {
        return children == null;
    }

    public boolean isBranch() {
        return children != null;
    }

    public TreeNode clone() {
        return new TreeNode(this.name, this.fullName, isBranch() ? new TreeSet<>() : null);
    }

    public static TreeNode leaf(String name, String fullName) {
        return new TreeNode(name, fullName, null);
    }

    public static TreeNode branch(String name, String fullName) {
        return new TreeNode(name, fullName, new TreeSet<>());
    }
}
