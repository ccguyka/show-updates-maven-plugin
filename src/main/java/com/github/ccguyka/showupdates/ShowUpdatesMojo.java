package com.github.ccguyka.showupdates;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.consumer.PrintUpdates;
import com.github.ccguyka.showupdates.consumer.SaveUpdates;
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts;
import com.github.ccguyka.showupdates.filter.VersionFilter;
import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;
import com.github.ccguyka.showupdates.producer.ArtifactSource;
import com.github.ccguyka.showupdates.producer.UpdateSource;
import com.google.common.collect.Lists;

/**
 * Shows all dependencies and parent updates.
 * @todo Reduce size of class
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

    @Parameter(property = "excludes", defaultValue = "alpha,beta,SNAPSHOT")
    private String[] excludes = new String[] { "alpha", "beta", "SNAPSHOT" };

    @Parameter(property = "versions", defaultValue = "major")
    private String versions = "major";

    @Override
    public void execute() {
        DependencyUpdates parentUpdate = getParentUpdate();
        DependencyUpdates dependencyUpdates = getDependencyUpdates();
        DependencyUpdates pluginUpdates = getPluginUpdates();
        DependencyUpdates dependencyManagementUpdates = getDependencyManagement();
        ProjectUpdates projectUpdates = ProjectUpdates.builder()
                .withParent(parentUpdate)
                .withDependency(dependencyUpdates)
                .withPlugin(pluginUpdates)
                .withDependencyManagement(dependencyManagementUpdates)
                .build();

        PrintUpdates.print(projectUpdates, getLog());
        SaveUpdates.save(projectUpdates, getLog(), getReportsFile(project));
    }

    private DependencyUpdates getParentUpdate() {
        final Artifact parent = project.getParentArtifact();
        List<ArtifactUpdate> dependencyUpdates = new ArrayList<>();
        if (parent != null) {
            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories, getLog()).getUpdate(parent);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = new FilterExcludedArtifacts(excludes)
                    .filter(updates);

            ArtifactVersion filter = getFilterVersions().filter(filteredUpdates).get(parent);
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
            final Set<Artifact> filterArtifacts = filterArtifacts(artifacts);
            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories, getLog()).getUpdates(filterArtifacts);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = new FilterExcludedArtifacts(excludes)
                    .filter(updates);

            Map<Artifact, ArtifactVersion> filter = getFilterVersions().filter(filteredUpdates);
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
            final List<Dependency> filterDependencies = filterDependencies(dependencies);
            final List<Artifact> artifacts = new ArtifactSource(artifactFactory).getArtifacts(filterDependencies);

            final Map<Artifact, List<ArtifactVersion>> updates = new UpdateSource(artifactMetadataSource,
                    localRepository, remoteArtifactRepositories, getLog()).getUpdates(artifacts);

            final Map<Artifact, List<ArtifactVersion>> filteredUpdates = new FilterExcludedArtifacts(excludes)
                    .filter(updates);

            Map<Artifact, ArtifactVersion> filter = getFilterVersions().filter(filteredUpdates);
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

    private List<Dependency> filterDependencies(final List<Dependency> dependencies) {
        try {
            final String content = new String(Files.readAllBytes(Paths.get(project.getFile().getAbsolutePath())));
            return dependencies.stream()
                    .filter(dependency -> content.contains(dependency.getArtifactId()))
                    .collect(toList());
        } catch (final IOException e) {
            getLog().warn("Not able to read pom.xml file");
            return dependencies;
        }
    }

    /**
     * @todo Fix reporting of transitive dependency updates e.g. when guava is dependency and spring-guava is a transitive dependency.
     */
    private Set<Artifact> filterArtifacts(final Set<Artifact> artifacts) {
        try {
            final String content = new String(Files.readAllBytes(Paths.get(project.getFile().getAbsolutePath())));
            return artifacts.stream()
                    .filter(artifact -> content.contains(artifact.getArtifactId()))
                    .collect(toSet());
        } catch (final IOException e) {
            getLog().warn("Not able to read pom.xml file");
            return artifacts;
        }
    }

    private VersionFilter getFilterVersions() {
        return VersionFilter.getFilterVersionsFor(versions);
    }

    protected File getReportsFile(MavenProject project) {
        String buildDir = project.getBuild().getDirectory();
        return new File(buildDir + "/maven-updates.json");
    }
}
