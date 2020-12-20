package com.github.ccguyka.showupdates.filter

import com.google.common.collect.Iterables
import com.google.common.collect.ListMultimap
import com.google.common.collect.Lists
import com.google.common.collect.MultimapBuilder
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import java.util.stream.Collectors

class VersionFilter private constructor(val filter: List<VersionMatcher>) {

    fun filter(updates: Map<Artifact, List<ArtifactVersion>>): ListMultimap<Artifact, ArtifactVersion> {
        val latestVersions = MultimapBuilder.hashKeys().arrayListValues().build<Artifact, ArtifactVersion>()
        for (versionFilter in filter) {
            for ((key, value) in updates) {
                val currentVersion = DefaultArtifactVersion(key.version)
                val latestUpdates = value.stream()
                        .filter { updateVersion: ArtifactVersion -> versionFilter.matches(currentVersion, updateVersion) }
                        .collect(Collectors.toList())
                if (latestUpdates.isNotEmpty()) {
                    latestVersions.put(key, Iterables.getLast(latestUpdates))
                }
            }
        }
        val distinctLatestVersions = MultimapBuilder.hashKeys().arrayListValues().build<Artifact, ArtifactVersion>()
        for ((key, value) in latestVersions.asMap()) {
            distinctLatestVersions.putAll(key,
                    value.stream().distinct().collect(Collectors.toList()))
        }
        return distinctLatestVersions
    }

    private interface VersionMatcher {
        fun matches(artifactVersion: DefaultArtifactVersion, updateVersion: ArtifactVersion): Boolean
    }

    private class PatchVersionMatcher : VersionMatcher {
        override fun matches(artifactVersion: DefaultArtifactVersion, updateVersion: ArtifactVersion): Boolean {
            return (updateVersion.majorVersion == artifactVersion.majorVersion
                    && updateVersion.minorVersion == artifactVersion.minorVersion)
        }
    }

    private class MinorVersionMatcher : VersionMatcher {
        override fun matches(artifactVersion: DefaultArtifactVersion, updateVersion: ArtifactVersion): Boolean {
            return (updateVersion.majorVersion == artifactVersion.majorVersion
                    && updateVersion.minorVersion > artifactVersion.minorVersion)
        }
    }

    private class MajorVersionMatcher : VersionMatcher {
        override fun matches(artifactVersion: DefaultArtifactVersion, updateVersion: ArtifactVersion): Boolean {
            return true
        }
    }

    companion object {
        @JvmStatic
        fun getFilterVersionsFor(versions: String?): VersionFilter {
            return when (versions) {
                "patch" -> VersionFilter(Lists.newArrayList(PatchVersionMatcher()))
                "minor" -> VersionFilter(Lists.newArrayList(MinorVersionMatcher()))
                "major" -> VersionFilter(Lists.newArrayList(MajorVersionMatcher()))
                else -> VersionFilter(Lists.newArrayList(PatchVersionMatcher(), MinorVersionMatcher(), MajorVersionMatcher()))
            }
        }
    }
}
