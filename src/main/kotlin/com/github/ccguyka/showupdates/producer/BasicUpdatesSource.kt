package com.github.ccguyka.showupdates.producer

import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.versioning.ArtifactVersion
import java.util.stream.Collectors

open class BasicUpdatesSource {

    protected fun from(artifact: Artifact, artifactVersion: Collection<ArtifactVersion>?): ArtifactUpdate? {
        return if (artifactVersion == null || artifactVersion.isEmpty()) {
            null
        } else ArtifactUpdate(artifact.groupId + ":" + artifact.artifactId, artifact.version,
                artifactVersion.stream().map { obj: ArtifactVersion -> obj.toString() }.collect(Collectors.toList()))
    }
}
