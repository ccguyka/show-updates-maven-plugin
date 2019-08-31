package com.github.ccguyka.showupdates;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.apache.maven.model.Build;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.github.tomakehurst.wiremock.WireMockServer;

public class SendSlackMessageMojoTest extends AbstractMojoTestCase {

    private MavenProject project;
    private Log log;
    private WireMockServer wireMockServer;

    private SendSlackMessageMojo mojo;

    @Override
    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();

        project = mock(MavenProject.class);
        when(project.isExecutionRoot()).thenReturn(true);
        when(project.getArtifactId()).thenReturn("my-artifact");
        useUpdateFolder("updates");

        mojo = (SendSlackMessageMojo) lookupEmptyMojo("send-slack-message", "src/test/resources/test-mojo-pom.xml");
        setVariableValueToObject(mojo, "project", project);
        setVariableValueToObject(mojo, "token", "http://localhost:37654/services/aa385a4c/e3aa/fb03934970b6");

        log = mock(Log.class);
        mojo.setLog(log);

        wireMockServer = new WireMockServer(wireMockConfig().port(37654));
        wireMockServer.start();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        wireMockServer.stop();
    }

    @Test
    public void testNoMessageSendIfNotInRootModule() throws Exception {
        // GIVEN
        when(project.isExecutionRoot()).thenReturn(false);

        // WHEN
        mojo.execute();

        // THEN
        verify(log).info("Only able to send from root folder");
        wireMockServer.verify(0, postRequestedFor(urlMatching("/services/aa385a4c/e3aa/fb03934970b6")));
    }

    @Test
    public void testNoMessageSendWhenNoUpdatesAvailable() throws Exception {
        // GIVEN
        useUpdateFolder("noupdates");

        // WHEN
        mojo.execute();

        // THEN
        verify(log).info("Project is up-to-date");
        wireMockServer.verify(0, postRequestedFor(urlMatching("/services/aa385a4c/e3aa/fb03934970b6")));
    }

    @Test
    public void testMessageSentInCaseOfUpdates() throws Exception {
        // GIVEN
        useUpdateFolder("updates");
        slackSendSuccessful();

        // WHEN
        mojo.execute();

        // THEN
        wireMockServer.verify(postRequestedFor(urlMatching("/services/aa385a4c/e3aa/fb03934970b6"))
                .withRequestBody(matching(".*my-artifact.*"))
                .withHeader("Content-Type", matching("application/json; charset=UTF-8")));
    }

    @Test
    public void testMessageSentInCaseOfAggregatedUpdates() throws Exception {
        // GIVEN
        useUpdateFolder("aggregated-updates");
        slackSendSuccessful();

        // WHEN
        mojo.execute();

        // THEN
        wireMockServer.verify(postRequestedFor(urlMatching("/services/aa385a4c/e3aa/fb03934970b6"))
                .withRequestBody(matching(".*my-artifact.*"))
                .withHeader("Content-Type", matching("application/json; charset=UTF-8")));
    }

    @Test
    public void testMessageSendingFailed() throws Exception {
        // GIVEN
        slackSendFailed();

        // WHEN
        mojo.execute();

        // THEN
        ArgumentCaptor<String> logMessage = ArgumentCaptor.forClass(String.class);
        verify(log).error(logMessage.capture());
        assertThat(logMessage.getValue())
            .contains("403")
            .contains("invalid_token");
    }

    private void slackSendSuccessful() {
        wireMockServer.stubFor(post(urlEqualTo("/services/aa385a4c/e3aa/fb03934970b6"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml")
                    .withBody("ok")));

    }

    private void slackSendFailed() {
        wireMockServer.stubFor(post(urlEqualTo("/services/aa385a4c/e3aa/fb03934970b6"))
                .willReturn(aResponse()
                    .withStatus(403)
                    .withHeader("Content-Type", "text/xml")
                    .withBody("invalid_token")));

    }

    private void useUpdateFolder(String folder) {
        URL path = this.getClass().getResource("");
        Build build = mock(Build.class);
        when(build.getDirectory()).thenReturn(path.getPath() + "/slack/" + folder);
        when(project.getBuild()).thenReturn(build);
    }
}
