package com.github.ccguyka.showupdates;

import static com.github.ccguyka.showupdates.ArtifactBuilder.anArtifact;
import static com.github.ccguyka.showupdates.ArtifactVersionListBuilder.updates;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ShowPluginUpdatesMojoTest extends AbstractMojoTestCase {

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

        MavenSession mavenSession = mock(MavenSession.class);
        artifactMetadataSource = mock(ArtifactMetadataSource.class);
        ArtifactFactory artifactFactory = mock(ArtifactFactory.class);
        remoteArtifactRepositories = mock(List.class);
        localRepository = mock(ArtifactRepository.class);
        project = mock(MavenProject.class);
        Build build = mock(Build.class);
        when(build.getDirectory()).thenReturn(getBasedir() + "/target");
        when(project.getBuild()).thenReturn(build);
        when(project.getFile()).thenReturn(new File(getBasedir() + "/src/test/resources/test-mojo-pom.xml"));

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
    public void testExcludeBlacklistedUpdates() throws Exception {
        final Artifact artifact = anArtifact().version("1.1.1").build();
        Set<Artifact> plugins = Sets.newHashSet(artifact);
        when(project.getPluginArtifacts()).thenReturn(plugins);
        final List<ArtifactVersion> updates = updates()
                .version("1.2.0")
                .version("2.0.0-beta1")
                .version("2.0.0-alpha1")
                .version("2.0.0-SNAPSHOT").build();
        when(artifactMetadataSource
                .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates);

        mojo.execute();

        verify(log).info("Available plugin updates:");
        verify(log).info("  groupId:artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testExcludeBlacklistedUpdatesWithParameters() throws Exception {
        final Artifact artifact = anArtifact().version("1.1.1").build();
        Set<Artifact> plugins = Sets.newHashSet(artifact);
        when(project.getPluginArtifacts()).thenReturn(plugins);
        final List<ArtifactVersion> updates = updates()
                .version("1.2.0")
                .version("2.0.0-test").build();
        when(artifactMetadataSource
                .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates);
        setVariableValueToObject(mojo, "excludes", new String[] {"test"});

        mojo.execute();

        verify(log).info("Available plugin updates:");
        verify(log).info("  groupId:artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testMinorUpdates() throws Exception {
        final Artifact artifact = anArtifact().version("1.1.1").build();
        Set<Artifact> plugins = Sets.newHashSet(artifact);
        when(project.getPluginArtifacts()).thenReturn(plugins);
        final List<ArtifactVersion> updates = updates()
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        when(artifactMetadataSource
                .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates);
        setVariableValueToObject(mojo, "versions", "minor");

        mojo.execute();

        verify(log).info("Available plugin updates:");
        verify(log).info("  groupId:artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }
}
