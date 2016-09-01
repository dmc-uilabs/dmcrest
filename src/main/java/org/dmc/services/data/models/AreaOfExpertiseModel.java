package org.dmc.services.data.models;

public class AreaOfExpertiseModel extends BaseModel {

	private String name;

	private String description;

	private String link;

	private Boolean isDmdii = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getIsDmdii() {
		return isDmdii;
	}

	public void setIsDmdii(Boolean isDmdii) {
		this.isDmdii = isDmdii;
	}

}
