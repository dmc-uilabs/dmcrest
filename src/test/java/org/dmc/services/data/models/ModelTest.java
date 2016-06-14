package org.dmc.services.data.models;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

public class ModelTest {
	
	@Test
	public void testGettersAndSetters() throws Exception {
		
		ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
		ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClasses("org.dmc.services.data.models");
		
		List<String> foundModels = classes
										.stream()
										.filter(c -> {
											if(!c.getSimpleName().equals("BaseModel"))
												return c.getName() != null;
											return false;
										})
										.map(s -> s.getName())
										.collect(Collectors.toList());
		
		
		for(String name : foundModels) {
			Object obj = null;
			Class<?> clazz = Class.forName(name);
			
			obj = clazz.newInstance();
			
			PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(clazz);
			
			for(PropertyDescriptor descriptor : properties) {
				Method getter = descriptor.getReadMethod();
				Method setter = descriptor.getWriteMethod();
				
				if(getter != null && setter != null) {
					Object expected = getValueForSetter(descriptor);
					setter.invoke(obj, expected);
					Object actual = getter.invoke(obj);
					
					Assert.assertEquals("Failed on " + clazz.getSimpleName() + "." + descriptor.getName(), expected, actual);
				}
			}
		}
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
				System.out.println("Skipped tests for Getters in Setters on a Final class: " + type.getSimpleName());
			}
		} else {
			returnObj = Mockito.mock(descriptor.getPropertyType());
		}
		
		return returnObj;
	}
}
