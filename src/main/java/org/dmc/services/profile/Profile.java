package org.dmc.services.profile;

import java.util.ArrayList;

public class Profile {

	private final String logTag = Profile.class.getName();

	private final int id;
	private final String displayName;
	private final String company;
	private final String jobTitle;
	private final String phone;
	private final String email;
	private final String location;
	private final String image;
	private final String description;
	private final ArrayList<String> skills;

	private Profile(ProfileBuilder builder) {
		this.id = builder.id;
		this.displayName = builder.displayName;
		this.company = builder.company;
		this.jobTitle = builder.jobTitle;
		this.phone = builder.phone;
		this.email = builder.email;
		this.location = builder.location;
		this.image = builder.image;
		this.description = builder.description;
		this.skills = builder.skills;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getCompany() {
		return company;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getLocation() {
		return location;
	}

	public String getImage() {
		return image;
	}
	
	public String getDescription() {
		return image;
	}

	public ArrayList<String> getSkills() {
		return skills;
	}

	// Service Builder
	public static class ProfileBuilder {
		private final int id;
		private final String displayName;
		private final String company;
		private String jobTitle;
		private String phone;
		private String email;
		private String location;
		private String image;
		private String description;
		private ArrayList<String> skills;

		public ProfileBuilder(int id, String displayName, String company) {
			this.id = id;
			this.displayName = displayName;
			this.company = company;
		}

		public ProfileBuilder jobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}

		public ProfileBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public ProfileBuilder location(String location) {
			this.location = location;
			return this;
		}

		public ProfileBuilder image(String image) {
			this.image = image;
			return this;
		}

		public ProfileBuilder description(String description) {
			this.description = description;
			return this;
		}

		public ProfileBuilder tags(ArrayList<String> skills) {
			this.skills = skills;
			return this;
		}

		public Profile build() {
			return new Profile(this);
		}
	}
}