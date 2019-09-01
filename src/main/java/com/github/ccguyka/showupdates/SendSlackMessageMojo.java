package com.github.ccguyka.showupdates;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.consumer.ReadUpdatesFile;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;
import com.github.ccguyka.showupdates.slack.SlackClient;
import com.github.ccguyka.showupdates.slack.SlackClient.Response;

/**
 * Aggregates previously created report files
 */
@Mojo(name = "send-slack-message")
public class SendSlackMessageMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    protected MavenProject project;

    /**
     * The projects in the reactor for aggregation report.
     */
    @Parameter( defaultValue = "${reactorProjects}", readonly = true )
    private List<MavenProject> reactorProjects;

    @Parameter(property = "token")
    private String token ;

    @Override
    public void execute() {
        if (!project.isExecutionRoot()) {
            getLog().info("Only able to send from root folder");
            return;
        }

        File updatesFile = getUpdatesFile();
        if (!updatesFile.exists()) {
            getLog().info("No updates file found. Please run 'updates' first");
        }

        ProjectUpdates projectUpdates;
        try {
            projectUpdates = new ReadUpdatesFile().readFromFile(updatesFile);
        } catch (IOException e) {
            getLog().error("Unable to read updates file.", e);
            return;
        }
        if (hasNoUpdates(projectUpdates)) {
            getLog().info("Project is up-to-date");
            return;
        }
        try {
            Response response = SlackClient.forArtifact(project.getArtifactId())
                    .token(token)
                    .updates(projectUpdates)
                    .send();
            checkIfSuccessful(response);
        } catch (IOException e) {
            getLog().error("Error sending updates to slack", e);
        }
    }

    private boolean hasNoUpdates(ProjectUpdates projectUpdates) {
        return projectUpdates.getParent().getArtifacts().isEmpty()
                && projectUpdates.getDependency().getArtifacts().isEmpty()
                && projectUpdates.getDependencyManagement().getArtifacts().isEmpty()
                && projectUpdates.getPlugin().getArtifacts().isEmpty();
    }


    private void checkIfSuccessful(Response response) throws IOException {
        if (response.getCode() >= 300) {
            getLog().error(String.format("Sending message to slack failed. Status code: %s and message %s",
                    response.getCode(), response.getMessage()));
        }
    }

    private File getUpdatesFile() {
        File aggregatedFile = getAggregationFile();
        if (aggregatedFile.exists()) {
            return aggregatedFile;
        }

        String buildDir = project.getBuild().getDirectory();
        return new File(buildDir + "/maven-updates.json");
    }

    protected File getAggregationFile() {
        String buildDir = project.getBuild().getDirectory();
        return new File(buildDir + "/aggregated-maven-updates.json");
    }
}
