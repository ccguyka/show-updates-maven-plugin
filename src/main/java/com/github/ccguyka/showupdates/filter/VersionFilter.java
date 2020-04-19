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

public interface VersionFilter {

    default Map<Artifact, ArtifactVersion> filter(final Map<Artifact, List<ArtifactVersion>> updates) {
        final Map<Artifact, ArtifactVersion> latestVersions = new HashMap<>();
        for (final Entry<Artifact, List<ArtifactVersion>> update : updates.entrySet()) {
            final DefaultArtifactVersion updateVersion = new DefaultArtifactVersion(update.getKey().getVersion());
            final List<ArtifactVersion> latestMinorUpdate = update.getValue().stream()
                    .filter(artifactVersion -> filter(updateVersion, artifactVersion))
                    .collect(toList());
            if (!latestMinorUpdate.isEmpty()) {
                latestVersions.put(update.getKey(), Iterables.getLast(latestMinorUpdate));
            }
        }

        return latestVersions;
    }

    default boolean filter(final DefaultArtifactVersion updateVersion, final ArtifactVersion artifactVersion) {
        return false;
    }

    static VersionFilter getFilterVersionsFor(final String versions) {

        switch (versions) {
        case "patch":
            return new PatchVersionFilter();
        case "minor":
            return new MinorVersionFilter();
        default:
            return new MajorVersionFilter();
        }
    }
}
