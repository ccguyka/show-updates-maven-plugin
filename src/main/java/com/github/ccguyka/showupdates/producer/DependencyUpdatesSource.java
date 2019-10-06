package com.github.ccguyka.showupdates.producer;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.filter.DependencyFilter;
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts;
import com.github.ccguyka.showupdates.filter.VersionFilter;
import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;

public class DependencyUpdatesSource extends BasicDependencyUpdatesSource {

    private final MavenProject project;

    public DependencyUpdatesSource(MavenProject project, UpdateSource updateSource, ArtifactSource artifactSource,
            FilterExcludedArtifacts filterExcludedArtifacts, VersionFilter versionFilter,
            DependencyFilter dependencyFilter) {
        super(updateSource, artifactSource, filterExcludedArtifacts, versionFilter, dependencyFilter);
        this.project = project;
    }

    public DependencyUpdates getUpdates() {
        final List<Dependency> dependencies = project.getDependencies();
        List<ArtifactUpdate> dependencyUpdates = getDependencyUpdates(dependencies);

        return new DependencyUpdates(dependencyUpdates);
    }
}
