package org.dmc.services.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organization_dmdii_type_category")
public class DMDIITypeCategory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String category;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DMDIITypeCategory))
			return false;

		DMDIITypeCategory that = (DMDIITypeCategory) o;

		if (!id.equals(that.id))
			return false;
		return category.equals(that.category);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + category.hashCode();
		return result;
	}
}
