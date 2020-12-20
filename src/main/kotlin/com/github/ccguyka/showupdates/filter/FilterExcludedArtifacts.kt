package com.github.ccguyka.showupdates.filter

import com.google.common.collect.Lists
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.versioning.ArtifactVersion
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

/**
 * Filter out artifacts of which the version contains one of the excludes.
 */
class FilterExcludedArtifacts(vararg excludes: String) {

    private val excludes: List<String>

    fun filter(updates: Map<Artifact, List<ArtifactVersion>>): Map<Artifact, List<ArtifactVersion>> {
        val filteredExcludedArtifacts: MutableMap<Artifact, List<ArtifactVersion>> = HashMap()
        for ((key, value) in updates) {
            filteredExcludedArtifacts[key] = filterExcludedArtifacts(value)
        }
        return filteredExcludedArtifacts
    }

    private fun filterExcludedArtifacts(update: List<ArtifactVersion>): List<ArtifactVersion> {
        return update.stream().filter(filterExcludedArtifact()).collect(Collectors.toList())
    }

    private fun filterExcludedArtifact(): Predicate<in ArtifactVersion> {
        return Predicate { artifact: ArtifactVersion ->
            Lists.newArrayList(excludes).stream()
                    .noneMatch { s: String? -> artifact.toString().contains(s!!) }
        }
    }

    init {
        this.excludes = Lists.newArrayList(*excludes)
    }
}
