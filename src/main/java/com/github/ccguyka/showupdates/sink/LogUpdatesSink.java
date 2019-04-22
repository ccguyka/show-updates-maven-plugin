package com.github.ccguyka.showupdates.sink;

import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

import com.github.ccguyka.showupdates.objects.DependencyUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;

public class LogUpdatesSink {

    private final Log log;
    private final String type;

    public LogUpdatesSink(final String type, final Log log) {
        this.type = type;
        this.log = log;
    }

    public void printUpdates(final DependencyUpdates latestUpdates) {
        if (!latestUpdates.getDependency().isEmpty()) {
            log.info("Available " + type + " updates:");

            for (final DependencyUpdate dependencyUpdate : latestUpdates.getDependency()) {
                log.info(getLineText(dependencyUpdate));
            }
        }
    }

    private String getLineText(final DependencyUpdate latestUpdate) {
        final StringBuilder result = new StringBuilder();
        result.append("  ");
        result.append(latestUpdate.getName());
        result.append(" ... ");
        result.append(latestUpdate.getCurrentVersion());
        result.append(" -> ");
        result.append(latestUpdate.getUpdates().stream().collect(Collectors.joining(",")));
        return result.toString();
    }
}
