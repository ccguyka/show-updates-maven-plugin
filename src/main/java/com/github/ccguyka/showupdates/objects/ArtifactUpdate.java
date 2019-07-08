package com.github.ccguyka.showupdates.objects;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import java.util.Objects;

public class ArtifactUpdate {

    private final String name;
    private final String current;
    private final List<String> updates;

    @JsonCreator
    public ArtifactUpdate(@JsonProperty("name") String name,
            @JsonProperty("current") String current,
            @JsonProperty("updates") List<String> updates) {
        this.name = name;
        this.current = current;
        this.updates = updates;
    }

    public String getName() {
        return name;
    }

    public String getCurrent() {
        return current;
    }

    public List<String> getUpdates() {
        return updates;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).add("current", current).add("updates", updates)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, current, updates);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ArtifactUpdate) {
            ArtifactUpdate that = (ArtifactUpdate) object;
            return Objects.equals(this.name, that.name) && Objects.equals(this.current, that.current)
                    && Objects.equals(this.updates, that.updates);
        }
        return false;
    }
}
