package org.dmc.services.search;

import org.dmc.services.company.Company;
import org.dmc.services.components.Component;
import org.dmc.services.profile.Profile;
import org.dmc.services.projects.Project;
import org.dmc.services.services.Service;
import org.dmc.services.users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 200005921 on 2/1/2016.
 */
public class SearchResult {

    private List<Company> companies;
    private List<Component> components;
    private List<Project> projects;
    private List<Service> services;
    private List<User> users;


    private List<Profile> profiles;


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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }


    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
