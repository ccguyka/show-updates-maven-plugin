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

public class MajorVersionFilterTest {

    private MajorVersionFilter filter = new MajorVersionFilter();

    @Test
    public void filterMinorVersion() throws Exception {
        Map<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        final Artifact artifact = new DefaultArtifact("groupId", "artifactId", "1.1.1", "compile", "type",
                "classifier", null);
        final List<ArtifactVersion> updateVersions = new ArrayList<>();
        updateVersions.add(new DefaultArtifactVersion("1.2.0"));
        updateVersions.add(new DefaultArtifactVersion("2.0.0"));
        updateVersions.add(new DefaultArtifactVersion("2.1.0"));
        updates.put(artifact, newArrayList(updateVersions));

        Map<Artifact, ArtifactVersion> minorUpdate = filter.filter(updates);

        assertThat(minorUpdate).containsEntry(artifact, new DefaultArtifactVersion("2.1.0"));
    }
}
