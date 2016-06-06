package org.dmc.services.models;

public class DMDIIMemberUserModel extends BaseModel {

	
	private UserModel user;
	private DMDIIRoleModel role;
	
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public DMDIIRoleModel getRole() {
		return role;
	}
	public void setRole(DMDIIRoleModel role) {
		this.role = role;
	}
	
}
