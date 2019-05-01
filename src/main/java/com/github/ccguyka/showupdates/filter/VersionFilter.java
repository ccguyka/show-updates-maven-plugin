package com.github.ccguyka.showupdates.filter;

import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;

public interface VersionFilter {

    Map<Artifact, ArtifactVersion> filter(Map<Artifact, List<ArtifactVersion>> updates);



    static VersionFilter getFilterVersionsFor(Object versions) {
        if ("minor".equals(versions)) {
            return new MinorVersionFilter();
        } else {
            return new MajorVersionFilter();
        }
    }
}
