package org.dmc.services.projects;

//import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectCreateRequest {
	private String name = null;
	private String description = null; 
	
//	@JsonCreator
//	public ProjectCreateRequest(@JsonProperty("name") String name, @JsonProperty("description") String description)
//	{
//		this.name = name;
//		this.description = description;
//	}
	
	public ProjectCreateRequest() { 
	}

	@JsonProperty("name")
    public String getName(){
		return name;
	}
	
	@JsonProperty("name")
	public void setName(String value){
		name = value;
	}
	
	@JsonProperty("description")
	public String getDescription(){
		return description;
	}
	
	@JsonProperty("description")
	public void setDescription(String value){
		description = value;
	}
}
