package com.github.ccguyka.showupdates.consumer

import com.github.ccguyka.showupdates.objects.ProjectUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates.Companion.builder
import org.apache.maven.plugin.logging.Log
import java.io.File
import java.io.IOException

class ResultAggregator private constructor(private val reportFiles: List<File>, private val log: Log) {

    private val readUpdatesFile: ReadUpdatesFile = ReadUpdatesFile()
    private val merger: ProjectUpdatesMerger = ProjectUpdatesMerger()

    private fun aggregate(): ProjectUpdates {
        var completeProjectUpdates = builder().build()
        for (reportFile in reportFiles) {
            try {
                val projectUpdates = readUpdatesFile.readFromFile(reportFile)
                completeProjectUpdates = merger.merge(completeProjectUpdates, projectUpdates)
            } catch (e: IOException) {
                log.error("Could not read from file", e)
            }
        }
        return completeProjectUpdates
    }

    companion object {
        @JvmStatic
        fun aggregate(reportFiles: List<File>, log: Log): ProjectUpdates {
            val aggregator = ResultAggregator(reportFiles, log)
            return aggregator.aggregate()
        }
    }
}
