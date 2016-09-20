package org.dmc.services.dmdiitype;

import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.entities.DMDIITypeCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "organization_dmdii_type")
public class DMDIIType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "tier")
	private Integer tier;

	@ManyToOne
	@JoinColumn(name = "organization_dmdii_type_category_id")
	private DMDIITypeCategory dmdiiTypeCategory;

	@Column(name = "dmdii_member_desc")
	private String description;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DMDIITypeCategory getDmdiiTypeCategory() {
		return dmdiiTypeCategory;
	}

	public void setDmdiiTypeCategory(DMDIITypeCategory dmdiiTypeCategory) {
		this.dmdiiTypeCategory = dmdiiTypeCategory;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
