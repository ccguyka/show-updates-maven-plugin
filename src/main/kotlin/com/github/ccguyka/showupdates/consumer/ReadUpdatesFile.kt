package com.github.ccguyka.showupdates.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.ccguyka.showupdates.objects.ProjectUpdates
import java.io.File
import java.io.IOException

class ReadUpdatesFile {

    private val mapper: ObjectMapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    @Throws(IOException::class)
    fun readFromFile(updatesFile: File?): ProjectUpdates {
        return mapper.readValue(updatesFile, ProjectUpdates::class.java)
    }
}
