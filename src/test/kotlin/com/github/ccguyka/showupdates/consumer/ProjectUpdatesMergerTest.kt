package com.github.ccguyka.showupdates.consumer

import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates.Companion.builder
import com.google.common.collect.Lists
import org.assertj.core.api.Assertions
import org.junit.Test

class ProjectUpdatesMergerTest {

    private val merger = ProjectUpdatesMerger()

    @Test
    fun mergeParent() {
        val first = builder()
                .withParent(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("par1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("par2", "2", Lists.newArrayList("11")))))
                .build()
        val second = builder()
                .withParent(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("par1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("par3", "5", Lists.newArrayList("6")))))
                .build()
        val result = merger.merge(first, second)
        val expected = builder()
                .withParent(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("par1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("par2", "2", Lists.newArrayList("11")),
                        ArtifactUpdate("par3", "5", Lists.newArrayList("6")))))
                .build()
        Assertions.assertThat(result.parent.artifacts)
                .containsExactlyInAnyOrderElementsOf(expected.parent.artifacts)
    }

    @Test
    fun mergeDependency() {
        val first = builder()
                .withDependency(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("dep1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("dep2", "2", Lists.newArrayList("11")))))
                .build()
        val second = builder()
                .withDependency(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("dep1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("dep3", "5", Lists.newArrayList("6")))))
                .build()
        val result = merger.merge(first, second)
        val expected = builder()
                .withDependency(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("dep1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("dep2", "2", Lists.newArrayList("11")),
                        ArtifactUpdate("dep3", "5", Lists.newArrayList("6")))))
                .build()
        Assertions.assertThat(result.dependency.artifacts)
                .containsExactlyInAnyOrderElementsOf(expected.dependency.artifacts)
    }

    @Test
    fun mergeDependencyManagement() {
        val first = builder()
                .withDependencyManagement(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("depMgnt1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("depMgnt2", "2", Lists.newArrayList("11")))))
                .build()
        val second = builder()
                .withDependencyManagement(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("depMgnt1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("depMgnt3", "5", Lists.newArrayList("6")))))
                .build()
        val result = merger.merge(first, second)
        val expected = builder()
                .withDependencyManagement(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("depMgnt1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("depMgnt2", "2", Lists.newArrayList("11")),
                        ArtifactUpdate("depMgnt3", "5", Lists.newArrayList("6")))))
                .build()
        Assertions.assertThat(result.dependencyManagement.artifacts)
                .containsExactlyInAnyOrderElementsOf(expected.dependencyManagement.artifacts)
    }

    @Test
    fun mergePlugin() {
        val first = builder()
                .withPlugin(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("plugin1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("plugin2", "2", Lists.newArrayList("11")))))
                .build()
        val second = builder()
                .withPlugin(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("plugin1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("plugin3", "5", Lists.newArrayList("6")))))
                .build()
        val result = merger.merge(first, second)
        val expected = builder()
                .withPlugin(DependencyUpdates(Lists.newArrayList(ArtifactUpdate("plugin1", "1", Lists.newArrayList("2")),
                        ArtifactUpdate("plugin2", "2", Lists.newArrayList("11")),
                        ArtifactUpdate("plugin3", "5", Lists.newArrayList("6")))))
                .build()
        Assertions.assertThat(result.plugin.artifacts)
                .containsExactlyInAnyOrderElementsOf(expected.plugin.artifacts)
    }
}
