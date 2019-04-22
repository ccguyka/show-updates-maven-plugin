package com.github.ccguyka.showupdates.objects;

public class Updates {

    private DependencyUpdate parent;

    public Updates(DependencyUpdate parent) {
        this.parent = parent;
    }

    public DependencyUpdate getParent() {
        return parent;
    }
}
