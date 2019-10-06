package com.github.ccguyka.showupdates.producer;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.filter.DependencyFilter;
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts;
import com.github.ccguyka.showupdates.filter.VersionFilter;
import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;

public class DependencyManagementUpdatesSource extends BasicDependencyUpdatesSource {

    private final MavenProject project;

    public DependencyManagementUpdatesSource(MavenProject project, UpdateSource updateSource,
            ArtifactSource artifactSource, FilterExcludedArtifacts filterExcludedArtifacts, VersionFilter versionFilter,
            DependencyFilter dependencyFilter) {
        super(updateSource, artifactSource, filterExcludedArtifacts, versionFilter, dependencyFilter);

        this.project = project;
    }

    public DependencyUpdates getUpdates() {
        final DependencyManagement dependencyManagement = project.getDependencyManagement();
        if (dependencyManagement == null) {
            return new DependencyUpdates(new ArrayList<>());
        }

        final List<Dependency> dependencies = dependencyManagement.getDependencies();
        List<ArtifactUpdate> dependencyUpdates = getDependencyUpdates(dependencies);

        return new DependencyUpdates(dependencyUpdates);
    }
}
