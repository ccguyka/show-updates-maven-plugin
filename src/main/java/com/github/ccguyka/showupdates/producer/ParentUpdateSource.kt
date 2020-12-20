package com.github.ccguyka.showupdates.producer

import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts
import com.github.ccguyka.showupdates.filter.VersionFilter
import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.project.MavenProject
import java.util.*

class ParentUpdateSource(private val project: MavenProject, private val updateSource: UpdateSource,
                         private val filterExcludedArtifacts: FilterExcludedArtifacts, private val versionFilter: VersionFilter) : BasicUpdatesSource() {

    val update: DependencyUpdates
        get() {
            val parent = project.parentArtifact
            val dependencyUpdates: MutableList<ArtifactUpdate> = ArrayList()
            if (parent != null) {
                val updates = updateSource.getUpdate(parent)
                val filteredUpdates = filterExcludedArtifacts.filter(updates)
                val filter: Collection<ArtifactVersion> = versionFilter.filter(filteredUpdates)[parent]
                val dependencyUpdate = from(parent, filter)
                if (dependencyUpdate != null) {
                    dependencyUpdates.add(dependencyUpdate)
                }
            }
            return DependencyUpdates(dependencyUpdates)
        }
}
