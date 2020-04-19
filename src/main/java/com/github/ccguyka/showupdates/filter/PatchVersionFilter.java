package com.github.ccguyka.showupdates.filter;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

class PatchVersionFilter implements VersionFilter {

    @Override
    public boolean filter(final DefaultArtifactVersion updateVersion, final ArtifactVersion artifactVersion) {
        return artifactVersion.getMajorVersion() == updateVersion.getMajorVersion()
                && artifactVersion.getMinorVersion() == updateVersion.getMinorVersion();
    }
}
