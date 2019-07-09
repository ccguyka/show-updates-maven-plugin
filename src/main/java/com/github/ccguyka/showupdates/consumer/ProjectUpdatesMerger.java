package com.github.ccguyka.showupdates.consumer;

import static java.util.stream.Collectors.toList;

import java.util.Set;
import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;
import com.google.common.collect.Sets;

class ProjectUpdatesMerger {

    public ProjectUpdates merge(ProjectUpdates first, ProjectUpdates second) {
        Set<ArtifactUpdate> parent = Sets.newHashSet();
        parent.addAll(first.getParent().getArtifacts());
        parent.addAll(second.getParent().getArtifacts());

        Set<ArtifactUpdate> dependency = Sets.newHashSet();
        dependency.addAll(first.getDependency().getArtifacts());
        dependency.addAll(second.getDependency().getArtifacts());

        Set<ArtifactUpdate> dependencyManagement = Sets.newHashSet();
        dependencyManagement.addAll(first.getDependencyManagement().getArtifacts());
        dependencyManagement.addAll(second.getDependencyManagement().getArtifacts());

        Set<ArtifactUpdate> plugin = Sets.newHashSet();
        plugin.addAll(first.getPlugin().getArtifacts());
        plugin.addAll(second.getPlugin().getArtifacts());

        return ProjectUpdates.builder()
                .withParent(new DependencyUpdates(parent.stream().collect(toList())))
                .withDependency(new DependencyUpdates(dependency.stream().collect(toList())))
                .withDependencyManagement(new DependencyUpdates(dependencyManagement.stream().collect(toList())))
                .withPlugin(new DependencyUpdates(plugin.stream().collect(toList())))
                .build();
    }
}
