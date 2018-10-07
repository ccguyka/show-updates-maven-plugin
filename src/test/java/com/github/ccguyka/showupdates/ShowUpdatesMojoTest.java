package com.github.ccguyka.showupdates;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class ShowUpdatesMojoTest extends AbstractMojoTestCase {

    private MavenSession mavenSession;
    private ArtifactMetadataSource artifactMetadataSource;
    private ArtifactFactory artifactFactory;
    private List<ArtifactRepository> remoteArtifactRepositories;
    private ArtifactRepository localRepository;
    private MavenProject project;
    private Log log;

    private ShowUpdatesMojo mojo;

    @Override
    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();

        mavenSession = mock(MavenSession.class);
        artifactMetadataSource = mock(ArtifactMetadataSource.class);
        artifactFactory = mock(ArtifactFactory.class);
        remoteArtifactRepositories = mock(List.class);
        localRepository = mock(ArtifactRepository.class);
        project = mock(MavenProject.class);

        mojo = (ShowUpdatesMojo) lookupEmptyMojo("updates", "src/test/resources/test-mojo-pom.xml");
        setVariableValueToObject(mojo, "mavenSession", mavenSession);
        setVariableValueToObject(mojo, "artifactMetadataSource", artifactMetadataSource);
        setVariableValueToObject(mojo, "artifactFactory", artifactFactory);
        setVariableValueToObject(mojo, "remoteArtifactRepositories", remoteArtifactRepositories);
        setVariableValueToObject(mojo, "localRepository", localRepository);
        setVariableValueToObject(mojo, "project", project);

        log = mock(Log.class);
        mojo.setLog(log);
    }

    @Test
    public void testParentUpdates() throws Exception {
        final Artifact artifact = new DefaultArtifact("groupId", "artifactId", "1.2.3", "compile", "type",
                "classifier", null);
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = new ArrayList<>();
        updates.add(new DefaultArtifactVersion("1.2.4"));
        updates.add(new DefaultArtifactVersion("1.3-SNAPSHOT"));
        when(artifactMetadataSource
                .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates);

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  groupId:artifactId ... 1.2.3 -> 1.2.4");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testExcludeBlacklistedUpdates() throws Exception {
        final Artifact artifact = new DefaultArtifact("groupId", "artifactId", "1.1.1", "compile", "type",
                "classifier", null);
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = new ArrayList<>();
        updates.add(new DefaultArtifactVersion("1.2.0"));
        updates.add(new DefaultArtifactVersion("2.0.0-beta1"));
        when(artifactMetadataSource
                .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates);

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  groupId:artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }
}
