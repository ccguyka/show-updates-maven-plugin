package com.github.ccguyka.showupdates.producer

import com.github.ccguyka.showupdates.filter.DependencyManagementFilter
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts
import com.github.ccguyka.showupdates.filter.VersionFilter
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import org.apache.maven.project.MavenProject
import java.util.*

class DependencyManagementUpdatesSource(private val project: MavenProject, updateSource: UpdateSource?,
                                        artifactSource: ArtifactSource?, filterExcludedArtifacts: FilterExcludedArtifacts?, versionFilter: VersionFilter?,
                                        dependencyFilter: DependencyManagementFilter?) : BasicDependencyUpdatesSource(updateSource!!, artifactSource!!, filterExcludedArtifacts!!, versionFilter!!, dependencyFilter!!) {

    val updates: DependencyUpdates
        get() {
            val dependencyManagement = project.dependencyManagement
                    ?: return DependencyUpdates(ArrayList())
            val dependencies = dependencyManagement.dependencies
            val dependencyUpdates = getDependencyUpdates(dependencies)
            return DependencyUpdates(dependencyUpdates)
        }
}
