package com.github.ccguyka.showupdates.filter.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Build {

    var plugins: List<Dependency> = ArrayList()
}
