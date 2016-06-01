package org.dmc.services.entities;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	@Id
	@GenericGenerator(name = "seq", strategy = "sequence")
	@GeneratedValue(generator = "seq")
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(o == this) {
			return true;
		}
		
		if(!(o instanceof BaseEntity)) {
			return false;
		}
		
		BaseEntity that = (BaseEntity) o;
		
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(this.id, that.getId());
		
		return eb.isEquals();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}
