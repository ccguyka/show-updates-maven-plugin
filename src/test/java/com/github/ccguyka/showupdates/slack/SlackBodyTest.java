package com.github.ccguyka.showupdates.slack;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

public class SlackBodyTest {

    @Test
    public void testBodyHasHeader() throws Exception {
        ProjectUpdates projectUpdates = ProjectUpdates.builder()
                .build();

        // WHEN
        String body = SlackBody.artifact("my-artifact").updates(projectUpdates).build();

        // THEN
        assertThat(body)
            .contains("*Updates for _my-artifact_*")
            .doesNotContain("Available parent updates")
            .doesNotContain("Available dependency updates")
            .doesNotContain("Available plugin updates")
            .doesNotContain("Available dependency management updates");
    }


    @Test
    public void testBodyHasParentUpdates() throws Exception {
        ProjectUpdates projectUpdates = ProjectUpdates.builder()
                .withParent(new DependencyUpdates(newArrayList(
                        new ArtifactUpdate("dep1", "1", newArrayList("2")),
                        new ArtifactUpdate("dep2", "2", newArrayList("11")))))
                .build();

        // WHEN
        String body = SlackBody.artifact("my-artifact").updates(projectUpdates).build();

        // THEN
        assertThat(body)
            .contains("*Available parent updates*")
            .contains("dep1 ... 1 -> 2")
            .contains("dep2 ... 2 -> 11");
    }

    @Test
    public void testBodyHasDependencyUpdates() throws Exception {
        ProjectUpdates projectUpdates = ProjectUpdates.builder()
                .withDependency(new DependencyUpdates(newArrayList(
                        new ArtifactUpdate("dep1", "1", newArrayList("2")),
                        new ArtifactUpdate("dep2", "2", newArrayList("11")))))
                .build();

        // WHEN
        String body = SlackBody.artifact("my-artifact").updates(projectUpdates).build();

        // THEN
        assertThat(body)
            .contains("*Available dependency updates*")
            .contains("dep1 ... 1 -> 2")
            .contains("dep2 ... 2 -> 11");
    }

    @Test
    public void testBodyHasPluginUpdates() throws Exception {
        ProjectUpdates projectUpdates = ProjectUpdates.builder()
                .withPlugin(new DependencyUpdates(newArrayList(
                        new ArtifactUpdate("dep1", "1", newArrayList("2")),
                        new ArtifactUpdate("dep2", "2", newArrayList("11")))))
                .build();

        // WHEN
        String body = SlackBody.artifact("my-artifact").updates(projectUpdates).build();

        // THEN
        assertThat(body)
            .contains("*Available plugin updates*")
            .contains("dep1 ... 1 -> 2")
            .contains("dep2 ... 2 -> 11");
    }

    @Test
    public void testBodyHasDependencyManagementUpdates() throws Exception {
        ProjectUpdates projectUpdates = ProjectUpdates.builder()
                .withDependencyManagement(new DependencyUpdates(newArrayList(
                        new ArtifactUpdate("dep1", "1", newArrayList("2")),
                        new ArtifactUpdate("dep2", "2", newArrayList("11")))))
                .build();

        // WHEN
        String body = SlackBody.artifact("my-artifact").updates(projectUpdates).build();

        // THEN
        assertThat(body)
            .contains("*Available dependency management updates*")
            .contains("dep1 ... 1 -> 2")
            .contains("dep2 ... 2 -> 11");
    }
}
