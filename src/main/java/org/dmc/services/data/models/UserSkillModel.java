package org.dmc.services.data.models;

public class UserSkillModel extends BaseModel {

	private Integer id;
	private String skillName;
	private Integer experienceLevel;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public Integer getExperienceLevel() {
		return experienceLevel;
	}

	public void setExperienceLevel(Integer experienceLevel) {
		this.experienceLevel = experienceLevel;
	}

}
