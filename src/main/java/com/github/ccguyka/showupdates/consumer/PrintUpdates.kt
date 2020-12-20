package com.github.ccguyka.showupdates.consumer

import com.github.ccguyka.showupdates.objects.ArtifactUpdate
import com.github.ccguyka.showupdates.objects.DependencyUpdates
import com.github.ccguyka.showupdates.objects.ProjectUpdates
import org.apache.maven.plugin.logging.Log
import java.util.stream.Collectors

class PrintUpdates private constructor(private val projectUpdates: ProjectUpdates, private val log: Log) {

    private fun print() {
        printUpdates("parent", projectUpdates.parent)
        printUpdates("dependency", projectUpdates.dependency)
        printUpdates("plugin", projectUpdates.plugin)
        printUpdates("dependency management", projectUpdates.dependencyManagement)
    }

    private fun printUpdates(type: String, updates: DependencyUpdates) {
        if (!updates.artifacts.isEmpty()) {
            log.info("Available $type updates:")
            for (dependencyUpdate in updates.artifacts) {
                log.info(getLineText(dependencyUpdate))
            }
        }
    }

    private fun getLineText(updates: ArtifactUpdate): String {
        val result = StringBuilder()
        result.append("  ")
        result.append(updates.name)
        result.append(" ... ")
        result.append(updates.current)
        result.append(" -> ")
        result.append(updates.updates.stream().collect(Collectors.joining(", ")))
        return result.toString()
    }

    companion object {
        @JvmStatic
        fun print(projectUpdates: ProjectUpdates, log: Log) {
            val printUpdates = PrintUpdates(projectUpdates, log)
            printUpdates.print()
        }
    }
}
