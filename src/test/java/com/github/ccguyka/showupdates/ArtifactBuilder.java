package com.github.ccguyka.showupdates;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;

public class ArtifactBuilder {

    private String version;
    private String groupId;
    private String artifactId;

    private ArtifactBuilder() {
        // prevent instantiation
    }

    public static ArtifactBuilder aParent() {
        return new ArtifactBuilder().groupId("parent-groupId").artifactId("parent-artifactId");
    }

    public static ArtifactBuilder aDependency() {
        return new ArtifactBuilder().groupId("dep-groupId").artifactId("dep-artifactId");
    }

    public static ArtifactBuilder aManagedDependency() {
        return new ArtifactBuilder().groupId("dep-mgnt-groupId").artifactId("dep-mgnt-artifactId");
    }

    public static ArtifactBuilder aPlugin() {
        return new ArtifactBuilder().groupId("plugin-groupId").artifactId("plugin-artifactId");
    }

    public ArtifactBuilder version(String version) {
        this.version = version;

        return this;
    }

    public ArtifactBuilder groupId(String groupId) {
        this.groupId = groupId;

        return this;
    }

    public ArtifactBuilder artifactId(String artifactId) {
        this.artifactId  = artifactId;

        return this;
    }

    public Artifact build() {
        return new DefaultArtifact(groupId, artifactId, version, "compile", "type", "classifier", null);
    }
}
