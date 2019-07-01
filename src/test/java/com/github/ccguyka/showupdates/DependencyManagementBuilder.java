package com.github.ccguyka.showupdates;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;

class DependencyManagementBuilder {

    public static DependencyManagement from(Dependency dependency) {
        DependencyManagement dependencyManagement = new DependencyManagement();
        dependencyManagement.addDependency(dependency);
        return dependencyManagement;
    }
}
