package com.github.ccguyka.showupdates;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;

class DependencyBuilder {

    public static Dependency aDependency(Artifact artifact) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(artifact.getGroupId());
        dependency.setArtifactId(artifact.getArtifactId());
        dependency.setVersion(artifact.getVersion());
        dependency.setScope(artifact.getScope());
        dependency.setType(artifact.getType());
        dependency.setClassifier(artifact.getClassifier());
        return dependency;
    }
}
