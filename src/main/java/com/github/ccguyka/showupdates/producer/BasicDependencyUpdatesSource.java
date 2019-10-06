package com.github.ccguyka.showupdates.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.model.Dependency;

import com.github.ccguyka.showupdates.filter.DependencyFilter;
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts;
import com.github.ccguyka.showupdates.filter.VersionFilter;
import com.github.ccguyka.showupdates.objects.ArtifactUpdate;

class BasicDependencyUpdatesSource extends BasicUpdatesSource {

    private final UpdateSource updateSource;
    private final ArtifactSource artifactSource;
    private final FilterExcludedArtifacts filterExcludedArtifacts;
    private final VersionFilter versionFilter;
    private final DependencyFilter dependencyFilter;

    public BasicDependencyUpdatesSource(UpdateSource updateSource, ArtifactSource artifactSource,
            FilterExcludedArtifacts filterExcludedArtifacts, VersionFilter versionFilter,
            DependencyFilter dependencyFilter) {
        this.updateSource = updateSource;
        this.artifactSource = artifactSource;
        this.filterExcludedArtifacts = filterExcludedArtifacts;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
    }

    protected List<ArtifactUpdate> getDependencyUpdates(final List<Dependency> dependencies) {
        List<ArtifactUpdate> dependencyUpdates = new ArrayList<>();
        if (dependencies != null && !dependencies.isEmpty()) {
            final List<Dependency> filterDependencies = dependencyFilter.filter(dependencies);
            final List<Artifact> artifacts = artifactSource.getArtifacts(filterDependencies);

            final Map<Artifact, List<ArtifactVersion>> updates = updateSource.getUpdates(artifacts);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = filterExcludedArtifacts.filter(updates);

            Map<Artifact, ArtifactVersion> filter = versionFilter.filter(filteredUpdates);
            for (Entry<Artifact, ArtifactVersion> entry : filter.entrySet()) {
                ArtifactUpdate dependencyUpdate = from(entry.getKey(), entry.getValue());
                if (dependencyUpdate != null) {
                    dependencyUpdates.add(dependencyUpdate);
                }
            }
        }

        return dependencyUpdates;
    }
}
