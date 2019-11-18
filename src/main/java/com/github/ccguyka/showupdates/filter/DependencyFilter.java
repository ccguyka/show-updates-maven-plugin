package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.ccguyka.showupdates.filter.pom.Project;

public class DependencyFilter {

    private final MavenProject project;
    private final Log log;
    private final XmlMapper xmlMapper = new XmlMapper();

    public DependencyFilter(MavenProject project, Log log) {
        this.project = project;
        this.log = log;
    }

    public List<Dependency> filter(final List<Dependency> dependencies) {
        try {
            Project pom = xmlMapper.readValue(project.getFile(), Project.class);
            return filter(dependencies, pom);
        } catch (final IOException e) {
            log.warn("Not able to read pom.xml file");
            return dependencies;
        }
    }

    protected List<Dependency> filter(final List<Dependency> dependencies, Project pom) {
        return dependencies.stream()
            .filter(dependency -> pom.getDependencies()
                    .stream()
                    .anyMatch(dep -> dep.getArtifactId().equals(dependency.getArtifactId())
                            &&  dep.getGroupId().equals(dependency.getGroupId())))
            .collect(toList());
    }
}
