package com.github.ccguyka.showupdates.filter

import com.github.ccguyka.showupdates.filter.VersionFilter.Companion.getFilterVersionsFor
import com.google.common.collect.Lists
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import org.assertj.guava.api.Assertions
import org.junit.Test
import java.util.*
import java.util.stream.Collectors

class VersionFilterTest {

    @Test
    fun filterLatestVersions() {
        val filter = getFilterVersionsFor("latest")
        val artifact: Artifact = currentVersion("1.1.1")
        val updateVersions = availableUpdates("1.1.2", "1.1.3", "1.2.0", "1.3.0", "2.0.0", "2.1.5")
        val updates: MutableMap<Artifact, List<ArtifactVersion>> = HashMap()
        updates[artifact] = Lists.newArrayList(updateVersions)
        val latestUpdates = filter.filter(updates)
        Assertions.assertThat(latestUpdates).contains(
                Assertions.entry(artifact, DefaultArtifactVersion("1.1.3")),
                Assertions.entry(artifact, DefaultArtifactVersion("1.3.0")),
                Assertions.entry(artifact, DefaultArtifactVersion("2.1.5")))
    }

    @Test
    fun filterPatchVersion() {
        val filter = getFilterVersionsFor("patch")
        val artifact: Artifact = currentVersion("1.1.1")
        val updateVersions = availableUpdates("1.1.2", "1.1.3", "1.2.0", "1.3.0", "2.0.0", "2.1.5")
        val updates: MutableMap<Artifact, List<ArtifactVersion>> = HashMap()
        updates[artifact] = Lists.newArrayList(updateVersions)
        val patchUpdates = filter.filter(updates)
        Assertions.assertThat(patchUpdates).contains(Assertions.entry(artifact, DefaultArtifactVersion("1.1.3")))
    }

    @Test
    fun filterMinorVersion() {
        val filter = getFilterVersionsFor("minor")
        val artifact: Artifact = currentVersion("1.1.1")
        val updateVersions = availableUpdates("1.1.2", "1.1.3", "1.2.0", "1.3.0", "2.0.0", "2.1.5")
        val updates: MutableMap<Artifact, List<ArtifactVersion>> = HashMap()
        updates[artifact] = Lists.newArrayList(updateVersions)
        val minorUpdates = filter.filter(updates)
        Assertions.assertThat(minorUpdates).contains(Assertions.entry(artifact, DefaultArtifactVersion("1.3.0")))
    }

    @Test
    fun filterMajorVersion() {
        val filter = getFilterVersionsFor("major")
        val artifact: Artifact = currentVersion("1.1.1")
        val updateVersions = availableUpdates("1.1.2", "1.1.3", "1.2.0", "1.3.0", "2.0.0", "2.1.5")
        val updates: MutableMap<Artifact, List<ArtifactVersion>> = HashMap()
        updates[artifact] = Lists.newArrayList(updateVersions)
        val majorUpdates = filter.filter(updates)
        Assertions.assertThat(majorUpdates).contains(Assertions.entry(artifact, DefaultArtifactVersion("2.1.5")))
    }

    private fun currentVersion(version: String): DefaultArtifact {
        return DefaultArtifact("groupId", "artifactId", version, "compile", "type",
                "classifier", null)
    }

    private fun availableUpdates(vararg updates: String): List<ArtifactVersion> {
        return Lists.newArrayList(*updates).stream().map { version: String? -> DefaultArtifactVersion(version) }.collect(Collectors.toList())
    }
}
