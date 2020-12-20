package com.github.ccguyka.showupdates.producer

import com.github.ccguyka.showupdates.filter.FilterExcludedArtifacts
import com.github.ccguyka.showupdates.filter.PluginFilter
import com.github.ccguyka.showupdates.filter.VersionFilter
import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import org.apache.maven.project.MavenProject
import java.util.*

class PluginUpdatesSource(private val project: MavenProject, private val updateSource: UpdateSource,
                          private val filterExcludedArtifacts: FilterExcludedArtifacts, private val versionFilter: VersionFilter,
                          private val pluginFilter: PluginFilter) : BasicUpdatesSource() {

    val updates: DependencyUpdates
        get() {
            val artifacts = project.pluginArtifacts
            val dependencyUpdates: MutableList<ArtifactUpdate> = ArrayList()
            if (artifacts != null && !artifacts.isEmpty()) {
                val filterArtifacts = pluginFilter.filter(artifacts)
                val updates = updateSource.getUpdates(filterArtifacts)
                val filteredUpdates = filterExcludedArtifacts.filter(updates)
                val filter = versionFilter.filter(filteredUpdates)
                for ((key, value) in filter.asMap()) {
                    val dependencyUpdate = from(key, value)
                    if (dependencyUpdate != null) {
                        dependencyUpdates.add(dependencyUpdate)
                    }
                }
            }
            return DependencyUpdates(dependencyUpdates)
        }
}
