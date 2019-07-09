package com.github.ccguyka.showupdates.consumer;

import static java.util.stream.Collectors.joining;

import org.apache.maven.plugin.logging.Log;

import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

public class PrintUpdates {

    private final ProjectUpdates projectUpdates;
    private final Log log;

    private PrintUpdates(ProjectUpdates projectUpdates, Log log) {
        this.projectUpdates = projectUpdates;
        this.log = log;
    }

    public static void print(ProjectUpdates projectUpdates, final Log log) {
        PrintUpdates printUpdates = new PrintUpdates(projectUpdates, log);

        printUpdates.print();
    }

    private void print() {
        printUpdates("parent", projectUpdates.getParent());
        printUpdates("dependency", projectUpdates.getDependency());
        printUpdates("plugin", projectUpdates.getPlugin());
        printUpdates("dependency management", projectUpdates.getDependencyManagement());

    }

    private void printUpdates(String type, final DependencyUpdates updates) {
        if (!updates.getArtifacts().isEmpty()) {
            log.info("Available " + type + " updates:");

            for (final ArtifactUpdate dependencyUpdate : updates.getArtifacts()) {
                log.info(getLineText(dependencyUpdate));
            }
        }
    }

    private String getLineText(final ArtifactUpdate updates) {
        final StringBuilder result = new StringBuilder();
        result.append("  ");
        result.append(updates.getName());
        result.append(" ... ");
        result.append(updates.getCurrent());
        result.append(" -> ");
        result.append(updates.getUpdates().stream().collect(joining(",")));
        return result.toString();
    }

}
