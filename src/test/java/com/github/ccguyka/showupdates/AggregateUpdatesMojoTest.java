package com.github.ccguyka.showupdates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Build;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class AggregateUpdatesMojoTest extends AbstractMojoTestCase {

    private MavenProject project;
    private List<MavenProject> reactorProjects = new ArrayList<>();
    private Log log;

    private AggregateUpdatesMojo mojo;

    @Override
    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();

        URL path = this.getClass().getResource("");

        project = mock(MavenProject.class);
        when(project.isExecutionRoot()).thenReturn(true);
        Build build = mock(Build.class);
        when(build.getDirectory()).thenReturn(getBasedir() + "/target");
        when(project.getBuild()).thenReturn(build);

        Build buildA = mock(Build.class);
        when(buildA.getDirectory()).thenReturn(path.getPath() + "/projectA");
        MavenProject projectA = mock(MavenProject.class);
        when(projectA.getBuild()).thenReturn(buildA);

        Build buildB = mock(Build.class);
        when(buildB.getDirectory()).thenReturn(path.getPath() + "/projectB");
        MavenProject projectB = mock(MavenProject.class);
        when(projectB.getBuild()).thenReturn(buildB);
        reactorProjects.add(projectA);
        reactorProjects.add(projectB);

        mojo = (AggregateUpdatesMojo) lookupEmptyMojo("aggregate", "src/test/resources/test-mojo-pom.xml");
        setVariableValueToObject(mojo, "project", project);
        setVariableValueToObject(mojo, "reactorProjects", reactorProjects);

        log = mock(Log.class);
        mojo.setLog(log);
    }

    @Test
    public void testAggregation() throws Exception {
        mojo.execute();

        File result = new File(getBasedir() + "/target/aggregated-maven-updates.json");
        File expected = new File(this.getClass().getResource("aggregated-maven-updates.json").getPath());
        assertThat(result).hasSameContentAs(expected);
    }
}
