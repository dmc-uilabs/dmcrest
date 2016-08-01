package org.dmc.services.data.models;

public class UserRoleModel extends BaseModel {

	private Integer organizationId;
	private Integer userId;
	private Integer roleId;
	
	public Integer getOrganizationId() {
		return organizationId;
	}
	
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getRoleId() {
		return roleId;
	}
	
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
}
