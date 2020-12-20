package com.github.ccguyka.showupdates

import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import java.util.*

class ArtifactVersionListBuilder private constructor() {

    private val versions: MutableList<ArtifactVersion> = ArrayList()

    fun version(version: String?): ArtifactVersionListBuilder {
        versions.add(DefaultArtifactVersion(version))
        return this
    }

    fun build(): List<ArtifactVersion> {
        return versions
    }

    companion object {
        @JvmStatic
        fun updates(): ArtifactVersionListBuilder {
            return ArtifactVersionListBuilder()
        }
    }
}
