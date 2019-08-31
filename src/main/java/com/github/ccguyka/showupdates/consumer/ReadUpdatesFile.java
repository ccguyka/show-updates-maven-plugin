package com.github.ccguyka.showupdates.consumer;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

public class ReadUpdatesFile {

    private final ObjectMapper mapper;

    public ReadUpdatesFile() {

        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public ProjectUpdates readFromFile(File updatesFile) throws IOException {
        return mapper.readValue(updatesFile, ProjectUpdates.class);
    }
}
