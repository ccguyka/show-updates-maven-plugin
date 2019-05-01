package com.github.ccguyka.showupdates.consumer;

import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

import com.github.ccguyka.showupdates.objects.DependencyUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

public class PrintUpdates {

    private ProjectUpdates projectUpdates;
    private Log log;

    private PrintUpdates(ProjectUpdates projectUpdates, Log log) {
        this.projectUpdates = projectUpdates;
        this.log = log;
    }

    public static void print(ProjectUpdates projectUpdates, final Log log) {
        PrintUpdates printUpdates = new PrintUpdates(projectUpdates, log);

        printUpdates.print();
    }

    private void print() {
        printUpdates("parent", projectUpdates.getParentUpdates());
        printUpdates("dependency", projectUpdates.getDependencyUpdates());
        printUpdates("plugin", projectUpdates.getPluginUpdates());
        printUpdates("dependency management", projectUpdates.getDependencyManagementUpdates());

    }

    public void printUpdates(String type, final DependencyUpdates updates) {
        if (!updates.getDependency().isEmpty()) {
            log.info("Available " + type + " updates:");

            for (final DependencyUpdate dependencyUpdate : updates.getDependency()) {
                log.info(getLineText(dependencyUpdate));
            }
        }
    }

    private String getLineText(final DependencyUpdate updates) {
        final StringBuilder result = new StringBuilder();
        result.append("  ");
        result.append(updates.getName());
        result.append(" ... ");
        result.append(updates.getCurrentVersion());
        result.append(" -> ");
        result.append(updates.getUpdates().stream().collect(Collectors.joining(",")));
        return result.toString();
    }

}
