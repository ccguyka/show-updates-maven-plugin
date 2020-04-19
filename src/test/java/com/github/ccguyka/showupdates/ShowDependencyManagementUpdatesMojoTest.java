package com.github.ccguyka.showupdates;

import static com.github.ccguyka.showupdates.ArtifactBuilder.aManagedDependency;
import static com.github.ccguyka.showupdates.ArtifactVersionListBuilder.updates;
import static com.github.ccguyka.showupdates.DependencyBuilder.aDependency;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Build;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class ShowDependencyManagementUpdatesMojoTest extends AbstractMojoTestCase {

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

        final MavenSession mavenSession = mock(MavenSession.class);
        artifactMetadataSource = mock(ArtifactMetadataSource.class);
        artifactFactory = mock(ArtifactFactory.class);
        remoteArtifactRepositories = mock(List.class);
        localRepository = mock(ArtifactRepository.class);
        project = mock(MavenProject.class);
        final Build build = mock(Build.class);
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
        final Artifact artifact = aManagedDependency().version("1.1.1").build();
        final DependencyManagement dependencyManagement = DependencyManagementBuilder.from(aDependency(artifact));
        when(project.getDependencyManagement()).thenReturn(dependencyManagement);
        final List<ArtifactVersion> updates = updates()
                .version("1.2.0")
                .version("2.0.0-beta1")
                .version("2.0.0-alpha1")
                .version("2.0.0-SNAPSHOT").build();
        mockUpdates(artifact, updates);

        mojo.execute();

        verify(log).info("Available dependency management updates:");
        verify(log).info("  dep-mgnt-groupId:dep-mgnt-artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testExcludeBlacklistedUpdatesWithParameters() throws Exception {
        final Artifact artifact = aManagedDependency().version("1.1.1").build();
        final DependencyManagement dependencyManagement = DependencyManagementBuilder.from(aDependency(artifact));
        when(project.getDependencyManagement()).thenReturn(dependencyManagement);
        final List<ArtifactVersion> updates = updates()
                .version("1.2.0")
                .version("2.0.0-test").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "excludes", new String[] {"test"});

        mojo.execute();

        verify(log).info("Available dependency management updates:");
        verify(log).info("  dep-mgnt-groupId:dep-mgnt-artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testMajorUpdates() throws Exception {
        final Artifact artifact = aManagedDependency().version("1.1.1").build();
        final DependencyManagement dependencyManagement = DependencyManagementBuilder.from(aDependency(artifact));
        when(project.getDependencyManagement()).thenReturn(dependencyManagement);
        final List<ArtifactVersion> updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "versions", "major");

        mojo.execute();

        verify(log).info("Available dependency management updates:");
        verify(log).info("  dep-mgnt-groupId:dep-mgnt-artifactId ... 1.1.1 -> 2.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testMinorUpdates() throws Exception {
        final Artifact artifact = aManagedDependency().version("1.1.1").build();
        final DependencyManagement dependencyManagement = DependencyManagementBuilder.from(aDependency(artifact));
        when(project.getDependencyManagement()).thenReturn(dependencyManagement);
        final List<ArtifactVersion> updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "versions", "minor");

        mojo.execute();

        verify(log).info("Available dependency management updates:");
        verify(log).info("  dep-mgnt-groupId:dep-mgnt-artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testPatchUpdates() throws Exception {
        final Artifact artifact = aManagedDependency().version("1.1.1").build();
        final DependencyManagement dependencyManagement = DependencyManagementBuilder.from(aDependency(artifact));
        when(project.getDependencyManagement()).thenReturn(dependencyManagement);
        final List<ArtifactVersion> updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "versions", "patch");

        mojo.execute();

        verify(log).info("Available dependency management updates:");
        verify(log).info("  dep-mgnt-groupId:dep-mgnt-artifactId ... 1.1.1 -> 1.1.3");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testLatestUpdates() throws Exception {
        final Artifact artifact = aManagedDependency().version("1.1.1").build();
        final DependencyManagement dependencyManagement = DependencyManagementBuilder.from(aDependency(artifact));
        when(project.getDependencyManagement()).thenReturn(dependencyManagement);
        final List<ArtifactVersion> updates = updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build();
        mockUpdates(artifact, updates);
        setVariableValueToObject(mojo, "versions", "latest");

        mojo.execute();

        verify(log).info("Available dependency management updates:");
        verify(log).info("  dep-mgnt-groupId:dep-mgnt-artifactId ... 1.1.1 -> 1.1.3, 1.2.0, 2.2.0");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void testTransitiveUpdates() throws Exception {
        final Artifact artifact = aManagedDependency().version("1.1.1").build();
        final Artifact transitiveArtifact = aManagedDependency().groupId("another-groupId").version("2.0.0").build();
        final DependencyManagement dependencyManagement = DependencyManagementBuilder.from(aDependency(artifact),
                aDependency(transitiveArtifact));
        when(project.getDependencyManagement()).thenReturn(dependencyManagement);
        mockUpdates(artifact, updates()
                .version("1.1.2")
                .version("1.1.3")
                .version("1.2.0")
                .version("2.0.0")
                .version("2.2.0").build());
        mockUpdates(transitiveArtifact, updates()
                .version("2.2.0").build());
        setVariableValueToObject(mojo, "versions", "minor");

        mojo.execute();

        verify(log).info("Available dependency management updates:");
        verify(log).info("  dep-mgnt-groupId:dep-mgnt-artifactId ... 1.1.1 -> 1.2.0");
        verifyNoMoreInteractions(log);
    }

    private void mockUpdates(final Artifact artifact, final List<ArtifactVersion> updates)
            throws ArtifactMetadataRetrievalException {
        when(artifactFactory.createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                artifact.getScope(), artifact.getType())).thenReturn(artifact);
        when(artifactMetadataSource
                .retrieveAvailableVersions(artifact, localRepository, remoteArtifactRepositories)).thenReturn(updates);
    }
}
