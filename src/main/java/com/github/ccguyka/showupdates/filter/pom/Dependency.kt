package com.github.ccguyka.showupdates.filter.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Dependency {

    var groupId: String? = null
    var artifactId: String? = null
}
