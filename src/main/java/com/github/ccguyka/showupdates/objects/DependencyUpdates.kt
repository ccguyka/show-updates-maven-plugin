package com.github.ccguyka.showupdates.objects

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.MoreObjects
import java.util.*

class DependencyUpdates @JsonCreator constructor(@param:JsonProperty("artifacts") val artifacts: List<ArtifactUpdate>) {

    override fun toString(): String {
        return MoreObjects.toStringHelper(this).add("artifacts", artifacts).toString()
    }

    override fun hashCode(): Int {
        return Objects.hashCode(artifacts)
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` is DependencyUpdates) {
            return artifacts == `object`.artifacts
        }
        return false
    }
}
