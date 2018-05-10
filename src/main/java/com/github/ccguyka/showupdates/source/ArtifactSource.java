package com.github.ccguyka.showupdates.source;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.model.Dependency;

public class ArtifactSource {

    private final ArtifactFactory artifactFactory;

    public ArtifactSource(final ArtifactFactory artifactFactory) {
        this.artifactFactory = artifactFactory;
    }

    public List<Artifact> getArtifacts(final List<Dependency> dependencies) {
        final List<Artifact> artifacts = new ArrayList<>();
        for (final Dependency dependency : dependencies) {
            artifacts.add(artifactFactory.createArtifact(dependency.getGroupId(), dependency.getArtifactId(),
                    dependency.getVersion(), dependency.getScope(), dependency.getType()));
        }
        return artifacts;
    }

}
