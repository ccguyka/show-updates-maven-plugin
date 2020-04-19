package com.github.ccguyka.showupdates.producer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts;
import com.github.ccguyka.showupdates.filter.PluginFilter;
import com.github.ccguyka.showupdates.filter.VersionFilter;
import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.google.common.collect.ListMultimap;

public class PluginUpdatesSource extends BasicUpdatesSource {

    private final MavenProject project;
    private final UpdateSource updateSource;
    private final FilterExcludedArtifacts filterExcludedArtifacts;
    private final VersionFilter versionFilter;
    private final PluginFilter pluginFilter;

    public PluginUpdatesSource(final MavenProject project, final UpdateSource updateSource,
            final FilterExcludedArtifacts filterExcludedArtifacts, final VersionFilter versionFilter,
            final PluginFilter artifactFilter) {
        this.project = project;
        this.updateSource = updateSource;
        this.filterExcludedArtifacts = filterExcludedArtifacts;
        this.versionFilter = versionFilter;
        this.pluginFilter = artifactFilter;
    }

    public DependencyUpdates getUpdates() {
        final Set<Artifact> artifacts = project.getPluginArtifacts();
        final List<ArtifactUpdate> dependencyUpdates = new ArrayList<>();
        if (artifacts != null && !artifacts.isEmpty()) {
            final Set<Artifact> filterArtifacts = pluginFilter.filter(artifacts);
            final Map<Artifact, List<ArtifactVersion>> updates = updateSource.getUpdates(filterArtifacts);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = filterExcludedArtifacts.filter(updates);

            final ListMultimap<Artifact, ArtifactVersion> filter = versionFilter.filter(filteredUpdates);
            for (final Entry<Artifact, Collection<ArtifactVersion>> entry : filter.asMap().entrySet()) {
                final ArtifactUpdate dependencyUpdate = from(entry.getKey(), entry.getValue());
                if (dependencyUpdate != null) {
                    dependencyUpdates.add(dependencyUpdate);
                }
            }
        }

        return new DependencyUpdates(dependencyUpdates);
    }
}
