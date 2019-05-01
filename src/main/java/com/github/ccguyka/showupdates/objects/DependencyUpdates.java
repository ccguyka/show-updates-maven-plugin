package com.github.ccguyka.showupdates.objects;

import java.util.List;

public class DependencyUpdates {

    private List<ArtifactUpdate> artifacts;

    public DependencyUpdates(List<ArtifactUpdate> artifacts) {
        this.artifacts = artifacts;
    }

    public List<ArtifactUpdate> getArtifacts() {
        return artifacts;
    }
}
