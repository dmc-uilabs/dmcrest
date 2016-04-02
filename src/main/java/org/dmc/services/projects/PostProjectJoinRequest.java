package org.dmc.services.projects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dmc.services.EqualsUtil;
import org.dmc.services.HashCodeUtil;

// JSON sent by frontend as of 25-Mar-2016
// {
//   projectId: "100"
//   profileId: "14"
// }

public class PostProjectJoinRequest {

	private String projectId = null; 
	private String profileId = null;
	
	public PostProjectJoinRequest() { 
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
		if (!(o instanceof PostProjectJoinRequest)) return false;
		
		PostProjectJoinRequest pjr = (PostProjectJoinRequest) o;
		if (!EqualsUtil.areEqual(this.projectId, pjr.projectId)) return false;
		if (!EqualsUtil.areEqual(this.profileId, pjr.profileId)) return false;
		return true;
	}

	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.projectId);
		result = HashCodeUtil.hash(result, this.profileId);
	    return result;
	}
}
