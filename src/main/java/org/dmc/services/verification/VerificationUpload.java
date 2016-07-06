package org.dmc.services.verification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerificationUpload {
	
	private String url; 
	private String table; 
	private int id; 
	private String userEPPN;
	
    @JsonProperty("url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
    @JsonProperty("table")
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	
    @JsonProperty("id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
    @JsonProperty("userEPPN")
	public String getUserEPPN() {
		return userEPPN;
	}
	public void setUserEPPN(String userEPPN) {
		this.userEPPN = userEPPN;
	} 
	

}
