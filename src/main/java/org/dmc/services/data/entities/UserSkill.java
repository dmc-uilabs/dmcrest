package org.dmc.services.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_skills")
public class UserSkill extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_skill_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "user_skill_name")
	private String skillName;

	@Column(name = "experience_level")
	private Integer experienceLevel;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((experienceLevel == null) ? 0 : experienceLevel.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((skillName == null) ? 0 : skillName.hashCode());
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
		UserSkill other = (UserSkill) obj;
		if (experienceLevel == null) {
			if (other.experienceLevel != null)
				return false;
		} else if (!experienceLevel.equals(other.experienceLevel))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (skillName == null) {
			if (other.skillName != null)
				return false;
		} else if (!skillName.equals(other.skillName))
			return false;
		return true;
	}

}
