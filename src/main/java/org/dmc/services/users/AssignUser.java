package org.dmc.services.users;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AssignUser {
	
	private int id;
	private String name;
	
	private final String logTag = AssignUser.class.getName();

	public AssignUser() {
		this.id = -1;
		this.name = new String();
	}

	public AssignUser(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
