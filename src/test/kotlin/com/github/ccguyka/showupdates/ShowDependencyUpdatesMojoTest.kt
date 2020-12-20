package com.github.ccguyka.showupdates

import com.github.ccguyka.showupdates.ArtifactBuilder.Companion.aDependency
import com.github.ccguyka.showupdates.ArtifactVersionListBuilder.Companion.updates
import com.github.ccguyka.showupdates.DependencyBuilder.aDependency
import com.google.common.collect.Lists
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.factory.ArtifactFactory
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException
import org.apache.maven.artifact.metadata.ArtifactMetadataSource
import org.apache.maven.artifact.repository.ArtifactRepository
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.model.Build
import org.apache.maven.model.Dependency
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugin.testing.AbstractMojoTestCase
import org.apache.maven.project.MavenProject
import org.junit.Test
import org.mockito.Mockito
import java.io.File
import java.util.*

class ShowDependencyUpdatesMojoTest : AbstractMojoTestCase() {

    private var artifactMetadataSource: ArtifactMetadataSource? = null
    private var artifactFactory: ArtifactFactory? = null
    private var remoteArtifactRepositories: List<ArtifactRepository> = ArrayList()
    private var localRepository: ArtifactRepository? = null
    private var project: MavenProject = Mockito.mock(MavenProject::class.java)
    private var log: Log? = null
    private var mojo: ShowUpdatesMojo? = null

    @Throws(Exception::class)
    override fun setUp() {
        // required for mojo lookups to work
        super.setUp()
        artifactMetadataSource = Mockito.mock(ArtifactMetadataSource::class.java)
        artifactFactory = Mockito.mock(ArtifactFactory::class.java)

        localRepository = Mockito.mock(ArtifactRepository::class.java)
        val build = Mockito.mock(Build::class.java)
        Mockito.`when`(build.directory).thenReturn(getBasedir() + "/target")
        Mockito.`when`(project.build).thenReturn(build)
        Mockito.`when`(project.file).thenReturn(File(getBasedir() + "/src/test/resources/test-mojo-pom.xml"))
        mojo = lookupEmptyMojo("updates", "src/test/resources/test-mojo-pom.xml") as ShowUpdatesMojo
        setVariableValueToObject(mojo, "artifactMetadataSource", artifactMetadataSource)
        setVariableValueToObject(mojo, "artifactFactory", artifactFactory)
        setVariableValueToObject(mojo, "remoteArtifactRepositories", remoteArtifactRepositories)
        setVariableValueToObject(mojo, "localRepository", localRepository)
        setVariableValueToObject(mojo, "project", project)
        log = Mockito.mock(Log::class.java)
        mojo!!.log = log
    }

    @Test
    @Throws(Exception::class)
    fun testExcludeBlacklistedUpdates() {
        val artifact = aDependency().version("1.1.1").build()
        val dependencies: List<Dependency> = Lists.newArrayList(aDependency(artifact))
        Mockito.`when`(project.dependencies).thenReturn(dependencies)
        val updates = updates()
                .version("1.2.0")
                .version("2.0.0-beta1")
                .version("2.0.0-alpha1")
                .version("2.0.0-SNAPSHOT").build()
        mockUpdates(artifact, updates)
        mojo!!.execute()
        Mockito.verify(log)?.info("Available dependency updates:")
        Mockito.verify(log)?.info("  dep-groupId:dep-artifactId ... 1.1.1 -> 1.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testExcludeBlacklistedUpdatesWithParameters() {
        val artifact = aDependency().version("1.1.1").build()
        val dependencies: List<Dependency> = Lists.newArrayList(aDependency(artifact))
        Mockito.`when`(project.dependencies).thenReturn(dependencies)
        val updates = updates()
                .version("1.2.0")
                .version("2.0.0-test").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "excludes", arrayOf("test"))
        mojo!!.execute()
        Mockito.verify(log)?.info("Available dependency updates:")
        Mockito.verify(log)?.info("  dep-groupId:dep-artifactId ... 1.1.1 -> 1.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testMajorUpdates() {
        val artifact = aDependency().version("1.1.1").build()
        val dependencies: List<Dependency> = Lists.newArrayList(aDependency(artifact))
        Mockito.`when`(project.dependencies).thenReturn(dependencies)
        val updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "versions", "major")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available dependency updates:")
        Mockito.verify(log)?.info("  dep-groupId:dep-artifactId ... 1.1.1 -> 2.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testMinorUpdates() {
        val artifact = aDependency().version("1.1.1").build()
        val dependencies: List<Dependency> = Lists.newArrayList(aDependency(artifact))
        Mockito.`when`(project.dependencies).thenReturn(dependencies)
        val updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "versions", "minor")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available dependency updates:")
        Mockito.verify(log)?.info("  dep-groupId:dep-artifactId ... 1.1.1 -> 1.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testPatchUpdates() {
        val artifact = aDependency().version("1.1.1").build()
        val dependencies: List<Dependency> = Lists.newArrayList(aDependency(artifact))
        Mockito.`when`(project.dependencies).thenReturn(dependencies)
        val updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "versions", "patch")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available dependency updates:")
        Mockito.verify(log)?.info("  dep-groupId:dep-artifactId ... 1.1.1 -> 1.1.3")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testLatestUpdates() {
        val artifact = aDependency().version("1.1.1").build()
        val dependencies: List<Dependency> = Lists.newArrayList(aDependency(artifact))
        Mockito.`when`(project.dependencies).thenReturn(dependencies)
        val updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "versions", "latest")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available dependency updates:")
        Mockito.verify(log)?.info("  dep-groupId:dep-artifactId ... 1.1.1 -> 1.1.3, 1.2.0, 2.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testTransitiveUpdates() {
        val artifact = aDependency().version("1.1.1").build()
        val transitiveArtifact = aDependency().groupId("another-groupId").version("2.0.0").build()
        val dependencies: List<Dependency> = Lists.newArrayList(aDependency(artifact), aDependency(transitiveArtifact))
        Mockito.`when`(project.dependencies).thenReturn(dependencies)
        mockUpdates(artifact, updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build())
        mockUpdates(transitiveArtifact, updates()
                .version("2.2.0").build())
        setVariableValueToObject(mojo, "versions", "minor")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available dependency updates:")
        Mockito.verify(log)?.info("  dep-groupId:dep-artifactId ... 1.1.1 -> 1.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Throws(ArtifactMetadataRetrievalException::class)
    private fun mockUpdates(artifact: Artifact, updates: List<ArtifactVersion>) {
        Mockito.`when`(artifactFactory!!.createArtifact(artifact.groupId, artifact.artifactId, artifact.version,
                artifact.scope, artifact.type)).thenReturn(artifact)
        Mockito.`when`(artifactMetadataSource
                ?.retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates)
    }
}
