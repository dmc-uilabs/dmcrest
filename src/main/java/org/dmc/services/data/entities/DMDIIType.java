package org.dmc.services.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by kskronek on 8/11/2016.
 */
@Entity
@Table(name = "organization_dmdii_type")
public class DMDIIType extends BaseEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "organization_dmdii_type_category_id")
	private DMDIITypeCategory dmdiiTypeCategory;

	@Column(name = "tier")
	private Integer tier;

	@Column(name = "dmdii_member_desc")
	private String description;

	public Integer getId() {
		return id;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DMDIIType))
			return false;

		DMDIIType dmdiiType = (DMDIIType) o;

		if (!id.equals(dmdiiType.id))
			return false;
		if (!dmdiiTypeCategory.equals(dmdiiType.dmdiiTypeCategory))
			return false;
		if (tier != null ? !tier.equals(dmdiiType.tier) : dmdiiType.tier != null)
			return false;
		return description != null ? description.equals(dmdiiType.description) : dmdiiType.description == null;
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + (tier != null ? tier.hashCode() : 0);
		return result;
	}
}
