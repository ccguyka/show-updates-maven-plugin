package com.github.ccguyka.showupdates

import com.github.ccguyka.showupdates.consumer.ReadUpdatesFile
import com.github.ccguyka.showupdates.objects.ProjectUpdates
import com.github.ccguyka.showupdates.slack.SlackClient
import com.github.ccguyka.showupdates.slack.SlackClient.Companion.forArtifact
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import java.io.File
import java.io.IOException

/**
 * Aggregates previously created report files
 */
@Mojo(name = "send-slack-message")
class SendSlackMessageMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}")
    protected var project: MavenProject? = null

    /**
     * The projects in the reactor for aggregation report.
     */
    @Parameter(defaultValue = "\${reactorProjects}", readonly = true)
    private val reactorProjects: List<MavenProject>? = null

    @Parameter(property = "token")
    private val token: String? = null
    override fun execute() {
        if (!project!!.isExecutionRoot) {
            log.info("Only able to send from root folder")
            return
        }
        val updatesFile = updatesFile
        if (!updatesFile.exists()) {
            log.info("No updates file found. Please run 'updates' first")
        }
        val projectUpdates: ProjectUpdates
        projectUpdates = try {
            ReadUpdatesFile().readFromFile(updatesFile)
        } catch (e: IOException) {
            log.error("Unable to read updates file.", e)
            return
        }
        if (hasNoUpdates(projectUpdates)) {
            log.info("Project is up-to-date")
            return
        }
        try {
            val response = forArtifact(project!!.artifactId)
                    .token(token)
                    .updates(projectUpdates)
                    .send()
            checkIfSuccessful(response)
        } catch (e: IOException) {
            log.error("Error sending updates to slack", e)
        }
    }

    private fun hasNoUpdates(projectUpdates: ProjectUpdates): Boolean {
        return (projectUpdates.parent.artifacts.isEmpty()
                && projectUpdates.dependency.artifacts.isEmpty()
                && projectUpdates.dependencyManagement.artifacts.isEmpty()
                && projectUpdates.plugin.artifacts.isEmpty())
    }

    private fun checkIfSuccessful(response: SlackClient.Response) {
        if (response.code >= 300) {
            log.error(String.format("Sending message to slack failed. Status code: %s and message %s",
                    response.code, response.message))
        }
    }

    private val updatesFile: File
        private get() {
            val aggregatedFile = aggregationFile
            if (aggregatedFile.exists()) {
                return aggregatedFile
            }
            val buildDir = project!!.build.directory
            return File("$buildDir/maven-updates.json")
        }

    protected val aggregationFile: File
        protected get() {
            val buildDir = project!!.build.directory
            return File("$buildDir/aggregated-maven-updates.json")
        }
}
