package com.github.ccguyka.showupdates.filter.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Project {

    var dependencies: List<Dependency> = ArrayList()
    var dependencyManagement = DependencyManagement()
    var build = Build()
}
