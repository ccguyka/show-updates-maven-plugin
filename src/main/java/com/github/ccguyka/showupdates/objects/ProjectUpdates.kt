package com.github.ccguyka.showupdates.objects

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
import com.google.common.base.MoreObjects
import com.google.common.collect.Lists
import java.util.*

@JsonDeserialize(builder = ProjectUpdates.Builder::class)
class ProjectUpdates private constructor(builder: Builder) {

    val parent: DependencyUpdates
    val dependency: DependencyUpdates
    val plugin: DependencyUpdates
    val dependencyManagement: DependencyUpdates

    override fun toString(): String {
        return MoreObjects.toStringHelper(this).add("parent", parent).add("dependency", dependency)
                .add("plugin", plugin).add("dependencyManagement", dependencyManagement).toString()
    }

    override fun hashCode(): Int {
        return Objects.hash(parent, dependency, plugin, dependencyManagement)
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` is ProjectUpdates) {
            val that = `object`
            return (parent == that.parent && dependency == that.dependency
                    && plugin == that.plugin
                    && dependencyManagement == that.dependencyManagement)
        }
        return false
    }

    @JsonPOJOBuilder
    class Builder internal constructor() {

        internal var parent = DependencyUpdates(Lists.newArrayList())
        internal var dependency = DependencyUpdates(Lists.newArrayList())
        internal var plugin = DependencyUpdates(Lists.newArrayList())
        internal var dependencyManagement = DependencyUpdates(Lists.newArrayList())

        fun withParent(parent: DependencyUpdates): Builder {
            this.parent = parent
            return this
        }

        fun withDependency(dependency: DependencyUpdates): Builder {
            this.dependency = dependency
            return this
        }

        fun withPlugin(plugin: DependencyUpdates): Builder {
            this.plugin = plugin
            return this
        }

        fun withDependencyManagement(dependencyManagement: DependencyUpdates): Builder {
            this.dependencyManagement = dependencyManagement
            return this
        }

        fun build(): ProjectUpdates {
            return ProjectUpdates(this)
        }
    }

    companion object {
        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
    }

    init {
        parent = builder.parent
        dependency = builder.dependency
        plugin = builder.plugin
        dependencyManagement = builder.dependencyManagement
    }
}
