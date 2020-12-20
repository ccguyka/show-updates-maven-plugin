package com.github.ccguyka.showupdates.objects

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.MoreObjects
import java.util.*

open class ArtifactUpdate @JsonCreator constructor(@param:JsonProperty("name") val name: String,
                                              @param:JsonProperty("current") val current: String,
                                              @param:JsonProperty("updates") val updates: List<String>) {

    override fun toString(): String {
        return MoreObjects.toStringHelper(this).add("name", name).add("current", current).add("updates", updates)
                .toString()
    }

    override fun hashCode(): Int {
        return Objects.hash(name, current, updates)
    }

    override fun equals(other: Any?): Boolean {
        if (other is ArtifactUpdate) {
            return (name == other.name && current == other.current
                    && updates == other.updates)
        }
        return false
    }
}
