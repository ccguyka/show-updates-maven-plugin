package com.github.ccguyka.showupdates

import org.apache.maven.model.Dependency
import org.apache.maven.model.DependencyManagement

internal object DependencyManagementBuilder {

    @JvmStatic
    fun from(vararg dependencies: Dependency?): DependencyManagement {
        val dependencyManagement = DependencyManagement()
        for (dependency in dependencies) {
            dependencyManagement.addDependency(dependency)
        }
        return dependencyManagement
    }
}
