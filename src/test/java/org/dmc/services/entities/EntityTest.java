package org.dmc.services.entities;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class EntityTest {

	private String[] classes = {
			"org.dmc.services.entities.DMDIIAreaOfExpertise",
			"org.dmc.services.entities.DMDIIAward",
			"org.dmc.services.entities.DMDIIContactType",
			"org.dmc.services.entities.DMDIIInstituteInvolvement",
			"org.dmc.services.entities.DMDIIMember",
			"org.dmc.services.entities.DMDIIMemberContact",
			"org.dmc.services.entities.DMDIIMemberCustomer",
			"org.dmc.services.entities.DMDIIMemberFinance",
			"org.dmc.services.entities.DMDIIMemberUser",
			"org.dmc.services.entities.DMDIIProject",
			"org.dmc.services.entities.DMDIIProjectFocusArea",
			"org.dmc.services.entities.DMDIIProjectThrust",
			"org.dmc.services.entities.DMDIIRndFocus",
			"org.dmc.services.entities.DMDIIRole",
			"org.dmc.services.entities.DMDIISkill",
			"org.dmc.services.entities.Organization",
			"org.dmc.services.entities.User"
	};
	
	@Test
	public void testGettersAndSetters() throws Exception {
		
		for(String className : classes) {
			Object obj = null;
			Class<?> clazz = Class.forName(className);
			
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
		} else {
			returnObj = Mockito.mock(descriptor.getPropertyType());
		}
		
		return returnObj;
	}
}
