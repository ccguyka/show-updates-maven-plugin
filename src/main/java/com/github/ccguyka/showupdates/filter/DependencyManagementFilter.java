package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

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
                        .anyMatch(dep -> Objects.equals(dep.getArtifactId(), dependency.getArtifactId())
                                && Objects.equals(dep.getGroupId(), dependency.getGroupId())))
                .collect(toList());
    }
}
