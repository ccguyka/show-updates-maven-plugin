package com.github.ccguyka.showupdates.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.ccguyka.showupdates.objects.ProjectUpdates
import org.apache.maven.plugin.logging.Log
import java.io.File
import java.io.IOException
import java.nio.file.Files

class SaveUpdates private constructor(private val log: Log) {

    private val mapper: ObjectMapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private fun save(projectUpdates: ProjectUpdates, file: File) {
        try {
            Files.deleteIfExists(file.toPath())
            Files.createDirectories(file.parentFile.toPath())
            Files.createFile(file.toPath())
            mapper.writeValue(file, projectUpdates)
        } catch (e: IOException) {
            log.error("Could not create output file", e)
        }
    }

    companion object {
        @JvmStatic
        fun save(projectUpdates: ProjectUpdates, log: Log, file: File) {
            val save = SaveUpdates(log)
            save.save(projectUpdates, file)
        }
    }
}
