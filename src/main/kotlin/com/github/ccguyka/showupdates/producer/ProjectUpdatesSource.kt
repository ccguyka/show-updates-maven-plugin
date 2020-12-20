package com.github.ccguyka.showupdates.producer

import com.github.ccguyka.showupdates.objects.ProjectUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates.Companion.builder

/**
 * Find all the updates of this project.
 */
class ProjectUpdatesSource(private val parentUpdate: ParentUpdateSource,
                           private val dependencyUpdates: DependencyUpdatesSource,
                           private val pluginUpdates: PluginUpdatesSource,
                           private val dependencyManagementUpdatesSource: DependencyManagementUpdatesSource) {

    val projectUpdates: ProjectUpdates
        get() = builder()
                .withParent(parentUpdate.update)
                .withDependency(dependencyUpdates.updates)
                .withPlugin(pluginUpdates.updates)
                .withDependencyManagement(dependencyManagementUpdatesSource.updates)
                .build()
}
