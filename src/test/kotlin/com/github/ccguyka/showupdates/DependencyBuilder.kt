package com.github.ccguyka.showupdates

import org.apache.maven.artifact.Artifact
import org.apache.maven.model.Dependency

internal object DependencyBuilder {

    @JvmStatic
    fun aDependency(artifact: Artifact): Dependency {
        val dependency = Dependency()
        dependency.groupId = artifact.groupId
        dependency.artifactId = artifact.artifactId
        dependency.version = artifact.version
        dependency.scope = artifact.scope
        dependency.type = artifact.type
        dependency.classifier = artifact.classifier
        return dependency
    }
}
