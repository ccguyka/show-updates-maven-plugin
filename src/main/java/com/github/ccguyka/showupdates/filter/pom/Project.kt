package com.github.ccguyka.showupdates.filter.pom

import java.util.ArrayList
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Project(
    var dependencies: List<Dependency>? = ArrayList(),
    var dependencyManagement: DependencyManagement? = DependencyManagement(),
    var build: Build? = Build()) {

}
