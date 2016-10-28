package org.dmc.services.data.models;

import org.dmc.services.data.entities.ProjectJoinApprovalRequest.ProjectJoinApprovalRequestStatus;

public class ProjectJoinApprovalRequestModel extends BaseModel {
	
	private Integer projectId;
	private MiniUserModel user;
	private ProjectJoinApprovalRequestStatus status;
	
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public MiniUserModel getUser() {
		return user;
	}
	public void setUser(MiniUserModel user) {
		this.user = user;
	}
	public ProjectJoinApprovalRequestStatus getStatus() {
		return status;
	}
	public void setStatus(ProjectJoinApprovalRequestStatus status) {
		this.status = status;
	}

}
