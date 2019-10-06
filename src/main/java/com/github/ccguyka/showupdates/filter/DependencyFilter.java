package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public class DependencyFilter {

    private final MavenProject project;
    private final Log log;

    public DependencyFilter(MavenProject project, Log log) {
        this.project = project;
        this.log = log;
    }

    public List<Dependency> filter(final List<Dependency> dependencies) {
        try {
            final String content = new String(Files.readAllBytes(Paths.get(project.getFile().getAbsolutePath())));
            return dependencies.stream()
                    .filter(dependency -> content.contains(dependency.getArtifactId()))
                    .collect(toList());
        } catch (final IOException e) {
            log.warn("Not able to read pom.xml file");
            return dependencies;
        }
    }
}
