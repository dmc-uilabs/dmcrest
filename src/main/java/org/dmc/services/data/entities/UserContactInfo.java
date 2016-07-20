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

	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "user_private_contact_info_id")
	private UserPrivateContactInfo userPrivateContactInfo;

	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "user_public_contact_info_id")
	private UserPublicContactInfo userPublicContactInfo;

	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userPrivateContactInfo == null) ? 0 : userPrivateContactInfo.hashCode());
		result = prime * result + ((userPublicContactInfo == null) ? 0 : userPublicContactInfo.hashCode());
		result = prime * result + ((userMemberPortalContactInfo == null) ? 0 : userMemberPortalContactInfo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserContactInfo other = (UserContactInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userPrivateContactInfo == null) {
			if (other.userPrivateContactInfo != null)
				return false;
		} else if (!userPrivateContactInfo.equals(other.userPrivateContactInfo))
			return false;
		if (userPublicContactInfo == null) {
			if (other.userPublicContactInfo != null)
				return false;
		} else if (!userPublicContactInfo.equals(other.userPublicContactInfo))
			return false;
		if (userMemberPortalContactInfo == null) {
			if (other.userMemberPortalContactInfo != null)
				return false;
		} else if (!id.equals(other.userMemberPortalContactInfo))
			return false;
		return true;
	}

}
