package com.github.ccguyka.showupdates.producer

import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.factory.ArtifactFactory
import org.apache.maven.model.Dependency
import java.util.*

class ArtifactSource(private val artifactFactory: ArtifactFactory) {

    fun getArtifacts(dependencies: List<Dependency>): List<Artifact> {
        val artifacts: MutableList<Artifact> = ArrayList()
        dependencies.stream().forEach { dependency: Dependency -> artifacts.add(createArtifactFrom(dependency)) }
        return artifacts
    }

    private fun createArtifactFrom(dependency: Dependency): Artifact {
        return artifactFactory.createArtifact(dependency.groupId, dependency.artifactId,
                dependency.version, dependency.scope, dependency.type)
    }
}
