package com.github.ccguyka.showupdates

import org.apache.maven.model.Build
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugin.testing.AbstractMojoTestCase
import org.apache.maven.project.MavenProject
import org.assertj.core.api.Assertions
import org.junit.Test
import org.mockito.Mockito
import java.io.File
import java.util.*

class AggregateUpdatesMojoTest : AbstractMojoTestCase() {

    private val reactorProjects: MutableList<MavenProject> = ArrayList()
    private var log: Log? = null
    private var mojo: AggregateUpdatesMojo? = null

    @Throws(Exception::class)
    override fun setUp() {
        // required for mojo lookups to work
        super.setUp()
        val path = this.javaClass.getResource("")
        val project = Mockito.mock(MavenProject::class.java)
        Mockito.`when`(project.isExecutionRoot()).thenReturn(true)
        val build = Mockito.mock(Build::class.java)
        Mockito.`when`(build.directory).thenReturn(getBasedir() + "/target")
        Mockito.`when`(project.getBuild()).thenReturn(build)
        val buildA = Mockito.mock(Build::class.java)
        Mockito.`when`(buildA.directory).thenReturn(path.path + "/projectA")
        val projectA = Mockito.mock(MavenProject::class.java)
        Mockito.`when`(projectA.build).thenReturn(buildA)
        val buildB = Mockito.mock(Build::class.java)
        Mockito.`when`(buildB.directory).thenReturn(path.path + "/projectB")
        val projectB = Mockito.mock(MavenProject::class.java)
        Mockito.`when`(projectB.build).thenReturn(buildB)
        reactorProjects.add(projectA)
        reactorProjects.add(projectB)
        mojo = lookupEmptyMojo("aggregate", "src/test/resources/test-mojo-pom.xml") as AggregateUpdatesMojo
        setVariableValueToObject(mojo, "project", project)
        setVariableValueToObject(mojo, "reactorProjects", reactorProjects)
        log = Mockito.mock(Log::class.java)
        mojo!!.log = log
    }

    @Test
    @Throws(Exception::class)
    fun testAggregation() {
        mojo!!.execute()
        val result = File(getBasedir() + "/target/aggregated-maven-updates.json")
        val expected = File(this.javaClass.getResource("aggregated-maven-updates.json").path)
        Assertions.assertThat(result).hasSameContentAs(expected)
    }
}
