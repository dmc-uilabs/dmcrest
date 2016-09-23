package org.dmc.services.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by kskronek on 8/16/2016.
 */
@Entity
@Table(name = "onboarding_status")
public class OnboardingStatus extends BaseEntity {

	@Id
	@Column(name = "user_id")
	private Integer id;

	private boolean profile;

	private boolean account;

	private boolean company;

	private boolean storefront;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isProfile() {
		return profile;
	}

	public void setProfile(boolean profile) {
		this.profile = profile;
	}

	public boolean isAccount() {
		return account;
	}

	public void setAccount(boolean account) {
		this.account = account;
	}

	public boolean isCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

	public boolean isStorefront() {
		return storefront;
	}

	public void setStorefront(boolean storefront) {
		this.storefront = storefront;
	}
}
