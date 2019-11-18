package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.github.ccguyka.showupdates.filter.pom.Project;

public class DependencyManagementFilter extends DependencyFilter {

    public DependencyManagementFilter(MavenProject project, Log log) {
        super(project, log);
    }

    @Override
    protected List<Dependency> filter(final List<Dependency> dependencies, Project pom) {
        return dependencies.stream()
                .filter(dependency -> pom.getDependencyManagement().getDependencies()
                        .stream()
                        .anyMatch(dep -> dep.getArtifactId().equals(dependency.getArtifactId())
                                &&  dep.getGroupId().equals(dependency.getGroupId())))
                .collect(toList());
    }
}
