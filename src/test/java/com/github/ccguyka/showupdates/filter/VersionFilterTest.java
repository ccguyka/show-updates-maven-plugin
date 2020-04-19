package com.github.ccguyka.showupdates.filter;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.entry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.junit.Test;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;

public class VersionFilterTest {

    @Test
    public void filterLatestVersions() {
        final VersionFilter filter = VersionFilter.getFilterVersionsFor("latest");

        final Artifact artifact = currentVersion("1.1.1");
        final List<ArtifactVersion> updateVersions = availableUpdates("1.1.2", "1.1.3", "1.2.0", "1.3.0", "2.0.0", "2.1.5");
        final Map<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        updates.put(artifact, newArrayList(updateVersions));

        final ListMultimap<Artifact, ArtifactVersion> latestUpdates = filter.filter(updates);

        assertThat(latestUpdates).contains(
                entry(artifact, new DefaultArtifactVersion("1.1.3")),
                entry(artifact, new DefaultArtifactVersion("1.3.0")),
                entry(artifact, new DefaultArtifactVersion("2.1.5")));
    }

    @Test
    public void filterPatchVersion() {
        final VersionFilter filter = VersionFilter.getFilterVersionsFor("patch");

        final Artifact artifact = currentVersion("1.1.1");
        final List<ArtifactVersion> updateVersions = availableUpdates("1.1.2", "1.1.3", "1.2.0", "1.3.0", "2.0.0", "2.1.5");
        final Map<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        updates.put(artifact, newArrayList(updateVersions));

        final ListMultimap<Artifact, ArtifactVersion> patchUpdates = filter.filter(updates);

        assertThat(patchUpdates).contains(entry(artifact, new DefaultArtifactVersion("1.1.3")));
    }

    @Test
    public void filterMinorVersion() {
        final VersionFilter filter = VersionFilter.getFilterVersionsFor("minor");

        final Artifact artifact = currentVersion("1.1.1");
        final List<ArtifactVersion> updateVersions = availableUpdates("1.1.2", "1.1.3", "1.2.0", "1.3.0", "2.0.0", "2.1.5");
        final Map<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        updates.put(artifact, newArrayList(updateVersions));

        final ListMultimap<Artifact, ArtifactVersion> minorUpdates = filter.filter(updates);

        assertThat(minorUpdates).contains(entry(artifact, new DefaultArtifactVersion("1.3.0")));
    }

    @Test
    public void filterMajorVersion() {
        final VersionFilter filter = VersionFilter.getFilterVersionsFor("major");

        final Artifact artifact = currentVersion("1.1.1");
        final List<ArtifactVersion> updateVersions = availableUpdates("1.1.2", "1.1.3", "1.2.0", "1.3.0", "2.0.0", "2.1.5");
        final Map<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        updates.put(artifact, newArrayList(updateVersions));

        final ListMultimap<Artifact, ArtifactVersion> majorUpdates = filter.filter(updates);

        assertThat(majorUpdates).contains(entry(artifact, new DefaultArtifactVersion("2.1.5")));
    }

    private DefaultArtifact currentVersion(String version) {
        return new DefaultArtifact("groupId", "artifactId", version, "compile", "type",
                "classifier", null);
    }

    private List<ArtifactVersion> availableUpdates(String... updates) {
        return Lists.newArrayList(updates).stream().map(DefaultArtifactVersion::new).collect(Collectors.toList());
    }
}
