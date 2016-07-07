package org.dmc.services.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author awamsley
 * This interface is used to prevent entity fields from being passed to the front end unless the
 * requesting user has the specified role for the entity's owning organization.
 * This can ONLY be used within entities that implement the SecuredEntity interface!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequiredPermission {

	/**
	 * This is the role that is required to have access to the annotated field
	 * Please use the static fields found in org.dmc.services.security.SecurityRoles
	 */
	String value();

}
