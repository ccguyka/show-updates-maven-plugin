package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.ccguyka.showupdates.filter.pom.Project;

public class PluginFilter {

    private final MavenProject project;
    private final Log log;
    private final XmlMapper xmlMapper = new XmlMapper();

    public PluginFilter(MavenProject project, Log log) {
        this.project = project;
        this.log = log;
    }

    public Set<Artifact> filter(final Set<Artifact> artifacts) {
        try {
            Project pom = xmlMapper.readValue(project.getFile(), Project.class);
            return filter(artifacts, pom);
        } catch (final IOException e) {
            log.warn("Not able to read pom.xml file");
            return artifacts;
        }
    }

    private Set<Artifact> filter(Set<Artifact> artifacts, Project pom) {
        return artifacts.stream()
            .filter(dependency -> pom.getBuild().getPlugins()
                    .stream()
                    .anyMatch(dep -> Objects.equals(dep.getArtifactId(), dependency.getArtifactId())
                            && Objects.equals(dep.getGroupId(), dependency.getGroupId())))
            .collect(toSet());
}
}
