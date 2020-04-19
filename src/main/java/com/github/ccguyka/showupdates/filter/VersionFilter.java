package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;

public class VersionFilter {

    private final List<VersionMatcher> filter;

    private VersionFilter(final VersionMatcher... filter) {
        this.filter = Lists.newArrayList(filter);
    }

    public ListMultimap<Artifact, ArtifactVersion> filter(final Map<Artifact, List<ArtifactVersion>> updates) {
        final ListMultimap<Artifact, ArtifactVersion> latestVersions = MultimapBuilder.hashKeys().arrayListValues().build();
        for (final VersionMatcher versionFilter : filter) {
            for (final Entry<Artifact, List<ArtifactVersion>> update : updates.entrySet()) {
                final DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(update.getKey().getVersion());
                final List<ArtifactVersion> latestUpdates = update.getValue().stream()
                        .filter(updateVersion -> versionFilter.matches(currentVersion, updateVersion))
                        .collect(toList());
                if (!latestUpdates.isEmpty()) {
                    latestVersions.put(update.getKey(), Iterables.getLast(latestUpdates));
                }
            }
        }

        final ListMultimap<Artifact, ArtifactVersion> distinctLatestVersions = MultimapBuilder.hashKeys().arrayListValues().build();
        for (final Entry<Artifact, Collection<ArtifactVersion>> entry : latestVersions.asMap().entrySet()) {
            distinctLatestVersions.putAll(entry.getKey(),
                    entry.getValue().stream().distinct().collect(Collectors.toList()));
        }

        return distinctLatestVersions;
    }

    public static VersionFilter getFilterVersionsFor(final String versions) {

        switch (versions) {
        case "patch":
            return new VersionFilter(new PatchVersionMatcher());
        case "minor":
            return new VersionFilter(new MinorVersionMatcher());
        case "major":
            return new VersionFilter(new MajorVersionMatcher());
        default:
            return new VersionFilter(new PatchVersionMatcher(), new MinorVersionMatcher(), new MajorVersionMatcher());
        }
    }

    private interface VersionMatcher {

        boolean matches(final DefaultArtifactVersion artifactVersion, final ArtifactVersion updateVersion);
    }

    private static class PatchVersionMatcher implements VersionMatcher {

        @Override
        public boolean matches(final DefaultArtifactVersion currentVersion, final ArtifactVersion updateVersion) {
            return updateVersion.getMajorVersion() == currentVersion.getMajorVersion()
                    && updateVersion.getMinorVersion() == currentVersion.getMinorVersion();
        }
    }

    private static class MinorVersionMatcher implements VersionMatcher {

        @Override
        public boolean matches(final DefaultArtifactVersion currentVersion, final ArtifactVersion updateVersion) {
            return updateVersion.getMajorVersion() == currentVersion.getMajorVersion()
                    && updateVersion.getMinorVersion() > currentVersion.getMinorVersion();
        }
    }

    private static class MajorVersionMatcher implements VersionMatcher {

        @Override
        public boolean matches(final DefaultArtifactVersion currentVersion, final ArtifactVersion updateVersion) {
            return true;
        }
    }
}
