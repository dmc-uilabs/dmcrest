package org.dmc.services.security;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.dmc.services.data.entities.SecuredEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MapperFieldSecurityAspect {

	@Before("execution(* org.dmc.services.data.mappers.Mapper.mapToModel(..)) && args(entity) && !within(org.dmc.services.data.mappers.DefaultMapper)")
	public void nullifyProtectedFields(SecuredEntity entity) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : entity.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(RequiredPermission.class)) {
				field.setAccessible(true);
				String requiredRole = field.getAnnotation(RequiredPermission.class).value();
				if (!userHasRole(requiredRole, entity.getTenantId())) {
					field.set(entity, null);
				}
			}
		}
	}
	
	private boolean userHasRole(String requiredRole, Integer tenantId) {
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> userRoles = new ArrayList<String>();
		if (user.getIsSuperAdmin()) {
			return true;
		}
		String assignedRole = user.getRole(tenantId);
		switch (assignedRole) {
		case SecurityRoles.ADMIN:
			userRoles.add(SecurityRoles.ADMIN);
		case SecurityRoles.VIP:
			userRoles.add(SecurityRoles.VIP);
		case SecurityRoles.MEMBER:
			userRoles.add(SecurityRoles.MEMBER);
		}
		return userRoles.contains(requiredRole);
	}
}
