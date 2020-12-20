package com.github.ccguyka.showupdates

import com.github.ccguyka.showupdates.ArtifactBuilder.Companion.aParent
import com.github.ccguyka.showupdates.ArtifactVersionListBuilder.Companion.updates
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.factory.ArtifactFactory
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException
import org.apache.maven.artifact.metadata.ArtifactMetadataSource
import org.apache.maven.artifact.repository.ArtifactRepository
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.execution.MavenSession
import org.apache.maven.model.Build
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugin.testing.AbstractMojoTestCase
import org.apache.maven.project.MavenProject
import org.junit.Test
import org.mockito.Mockito
import java.util.ArrayList

class ShowParentUpdatesMojoTest : AbstractMojoTestCase() {

    private var artifactMetadataSource: ArtifactMetadataSource? = null
    private var remoteArtifactRepositories: List<ArtifactRepository> = ArrayList()
    private var localRepository: ArtifactRepository? = null
    private var project: MavenProject = Mockito.mock(MavenProject::class.java)
    private var log: Log? = null
    private var mojo: ShowUpdatesMojo? = null

    @Throws(Exception::class)
    override fun setUp() {
        // required for mojo lookups to work
        super.setUp()
        val mavenSession = Mockito.mock(MavenSession::class.java)
        artifactMetadataSource = Mockito.mock(ArtifactMetadataSource::class.java)
        val artifactFactory = Mockito.mock(ArtifactFactory::class.java)
        localRepository = Mockito.mock(ArtifactRepository::class.java)
        val build = Mockito.mock(Build::class.java)
        Mockito.`when`(build.directory).thenReturn(getBasedir() + "/target")
        Mockito.`when`(project.getBuild()).thenReturn(build)
        mojo = lookupEmptyMojo("updates", "src/test/resources/test-mojo-pom.xml") as ShowUpdatesMojo
        setVariableValueToObject(mojo, "mavenSession", mavenSession)
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
    fun testParentUpdates() {
        val artifact = aParent().version("1.2.3").build()
        Mockito.`when`(project!!.parentArtifact).thenReturn(artifact)
        val updates = updates()
                .version("1.2.4")
                .version("2.1.3").build()
        mockUpdates(artifact, updates)
        mojo!!.execute()
        Mockito.verify(log)?.info("Available parent updates:")
        Mockito.verify(log)?.info("  parent-groupId:parent-artifactId ... 1.2.3 -> 1.2.4, 2.1.3")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testNoUpdates() {
        val artifact = aParent().version("1.2.3").build()
        Mockito.`when`(project!!.parentArtifact).thenReturn(artifact)
        val updates = updates().build()
        mockUpdates(artifact, updates)
        mojo!!.execute()
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testExcludeBlacklistedUpdates() {
        val artifact = aParent().version("1.1.1").build()
        Mockito.`when`(project!!.parentArtifact).thenReturn(artifact)
        val updates = updates()
                .version("1.2.0")
                .version("2.0.0-beta1")
                .version("2.0.0-alpha1")
                .version("2.0.0-SNAPSHOT").build()
        mockUpdates(artifact, updates)
        mojo!!.execute()
        Mockito.verify(log)?.info("Available parent updates:")
        Mockito.verify(log)?.info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testExcludeBlacklistedUpdatesWithParameters() {
        val artifact = aParent().version("1.1.1").build()
        Mockito.`when`(project!!.parentArtifact).thenReturn(artifact)
        val updates = updates()
                .version("1.2.0")
                .version("2.0.0-test").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "excludes", arrayOf("test"))
        mojo!!.execute()
        Mockito.verify(log)?.info("Available parent updates:")
        Mockito.verify(log)?.info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testMajorUpdates() {
        val artifact = aParent().version("1.1.1").build()
        Mockito.`when`(project!!.parentArtifact).thenReturn(artifact)
        val updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "versions", "major")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available parent updates:")
        Mockito.verify(log)?.info("  parent-groupId:parent-artifactId ... 1.1.1 -> 2.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testMinorUpdates() {
        val artifact = aParent().version("1.1.1").build()
        Mockito.`when`(project!!.parentArtifact).thenReturn(artifact)
        val updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "versions", "minor")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available parent updates:")
        Mockito.verify(log)?.info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testPatchUpdates() {
        val artifact = aParent().version("1.1.1").build()
        Mockito.`when`(project!!.parentArtifact).thenReturn(artifact)
        val updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "versions", "patch")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available parent updates:")
        Mockito.verify(log)?.info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.1.3")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Test
    @Throws(Exception::class)
    fun testLatestUpdates() {
        val artifact = aParent().version("1.1.1").build()
        Mockito.`when`(project!!.parentArtifact).thenReturn(artifact)
        val updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build()
        mockUpdates(artifact, updates)
        setVariableValueToObject(mojo, "versions", "latest")
        mojo!!.execute()
        Mockito.verify(log)?.info("Available parent updates:")
        Mockito.verify(log)?.info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.1.3, 1.2.0, 2.2.0")
        Mockito.verifyNoMoreInteractions(log)
    }

    @Throws(ArtifactMetadataRetrievalException::class)
    private fun mockUpdates(artifact: Artifact, updates: List<ArtifactVersion>) {
        Mockito.`when`(artifactMetadataSource
                ?.retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates)
    }
}
