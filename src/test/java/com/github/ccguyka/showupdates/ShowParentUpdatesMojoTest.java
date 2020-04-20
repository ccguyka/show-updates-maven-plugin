package com.github.ccguyka.showupdates;

import static com.github.ccguyka.showupdates.ArtifactBuilder.aParent;
import static com.github.ccguyka.showupdates.ArtifactVersionListBuilder.updates;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class ShowParentUpdatesMojoTest extends AbstractMojoTestCase {

    private ArtifactMetadataSource artifactMetadataSource;
    private List<ArtifactRepository> remoteArtifactRepositories;
    private ArtifactRepository localRepository;
    private MavenProject project;
    private Log log;

    private ShowUpdatesMojo mojo;

    @Override
    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();

        final MavenSession mavenSession = mock(MavenSession.class);
        artifactMetadataSource = mock(ArtifactMetadataSource.class);
        final ArtifactFactory artifactFactory = mock(ArtifactFactory.class);
        remoteArtifactRepositories = mock(List.class);
        localRepository = mock(ArtifactRepository.class);
        project = mock(MavenProject.class);
        final Build build = mock(Build.class);
        when(build.getDirectory()).thenReturn(getBasedir() + "/target");
        when(project.getBuild()).thenReturn(build);

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
        final Artifact artifact = aParent().version("1.2.3").build();
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = updates()
                .version("1.2.4")
                .version("2.1.3").build();
        mockUpdates(artifact, updates);

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  parent-groupId:parent-artifactId ... 1.2.3 -> 1.2.4, 2.1.3");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testNoUpdates() throws Exception {
        final Artifact artifact = aParent().version("1.2.3").build();
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = updates().build();
        mockUpdates(artifact, updates);

        mojo.execute();

        verifyNoMoreInteractions(log);
    }

    @Test
    public void testExcludeBlacklistedUpdates() throws Exception {
        final Artifact artifact = aParent().version("1.1.1").build();
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = updates()
                .version("1.2.0")
                .version("2.0.0-beta1")
                .version("2.0.0-alpha1")
                .version("2.0.0-SNAPSHOT").build();
        mockUpdates(artifact, updates);

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testExcludeBlacklistedUpdatesWithParameters() throws Exception {
        final Artifact artifact = aParent().version("1.1.1").build();
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = updates()
                .version("1.2.0")
                .version("2.0.0-test").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "excludes", new String[] {"test"});

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testMajorUpdates() throws Exception {
        final Artifact artifact = aParent().version("1.1.1").build();
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "versions", "major");

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  parent-groupId:parent-artifactId ... 1.1.1 -> 2.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testMinorUpdates() throws Exception {
        final Artifact artifact = aParent().version("1.1.1").build();
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "versions", "minor");

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testPatchUpdates() throws Exception {
        final Artifact artifact = aParent().version("1.1.1").build();
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "versions", "patch");

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.1.3");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testLatestUpdates() throws Exception {
        final Artifact artifact = aParent().version("1.1.1").build();
        when(project.getParentArtifact()).thenReturn(artifact);
        final List<ArtifactVersion> updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "versions", "latest");

        mojo.execute();

        verify(log).info("Available parent updates:");
        verify(log).info("  parent-groupId:parent-artifactId ... 1.1.1 -> 1.1.3, 1.2.0, 2.2.0");
        verifyNoMoreInteractions(log);
    }

    private void mockUpdates(final Artifact artifact, final List<ArtifactVersion> updates)
            throws ArtifactMetadataRetrievalException {
        when(artifactMetadataSource
                .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates);
    }
}
