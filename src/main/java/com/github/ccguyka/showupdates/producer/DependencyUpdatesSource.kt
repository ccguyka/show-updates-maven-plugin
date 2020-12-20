package com.github.ccguyka.showupdates.producer

import com.github.ccguyka.showupdates.filter.DependencyFilter
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts
import com.github.ccguyka.showupdates.filter.VersionFilter
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import org.apache.maven.project.MavenProject

class DependencyUpdatesSource(private val project: MavenProject, updateSource: UpdateSource?, artifactSource: ArtifactSource?,
                              filterExcludedArtifacts: FilterExcludedArtifacts?, versionFilter: VersionFilter?,
                              dependencyFilter: DependencyFilter?) : BasicDependencyUpdatesSource(updateSource!!, artifactSource!!, filterExcludedArtifacts!!, versionFilter!!, dependencyFilter!!) {

    val updates: DependencyUpdates
        get() {
            val dependencies = project.dependencies
            val dependencyUpdates = getDependencyUpdates(dependencies)
            return DependencyUpdates(dependencyUpdates)
        }
}
