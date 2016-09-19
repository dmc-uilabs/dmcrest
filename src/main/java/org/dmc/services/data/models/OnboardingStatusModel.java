package org.dmc.services.data.models;

/**
 * Created by kskronek on 8/16/2016.
 */

public class OnboardingStatusModel extends BaseModel {

	private boolean profile;

	private boolean account;

	private boolean company;

	private boolean storefront;

	public boolean isProfile() {
		return profile;
	}

	public boolean getProfile() {
		return profile;
	}

	public void setProfile(boolean profile) {
		this.profile = profile;
	}

	public boolean isAccount() {
		return account;
	}

	public boolean getAccount() {
		return account;
	}

	public void setAccount(boolean account) {
		this.account = account;
	}

	public boolean isCompany() {
		return company;
	}

	public boolean getCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

	public boolean isStorefront() {
		return storefront;
	}

	public boolean getStorefront() {
		return storefront;
	}

	public void setStorefront(boolean storefront) {
		this.storefront = storefront;
	}
}
