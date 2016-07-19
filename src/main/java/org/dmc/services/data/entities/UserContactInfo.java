package org.dmc.services.data.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_contact_info")
public class UserContactInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "user_private_contact_info_id")
	private UserPrivateContactInfo userPrivateContactInfo;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "user_public_contact_info_id")
	private UserPublicContactInfo userPublicContactInfo;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "user_member_portal_contact_info_id")
	private UserMemberPortalContactInfo userMemberPortalContactInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserPrivateContactInfo getUserPrivateContactInfo() {
		return userPrivateContactInfo;
	}

	public void setUserPrivateContactInfo(UserPrivateContactInfo userPrivateContactInfo) {
		this.userPrivateContactInfo = userPrivateContactInfo;
	}

	public UserPublicContactInfo getUserPublicContactInfo() {
		return userPublicContactInfo;
	}

	public void setUserPublicContactInfo(UserPublicContactInfo userPublicContactInfo) {
		this.userPublicContactInfo = userPublicContactInfo;
	}

	public UserMemberPortalContactInfo getUserMemberPortalContactInfo() {
		return userMemberPortalContactInfo;
	}

	public void setUserMemberPortalContactInfo(UserMemberPortalContactInfo userMemberPortalContactInfo) {
		this.userMemberPortalContactInfo = userMemberPortalContactInfo;
	}

}
