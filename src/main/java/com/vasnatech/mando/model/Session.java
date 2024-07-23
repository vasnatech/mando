package com.vasnatech.mando.model;

import com.vasnatech.commons.collection.Lists;
import com.vasnatech.commons.type.VariableContainer;
import com.vasnatech.mando.schema.Environment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Session implements VariableContainer {

    final GlobalVariables globalVariables;
    final LinkedHashMap<String, Object> variables;
    final LinkedHashMap<String, Environment> environments;
    Project currentProject;


    public Session(GlobalVariables globalVariables) {
        this.globalVariables = globalVariables;
        variables = new LinkedHashMap<>();
        environments = new LinkedHashMap<>();
    }

    public GlobalVariables globalVariables() {
        return globalVariables;
    }

    public LinkedHashMap<String, Environment> environments() {
        return environments;
    }

    public List<String> environmentNames() {
        return new ArrayList<>(environments.keySet());
    }

    public List<Environment> environmentValues() {
        return new ArrayList<>(environments.values());
    }


    @Override
    public boolean containsKey(String name) {
        return variables.containsKey(name) || environments.values().stream().anyMatch(env -> env.containsKey(name));
    }

    @Override
    public Object get(String name) {
        return Optional.ofNullable(variables.get(name))
                .or(() -> environments.values().stream()
                        .filter(env -> env.containsKey(name))
                        .map(env -> env.get(name))
                        .findFirst()
                )
                .orElse(null);
    }

    @Override
    public Object put(String name, Object value) {
        return variables.put(name, value);
    }

    @Override
    public Object remove(String name) {
        return variables.remove(name);
    }

    @Override
    public Map<String, Object> flattenAsMap() {
        TreeMap<String, Object> all = Lists.reversed(environmentValues()).stream()
                .map(VariableContainer::flattenAsMap)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, TreeMap::new));
        all.putAll(variables);
        return all;
    }

    public Project currentProject() {
        return currentProject;
    }

    public void currentProject(Project project) {
        this.currentProject = project;
    }
}
