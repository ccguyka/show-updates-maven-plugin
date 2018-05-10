package com.github.ccguyka.showupdates.source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;

public class UpdateSource {

    private final ArtifactMetadataSource artifactMetadataSource;
    private final ArtifactRepository localRepository;
    private final List<ArtifactRepository> remoteArtifactRepositories;

    public UpdateSource(final ArtifactMetadataSource artifactMetadataSource, final ArtifactRepository localRepository,
            final List<ArtifactRepository> remoteArtifactRepositories) {
        this.artifactMetadataSource = artifactMetadataSource;
        this.localRepository = localRepository;
        this.remoteArtifactRepositories = remoteArtifactRepositories;
    }

    public Map<Artifact, List<ArtifactVersion>> getUpdate(final Artifact artifact) {
        final Map<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        try {
            final List<ArtifactVersion> retrieveAvailableVersions = artifactMetadataSource
                    .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories);

            updates.put(artifact, filter(artifact, retrieveAvailableVersions));
        } catch (ArtifactMetadataRetrievalException | OverConstrainedVersionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return updates;
    }

    public Map<Artifact, List<ArtifactVersion>> getUpdates(final Collection<Artifact> artifacts) {
        final Map<Artifact, List<ArtifactVersion>> updates = new HashMap<>();
        for (final Artifact artifact : artifacts) {
            try {
                final List<ArtifactVersion> retrieveAvailableVersions = artifactMetadataSource
                        .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories);

                updates.put(artifact, filter(artifact, retrieveAvailableVersions));
            } catch (ArtifactMetadataRetrievalException | OverConstrainedVersionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return updates;
    }

    private List<ArtifactVersion> filter(final Artifact artifact, final List<ArtifactVersion> retrieveAvailableVersions)
            throws OverConstrainedVersionException {
        final List<ArtifactVersion> updates = new ArrayList<>();

        for (final ArtifactVersion availableVersion : retrieveAvailableVersions) {
            if (availableVersion.getMajorVersion() > artifact.getSelectedVersion().getMajorVersion()) {
                updates.add(availableVersion);
            } else if (availableVersion.getMajorVersion() == artifact.getSelectedVersion().getMajorVersion()) {
                if (availableVersion.getMinorVersion() > artifact.getSelectedVersion().getMinorVersion()) {
                    updates.add(availableVersion);
                } else if (availableVersion.getMinorVersion() == artifact.getSelectedVersion().getMinorVersion()) {
                    if (availableVersion.getIncrementalVersion() > artifact.getSelectedVersion()
                            .getIncrementalVersion()) {
                        updates.add(availableVersion);
                    }
                }
            }
        }

        return updates;
    }
}
