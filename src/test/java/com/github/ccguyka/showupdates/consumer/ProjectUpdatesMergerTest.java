package com.github.ccguyka.showupdates.consumer;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

public class ProjectUpdatesMergerTest {

    private ProjectUpdatesMerger merger = new ProjectUpdatesMerger();

    @Test
    public void mergeParent() {
        ProjectUpdates first = ProjectUpdates.builder()
                .withParent(new DependencyUpdates(newArrayList(new ArtifactUpdate("par1", "1", newArrayList("2")),
                        new ArtifactUpdate("par2", "2", newArrayList("11")))))
                .build();
        ProjectUpdates second = ProjectUpdates.builder()
                .withParent(new DependencyUpdates(newArrayList(new ArtifactUpdate("par1", "1", newArrayList("2")),
                        new ArtifactUpdate("par3", "5", newArrayList("6")))))
                .build();

        ProjectUpdates result = merger.merge(first, second);

        ProjectUpdates expected = ProjectUpdates.builder()
                .withParent(new DependencyUpdates(newArrayList(new ArtifactUpdate("par1", "1", newArrayList("2")),
                        new ArtifactUpdate("par2", "2", newArrayList("11")),
                        new ArtifactUpdate("par3", "5", newArrayList("6")))))
            .build();
        assertThat(result.getParent().getArtifacts())
            .containsExactlyInAnyOrderElementsOf(expected.getParent().getArtifacts());
    }

    @Test
    public void mergeDependency() {
        ProjectUpdates first = ProjectUpdates.builder()
                .withDependency(new DependencyUpdates(newArrayList(new ArtifactUpdate("dep1", "1", newArrayList("2")),
                        new ArtifactUpdate("dep2", "2", newArrayList("11")))))
                .build();
        ProjectUpdates second = ProjectUpdates.builder()
                .withDependency(new DependencyUpdates(newArrayList(new ArtifactUpdate("dep1", "1", newArrayList("2")),
                        new ArtifactUpdate("dep3", "5", newArrayList("6")))))
                .build();

        ProjectUpdates result = merger.merge(first, second);

        ProjectUpdates expected = ProjectUpdates.builder()
                .withDependency(new DependencyUpdates(newArrayList(new ArtifactUpdate("dep1", "1", newArrayList("2")),
                        new ArtifactUpdate("dep2", "2", newArrayList("11")),
                        new ArtifactUpdate("dep3", "5", newArrayList("6")))))
            .build();
        assertThat(result.getDependency().getArtifacts())
            .containsExactlyInAnyOrderElementsOf(expected.getDependency().getArtifacts());
    }

    @Test
    public void mergeDependencyManagement() {
        ProjectUpdates first = ProjectUpdates.builder()
                .withDependencyManagement(new DependencyUpdates(newArrayList(new ArtifactUpdate("depMgnt1", "1", newArrayList("2")),
                        new ArtifactUpdate("depMgnt2", "2", newArrayList("11")))))
                .build();
        ProjectUpdates second = ProjectUpdates.builder()
                .withDependencyManagement(new DependencyUpdates(newArrayList(new ArtifactUpdate("depMgnt1", "1", newArrayList("2")),
                        new ArtifactUpdate("depMgnt3", "5", newArrayList("6")))))
                .build();

        ProjectUpdates result = merger.merge(first, second);

        ProjectUpdates expected = ProjectUpdates.builder()
                .withDependencyManagement(new DependencyUpdates(newArrayList(new ArtifactUpdate("depMgnt1", "1", newArrayList("2")),
                        new ArtifactUpdate("depMgnt2", "2", newArrayList("11")),
                        new ArtifactUpdate("depMgnt3", "5", newArrayList("6")))))
            .build();
        assertThat(result.getDependencyManagement().getArtifacts())
            .containsExactlyInAnyOrderElementsOf(expected.getDependencyManagement().getArtifacts());
    }

    @Test
    public void mergePlugin() {
        ProjectUpdates first = ProjectUpdates.builder()
                .withPlugin(new DependencyUpdates(newArrayList(new ArtifactUpdate("plugin1", "1", newArrayList("2")),
                        new ArtifactUpdate("plugin2", "2", newArrayList("11")))))
                .build();
        ProjectUpdates second = ProjectUpdates.builder()
                .withPlugin(new DependencyUpdates(newArrayList(new ArtifactUpdate("plugin1", "1", newArrayList("2")),
                        new ArtifactUpdate("plugin3", "5", newArrayList("6")))))
                .build();

        ProjectUpdates result = merger.merge(first, second);

        ProjectUpdates expected = ProjectUpdates.builder()
                .withPlugin(new DependencyUpdates(newArrayList(new ArtifactUpdate("plugin1", "1", newArrayList("2")),
                        new ArtifactUpdate("plugin2", "2", newArrayList("11")),
                        new ArtifactUpdate("plugin3", "5", newArrayList("6")))))
            .build();
        assertThat(result.getPlugin().getArtifacts())
            .containsExactlyInAnyOrderElementsOf(expected.getPlugin().getArtifacts());
    }
}
