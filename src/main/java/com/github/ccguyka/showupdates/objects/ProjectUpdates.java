package com.github.ccguyka.showupdates.objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.Objects;

@JsonDeserialize(builder = ProjectUpdates.Builder.class)
public class ProjectUpdates {

    private final DependencyUpdates parent;
    private final DependencyUpdates dependency;
    private final DependencyUpdates plugin;
    private final DependencyUpdates dependencyManagement;

    private ProjectUpdates(Builder builder) {
        this.parent = builder.parent;
        this.dependency = builder.dependency;
        this.plugin = builder.plugin;
        this.dependencyManagement = builder.dependencyManagement;
    }

    public DependencyUpdates getParent() {
        return parent;
    }

    public DependencyUpdates getDependency() {
        return dependency;
    }

    public DependencyUpdates getPlugin() {
        return plugin;
    }

    public DependencyUpdates getDependencyManagement() {
        return dependencyManagement;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("parent", parent).add("dependency", dependency)
                .add("plugin", plugin).add("dependencyManagement", dependencyManagement).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, dependency, plugin, dependencyManagement);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProjectUpdates) {
            ProjectUpdates that = (ProjectUpdates) object;
            return Objects.equals(this.parent, that.parent) && Objects.equals(this.dependency, that.dependency)
                    && Objects.equals(this.plugin, that.plugin)
                    && Objects.equals(this.dependencyManagement, that.dependencyManagement);
        }
        return false;
    }

    @JsonPOJOBuilder
    public static final class Builder {
        private DependencyUpdates parent = new DependencyUpdates(Lists.newArrayList());
        private DependencyUpdates dependency = new DependencyUpdates(Lists.newArrayList());
        private DependencyUpdates plugin = new DependencyUpdates(Lists.newArrayList());
        private DependencyUpdates dependencyManagement = new DependencyUpdates(Lists.newArrayList());

        private Builder() {
        }

        public Builder withParent(DependencyUpdates parent) {
            this.parent = parent;
            return this;
        }

        public Builder withDependency(DependencyUpdates dependency) {
            this.dependency = dependency;
            return this;
        }

        public Builder withPlugin(DependencyUpdates plugin) {
            this.plugin = plugin;
            return this;
        }

        public Builder withDependencyManagement(DependencyUpdates dependencyManagement) {
            this.dependencyManagement = dependencyManagement;
            return this;
        }

        public ProjectUpdates build() {
            return new ProjectUpdates(this);
        }
    }
}
