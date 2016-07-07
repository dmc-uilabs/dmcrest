package org.dmc.services.data.entities;

/**
 * @author awamsley
 * This interface must be implemented by entities that are using the @RequiredPermission annotation
 */
public interface SecuredEntity {
	
	/**
	 * This must return the id for the organization that owns the entity instance
	 */
	Integer getTenantId();

}
