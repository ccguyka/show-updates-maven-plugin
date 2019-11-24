package com.github.ccguyka.showupdates.filter.pom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import com.google.common.collect.ArrayListMultimap

@JsonIgnoreProperties(ignoreUnknown = true)
data class Build (var plugins: List<Dependency> = ArrayList()) {

}
