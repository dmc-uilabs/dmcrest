package org.dmc.services.data.models;

public class UserContactInfoModel extends BaseModel {

	private Integer id;

	private UserPrivateContactInfoModel userPrivateContactInfo;

	private UserPublicContactInfoModel userPublicContactInfo;

	private UserMemberPortalContactInfoModel userMemberPortalContactInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserPrivateContactInfoModel getUserPrivateContactInfo() {
		return userPrivateContactInfo;
	}

	public void setUserPrivateContactInfo(UserPrivateContactInfoModel userPrivateContactInfo) {
		this.userPrivateContactInfo = userPrivateContactInfo;
	}

	public UserPublicContactInfoModel getUserPublicContactInfo() {
		return userPublicContactInfo;
	}

	public void setUserPublicContactInfo(UserPublicContactInfoModel userPublicContactInfo) {
		this.userPublicContactInfo = userPublicContactInfo;
	}

	public UserMemberPortalContactInfoModel getUserMemberPortalContactInfo() {
		return userMemberPortalContactInfo;
	}

	public void setUserMemberPortalContactInfo(UserMemberPortalContactInfoModel userMemberPortalContactInfo) {
		this.userMemberPortalContactInfo = userMemberPortalContactInfo;
	}

}
