package com.github.ccguyka.showupdates.sink;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.plugin.logging.Log;

public class LogUpdatesSink {

    private final Log log;
    private final String type;

    public LogUpdatesSink(final String type, final Log log) {
        this.type = type;
        this.log = log;
    }

    public void printUpdates(final Map<Artifact, ArtifactVersion> latestUpdates) {
        if (!latestUpdates.isEmpty()) {
            log.info("Available " + type + " updates:");
            for (final Entry<Artifact, ArtifactVersion> latestUpdate : latestUpdates.entrySet()) {
                log.info(getLineText(latestUpdate));
            }
        }
    }

    private String getLineText(final Entry<Artifact, ArtifactVersion> latestUpdate) {
        final StringBuilder result = new StringBuilder();
        result.append("  ");
        result.append(getDependency(latestUpdate.getKey()));
        result.append(" ... ");
        result.append(getCurrentVersion(latestUpdate.getKey()));
        result.append(" -> ");
        result.append(latestUpdate.getValue());
        return result.toString();
    }

    private String getCurrentVersion(final Artifact artifact) {
        return artifact.getBaseVersion();
    }

    private String getDependency(final Artifact artifact) {
        return artifact.getGroupId() + ":" + artifact.getArtifactId();
    }
}
