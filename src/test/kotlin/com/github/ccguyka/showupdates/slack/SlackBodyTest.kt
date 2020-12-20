package com.github.ccguyka.showupdates.slack

import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates.Companion.builder
import com.github.ccguyka.showupdates.slack.SlackBody.Companion.artifact
import com.google.common.collect.Lists
import org.assertj.core.api.Assertions
import org.junit.Test

class SlackBodyTest {

    @Test
    @Throws(Exception::class)
    fun testBodyHasHeader() {
        val projectUpdates = builder()
                .build()

        // WHEN
        val body = artifact("my-artifact").updates(projectUpdates).build()

        // THEN
        Assertions.assertThat(body)
                .contains("*Updates for _my-artifact_*")
                .doesNotContain("Available parent updates")
                .doesNotContain("Available dependency updates")
                .doesNotContain("Available plugin updates")
                .doesNotContain("Available dependency management updates")
    }

    @Test
    @Throws(Exception::class)
    fun testBodyHasParentUpdates() {
        val projectUpdates = builder()
                .withParent(DependencyUpdates(Lists.newArrayList(
                        ArtifactUpdate("dep1", "1", Lists.newArrayList("2", "3")))))
                .build()

        // WHEN
        val body = artifact("my-artifact").updates(projectUpdates).build()

        // THEN
        Assertions.assertThat(body)
                .contains("*Available parent updates*")
                .contains("dep1 ... 1 -> 2, 3")
    }

    @Test
    @Throws(Exception::class)
    fun testBodyHasDependencyUpdates() {
        val projectUpdates = builder()
                .withDependency(DependencyUpdates(Lists.newArrayList(
                        ArtifactUpdate("dep1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("dep2", "2", Lists.newArrayList("11", "12")))))
                .build()

        // WHEN
        val body = artifact("my-artifact").updates(projectUpdates).build()

        // THEN
        Assertions.assertThat(body)
                .contains("*Available dependency updates*")
                .contains("dep1 ... 1 -> 2")
                .contains("dep2 ... 2 -> 11, 12")
    }

    @Test
    @Throws(Exception::class)
    fun testBodyHasPluginUpdates() {
        val projectUpdates = builder()
                .withPlugin(DependencyUpdates(Lists.newArrayList(
                        ArtifactUpdate("dep1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("dep2", "2", Lists.newArrayList("11", "12")))))
                .build()

        // WHEN
        val body = artifact("my-artifact").updates(projectUpdates).build()

        // THEN
        Assertions.assertThat(body)
                .contains("*Available plugin updates*")
                .contains("dep1 ... 1 -> 2")
                .contains("dep2 ... 2 -> 11, 12")
    }

    @Test
    @Throws(Exception::class)
    fun testBodyHasDependencyManagementUpdates() {
        val projectUpdates = builder()
                .withDependencyManagement(DependencyUpdates(Lists.newArrayList(
                        ArtifactUpdate("dep1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("dep2", "2", Lists.newArrayList("11", "12")))))
                .build()

        // WHEN
        val body = artifact("my-artifact").updates(projectUpdates).build()

        // THEN
        Assertions.assertThat(body)
                .contains("*Available dependency management updates*")
                .contains("dep1 ... 1 -> 2")
                .contains("dep2 ... 2 -> 11, 12")
    }
}
