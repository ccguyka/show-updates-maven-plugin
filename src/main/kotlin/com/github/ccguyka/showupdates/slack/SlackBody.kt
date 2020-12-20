package com.github.ccguyka.showupdates.slack

import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates
import java.util.stream.Collectors

internal class SlackBody(private val artifact: String?) {

    private var projectUpdates: ProjectUpdates? = null

    fun updates(projectUpdates: ProjectUpdates?): SlackBody {
        this.projectUpdates = projectUpdates
        return this
    }

    fun build(): String {
        val text = StringBuilder()
        text.append("*Updates for _")
        text.append(artifact)
        text.append("_*")
        text.append("\n")
        text.append(printUpdates("parent", projectUpdates!!.parent))
        text.append(printUpdates("dependency", projectUpdates!!.dependency))
        text.append(printUpdates("plugin", projectUpdates!!.plugin))
        text.append(printUpdates("dependency management", projectUpdates!!.dependencyManagement))
        return text.toString()
    }

    private fun printUpdates(type: String, updates: DependencyUpdates): String {
        val text = StringBuilder()
        if (updates.artifacts.isNotEmpty()) {
            text.append("\n")
            text.append("*Available $type updates*")
            text.append("\n")
            for (dependencyUpdate in updates.artifacts) {
                text.append(getLineText(dependencyUpdate))
            }
        }
        return text.toString()
    }

    private fun getLineText(updates: ArtifactUpdate): String {
        val result = StringBuilder()
        result.append("• ")
        result.append(updates.name)
        result.append(" ... ")
        result.append(updates.current)
        result.append(" -> ")
        result.append(updates.updates.stream().collect(Collectors.joining(", ")))
        result.append("\n")
        return result.toString()
    }

    companion object {
        @JvmStatic
        fun artifact(artifact: String?): SlackBody {
            return SlackBody(artifact)
        }
    }
}
