package org.dmc.services.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MiniUserModel extends BaseModel {
	
	@JsonProperty("displayName")
	private String realname;
	private String email;
	
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
