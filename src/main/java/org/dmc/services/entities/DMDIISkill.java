package org.dmc.services.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "dmdii_skill")
public class DMDIISkill extends BaseEntity {

	@Id
	@SequenceGenerator(name = "dmdiiSkillSeqGen", sequenceName = "dmdii_skill_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dmdiiSkillSeqGen")
	private Integer id;

	@Column(name = "tag_name")
	private String tagName;

	@Column(name = "tag_link")
	private String tagLink;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((tagLink == null) ? 0 : tagLink.hashCode());
		result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
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
		DMDIISkill other = (DMDIISkill) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tagLink == null) {
			if (other.tagLink != null)
				return false;
		} else if (!tagLink.equals(other.tagLink))
			return false;
		if (tagName == null) {
			if (other.tagName != null)
				return false;
		} else if (!tagName.equals(other.tagName))
			return false;
		return true;
	}
}
