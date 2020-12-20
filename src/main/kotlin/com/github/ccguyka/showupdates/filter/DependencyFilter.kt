package com.github.ccguyka.showupdates.filter

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.ccguyka.showupdates.filter.pom.Project
import org.apache.maven.model.Dependency
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.io.IOException
import java.util.stream.Collectors

open class DependencyFilter(private val project: MavenProject, private val log: Log) {

    private val xmlMapper = XmlMapper()

    fun filter(dependencies: List<Dependency>): List<Dependency> {
        return try {
            val pom = xmlMapper.readValue(project.file, Project::class.java)
            filter(dependencies, pom)
        } catch (e: IOException) {
            log.warn("Not able to read pom.xml file")
            dependencies
        }
    }

    protected open fun filter(dependencies: List<Dependency>, pom: Project): List<Dependency> {
        return dependencies.stream()
                .filter { dependency: Dependency ->
                    pom.dependencies
                            .stream()
                            .anyMatch { dep: com.github.ccguyka.showupdates.filter.pom.Dependency ->
                                (dep.artifactId == dependency.artifactId
                                        && dep.groupId == dependency.groupId)
                            }
                }
                .collect(Collectors.toList())
    }
}
