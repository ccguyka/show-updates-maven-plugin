package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public class ArtifactFilter {

    private final MavenProject project;
    private final Log log;

    public ArtifactFilter(MavenProject project, Log log) {
        this.project = project;
        this.log = log;
    }

    /**
     * @todo #1:60min Fix reporting of transitive dependency updates e.g. when guava is dependency and spring-guava is a transitive dependency.
     */
    public Set<Artifact> filter(final Set<Artifact> artifacts) {
        try {
            final String content = new String(Files.readAllBytes(Paths.get(project.getFile().getAbsolutePath())));
            return artifacts.stream()
                    .filter(artifact -> content.contains(artifact.getArtifactId()))
                    .collect(toSet());
        } catch (final IOException e) {
            log.warn("Not able to read pom.xml file");
            return artifacts;
        }
    }
}
