package com.github.ccguyka.showupdates.objects;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import java.util.Objects;

public class DependencyUpdates {

    private final List<ArtifactUpdate> artifacts;

    @JsonCreator
    public DependencyUpdates(@JsonProperty("artifacts") List<ArtifactUpdate> artifacts) {
        this.artifacts = artifacts;
    }

    public List<ArtifactUpdate> getArtifacts() {
        return artifacts;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("artifacts", artifacts).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(artifacts);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DependencyUpdates) {
            DependencyUpdates that = (DependencyUpdates) object;
            return Objects.equals(this.artifacts, that.artifacts);
        }
        return false;
    }
}
