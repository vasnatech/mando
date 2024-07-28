package com.vasnatech.mando.service;

import com.vasnatech.mando.expression.function.Functions;
import com.vasnatech.mando.model.TreeNode;
import org.springframework.stereotype.Service;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FunctionService {

    public TreeNode tree(boolean type, String filter) {
        Pattern pattern = Pattern.compile(filter.replace("%", ".*"));
        TreeNode root = TreeNode.branch("ROOT", "");
        Functions.methodsGroupedBy().entrySet().stream()
                .map(groupEntry -> {
                    TreeNode treeBrunch = TreeNode.branch(groupEntry.getKey(), groupEntry.getKey());
                    if (type) {
                        groupEntry.getValue().entrySet().stream()
                                .filter(entry -> pattern.matcher(entry.getKey()).matches())
                                .map(entry -> Stream.of(entry.getValue().getParameters())
                                        .map(Parameter::getType)
                                        .map(Class::getSimpleName)
                                        .collect(Collectors.joining(
                                                ", ",
                                                entry.getKey() + "(",
                                                ") : " + entry.getValue().getReturnType().getSimpleName()
                                        ))
                                )
                                .sorted()
                                .map(name -> TreeNode.leaf(name, name))
                                .forEach(treeBrunch::add);
                    } else {
                        groupEntry.getValue().keySet().stream()
                                .filter(name -> pattern.matcher(name).matches())
                                .sorted()
                                .map(name -> TreeNode.leaf(name, name))
                                .forEach(treeBrunch::add);
                    }
                    return treeBrunch;
                })
                .filter(brunchNode -> !brunchNode.children().isEmpty())
                .sorted()
                .forEach(root::add);
        return root;
    }

    public List<String > list(boolean type, String filter) {
        Pattern pattern = Pattern.compile(filter.replace("%", ".*"));
        if (type) {
            return Functions.methods().entrySet().stream()
                    .filter(entry -> pattern.matcher(entry.getKey()).matches())
                    .map(entry -> Stream.of(entry.getValue().getParameters())
                            .map(Parameter::getType)
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(
                                    ", ",
                                    entry.getKey() + "(",
                                    ") : " + entry.getValue().getReturnType().getSimpleName()
                            ))
                    )
                    .sorted()
                    .toList();
        } else {
            return Functions.methods().keySet().stream().filter(name -> pattern.matcher(name).matches()).sorted().toList();
        }
    }
}
