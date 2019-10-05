package com.github.ccguyka.showupdates.filter;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import com.google.common.collect.Lists;

/**
 * @todo Write tests for FilterExcludedArtifacts class.
 */
public class FilterExcludedArtifacts {

    private final List<String> excludes;

    public FilterExcludedArtifacts(final String... excludes) {
        this.excludes = Lists.newArrayList(excludes);
    }

    public Map<Artifact, List<ArtifactVersion>> filter(final Map<Artifact, List<ArtifactVersion>> updates) {
        final Map<Artifact, List<ArtifactVersion>> filteredExcludedArtifacts = new HashMap<>();
        for (final Entry<Artifact, List<ArtifactVersion>> update : updates.entrySet()) {
            filteredExcludedArtifacts.put(update.getKey(), filterExcludedArtifacts(update.getValue()));
        }

        return filteredExcludedArtifacts;
    }

    private List<ArtifactVersion> filterExcludedArtifacts(final List<ArtifactVersion> update) {
        return update.stream().filter(filterExcludedArtifact()).collect(toList());
    }

    private Predicate<? super ArtifactVersion> filterExcludedArtifact() {
        return artifact -> Lists.newArrayList(excludes).stream()
                .noneMatch(exclude -> artifact.toString().contains(exclude));
    }
}
