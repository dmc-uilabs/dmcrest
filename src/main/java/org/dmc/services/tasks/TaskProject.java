package org.dmc.services.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskProject {
	private final String logTag = TaskProject.class.getName();
    private int id;
    private String title;

    public TaskProject() {
    }

    public TaskProject(int id, String title)
    {
    	this.id=id;
    	this.title=title;
    }

    @JsonProperty("id")
    public int getId()
    {
    	return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle()
    {
    	return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}