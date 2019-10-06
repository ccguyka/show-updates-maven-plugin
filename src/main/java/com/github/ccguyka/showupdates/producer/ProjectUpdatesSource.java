package com.github.ccguyka.showupdates.producer;

import com.github.ccguyka.showupdates.objects.ProjectUpdates;

/**
 * Find all the updates of this project.
 */
public class ProjectUpdatesSource {

    private final ParentUpdateSource parentUpdate;
    private final DependencyUpdatesSource dependencyUpdates;
    private final PluginUpdatesSource pluginUpdates;
    private final DependencyManagementUpdatesSource dependencyManagementUpdatesSource;

    public ProjectUpdatesSource(ParentUpdateSource parentUpdate,
            DependencyUpdatesSource dependencyUpdates, PluginUpdatesSource pluginUpdates,
            DependencyManagementUpdatesSource dependencyManagementUpdatesSource) {
        this.parentUpdate = parentUpdate;
        this.dependencyUpdates = dependencyUpdates;
        this.pluginUpdates = pluginUpdates;
        this.dependencyManagementUpdatesSource = dependencyManagementUpdatesSource;
    }

    public ProjectUpdates getProjectUpdates() {
        return ProjectUpdates.builder()
                .withParent(parentUpdate.getUpdate())
                .withDependency(dependencyUpdates.getUpdates())
                .withPlugin(pluginUpdates.getUpdates())
                .withDependencyManagement(dependencyManagementUpdatesSource.getUpdates())
                .build();
    }
}
