package com.github.ccguyka.showupdates;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;

public class ArtifactBuilder {

    private String version;

    private ArtifactBuilder() {
        // prevent instantiation
    }

    public static ArtifactBuilder anArtifact() {
        return new ArtifactBuilder();
    }

    public ArtifactBuilder version(String version) {
        this.version = version;

        return this;
    }

    public Artifact build() {
        return new DefaultArtifact("groupId", "artifactId", version, "compile", "type", "classifier", null);
    }
}
