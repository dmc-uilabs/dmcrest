package org.dmc.services.data.entities;

import java.util.List;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ResourceEntity extends BaseEntity {
	
	public abstract User getOwner();

	public abstract List<User> getVips();
	
	public abstract Boolean getIsPublic();
}
