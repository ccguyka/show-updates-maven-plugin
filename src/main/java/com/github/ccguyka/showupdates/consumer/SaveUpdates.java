package com.github.ccguyka.showupdates.consumer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.maven.plugin.logging.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

public class SaveUpdates {

    private final ObjectMapper mapper;

    private Log log;

    public SaveUpdates(Log log) {
        this.log = log;

        mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void save(ProjectUpdates projectUpdates, Log log, File file) {
        SaveUpdates save = new SaveUpdates(log);
        save.save(projectUpdates, file);
    }

    private void save(ProjectUpdates projectUpdates, File file) {
        try {
            Files.deleteIfExists(file.toPath());
            Files.createDirectories(file.getParentFile().toPath());
            Files.createFile(file.toPath());
            mapper.writeValue(file, projectUpdates);
        } catch (IOException e) {
            log.error("Could not create output file", e);
        }
    }

}
