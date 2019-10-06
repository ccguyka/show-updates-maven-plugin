package com.github.ccguyka.showupdates.filter;


import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.junit.Test;

public class FilterExcludedArtifactsTest {

    private Artifact artifact = new DefaultArtifact("groupId", "artifactId", "1.1.1", "compile", "type", "classifier",
            null);

    @Test
    public void filterNoUpdates() throws Exception {
        // GIVEN
        FilterExcludedArtifacts filter = new FilterExcludedArtifacts("SNAPSHOT");

        // WHEN
        Map<Artifact, List<ArtifactVersion>> filtered = filter.filter(new HashMap<>());

        // THEN
        assertThat(filtered).isEmpty();
    }

    @Test
    public void filterAllUpdates() throws Exception {
        // GIVEN
        FilterExcludedArtifacts filter = new FilterExcludedArtifacts("SNAPSHOT");
        HashMap<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        updates.put(artifact, newArrayList(
                new DefaultArtifactVersion("1.2.0-SNAPSHOT")));

        // WHEN
        Map<Artifact, List<ArtifactVersion>> filtered = filter.filter(updates);

        // THEN
        assertThat(filtered).containsEntry(artifact, new ArrayList<>());
    }

    @Test
    public void filterSomeUpdates() throws Exception {
        // GIVEN
        FilterExcludedArtifacts filter = new FilterExcludedArtifacts("SNAPSHOT");
        HashMap<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        updates.put(artifact, newArrayList(
                new DefaultArtifactVersion("1.1.2"),
                new DefaultArtifactVersion("1.2.0-SNAPSHOT")));

        // WHEN
        Map<Artifact, List<ArtifactVersion>> filtered = filter.filter(updates);

        // THEN
        assertThat(filtered).containsEntry(artifact, newArrayList(new DefaultArtifactVersion("1.1.2")));
    }

    @Test
    public void multipleExclusionFilter() throws Exception {
        // GIVEN
        FilterExcludedArtifacts filter = new FilterExcludedArtifacts("RC2", "RC1", "SNAPSHOT");
        HashMap<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        updates.put(artifact, newArrayList(
                new DefaultArtifactVersion("1.1.2"),
                new DefaultArtifactVersion("1.2.0-SNAPSHOT"),
                new DefaultArtifactVersion("1.3.0-RC1")));

        // WHEN
        Map<Artifact, List<ArtifactVersion>> filtered = filter.filter(updates);

        // THEN
        assertThat(filtered).containsEntry(artifact, newArrayList(new DefaultArtifactVersion("1.1.2")));
    }
}
