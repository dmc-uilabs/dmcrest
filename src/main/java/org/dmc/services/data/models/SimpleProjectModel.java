package org.dmc.services.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleProjectModel extends BaseModel {
	
	Integer id;
	
	@JsonProperty("title")
	String group_name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	
}
