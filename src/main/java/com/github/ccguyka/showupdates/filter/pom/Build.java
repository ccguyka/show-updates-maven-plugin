package com.github.ccguyka.showupdates.filter.pom;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {

    private List<Dependency> plugins;

    public List<Dependency> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<Dependency> plugins) {
        this.plugins = plugins;
    }
}
