package com.github.ccguyka.showupdates.producer;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.google.common.collect.Lists;

class BasicUpdatesSource {

    protected ArtifactUpdate from(Artifact artifact, ArtifactVersion artifactVersion) {
        if (artifactVersion == null) {
            return null;
        }
        return new ArtifactUpdate(artifact.getGroupId() + ":" + artifact.getArtifactId(), artifact.getVersion(),
                Lists.newArrayList(artifactVersion.toString()));
    }
}
