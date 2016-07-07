package org.dmc.services.security;

import java.lang.reflect.Field;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.dmc.services.data.entities.SecuredEntity;
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
				if (!PermissionEvaluationHelper.userHasRole(requiredRole, entity.getTenantId())) {
					field.set(entity, null);
				}
			}
		}
	}
	
}
