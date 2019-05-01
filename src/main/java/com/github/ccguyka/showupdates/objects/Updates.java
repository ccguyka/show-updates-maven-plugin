package com.github.ccguyka.showupdates.objects;

public class Updates {

    private ArtifactUpdate parent;

    public Updates(ArtifactUpdate parent) {
        this.parent = parent;
    }

    public ArtifactUpdate getParent() {
        return parent;
    }
}
