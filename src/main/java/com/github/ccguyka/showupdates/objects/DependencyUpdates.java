package com.github.ccguyka.showupdates.objects;

import java.util.List;

public class DependencyUpdates {

    private List<DependencyUpdate> dependency;

    public DependencyUpdates(List<DependencyUpdate> dependency) {
        this.dependency = dependency;
    }

    public List<DependencyUpdate> getDependency() {
        return dependency;
    }
}
