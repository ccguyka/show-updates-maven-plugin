package com.github.ccguyka.showupdates;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;

class DependencyManagementBuilder {

    public static DependencyManagement from(Dependency... dependencies) {
        DependencyManagement dependencyManagement = new DependencyManagement();
        for (Dependency dependency : dependencies) {
            dependencyManagement.addDependency(dependency);
        }
        return dependencyManagement;
    }
}
