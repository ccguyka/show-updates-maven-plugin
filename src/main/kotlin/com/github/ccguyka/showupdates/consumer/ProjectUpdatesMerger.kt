package com.github.ccguyka.showupdates.consumer

import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates.Companion.builder
import com.google.common.collect.Sets
import java.util.stream.Collectors

internal class ProjectUpdatesMerger {

    fun merge(first: ProjectUpdates, second: ProjectUpdates): ProjectUpdates {
        val parent: MutableSet<ArtifactUpdate> = Sets.newHashSet()
        parent.addAll(first.parent.artifacts)
        parent.addAll(second.parent.artifacts)
        val dependency: MutableSet<ArtifactUpdate> = Sets.newHashSet()
        dependency.addAll(first.dependency.artifacts)
        dependency.addAll(second.dependency.artifacts)
        val dependencyManagement: MutableSet<ArtifactUpdate> = Sets.newHashSet()
        dependencyManagement.addAll(first.dependencyManagement.artifacts)
        dependencyManagement.addAll(second.dependencyManagement.artifacts)
        val plugin: MutableSet<ArtifactUpdate> = Sets.newHashSet()
        plugin.addAll(first.plugin.artifacts)
        plugin.addAll(second.plugin.artifacts)
        return builder()
                .withParent(DependencyUpdates(parent.stream().collect(Collectors.toList())))
                .withDependency(DependencyUpdates(dependency.stream().collect(Collectors.toList())))
                .withDependencyManagement(DependencyUpdates(dependencyManagement.stream().collect(Collectors.toList())))
                .withPlugin(DependencyUpdates(plugin.stream().collect(Collectors.toList())))
                .build()
    }
}
