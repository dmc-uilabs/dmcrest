package org.dmc.services.projects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectTag {

	private String id = null;
	private String projectId = null;
	private String name = null;

	public ProjectTag() {	
	}
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("projectId")
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
