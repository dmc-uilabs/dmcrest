package org.dmc.services.models;

import javax.persistence.Column;

public class DMDIISkillModel extends BaseModel {

	private String tagName;
	private String tagLink;
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagLink() {
		return tagLink;
	}
	public void setTagLink(String tagLink) {
		this.tagLink = tagLink;
	}
}
