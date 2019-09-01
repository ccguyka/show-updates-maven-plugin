package com.github.ccguyka.showupdates.consumer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.ccguyka.showupdates.objects.ProjectUpdates;

public class ResultAggregator {

    private final List<File> reportFiles;
    private final ReadUpdatesFile readUpdatesFile;
    private final ProjectUpdatesMerger merger;
    private final Log log;

    private ResultAggregator(List<File> reportFiles, Log log) {
        this.reportFiles = reportFiles;
        this.log = log;

        this.readUpdatesFile = new ReadUpdatesFile();
        this.merger = new ProjectUpdatesMerger();
    }

    public static ProjectUpdates aggregate(List<File> reportFiles, Log log) {
        ResultAggregator aggregator = new ResultAggregator(reportFiles, log);
        return aggregator.aggregate();
    }

    private ProjectUpdates aggregate() {
        ProjectUpdates completeProjectUpdates = ProjectUpdates.builder().build();
        for(File reportFile : reportFiles) {
            try {
                ProjectUpdates projectUpdates = readUpdatesFile.readFromFile(reportFile);
                completeProjectUpdates = merger.merge(completeProjectUpdates, projectUpdates);
            } catch (IOException e) {
                log.error("Could not read from file", e);
            }
        }

        return completeProjectUpdates;
    }

}
