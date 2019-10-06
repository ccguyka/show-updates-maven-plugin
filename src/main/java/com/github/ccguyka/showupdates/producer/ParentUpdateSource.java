package com.github.ccguyka.showupdates.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts;
import com.github.ccguyka.showupdates.filter.VersionFilter;
import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;

public class ParentUpdateSource extends BasicUpdatesSource {

    private final MavenProject project;
    private final UpdateSource updateSource;
    private final FilterExcludedArtifacts filterExcludedArtifacts;
    private final VersionFilter versionFilter;

    public ParentUpdateSource(MavenProject project, UpdateSource updateSource,
            FilterExcludedArtifacts filterExcludedArtifacts, VersionFilter versionFilter) {
        this.project = project;
        this.updateSource = updateSource;
        this.filterExcludedArtifacts = filterExcludedArtifacts;
        this.versionFilter = versionFilter;
    }

    public DependencyUpdates getUpdate() {
        final Artifact parent = project.getParentArtifact();
        List<ArtifactUpdate> dependencyUpdates = new ArrayList<>();
        if (parent != null) {
            final Map<Artifact, List<ArtifactVersion>> updates = updateSource.getUpdate(parent);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = filterExcludedArtifacts.filter(updates);

            ArtifactVersion filter = versionFilter.filter(filteredUpdates).get(parent);
            ArtifactUpdate dependencyUpdate = from(parent, filter);
            if (dependencyUpdate != null) {
                dependencyUpdates.add(dependencyUpdate);
            }
        }

        return new DependencyUpdates(dependencyUpdates);
    }
}
