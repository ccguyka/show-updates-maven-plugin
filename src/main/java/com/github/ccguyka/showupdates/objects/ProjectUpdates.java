package com.github.ccguyka.showupdates.objects;

public class ProjectUpdates {

    private DependencyUpdates parentUpdates;
    private DependencyUpdates dependencyUpdates;
    private DependencyUpdates pluginUpdates;
    private DependencyUpdates dependencyManagementUpdates;

    private ProjectUpdates(Builder builder) {
        this.parentUpdates = builder.parentUpdates;
        this.dependencyUpdates = builder.dependencyUpdates;
        this.pluginUpdates = builder.pluginUpdates;
        this.dependencyManagementUpdates = builder.dependencyManagementUpdates;
    }

    public DependencyUpdates getParentUpdates() {
        return parentUpdates;
    }

    public DependencyUpdates getDependencyUpdates() {
        return dependencyUpdates;
    }

    public DependencyUpdates getPluginUpdates() {
        return pluginUpdates;
    }

    public DependencyUpdates getDependencyManagementUpdates() {
        return dependencyManagementUpdates;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private DependencyUpdates parentUpdates;
        private DependencyUpdates dependencyUpdates;
        private DependencyUpdates pluginUpdates;
        private DependencyUpdates dependencyManagementUpdates;

        private Builder() {
        }

        public Builder withParentUpdates(DependencyUpdates parentUpdates) {
            this.parentUpdates = parentUpdates;
            return this;
        }

        public Builder withDependencyUpdates(DependencyUpdates dependencyUpdates) {
            this.dependencyUpdates = dependencyUpdates;
            return this;
        }

        public Builder withPluginUpdates(DependencyUpdates pluginUpdates) {
            this.pluginUpdates = pluginUpdates;
            return this;
        }

        public Builder withDependencyManagementUpdates(DependencyUpdates dependencyManagementUpdates) {
            this.dependencyManagementUpdates = dependencyManagementUpdates;
            return this;
        }

        public ProjectUpdates build() {
            return new ProjectUpdates(this);
        }
    }
}
