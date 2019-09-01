package com.github.ccguyka.showupdates.slack;

import static java.util.stream.Collectors.joining;

import com.github.ccguyka.showupdates.objects.ArtifactUpdate;
import com.github.ccguyka.showupdates.objects.DependencyUpdates;
import com.github.ccguyka.showupdates.objects.ProjectUpdates;

class SlackBody {

    private ProjectUpdates projectUpdates;
    private String artifact;

    public SlackBody(String artifact) {
        this.artifact = artifact;
    }

    static SlackBody artifact(String artifact) {
        return new SlackBody(artifact);
    }

    SlackBody updates(ProjectUpdates projectUpdates) {
        this.projectUpdates = projectUpdates;

        return this;
    }

    String build() {
        StringBuilder text = new StringBuilder();
        text.append("*Updates for _");
        text.append(artifact);
        text.append("_*");
        text.append("\n");
        text.append(printUpdates("parent", projectUpdates.getParent()));
        text.append(printUpdates("dependency", projectUpdates.getDependency()));
        text.append(printUpdates("plugin", projectUpdates.getPlugin()));
        text.append(printUpdates("dependency management", projectUpdates.getDependencyManagement()));

        return text.toString();
    }

    private String printUpdates(String type, final DependencyUpdates updates) {
        StringBuilder text = new StringBuilder();
        if (!updates.getArtifacts().isEmpty()) {
            text.append("\n");
            text.append("*Available " + type + " updates*");
            text.append("\n");

            for (final ArtifactUpdate dependencyUpdate : updates.getArtifacts()) {
                text.append(getLineText(dependencyUpdate));
            }
        }
        return text.toString();
    }

    private String getLineText(final ArtifactUpdate updates) {
        final StringBuilder result = new StringBuilder();
        result.append("• ");
        result.append(updates.getName());
        result.append(" ... ");
        result.append(updates.getCurrent());
        result.append(" -> ");
        result.append(updates.getUpdates().stream().collect(joining(",")));
        result.append("\n");
        return result.toString();
    }
}
