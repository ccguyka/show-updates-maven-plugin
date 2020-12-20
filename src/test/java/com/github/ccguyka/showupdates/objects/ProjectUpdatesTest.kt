package com.github.ccguyka.showupdates.objects

import com.github.ccguyka.showupdates.objects.ProjectUpdates.Companion.builder
import com.google.common.collect.Lists
import org.assertj.core.api.Assertions
import org.junit.Test
import org.mockito.Mockito

class ProjectUpdatesTest {

    @Test
    fun parentUpdateIsSet() {
        val updates = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java)))
        val projectUpdates = builder()
                .withParent(updates)
                .build()
        Assertions.assertThat(projectUpdates.parent)
                .isEqualTo(updates)
    }

    @Test
    fun parentUpdateIsOverwritten() {
        val firstUpdate = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java, "first update")))
        val secondUpdate = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java, "second update")))
        val projectUpdates = builder()
                .withParent(firstUpdate)
                .withParent(secondUpdate)
                .build()
        Assertions.assertThat(projectUpdates.parent)
                .isEqualTo(secondUpdate)
    }

    @Test
    fun dependencyUpdateIsSet() {
        val updates = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java),
                        Mockito.mock(ArtifactUpdate::class.java)))
        val projectUpdates = builder()
                .withDependency(updates)
                .build()
        Assertions.assertThat(projectUpdates.dependency)
                .isEqualTo(updates)
    }

    @Test
    fun dependencyUpdateIsOverwritten() {
        val firstUpdate = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java, "first update A"),
                        Mockito.mock(ArtifactUpdate::class.java, "first update B")))
        val secondUpdate = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java, "second update A")))
        val projectUpdates = builder()
                .withDependency(firstUpdate)
                .withDependency(secondUpdate)
                .build()
        Assertions.assertThat(projectUpdates.dependency)
                .isEqualTo(secondUpdate)
    }

    @Test
    fun dependencyManagementUpdateIsSet() {
        val updates = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java),
                        Mockito.mock(ArtifactUpdate::class.java)))
        val projectUpdates = builder()
                .withDependencyManagement(updates)
                .build()
        Assertions.assertThat(projectUpdates.dependencyManagement)
                .isEqualTo(updates)
    }

    @Test
    fun dependencyManagementUpdateIsOverwritten() {
        val firstUpdate = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java, "first update A"),
                        Mockito.mock(ArtifactUpdate::class.java, "first update B")))
        val secondUpdate = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java, "second update A")))
        val projectUpdates = builder()
                .withDependencyManagement(firstUpdate)
                .withDependencyManagement(secondUpdate)
                .build()
        Assertions.assertThat(projectUpdates.dependencyManagement)
                .isEqualTo(secondUpdate)
    }

    @Test
    fun pluginUpdateIsSet() {
        val updates = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java),
                        Mockito.mock(ArtifactUpdate::class.java)))
        val projectUpdates = builder()
                .withPlugin(updates)
                .build()
        Assertions.assertThat(projectUpdates.plugin)
                .isEqualTo(updates)
    }

    @Test
    fun pluginUpdateIsOverwritten() {
        val firstUpdate = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java, "first update A"),
                        Mockito.mock(ArtifactUpdate::class.java, "first update B")))
        val secondUpdate = DependencyUpdates(
                Lists.newArrayList(
                        Mockito.mock(ArtifactUpdate::class.java, "second update A")))
        val projectUpdates = builder()
                .withPlugin(firstUpdate)
                .withPlugin(secondUpdate)
                .build()
        Assertions.assertThat(projectUpdates.plugin)
                .isEqualTo(secondUpdate)
    }
}
