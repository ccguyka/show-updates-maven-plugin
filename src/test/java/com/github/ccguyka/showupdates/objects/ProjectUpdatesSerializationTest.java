package com.github.ccguyka.showupdates.objects;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class ProjectUpdatesSerializationTest {

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private String mavenUpdates;
    private ProjectUpdates updates;

    @Before
    public void setUp() throws IOException {
        mavenUpdates = Resources.toString(this.getClass().getResource("maven-updates.json"), Charsets.UTF_8);
        DependencyUpdates update = new DependencyUpdates(newArrayList(new ArtifactUpdate("test:test", "1", newArrayList("2"))));
        updates = ProjectUpdates.builder()
                .withParent(update)
                .withDependency(update)
                .withDependencyManagement(update)
                .withPlugin(update)
                .build();
    }

    @Test
    public void serialize() throws Exception {

        String json = objectMapper.writeValueAsString(updates);

        assertThat(json).isEqualTo(mavenUpdates);
    }

    @Test
    public void deserialize() throws Exception {

        ProjectUpdates readUpdates = objectMapper.readValue(mavenUpdates, ProjectUpdates.class);

        assertThat(readUpdates).isEqualTo(updates);
    }
}
