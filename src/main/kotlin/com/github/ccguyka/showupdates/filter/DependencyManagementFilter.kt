package com.github.ccguyka.showupdates.filter

import com.github.ccguyka.showupdates.filter.pom.Project
import org.apache.maven.model.Dependency
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import java.util.stream.Collectors

class DependencyManagementFilter(project: MavenProject?, log: Log?) : DependencyFilter(project!!, log!!) {

    override fun filter(dependencies: List<Dependency>, pom: Project): List<Dependency> {
        return dependencies.stream()
                .filter { dependency: Dependency ->
                    pom.dependencyManagement.dependencies
                            .stream()
                            .anyMatch { dep: com.github.ccguyka.showupdates.filter.pom.Dependency ->
                                (dep.artifactId == dependency.artifactId
                                        && dep.groupId == dependency.groupId)
                            }
                }
                .collect(Collectors.toList())
    }
}
