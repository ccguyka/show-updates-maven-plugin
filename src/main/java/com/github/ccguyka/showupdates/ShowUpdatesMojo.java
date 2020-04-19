package com.github.ccguyka.showupdates;

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.consumer.PrintUpdates;
import com.github.ccguyka.showupdates.consumer.SaveUpdates;
import com.github.ccguyka.showupdates.filter.DependencyFilter;
import com.github.ccguyka.showupdates.filter.DependencyManagementFilter;
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts;
import com.github.ccguyka.showupdates.filter.PluginFilter;
import com.github.ccguyka.showupdates.filter.VersionFilter;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;
import com.github.ccguyka.showupdates.producer.ArtifactSource;
import com.github.ccguyka.showupdates.producer.DependencyManagementUpdatesSource;
import com.github.ccguyka.showupdates.producer.DependencyUpdatesSource;
import com.github.ccguyka.showupdates.producer.ParentUpdateSource;
import com.github.ccguyka.showupdates.producer.PluginUpdatesSource;
import com.github.ccguyka.showupdates.producer.ProjectUpdatesSource;
import com.github.ccguyka.showupdates.producer.UpdateSource;

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

    @Parameter(property = "excludes", defaultValue = "alpha,beta,SNAPSHOT")
    private String[] excludes = new String[] { "alpha", "beta", "SNAPSHOT" };

    @Parameter(property = "versions", defaultValue = "latest")
    private String versions = "latest";

    @Override
    public void execute() {
        final ProjectUpdates projectUpdates = getProjectUpdates();

        PrintUpdates.print(projectUpdates, getLog());
        SaveUpdates.save(projectUpdates, getLog(), getReportsFile(project));
    }

    private ProjectUpdates getProjectUpdates() {
        final UpdateSource updateSource = new UpdateSource(artifactMetadataSource, localRepository, remoteArtifactRepositories, getLog());
        final ArtifactSource artifactSource = new ArtifactSource(artifactFactory);
        final FilterExcludedArtifacts filterExcludedArtifacts = new FilterExcludedArtifacts(excludes);
        final VersionFilter versionFilter = VersionFilter.getFilterVersionsFor(versions);
        final DependencyFilter dependencyFilter = new DependencyFilter(project, getLog());
        final DependencyManagementFilter dependencyManagementFilter = new DependencyManagementFilter(project, getLog());
        final PluginFilter artifactFilter = new PluginFilter(project, getLog());

        final ProjectUpdatesSource getProjectUpdates = new ProjectUpdatesSource(
                new ParentUpdateSource(project, updateSource, filterExcludedArtifacts, versionFilter),
                new DependencyUpdatesSource(project, updateSource, artifactSource, filterExcludedArtifacts, versionFilter, dependencyFilter),
                new PluginUpdatesSource(project, updateSource, filterExcludedArtifacts, versionFilter, artifactFilter),
                new DependencyManagementUpdatesSource(project, updateSource, artifactSource, filterExcludedArtifacts, versionFilter, dependencyManagementFilter));

        return getProjectUpdates.getProjectUpdates();
    }

    protected File getReportsFile(final MavenProject project) {
        final String buildDir = project.getBuild().getDirectory();
        return new File(buildDir + "/maven-updates.json");
    }
}
