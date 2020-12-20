package com.github.ccguyka.showupdates.producer

import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException
import org.apache.maven.artifact.metadata.ArtifactMetadataSource
import org.apache.maven.artifact.repository.ArtifactRepository
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.artifact.versioning.OverConstrainedVersionException
import org.apache.maven.plugin.logging.Log
import java.util.*

class UpdateSource(private val artifactMetadataSource: ArtifactMetadataSource, private val localRepository: ArtifactRepository,
                   private val remoteArtifactRepositories: List<ArtifactRepository>, private val log: Log) {
    fun getUpdate(artifact: Artifact): Map<Artifact, List<ArtifactVersion>> {
        val updates: MutableMap<Artifact, List<ArtifactVersion>> = HashMap()
        try {
            val retrieveAvailableVersions = artifactMetadataSource
                    .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)
            updates[artifact] = filter(artifact, retrieveAvailableVersions)
        } catch (e: ArtifactMetadataRetrievalException) {
            log.warn("Not able to get updates")
        } catch (e: OverConstrainedVersionException) {
            log.warn("Not able to get updates")
        }
        return updates
    }

    fun getUpdates(artifacts: Collection<Artifact>): Map<Artifact, List<ArtifactVersion>> {
        val updates: MutableMap<Artifact, List<ArtifactVersion>> = HashMap()
        artifacts.stream().forEach { artifact: Artifact -> getArtifactUpdate(artifact, updates) }
        return updates
    }

    private fun getArtifactUpdate(artifact: Artifact, updates: MutableMap<Artifact, List<ArtifactVersion>>) {
        try {
            updates[artifact] = getArtifactUpdate(artifact)
        } catch (e: ArtifactMetadataRetrievalException) {
            log.warn("Not able to get updates")
        } catch (e: OverConstrainedVersionException) {
            log.warn("Not able to get updates")
        }
    }

    @Throws(ArtifactMetadataRetrievalException::class, OverConstrainedVersionException::class)
    private fun getArtifactUpdate(artifact: Artifact): List<ArtifactVersion> {
        val retrieveAvailableVersions = artifactMetadataSource
                .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)
        return filter(artifact, retrieveAvailableVersions)
    }

    @Throws(OverConstrainedVersionException::class)
    private fun filter(artifact: Artifact, retrieveAvailableVersions: List<ArtifactVersion>): List<ArtifactVersion> {
        val updates: MutableList<ArtifactVersion> = ArrayList()
        for (availableVersion in retrieveAvailableVersions) {
            if (availableVersion.majorVersion > artifact.selectedVersion.majorVersion) {
                updates.add(availableVersion)
            } else if (availableVersion.majorVersion == artifact.selectedVersion.majorVersion) {
                if (availableVersion.minorVersion > artifact.selectedVersion.minorVersion) {
                    updates.add(availableVersion)
                } else if (availableVersion.minorVersion == artifact.selectedVersion.minorVersion) {
                    if (availableVersion.incrementalVersion > artifact.selectedVersion
                                    .incrementalVersion) {
                        updates.add(availableVersion)
                    }
                }
            }
        }
        return updates
    }
}
