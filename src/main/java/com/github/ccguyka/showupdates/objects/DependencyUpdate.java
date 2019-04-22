package com.github.ccguyka.showupdates.objects;

import java.util.List;

public class DependencyUpdate {

    private String name;
    private String currentVersion;
    private List<String> updates;

    public DependencyUpdate(String name, String currentVersion, List<String> updates) {
        this.name = name;
        this.currentVersion = currentVersion;
        this.updates = updates;
    }

    public String getName() {
        return name;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public List<String> getUpdates() {
        return updates;
    }
}
