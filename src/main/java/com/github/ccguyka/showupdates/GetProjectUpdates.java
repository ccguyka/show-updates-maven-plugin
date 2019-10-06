package com.github.ccguyka.showupdates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.filter.ArtifactFilter;
import com.github.ccguyka.showupdates.filter.DependencyFilter;
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts;
import com.github.ccguyka.showupdates.filter.VersionFilter;
import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;
import com.github.ccguyka.showupdates.producer.ArtifactSource;
import com.github.ccguyka.showupdates.producer.UpdateSource;
import com.google.common.collect.Lists;

/**
 * Find all the updates of this project.
 * @todo #34:60min Reduce number of dependencies as class is to big
 */
public class GetProjectUpdates {

    private final MavenProject project;
    private final UpdateSource updateSource;
    private final ArtifactSource artifactSource;
    private final FilterExcludedArtifacts filterExcludedArtifacts;
    private final VersionFilter versionFilter;
    private final DependencyFilter dependencyFilter;
    private final ArtifactFilter artifactFilter;

    public GetProjectUpdates(MavenProject project, UpdateSource updateSource, ArtifactSource artifactSource,
            FilterExcludedArtifacts filterExcludedArtifacts, VersionFilter versionFilter,
            DependencyFilter dependencyFilter, ArtifactFilter artifactFilter) {
        this.project = project;
        this.updateSource = updateSource;
        this.artifactSource = artifactSource;
        this.filterExcludedArtifacts = filterExcludedArtifacts;
        this.versionFilter = versionFilter;
        this.dependencyFilter = dependencyFilter;
        this.artifactFilter = artifactFilter;
    }

    public ProjectUpdates getProjectUpdates() {
        DependencyUpdates parentUpdate = getParentUpdate();
        DependencyUpdates dependencyUpdates = getDependencyUpdates();
        DependencyUpdates pluginUpdates = getPluginUpdates();
        DependencyUpdates dependencyManagementUpdates = getDependencyManagement();
        return ProjectUpdates.builder()
                .withParent(parentUpdate)
                .withDependency(dependencyUpdates)
                .withPlugin(pluginUpdates)
                .withDependencyManagement(dependencyManagementUpdates)
                .build();
    }

    private DependencyUpdates getParentUpdate() {
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

    private DependencyUpdates getDependencyUpdates() {
        final List<Dependency> dependencies = project.getDependencies();
        List<ArtifactUpdate> dependencyUpdates = getDependencyUpdates(dependencies);

        return new DependencyUpdates(dependencyUpdates);
    }

    private DependencyUpdates getPluginUpdates() {
        final Set<Artifact> artifacts = project.getPluginArtifacts();
        List<ArtifactUpdate> dependencyUpdates = new ArrayList<>();
        if (artifacts != null && !artifacts.isEmpty()) {
            final Set<Artifact> filterArtifacts = artifactFilter.filter(artifacts);
            final Map<Artifact, List<ArtifactVersion>> updates = updateSource.getUpdates(filterArtifacts);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = filterExcludedArtifacts.filter(updates);

            Map<Artifact, ArtifactVersion> filter = versionFilter.filter(filteredUpdates);
            for (Entry<Artifact, ArtifactVersion> entry : filter.entrySet()) {
                ArtifactUpdate dependencyUpdate = from(entry.getKey(), entry.getValue());
                if (dependencyUpdate != null) {
                    dependencyUpdates.add(dependencyUpdate);
                }
            }
        }

        return new DependencyUpdates(dependencyUpdates);
    }

    private DependencyUpdates getDependencyManagement() {
        final DependencyManagement dependencyManagement = project.getDependencyManagement();
        if (dependencyManagement == null) {
            return new DependencyUpdates(new ArrayList<>());
        }

        final List<Dependency> dependencies = dependencyManagement.getDependencies();
        List<ArtifactUpdate> dependencyUpdates = getDependencyUpdates(dependencies);

        return new DependencyUpdates(dependencyUpdates);
    }

    private List<ArtifactUpdate> getDependencyUpdates(final List<Dependency> dependencies) {
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

    private ArtifactUpdate from(Artifact artifact, ArtifactVersion artifactVersion) {
        if (artifactVersion == null) {
            return null;
        }
        return new ArtifactUpdate(artifact.getGroupId() + ":" + artifact.getArtifactId(), artifact.getVersion(),
                Lists.newArrayList(artifactVersion.toString()));
    }
}
