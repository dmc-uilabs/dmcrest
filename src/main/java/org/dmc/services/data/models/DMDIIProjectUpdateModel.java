package org.dmc.services.data.models;

import java.util.Date;

public class DMDIIProjectUpdateModel extends BaseModel {

	private String created;
	
	private String title;
	
	private String description;
	
	private Integer creator;
	
	private Integer dmdiiProject;

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Integer getDmdiiProject() {
		return dmdiiProject;
	}

	public void setDmdiiProject(Integer dmdiiProject) {
		this.dmdiiProject = dmdiiProject;
	}
}
