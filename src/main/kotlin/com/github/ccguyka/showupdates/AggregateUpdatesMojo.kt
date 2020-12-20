package com.github.ccguyka.showupdates

import com.github.ccguyka.showupdates.consumer.PrintUpdates.Companion.print
import com.github.ccguyka.showupdates.consumer.ResultAggregator.Companion.aggregate
import com.github.ccguyka.showupdates.consumer.SaveUpdates.Companion.save
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import java.io.File
import java.util.stream.Collectors

/**
 * Aggregates previously created report files
 */
@Mojo(name = "aggregate")
class AggregateUpdatesMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}")
    protected var project: MavenProject? = null

    /**
     * The projects in the reactor for aggregation report.
     */
    @Parameter(defaultValue = "\${reactorProjects}", readonly = true)
    private val reactorProjects: List<MavenProject>? = null
    override fun execute() {
        if (project!!.isExecutionRoot) {
            val reportFiles = reactorProjects!!.stream().map { project: MavenProject -> getReportFile(project) }.collect(Collectors.toList())
            val aggregate = aggregate(reportFiles, log)
            print(aggregate, log)
            save(aggregate, log, aggregationReportDirectory)
        } else {
            log.info("Aggregation is only executed in root module")
        }
    }

    protected fun getReportFile(project: MavenProject): File {
        val buildDir = project.build.directory
        return File("$buildDir/maven-updates.json")
    }

    protected val aggregationReportDirectory: File
        protected get() {
            val buildDir = project!!.build.directory
            return File("$buildDir/aggregated-maven-updates.json")
        }
}
