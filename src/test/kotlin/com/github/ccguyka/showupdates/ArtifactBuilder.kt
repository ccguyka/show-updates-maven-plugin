package com.github.ccguyka.showupdates

import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact

class ArtifactBuilder private constructor() {

    private var version: String? = null
    private var groupId: String? = null
    private var artifactId: String? = null

    fun version(version: String?): ArtifactBuilder {
        this.version = version
        return this
    }

    fun groupId(groupId: String?): ArtifactBuilder {
        this.groupId = groupId
        return this
    }

    fun artifactId(artifactId: String?): ArtifactBuilder {
        this.artifactId = artifactId
        return this
    }

    fun build(): Artifact {
        return DefaultArtifact(groupId, artifactId, version, "compile", "type", "classifier", null)
    }

    companion object {
        @JvmStatic
        fun aParent(): ArtifactBuilder {
            return ArtifactBuilder().groupId("parent-groupId").artifactId("parent-artifactId")
        }

        @JvmStatic
        fun aDependency(): ArtifactBuilder {
            return ArtifactBuilder().groupId("dep-groupId").artifactId("dep-artifactId")
        }

        @JvmStatic
        fun aManagedDependency(): ArtifactBuilder {
            return ArtifactBuilder().groupId("dep-mgnt-groupId").artifactId("dep-mgnt-artifactId")
        }

        @JvmStatic
        fun aPlugin(): ArtifactBuilder {
            return ArtifactBuilder().groupId("plugin-groupId").artifactId("plugin-artifactId")
        }
    }
}
