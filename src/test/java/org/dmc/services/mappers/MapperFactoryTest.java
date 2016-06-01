package org.dmc.services.mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.dmc.services.TestConfig;
import org.dmc.services.entities.BaseEntity;
import org.dmc.services.models.BaseModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@SuppressWarnings("unchecked")
public class MapperFactoryTest {

	private List<Mapper<? extends BaseEntity, ? extends BaseModel>> mappers = new ArrayList<Mapper<? extends BaseEntity, ? extends BaseModel>>();
	private MapperFactory mapperFactory;
	
	private Mapper<TestBaseEntity1, TestBaseModel1> mockMapper1;
	private Mapper<TestBaseEntity2, TestBaseModel2> mockMapper2;
	
	// List of mappers is converted into a table during a PostConstruct method
	// Creating a new factory for each test will allow for new table to be made each test
	// This must be done AFTER any mocked mappers have their supports methods stubbed
	private void initMapperFactory() {
		mapperFactory = new MapperFactory(mappers);
		mapperFactory.postConstruct();
	}
	
	private class TestBaseEntity1 extends BaseEntity{}
	private class TestBaseEntity2 extends BaseEntity{}
	
	private class TestBaseModel1 extends BaseModel{}
	private class TestBaseModel2 extends BaseModel{}
	
	@Before
	public void before() {
		mockMapper1 = Mockito.mock(Mapper.class);
		when(mockMapper1.supportsEntity()).thenReturn(TestBaseEntity1.class);
		when(mockMapper1.supportsModel()).thenReturn(TestBaseModel1.class);
		mappers.add(mockMapper1);
		
		mockMapper2 = Mockito.mock(Mapper.class);
		when(mockMapper2.supportsEntity()).thenReturn(TestBaseEntity2.class);
		when(mockMapper2.supportsModel()).thenReturn(TestBaseModel2.class);
		mappers.add(mockMapper2);
	}
	
	@After
	public void after() {
		mappers.clear();
	}
	
	@Test
	public void testMapperFor() {
		initMapperFactory();
		
		Mapper<TestBaseEntity1, TestBaseModel1> returnedMapper1 = mapperFactory.mapperFor(TestBaseEntity1.class, TestBaseModel1.class);
		assertEquals(mockMapper1, returnedMapper1);
		
		Mapper<TestBaseEntity2, TestBaseModel2> returnedMapper2 = mapperFactory.mapperFor(TestBaseEntity2.class, TestBaseModel2.class);
		assertEquals(mockMapper2, returnedMapper2);
	}
	
	@Test
	public void testMapperFor_noMapperFound() {
		initMapperFactory();
		
		Mapper<TestBaseEntity1, TestBaseModel2> returnedMapper = mapperFactory.mapperFor(TestBaseEntity1.class, TestBaseModel2.class);
		assertNotNull(returnedMapper);
		assertNotEquals(mockMapper1, returnedMapper);
		assertNotEquals(mockMapper2, returnedMapper);
	}

}
