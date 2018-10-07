package com.github.ccguyka.showupdates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.sink.LogUpdatesSink;
import com.github.ccguyka.showupdates.source.ArtifactSource;
import com.github.ccguyka.showupdates.source.UpdateSource;

/**
 * Shows all dependencies and parent updates.
 */
@Mojo(name = "updates")
public class ShowUpdatesMojo extends AbstractMojo {

    @Parameter( defaultValue = "${session}", readonly = true )
    protected MavenSession mavenSession;

    @Component
    protected ArtifactMetadataSource artifactMetadataSource;

    @Component
    protected ArtifactFactory artifactFactory;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true)
    protected List<ArtifactRepository> remoteArtifactRepositories;

    @Parameter(defaultValue = "${localRepository}", readonly = true)
    protected ArtifactRepository localRepository;

    @Parameter(defaultValue = "${project}")
    protected MavenProject project;

    @Parameter(property = "excludes", defaultValue = "alpha,beta")
    private final String[] excludes = new String[] { "alpha", "beta" };

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Optional<Map<Artifact, ArtifactVersion>> parentUpdate = getParentUpdate();
        final Optional<Map<Artifact, ArtifactVersion>> dependencyUpdates = getDependencyUpdates();
        final Optional<Map<Artifact, ArtifactVersion>> pluginUpdates = getPluginUpdates();
        final Optional<Map<Artifact, ArtifactVersion>> dependencyManagementUpdates = getDependencyManagementUpdates();

        parentUpdate.ifPresent(new LogUpdatesSink("parent", getLog())::printUpdates);
        dependencyUpdates.ifPresent(new LogUpdatesSink("dependency", getLog())::printUpdates);
        pluginUpdates.ifPresent(new LogUpdatesSink("plugin", getLog())::printUpdates);
        dependencyManagementUpdates.ifPresent(new LogUpdatesSink("dependency management", getLog())::printUpdates);
    }

    private Optional<Map<Artifact, ArtifactVersion>> getParentUpdate() {
        final Artifact parent = project.getParentArtifact();
        if (parent != null) {
            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories, getLog()).getUpdate(parent);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = new FilterExcludedArtifacts(excludes)
                    .filter(updates);

            return Optional.of(new FilterLatestUpdates().getLatestUpdates(filteredUpdates));
        }

        return Optional.empty();
    }

    private Optional<Map<Artifact, ArtifactVersion>> getDependencyUpdates() {
        final List<Dependency> dependencies = project.getDependencies();
        if (dependencies != null && !dependencies.isEmpty()) {
            final List<Dependency> filterDependencies = filterDependencies(dependencies);
            final List<Artifact> artifacts = new ArtifactSource(artifactFactory).getArtifacts(filterDependencies);

            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories, getLog()).getUpdates(artifacts);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = new FilterExcludedArtifacts(excludes)
                    .filter(updates);

            return Optional.of(new FilterLatestUpdates().getLatestUpdates(filteredUpdates));
        }

        return Optional.empty();
    }

    private Optional<Map<Artifact, ArtifactVersion>> getPluginUpdates() {
        final Set<Artifact> artifacts = project.getPluginArtifacts();
        if (artifacts != null && !artifacts.isEmpty()) {
            final Set<Artifact> filterArtifacts = filterArtifacts(artifacts);
            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories, getLog()).getUpdates(filterArtifacts);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = new FilterExcludedArtifacts(excludes)
                    .filter(updates);

            return Optional.of(new FilterLatestUpdates().getLatestUpdates(filteredUpdates));
        }

        return Optional.empty();
    }

    private Optional<Map<Artifact, ArtifactVersion>> getDependencyManagementUpdates() {
        final DependencyManagement dependencyManagement = project.getDependencyManagement();
        if (dependencyManagement == null) {
            return Optional.empty();
        }

        final List<Dependency> dependencies = dependencyManagement.getDependencies();
        if (dependencies != null && !dependencies.isEmpty()) {
            final List<Dependency> filterDependencies = filterDependencies(dependencies);
            final List<Artifact> artifacts = new ArtifactSource(artifactFactory).getArtifacts(filterDependencies);

            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories, getLog()).getUpdates(artifacts);

            return Optional.of(new FilterLatestUpdates().getLatestUpdates(updates));
        }

        return Optional.empty();
    }

    private List<Dependency> filterDependencies(final List<Dependency> dependencies) {
        try {
            final String content = new String(Files.readAllBytes(Paths.get(project.getFile().getAbsolutePath())));
            return dependencies.stream().filter(dependency -> content.contains(dependency.getArtifactId()))
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            getLog().warn("Not able to read pom.xml file");
            return dependencies;
        }
    }

    private Set<Artifact> filterArtifacts(final Set<Artifact> artifacts) {
        try {
            final String content = new String(Files.readAllBytes(Paths.get(project.getFile().getAbsolutePath())));
            return artifacts.stream().filter(artifact -> content.contains(artifact.getArtifactId()))
                    .collect(Collectors.toSet());
        } catch (final IOException e) {
            getLog().warn("Not able to read pom.xml file");
            return artifacts;
        }
    }
}
