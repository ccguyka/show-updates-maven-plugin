package com.github.ccguyka.showupdates.objects;

import java.util.List;

public class ArtifactUpdate {

    private String name;
    private String current;
    private List<String> updates;

    public ArtifactUpdate(String name, String current, List<String> updates) {
        this.name = name;
        this.current = current;
        this.updates = updates;
    }

    public String getName() {
        return name;
    }

    public String getCurrent() {
        return current;
    }

    public List<String> getUpdates() {
        return updates;
    }
}
