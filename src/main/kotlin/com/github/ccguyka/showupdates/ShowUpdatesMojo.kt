package com.github.ccguyka.showupdates

import com.github.ccguyka.showupdates.consumer.PrintUpdates.Companion.print
import com.github.ccguyka.showupdates.consumer.SaveUpdates.Companion.save
import com.github.ccguyka.showupdates.filter.DependencyFilter
import com.github.ccguyka.showupdates.filter.DependencyManagementFilter
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts
import com.github.ccguyka.showupdates.filter.PluginFilter
import com.github.ccguyka.showupdates.filter.VersionFilter.Companion.getFilterVersionsFor
import com.github.ccguyka.showupdates.objects.ProjectUpdates
import com.github.ccguyka.showupdates.producer.ArtifactSource
import com.github.ccguyka.showupdates.producer.DependencyManagementUpdatesSource
import com.github.ccguyka.showupdates.producer.DependencyUpdatesSource
import com.github.ccguyka.showupdates.producer.ParentUpdateSource
import com.github.ccguyka.showupdates.producer.PluginUpdatesSource
import com.github.ccguyka.showupdates.producer.ProjectUpdatesSource
import com.github.ccguyka.showupdates.producer.UpdateSource
import org.apache.maven.artifact.factory.ArtifactFactory
import org.apache.maven.artifact.metadata.ArtifactMetadataSource
import org.apache.maven.artifact.repository.ArtifactRepository
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import java.io.File

/**
 * Shows all dependencies and parent updates.
 */
@Mojo(name = "updates")
class ShowUpdatesMojo : AbstractMojo() {

    @Component
    private var artifactMetadataSource: ArtifactMetadataSource? = null

    @Component
    private var artifactFactory: ArtifactFactory? = null

    @Parameter(defaultValue = "\${project.remoteArtifactRepositories}", readonly = true)
    private var remoteArtifactRepositories: List<ArtifactRepository>? = null

    @Parameter(defaultValue = "\${localRepository}", readonly = true)
    private var localRepository: ArtifactRepository? = null

    @Parameter(defaultValue = "\${project}")
    private var project: MavenProject? = null

    @Parameter(property = "excludes", defaultValue = "alpha,beta,SNAPSHOT")
    private val excludes = arrayOf("alpha", "beta", "SNAPSHOT")

    @Parameter(property = "versions", defaultValue = "latest")
    private val versions = "latest"
    override fun execute() {
        val projectUpdates = projectUpdates
        print(projectUpdates, log)
        save(projectUpdates, log, getReportsFile(project))
    }

    private val projectUpdates: ProjectUpdates
        get() {
            val updateSource = UpdateSource(artifactMetadataSource!!, localRepository!!, remoteArtifactRepositories!!, log)
            val artifactSource = ArtifactSource(artifactFactory!!)
            val filterExcludedArtifacts = FilterExcludedArtifacts(*excludes)
            val versionFilter = getFilterVersionsFor(versions)
            val dependencyFilter = DependencyFilter(project!!, log)
            val dependencyManagementFilter = DependencyManagementFilter(project, log)
            val artifactFilter = PluginFilter(project!!, log)
            val getProjectUpdates = ProjectUpdatesSource(
                    ParentUpdateSource(project!!, updateSource, filterExcludedArtifacts, versionFilter),
                    DependencyUpdatesSource(project!!, updateSource, artifactSource, filterExcludedArtifacts, versionFilter, dependencyFilter),
                    PluginUpdatesSource(project!!, updateSource, filterExcludedArtifacts, versionFilter, artifactFilter),
                    DependencyManagementUpdatesSource(project!!, updateSource, artifactSource, filterExcludedArtifacts, versionFilter, dependencyManagementFilter))
            return getProjectUpdates.projectUpdates
        }

    private fun getReportsFile(project: MavenProject?): File {
        val buildDir = project!!.build.directory
        return File("$buildDir/maven-updates.json")
    }
}
