package com.github.ccguyka.showupdates.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import com.google.common.collect.Lists;

public class ProjectUpdatesTest {

    @Test
    public void parentUpdateIsSet() {
        DependencyUpdates updates = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class)));

        ProjectUpdates projectUpdates = ProjectUpdates.builder()
            .withParent(updates)
            .build();

        assertThat(projectUpdates.getParent())
            .isEqualTo(updates);
    }

    @Test
    public void parentUpdateIsOverwritten() {
        DependencyUpdates firstUpdate = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class, "first update")));
        DependencyUpdates secondUpdate = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class, "second update")));

        ProjectUpdates projectUpdates = ProjectUpdates.builder()
            .withParent(firstUpdate)
            .withParent(secondUpdate)
            .build();

        assertThat(projectUpdates.getParent())
            .isEqualTo(secondUpdate);
    }

    @Test
    public void dependencyUpdateIsSet() {
        DependencyUpdates updates = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class),
                        mock(ArtifactUpdate.class)));

        ProjectUpdates projectUpdates = ProjectUpdates.builder()
            .withDependency(updates)
            .build();

        assertThat(projectUpdates.getDependency())
            .isEqualTo(updates);
    }

    @Test
    public void dependencyUpdateIsOverwritten() {
        DependencyUpdates firstUpdate = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class, "first update A"),
                        mock(ArtifactUpdate.class, "first update B")));
        DependencyUpdates secondUpdate = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class, "second update A")));

        ProjectUpdates projectUpdates = ProjectUpdates.builder()
            .withDependency(firstUpdate)
            .withDependency(secondUpdate)
            .build();

        assertThat(projectUpdates.getDependency())
            .isEqualTo(secondUpdate);
    }

    @Test
    public void dependencyManagementUpdateIsSet() {
        DependencyUpdates updates = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class),
                        mock(ArtifactUpdate.class)));

        ProjectUpdates projectUpdates = ProjectUpdates.builder()
            .withDependencyManagement(updates)
            .build();

        assertThat(projectUpdates.getDependencyManagement())
            .isEqualTo(updates);
    }

    @Test
    public void dependencyManagementUpdateIsOverwritten() {
        DependencyUpdates firstUpdate = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class, "first update A"),
                        mock(ArtifactUpdate.class, "first update B")));
        DependencyUpdates secondUpdate = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class, "second update A")));

        ProjectUpdates projectUpdates = ProjectUpdates.builder()
            .withDependencyManagement(firstUpdate)
            .withDependencyManagement(secondUpdate)
            .build();

        assertThat(projectUpdates.getDependencyManagement())
            .isEqualTo(secondUpdate);
    }

    @Test
    public void pluginUpdateIsSet() {
        DependencyUpdates updates = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class),
                        mock(ArtifactUpdate.class)));

        ProjectUpdates projectUpdates = ProjectUpdates.builder()
            .withPlugin(updates)
            .build();

        assertThat(projectUpdates.getPlugin())
            .isEqualTo(updates);
    }

    @Test
    public void pluginUpdateIsOverwritten() {
        DependencyUpdates firstUpdate = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class, "first update A"),
                        mock(ArtifactUpdate.class, "first update B")));
        DependencyUpdates secondUpdate = new DependencyUpdates(
                Lists.newArrayList(
                        mock(ArtifactUpdate.class, "second update A")));

        ProjectUpdates projectUpdates = ProjectUpdates.builder()
            .withPlugin(firstUpdate)
            .withPlugin(secondUpdate)
            .build();

        assertThat(projectUpdates.getPlugin())
            .isEqualTo(secondUpdate);
    }
}
