package org.dmc.services.data.entities;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.dmc.services.security.RequiredPermission;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

public class EntityTest {

	@Test
	public void testGettersAndSetters() throws Exception {
		List<String> foundEntities = findEntities();
		
		for(String name : foundEntities) {
			Object obj = null;
			Class<?> clazz = Class.forName(name);
			
			if(Modifier.isAbstract(clazz.getModifiers())) {
				System.out.println("Skipped test for Abstract class: " + clazz.getName());
			} else {
				obj = clazz.newInstance();
				
				PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(clazz);
				
				for(PropertyDescriptor descriptor : properties) {
					Method getter = descriptor.getReadMethod();
					Method setter = descriptor.getWriteMethod();
					
					if(getter != null && setter != null) {
						Object expected = getValueForSetter(descriptor);
						setter.invoke(obj, expected);
						Object actual = getter.invoke(obj);
						
						Assert.assertEquals("Failed on " + clazz.getSimpleName() + ".", actual, expected);
					}
				}
			}
		}
	}
	
	@Test
	public void testEntitySecuritySettings() throws IOException, ClassNotFoundException {
		List<String> foundEntities = findEntities();
		
		for (String entityName : foundEntities) {
			Class<?> clazz = Class.forName(entityName);
			if (!SecuredEntity.class.isAssignableFrom(clazz)) {
				for (Field field : clazz.getDeclaredFields()) {
					Assert.assertFalse("Error for " + entityName + ". Only SecuredEntity classes may use @RequiredPermission", field.isAnnotationPresent(RequiredPermission.class));
				}
			}
		}
	}
	
	private List<String> findEntities() throws IOException {
		ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
		ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClasses("org.dmc.services.data.entities");
		
		return classes
				.stream()
				.filter(c -> {
					if(!c.getSimpleName().equals("BaseEntity") && BaseEntity.class.isAssignableFrom(c.load()))
						return c.getName() != null;
					return false;
				})
				.map(s -> s.getName())
				.collect(Collectors.toList());
	}

	private Object getValueForSetter(PropertyDescriptor descriptor) throws NoSuchMethodException, SecurityException {
		
		Class<?> type = descriptor.getPropertyType();
		Object returnObj = null;
		
		if(type.isAnonymousClass()) {
			System.out.println("Skipped tests for Getters and Setters on an Anonymous class: " + type.getSimpleName());
		} else if(type.isPrimitive()) {
			if(type == int.class) {
				returnObj = new Integer(1);
			} else if(type == float.class) {
				returnObj = new Float("3.14");
			} else if(type == byte.class) {
				returnObj = new Byte("1");
			} else if(type == short.class) {
				returnObj = new Short("1");
			} else if(type == long.class) {
				returnObj = new Long("1");
			} else if(type == double.class) {
				returnObj = new Double("3.14");
			} else if(type == boolean.class) {
				returnObj = new Boolean(true);
			} else if(type == char.class) {
				returnObj = new Character('F');
			} 			
		} else if(Modifier.isFinal(type.getModifiers())) {
			
			if(type.equals(String.class)) {
				returnObj = new String("asdfasdf");
			} else {
				System.out.println("Skipped tests for Getters and Setters on a Final class: " + type.getSimpleName());
			}
		} else if(type == List.class) {
			returnObj = new ArrayList();
		} else {
			returnObj = Mockito.mock(descriptor.getPropertyType());
		}
		
		return returnObj;
	}
}
