package org.dmc.services.search;

import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;
import org.dmc.services.services.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 200005921 on 2/1/2016.
 */
public class SearchResult {

    private List<Component> components;
    private List<Project> projects;
    private List<Service> services;


    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void addComponent (Component component) {
        if (components == null) {
            components = new ArrayList<Component>();
        }
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
