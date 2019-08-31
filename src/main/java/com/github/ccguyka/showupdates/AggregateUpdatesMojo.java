package com.github.ccguyka.showupdates;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.consumer.PrintUpdates;
import com.github.ccguyka.showupdates.consumer.ResultAggregator;
import com.github.ccguyka.showupdates.consumer.SaveUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

/**
 * Aggregates previously created report files
 */
@Mojo(name = "aggregate")
public class AggregateUpdatesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    protected MavenProject project;

    /**
     * The projects in the reactor for aggregation report.
     */
    @Parameter( defaultValue = "${reactorProjects}", readonly = true )
    private List<MavenProject> reactorProjects;

    @Override
    public void execute() {
        if (project.isExecutionRoot()) {
            List<File> reportFiles = reactorProjects.stream().map(this::getReportFile).collect(toList());
            ProjectUpdates aggregate = ResultAggregator.aggregate(reportFiles, getLog());
            PrintUpdates.print(aggregate, getLog());
            SaveUpdates.save(aggregate, getLog(), getAggregationReportFile());
        } else {
            getLog().info("Aggregation is only executed in root module");
        }
    }

    protected File getReportFile(MavenProject project) {
        String buildDir = project.getBuild().getDirectory();
        return new File(buildDir + "/maven-updates.json");
    }

    protected File getAggregationReportFile() {
        String buildDir = project.getBuild().getDirectory();
        return new File(buildDir + "/aggregated-maven-updates.json");
    }
}
