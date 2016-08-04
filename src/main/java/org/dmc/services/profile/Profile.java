package org.dmc.services.profile;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Profile {

	private final String logTag = Profile.class.getName();

	private int id;
	private String displayName;
	private String company;
	private String jobTitle;
	private String phone;
	private String email;
	private String location;
	private String image;
	private String description;
	private ArrayList<String> skills;

    public Profile() {
        this.id = -1;
        this.displayName = new String();
        this.company = new String();
        this.jobTitle = new String();
        this.phone = new String();
        this.email = new String();
        this.location = new String();
        this.image = new String();
        this.description = new String();
        this.skills = new ArrayList<String>();
    }
    
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

    @JsonProperty("id")
	public int getId() {
		return id;
	}
    public void setId(String id) {
		this.id = Integer.parseInt(id);
	}
    
    
    @JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}
    public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

    
    @JsonProperty("company")
	public String getCompany() {
		return company;
	}
    public void setCompany(String company) {
		this.company = company;
	}

    
    @JsonProperty("jobTitle")
	public String getJobTitle() {
		return jobTitle;
	}
    public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
    

    @JsonProperty("phone")
	public String getPhone() {
		return phone;
	}
    public void setPhone(String phone) {
		this.phone = phone;
	}
    

    @JsonProperty("email")
	public String getEmail() {
		return email;
	}
    public void setEmail(String email) {
		this.email = email;
	}
    

    @JsonProperty("location")
	public String getLocation() {
		return location;
	}
    public void setLocation(String location) {
		this.location = location;
	}

    
    @JsonProperty("image")
	public String getImage() {
		return image;
	}
    public void setImage(String image) {
		this.image = image;
	}
	
    
    @JsonProperty("description")
	public String getDescription() {
		return description;
	}
    public void setDescription(String description) {
		this.description = description;
	}

    
    @JsonProperty("skills")
	public ArrayList<String> getSkills() {
		return skills;
	}
    public void setSkills(ArrayList<String> skills) {
		this.skills = skills;
	}

	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class Profile {\n");
		
		sb.append("  id: ").append(id).append("\n");
		sb.append("  displayName: ").append(displayName).append("\n");
		sb.append("  company: ").append(company).append("\n");
		sb.append("  jobTitle: ").append(jobTitle).append("\n");
		sb.append("  phone: ").append(phone).append("\n");
		sb.append("  email: ").append(email).append("\n");
		sb.append("  location: ").append(location).append("\n");
		sb.append("  image: ").append(image).append("\n");
		sb.append("  description: ").append(description).append("\n");
		sb.append("  skills: ").append(skills).append("\n");
		sb.append("}\n");
		return sb.toString();
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