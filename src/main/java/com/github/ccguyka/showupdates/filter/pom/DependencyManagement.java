package com.github.ccguyka.showupdates.filter.pom;

import java.util.ArrayList;
import java.util.List;

public class DependencyManagement {

    private List<Dependency> dependencies = new ArrayList<>();

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }
}
