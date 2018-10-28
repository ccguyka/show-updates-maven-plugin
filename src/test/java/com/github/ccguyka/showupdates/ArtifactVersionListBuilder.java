package com.github.ccguyka.showupdates;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class ArtifactVersionListBuilder {

    private final List<ArtifactVersion> versions = new ArrayList<>();

    private ArtifactVersionListBuilder() {
        // prevent instantiation
    }

    public static ArtifactVersionListBuilder updates() {
        return new ArtifactVersionListBuilder();
    }

    public ArtifactVersionListBuilder version(String version) {
        versions.add(new DefaultArtifactVersion(version));

        return this;
    }

    public List<ArtifactVersion> build() {
        return versions;
    }

}
