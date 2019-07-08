package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import com.google.common.collect.Iterables;

class MajorVersionFilter implements VersionFilter {

    @Override
    public Map<Artifact, ArtifactVersion> filter(final Map<Artifact, List<ArtifactVersion>> updates) {
        final Map<Artifact, ArtifactVersion> latestVersions = new HashMap<>();
        for (final Entry<Artifact, List<ArtifactVersion>> update : updates.entrySet()) {
            final List<ArtifactVersion> latestMajorVersion = update.getValue().stream().collect(toList());
            if (!latestMajorVersion.isEmpty()) {
                latestVersions.put(update.getKey(), Iterables.getLast(latestMajorVersion));
            }
        }

        return latestVersions;
    }

}
