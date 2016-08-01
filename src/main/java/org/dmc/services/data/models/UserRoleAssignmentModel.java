package org.dmc.services.data.models;

public class UserRoleAssignmentModel extends BaseModel {
	
	private Integer organizationId;
	private RoleModel role;
	
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public RoleModel getRole() {
		return role;
	}
	public void setRole(RoleModel role) {
		this.role = role;
	}

}
