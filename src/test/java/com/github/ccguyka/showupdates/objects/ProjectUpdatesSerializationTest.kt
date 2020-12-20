package com.github.ccguyka.showupdates.objects

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.ccguyka.showupdates.objects.ProjectUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates.Companion.builder
import com.google.common.base.Charsets
import com.google.common.collect.Lists
import com.google.common.io.Resources
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ProjectUpdatesSerializationTest {

    private val objectMapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
    private var mavenUpdates: String? = null
    private var updates: ProjectUpdates? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        mavenUpdates = Resources.toString(this.javaClass.getResource("maven-updates.json"), Charsets.UTF_8)
        val update = DependencyUpdates(Lists.newArrayList(ArtifactUpdate("test:test", "1", Lists.newArrayList("2"))))
        updates = builder()
                .withParent(update)
                .withDependency(update)
                .withDependencyManagement(update)
                .withPlugin(update)
                .build()
    }

    @Test
    @Throws(Exception::class)
    fun serialize() {
        val json = objectMapper.writeValueAsString(updates)
        Assertions.assertThat(json).isEqualTo(mavenUpdates)
    }

    @Test
    @Throws(Exception::class)
    fun deserialize() {
        val readUpdates = objectMapper.readValue(mavenUpdates, ProjectUpdates::class.java)
        Assertions.assertThat(readUpdates).isEqualTo(updates)
    }
}
