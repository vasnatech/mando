package com.vasnatech.mando.service;

import com.vasnatech.mando.model.TreeNode;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TreeService {

    public String listOfTree(TreeNode root, boolean list, String filter) {
        List<String> lines = listOfTree(new LinkedList<>(), filter(root, filter));
        return String.join(list ? "\n" : ", ", lines);
    }

    TreeNode filter(TreeNode root, String filter) {
        Pattern pattern = Pattern.compile(filter.replace("%", ".*"));
        TreeNode newRoot = root.clone();
        newRoot.addAll(filter(root, pattern));
        return newRoot;
    }

    List<TreeNode> filter(TreeNode parent, Pattern pattern) {
        List<TreeNode> filteredChildren = new LinkedList<>();
        for (TreeNode child : parent.children()) {
            if (child.isBranch()) {
                List<TreeNode> childFilteredChildren = filter(child, pattern);
                if (childFilteredChildren.isEmpty()) {
                    if (pattern.matcher(child.name()).matches())
                        filteredChildren.add(child.clone());
                } else {
                    TreeNode newChild = child.clone();
                    filteredChildren.add(newChild);
                    newChild.addAll(childFilteredChildren);
                }
            } else {
                if (pattern.matcher(child.name()).matches())
                    filteredChildren.add(child.clone());
            }
        }
        return filteredChildren;
    }

    List<String> listOfTree(List<String> result, TreeNode parent) {
        for (TreeNode child : parent.children()) {
            result.add(child.fullName());
            if (child.isBranch()) {
                listOfTree(result, child);
            }
        }
        return result;
    }

    public String treeOfTree(TreeNode root, String filter) {
        return String.join("\n", treeOfTree(new LinkedList<>(), filter(root, filter), ""));
    }

    List<String> treeOfTree(List<String> result, TreeNode parent, String prefix) {
        int index = 0;
        for (TreeNode child : parent.children()) {
            boolean isLast = index == parent.children().size() - 1;
            String infix = isLast ? "└─" : "├─";
            if (child.isBranch()) {
                result.add(prefix + infix + " " + child.name() + "/");
                treeOfTree(result, child, prefix + (isLast ? "   " : "│  "));
            } else {
                result.add(prefix + infix + " " + child.name());
            }
            ++index;
        }
        return result;
    }
}
