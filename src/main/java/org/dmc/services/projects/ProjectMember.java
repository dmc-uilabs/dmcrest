package org.dmc.services.projects;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectMember {
	private String id;
	private int profileId;
	private int projectId; 
	private boolean accept;
	private int fromProfileId;
	private String from;
	private Date date;
	
	public ProjectMember()
	{ 
	}

	@JsonProperty("id")
	public String getId(){
		return projectId + "-" + profileId;
	}
	
	@JsonProperty("profileId")
	public String getProfileId(){
		return Integer.toString(profileId);
	}
	
	@JsonProperty("profileId")
	public void setProfileId(String value){
		profileId = Integer.parseInt(value);
	}
	
	@JsonProperty("projectId")
	public String getProjectId(){
		return Integer.toString(projectId);
	}
	
	@JsonProperty("projectId")
	public void setProjectId(String value){
		projectId = Integer.parseInt(value);
	}
	
	@JsonProperty("accept")
	public boolean getAccept(){
		return accept;
	}
	
	@JsonProperty("accept")
	public void setAccept(boolean value){
		accept = value;
	}
	
	@JsonProperty("fromProfileId")
	public String getFromProfileId(){
		return Integer.toString(fromProfileId);
	}
	
	@JsonProperty("fromProfileId")
	public void setFromProfileId(String value){
		fromProfileId = Integer.parseInt(value);
	}
	
	@JsonProperty("from")
	public String getFrom(){
		return from;
	}
	
	@JsonProperty("from")
	public void setFrom(String value){
		from = value;
	}
	
	@JsonProperty("date")
	public long getDate(){
		return date.getTime();
	}
	
	@JsonProperty("date")
	public void setDate(long value){
		date = new Date(value);
	}
	
}
