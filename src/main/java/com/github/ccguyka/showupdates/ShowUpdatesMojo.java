package com.github.ccguyka.showupdates;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
                    localRepository, remoteArtifactRepositories).getUpdate(parent);

            return Optional.of(new FilterLatestUpdates().getLatestUpdates(updates));
        }

        return Optional.empty();
    }

    private Optional<Map<Artifact, ArtifactVersion>> getDependencyUpdates() {
        final List<Dependency> dependencies = project.getDependencies();
        if (dependencies != null && !dependencies.isEmpty()) {
            final List<Artifact> artifacts = new ArtifactSource(artifactFactory).getArtifacts(dependencies);

            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories).getUpdates(artifacts);

            return Optional.of(new FilterLatestUpdates().getLatestUpdates(updates));
        }

        return Optional.empty();
    }

    private Optional<Map<Artifact, ArtifactVersion>> getPluginUpdates() {
        final Set<Artifact> artifacts = project.getPluginArtifacts();
        if (artifacts != null && !artifacts.isEmpty()) {
            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories).getUpdates(artifacts);

            return Optional.of(new FilterLatestUpdates().getLatestUpdates(updates));
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
            final List<Artifact> artifacts = new ArtifactSource(artifactFactory).getArtifacts(dependencies);

            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories).getUpdates(artifacts);

            return Optional.of(new FilterLatestUpdates().getLatestUpdates(updates));
        }

        return Optional.empty();
    }

}
