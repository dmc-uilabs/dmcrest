package org.dmc.services.projects;

//import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.dmc.services.EqualsUtil;
import org.dmc.services.HashCodeUtil;

// JSON sent by frontend as of 10-Mar-2016
// {
//   description: "adfa"
//   dueDate: "1457413200000"
//   featureImage: {thumbnail: "/images/project_relay_controller.png", large: "/images/project_relay_controller.png"}
//   projectManager: "DMC member"
//   title: "cathy3"
//   type: "private"
// }

public class ProjectJoinRequest {

    private String id = null;
	private String projectId = null; 
	private String profileId = null;
	
//	@JsonCreator
//	public ProjectCreateRequest(@JsonProperty("name") String name, @JsonProperty("description") String description)
//	{
//		this.name = name;
//		this.description = description;
//	}
	
	public ProjectJoinRequest() { 
	}

	@JsonProperty("id")
    public String getId(){
		return id;
	}
	
	@JsonProperty("id")
	public void setId(String value){
		id = value;
	}
	
	@JsonProperty("projectId")
	public String getProjectId(){
		return projectId;
	}
	
	@JsonProperty("projectId")
	public void setProjectId(String value){
		projectId = value;
	}

	@JsonProperty("profileId")
	public String getProfileId(){
		return profileId;
	}
	
	@JsonProperty("profileId")
	public void setProfileId(String value){
		profileId = value;
	}

	public boolean equals(Object o) {
		if (null == o) return false;
		if (this == o) return true;
		if (!(o instanceof ProjectJoinRequest)) return false;
		
		ProjectJoinRequest pjr = (ProjectJoinRequest) o;
		if (!EqualsUtil.areEqual(this.id, pjr.id)) return false;
		if (!EqualsUtil.areEqual(this.projectId, pjr.projectId)) return false;
		if (!EqualsUtil.areEqual(this.profileId, pjr.profileId)) return false;
		return true;
	}

	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.id);
		result = HashCodeUtil.hash(result, this.projectId);
		result = HashCodeUtil.hash(result, this.profileId);
	    return result;
	}
}
