package com.github.ccguyka.showupdates.filter

import com.google.common.collect.Lists
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import org.assertj.core.api.Assertions
import org.junit.Test
import java.util.*

class FilterExcludedArtifactsTest {

    private val artifact: Artifact = DefaultArtifact("groupId", "artifactId", "1.1.1", "compile", "type", "classifier",
            null)

    @Test
    @Throws(Exception::class)
    fun filterNoUpdates() {
        // GIVEN
        val filter = FilterExcludedArtifacts("SNAPSHOT")

        // WHEN
        val filtered = filter.filter(HashMap())

        // THEN
        Assertions.assertThat(filtered).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun filterAllUpdates() {
        // GIVEN
        val filter = FilterExcludedArtifacts("SNAPSHOT")
        val updates = HashMap<Artifact, List<ArtifactVersion>>()
        updates[artifact] = Lists.newArrayList<ArtifactVersion>(
                DefaultArtifactVersion("1.2.0-SNAPSHOT"))

        // WHEN
        val filtered = filter.filter(updates)

        // THEN
        Assertions.assertThat(filtered).containsEntry(artifact, ArrayList())
    }

    @Test
    @Throws(Exception::class)
    fun filterSomeUpdates() {
        // GIVEN
        val filter = FilterExcludedArtifacts("SNAPSHOT")
        val updates = HashMap<Artifact, List<ArtifactVersion>>()
        updates[artifact] = Lists.newArrayList<ArtifactVersion>(
                DefaultArtifactVersion("1.1.2"),
                DefaultArtifactVersion("1.2.0-SNAPSHOT"))

        // WHEN
        val filtered = filter.filter(updates)

        // THEN
        Assertions.assertThat(filtered).containsEntry(artifact, Lists.newArrayList<ArtifactVersion>(DefaultArtifactVersion("1.1.2")))
    }

    @Test
    @Throws(Exception::class)
    fun multipleExclusionFilter() {
        // GIVEN
        val filter = FilterExcludedArtifacts("RC2", "RC1", "SNAPSHOT")
        val updates = HashMap<Artifact, List<ArtifactVersion>>()
        updates[artifact] = Lists.newArrayList<ArtifactVersion>(
                DefaultArtifactVersion("1.1.2"),
                DefaultArtifactVersion("1.2.0-SNAPSHOT"),
                DefaultArtifactVersion("1.3.0-RC1"))

        // WHEN
        val filtered = filter.filter(updates)

        // THEN
        Assertions.assertThat(filtered).containsEntry(artifact, Lists.newArrayList<ArtifactVersion>(DefaultArtifactVersion("1.1.2")))
    }
}
