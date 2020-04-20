package com.github.ccguyka.showupdates.producer;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import com.github.ccguyka.showupdates.objects.ArtifactUpdate;

class BasicUpdatesSource {

    protected ArtifactUpdate from(final Artifact artifact, final Collection<ArtifactVersion> artifactVersion) {
        if (artifactVersion == null || artifactVersion.isEmpty()) {
            return null;
        }
        return new ArtifactUpdate(artifact.getGroupId() + ":" + artifact.getArtifactId(), artifact.getVersion(),
                artifactVersion.stream().map(ArtifactVersion::toString).collect(Collectors.toList()));
    }
}
