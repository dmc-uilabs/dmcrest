package org.dmc.services.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.dmc.services.TestConfig;
import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.mappers.MapperFactory.MapperRegistrationException;
import org.dmc.services.data.models.BaseModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@SuppressWarnings("unchecked")
public class MapperFactoryTest {
	
	@Mock
	private ApplicationContext applicationContext;

	@InjectMocks
	private MapperFactory mapperFactory;
	
	private Mapper<TestBaseEntity1, TestBaseModel1> mockMapper1;
	private Mapper<TestBaseEntity2, TestBaseModel2> mockMapper2;

	private class TestBaseEntity1 extends BaseEntity{
		public Integer getId() {
			return 0;
		}
	}
	private class TestBaseEntity2 extends BaseEntity{
		public Integer getId() {
			return 0;
		}
	}
	
	private class TestBaseModel1 extends BaseModel{}
	private class TestBaseModel2 extends BaseModel{}
	
	@Before
	public void before() {
		mockMapper1 = Mockito.mock(Mapper.class);
		when(mockMapper1.supportsEntity()).thenReturn(TestBaseEntity1.class);
		when(mockMapper1.supportsModel()).thenReturn(TestBaseModel1.class);
		
		mockMapper2 = Mockito.mock(Mapper.class);
		when(mockMapper2.supportsEntity()).thenReturn(TestBaseEntity2.class);
		when(mockMapper2.supportsModel()).thenReturn(TestBaseModel2.class);
		
		mapperFactory.registerMapper(mockMapper1);
		mapperFactory.registerMapper(mockMapper2);
	}

	@Test
	public void testMapperFor() {
		when(applicationContext.getBean(mockMapper1.getClass())).thenAnswer(new Answer<Mapper>() {
			public Mapper answer(InvocationOnMock invocation) {
				return mockMapper1;
			}
		});
		Mapper<TestBaseEntity1, TestBaseModel1> returnedMapper1 = mapperFactory.mapperFor(TestBaseEntity1.class, TestBaseModel1.class);
		assertEquals(mockMapper1, returnedMapper1);
		
		when(applicationContext.getBean(mockMapper2.getClass())).thenAnswer(new Answer<Mapper>() {
			public Mapper answer(InvocationOnMock invocation) {
				return mockMapper2;
			}
		});
		Mapper<TestBaseEntity2, TestBaseModel2> returnedMapper2 = mapperFactory.mapperFor(TestBaseEntity2.class, TestBaseModel2.class);
		assertEquals(mockMapper2, returnedMapper2);
	}
	
	@Test
	public void testMapperFor_noMapperFound() {
		Mapper<TestBaseEntity1, TestBaseModel2> returnedMapper = mapperFactory.mapperFor(TestBaseEntity1.class, TestBaseModel2.class);
		assertNotNull(returnedMapper);
		assertNotEquals(mockMapper1, returnedMapper);
		assertNotEquals(mockMapper2, returnedMapper);
	}
	
	@Test(expected = MapperRegistrationException.class)
	public void testRegisterMapper_duplicateRegistration() {
		Mapper<TestBaseEntity1, TestBaseModel1> duplicateMockMapper = Mockito.mock(Mapper.class);
		when(duplicateMockMapper.supportsEntity()).thenReturn(TestBaseEntity1.class);
		when(duplicateMockMapper.supportsModel()).thenReturn(TestBaseModel1.class);
		mapperFactory.registerMapper(duplicateMockMapper);
	}

}
