package org.dmc.services.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dmdii_member_user")
public class DMDIIMemberUser extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "dmdii_role_id")
	private DMDIIRole role;

	@ManyToOne
	@JoinColumn(name = "organization_dmdii_member_id")
	private DMDIIMember dmdiiMember;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DMDIIRole getRole() {
		return role;
	}

	public void setRole(DMDIIRole role) {
		this.role = role;
	}

	public DMDIIMember getDmdiiMember() {
		return dmdiiMember;
	}

	public void setDmdiiMember(DMDIIMember dmdiiMember) {
		this.dmdiiMember = dmdiiMember;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DMDIIMemberUser))
			return false;

		DMDIIMemberUser that = (DMDIIMemberUser) o;

		if (!id.equals(that.id))
			return false;
		if (!user.equals(that.user))
			return false;
		if (role != null ? !role.equals(that.role) : that.role != null)
			return false;
		return dmdiiMember.equals(that.dmdiiMember);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + user.hashCode();
		result = 31 * result + (role != null ? role.hashCode() : 0);
		result = 31 * result + dmdiiMember.hashCode();
		return result;
	}
}
