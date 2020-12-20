package com.github.ccguyka.showupdates

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.apache.maven.model.Build
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugin.testing.AbstractMojoTestCase
import org.apache.maven.project.MavenProject
import org.assertj.core.api.Assertions
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

class SendSlackMessageMojoTest : AbstractMojoTestCase() {

    private var project: MavenProject = Mockito.mock(MavenProject::class.java)
    private var log: Log? = null
    private var wireMockServer: WireMockServer? = null
    private var mojo: SendSlackMessageMojo? = null

    @Throws(Exception::class)
    override fun setUp() {
        // required for mojo lookups to work
        super.setUp()
        Mockito.`when`(project.isExecutionRoot()).thenReturn(true)
        Mockito.`when`(project.getArtifactId()).thenReturn("my-artifact")
        useUpdateFolder("updates")
        mojo = lookupEmptyMojo("send-slack-message", "src/test/resources/test-mojo-pom.xml") as SendSlackMessageMojo
        setVariableValueToObject(mojo, "project", project)
        setVariableValueToObject(mojo, "token", "http://localhost:37654/services/aa385a4c/e3aa/fb03934970b6")
        log = Mockito.mock(Log::class.java)
        mojo!!.log = log
        wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(37654))
        wireMockServer!!.start()
    }

    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
        wireMockServer!!.stop()
    }

    @Test
    @Throws(Exception::class)
    fun testNoMessageSendIfNotInRootModule() {
        // GIVEN
        Mockito.`when`(project!!.isExecutionRoot).thenReturn(false)

        // WHEN
        mojo!!.execute()

        // THEN
        Mockito.verify(log)?.info("Only able to send from root folder")
        wireMockServer!!.verify(0, WireMock.postRequestedFor(WireMock.urlMatching("/services/aa385a4c/e3aa/fb03934970b6")))
    }

    @Test
    @Throws(Exception::class)
    fun testNoMessageSendWhenNoUpdatesAvailable() {
        // GIVEN
        useUpdateFolder("noupdates")

        // WHEN
        mojo!!.execute()

        // THEN
        Mockito.verify(log)?.info("Project is up-to-date")
        wireMockServer!!.verify(0, WireMock.postRequestedFor(WireMock.urlMatching("/services/aa385a4c/e3aa/fb03934970b6")))
    }

    @Test
    @Throws(Exception::class)
    fun testMessageSentInCaseOfUpdates() {
        // GIVEN
        useUpdateFolder("updates")
        slackSendSuccessful()

        // WHEN
        mojo!!.execute()

        // THEN
        wireMockServer!!.verify(WireMock.postRequestedFor(WireMock.urlMatching("/services/aa385a4c/e3aa/fb03934970b6"))
                .withRequestBody(WireMock.matching(".*my-artifact.*"))
                .withHeader("Content-Type", WireMock.matching("application/json; charset=UTF-8")))
    }

    @Test
    @Throws(Exception::class)
    fun testMessageSentInCaseOfAggregatedUpdates() {
        // GIVEN
        useUpdateFolder("aggregated-updates")
        slackSendSuccessful()

        // WHEN
        mojo!!.execute()

        // THEN
        wireMockServer!!.verify(WireMock.postRequestedFor(WireMock.urlMatching("/services/aa385a4c/e3aa/fb03934970b6"))
                .withRequestBody(WireMock.matching(".*my-artifact.*"))
                .withHeader("Content-Type", WireMock.matching("application/json; charset=UTF-8")))
    }

    @Test
    @Throws(Exception::class)
    fun testMessageSendingFailed() {
        // GIVEN
        slackSendFailed()

        // WHEN
        mojo!!.execute()

        // THEN
        val logMessage = ArgumentCaptor.forClass(String::class.java)
        Mockito.verify(log)?.error(logMessage.capture())
        Assertions.assertThat(logMessage.value)
                .contains("403")
                .contains("invalid_token")
    }

    private fun slackSendSuccessful() {
        wireMockServer!!.stubFor(WireMock.post(WireMock.urlEqualTo("/services/aa385a4c/e3aa/fb03934970b6"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("ok")))
    }

    private fun slackSendFailed() {
        wireMockServer!!.stubFor(WireMock.post(WireMock.urlEqualTo("/services/aa385a4c/e3aa/fb03934970b6"))
                .willReturn(WireMock.aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("invalid_token")))
    }

    private fun useUpdateFolder(folder: String) {
        val path = this.javaClass.getResource("")
        val build = Mockito.mock(Build::class.java)
        Mockito.`when`(build.directory).thenReturn(path.path + "/slack/" + folder)
        Mockito.`when`(project!!.build).thenReturn(build)
    }
}
