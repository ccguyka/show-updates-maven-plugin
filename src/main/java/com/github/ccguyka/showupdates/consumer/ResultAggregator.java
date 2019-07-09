package com.github.ccguyka.showupdates.consumer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

public class ResultAggregator {

    private final ObjectMapper mapper;
    private final List<File> reportFiles;
    private final ProjectUpdatesMerger merger;
    private final Log log;

    private ResultAggregator(List<File> reportFiles, Log log) {
        this.reportFiles = reportFiles;
        this.log = log;

        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
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
                ProjectUpdates projectUpdates = mapper.readValue(reportFile, ProjectUpdates.class);
                completeProjectUpdates = merger.merge(completeProjectUpdates, projectUpdates);
            } catch (IOException e) {
                log.error("Could not read from file", e);
            }
        }

        return completeProjectUpdates;
    }

}
