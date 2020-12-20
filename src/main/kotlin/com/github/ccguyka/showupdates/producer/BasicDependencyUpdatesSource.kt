package com.github.ccguyka.showupdates.producer

import com.github.ccguyka.showupdates.filter.DependencyFilter
import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts
import com.github.ccguyka.showupdates.filter.VersionFilter
import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import org.apache.maven.model.Dependency
import java.util.*

open class BasicDependencyUpdatesSource(private val updateSource: UpdateSource, private val artifactSource: ArtifactSource,
                                                 private val filterExcludedArtifacts: FilterExcludedArtifacts, private val versionFilter: VersionFilter,
                                                 private val dependencyFilter: DependencyFilter) : BasicUpdatesSource() {

    protected fun getDependencyUpdates(dependencies: List<Dependency>): List<ArtifactUpdate> {
        val dependencyUpdates: MutableList<ArtifactUpdate> = ArrayList()
        if (dependencies != null && !dependencies.isEmpty()) {
            val filterDependencies = dependencyFilter.filter(dependencies)
            val artifacts = artifactSource.getArtifacts(filterDependencies)
            val updates = updateSource.getUpdates(artifacts)
            val filteredUpdates = filterExcludedArtifacts.filter(updates)
            val filter = versionFilter.filter(filteredUpdates)
            for ((key, value) in filter.asMap()) {
                val dependencyUpdate = from(key, value)
                if (dependencyUpdate != null) {
                    dependencyUpdates.add(dependencyUpdate)
                }
            }
        }
        return dependencyUpdates
    }
}
