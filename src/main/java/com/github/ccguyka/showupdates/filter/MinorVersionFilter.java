package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import com.google.common.collect.Iterables;

class MinorVersionFilter implements VersionFilter {

    @Override
    public Map<Artifact, ArtifactVersion> filter(Map<Artifact, List<ArtifactVersion>> updates) {
        final Map<Artifact, ArtifactVersion> latestVersions = new HashMap<>();
        for (final Entry<Artifact, List<ArtifactVersion>> update : updates.entrySet()) {
            final int majorVersion = new DefaultArtifactVersion(update.getKey().getVersion()).getMajorVersion();
            final List<ArtifactVersion> latestMinorUpdate = update.getValue().stream()
                    .filter(artifactVersion -> artifactVersion.getMajorVersion() == majorVersion)
                    .collect(toList());
            if (!latestMinorUpdate.isEmpty()) {
                latestVersions.put(update.getKey(), Iterables.getLast(latestMinorUpdate));
            }
        }

        return latestVersions;
    }
}
