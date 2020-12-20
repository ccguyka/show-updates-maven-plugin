package com.github.ccguyka.showupdates.filter

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.ccguyka.showupdates.filter.pom.Dependency
import com.github.ccguyka.showupdates.filter.pom.Project
import org.apache.maven.artifact.Artifact
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.io.IOException
import java.util.stream.Collectors

class PluginFilter(private val project: MavenProject, private val log: Log) {

    private val xmlMapper = XmlMapper()

    fun filter(artifacts: Set<Artifact>): Set<Artifact> {
        return try {
            val pom = xmlMapper.readValue(project.file, Project::class.java)
            filter(artifacts, pom)
        } catch (e: IOException) {
            log.warn("Not able to read pom.xml file")
            artifacts
        }
    }

    private fun filter(artifacts: Set<Artifact>, pom: Project): Set<Artifact> {
        return artifacts.stream()
                .filter { dependency: Artifact ->
                    pom.build.plugins
                            .stream()
                            .anyMatch { dep: Dependency ->
                                (dep.artifactId == dependency.artifactId
                                        && dep.groupId == dependency.groupId)
                            }
                }
                .collect(Collectors.toSet())
    }
}
