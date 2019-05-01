package com.github.ccguyka.showupdates.objects;

public class ProjectUpdates {

    private DependencyUpdates parent;
    private DependencyUpdates dependency;
    private DependencyUpdates plugin;
    private DependencyUpdates dependencyManagement;

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

    public static final class Builder {
        private DependencyUpdates parent;
        private DependencyUpdates dependency;
        private DependencyUpdates plugin;
        private DependencyUpdates dependencyManagement;

        private Builder() {
        }

        public Builder withParentUpdates(DependencyUpdates parent) {
            this.parent = parent;
            return this;
        }

        public Builder withDependencyUpdates(DependencyUpdates dependency) {
            this.dependency = dependency;
            return this;
        }

        public Builder withPluginUpdates(DependencyUpdates plugin) {
            this.plugin = plugin;
            return this;
        }

        public Builder withDependencyManagementUpdates(DependencyUpdates dependencyManagement) {
            this.dependencyManagement = dependencyManagement;
            return this;
        }

        public ProjectUpdates build() {
            return new ProjectUpdates(this);
        }
    }
}
