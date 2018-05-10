package com.github.ccguyka.showupdates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import com.google.common.collect.Iterables;

class FilterLatestUpdates {

    Map<Artifact, ArtifactVersion> getLatestUpdates(final Map<Artifact, List<ArtifactVersion>> updates) {
        final Map<Artifact, ArtifactVersion> latestVersions = new HashMap<>();
        for (final Entry<Artifact, List<ArtifactVersion>> update : updates.entrySet()) {
            if (!update.getValue().isEmpty()) {
                latestVersions.put(update.getKey(), Iterables.getLast(update.getValue()));
            }
        }

        return latestVersions;
    }

}
