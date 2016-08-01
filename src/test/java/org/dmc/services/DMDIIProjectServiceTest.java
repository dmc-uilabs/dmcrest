package org.dmc.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dmc.services.data.Entities;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.mappers.DMDIIProjectMapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.repositories.DMDIIProjectRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.dmdiimember.DMDIIMemberDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DMDIIProjectServiceTest {

	@InjectMocks
	private final DMDIIProjectService testDMDIIProjectService = new DMDIIProjectService();
	
	@Mock
	private DMDIIProjectRepository testDMDIIProjectRepository;
	
	@Mock
	private DMDIIProjectMapper testDMDIIProjectMapper;
	
	@Mock
	private DMDIIMemberDao testDMDIIMemberDao;
	
	@Mock
	private UserRepository testUserRepository;
	
	@Mock
	private MapperFactory mapperFactory;
	
	private DMDIIProject testDMDIIProject;
	private DMDIIProject testDMDIIProject1;
	private DMDIIProject testDMDIIProject2;
	private List<DMDIIProject> testDMDIIProjects = new ArrayList();
	private DMDIIMember testDMDIIMember;
	private List<DMDIIMember> testDMDIIMembers = new ArrayList();
	private DMDIIProjectModel testDMDIIProjectModel;
	private List<DMDIIProjectModel> testDMDIIProjectModels;
	private Page<DMDIIProject> testDMDIIProjectsPage;
	
	@Before
	public void setup() throws Exception {
	
		this.testDMDIIProject = Entities.dmdiiProject();
		this.testDMDIIProject1 = Entities.dmdiiProject1();
		this.testDMDIIProject2 = Entities.dmdiiProject2();
		this.testDMDIIProjects = Arrays.asList(testDMDIIProject, testDMDIIProject1, testDMDIIProject2);
		this.testDMDIIProjectsPage = new PageImpl<DMDIIProject>(testDMDIIProjects);
	}
	
	@Test
	public void testFindDMDIIProjectsByPrimeOrganizationId() {
		when(this.testDMDIIProjectRepository.findByPrimeOrganizationId(any(Pageable.class), any(Integer.class)))
			.thenReturn(this.testDMDIIProjectsPage);
		List<DMDIIProject> list = any();
		when(this.testDMDIIProjectMapper.mapToModel(list))
			.thenReturn(this.testDMDIIProjectModels);
		when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
			.thenReturn(testDMDIIProjectMapper);
		when(this.testDMDIIProjectMapper.mapToModel(this.testDMDIIProjects))
			.thenReturn(this.testDMDIIProjectModels);
		
		List<DMDIIProjectModel> expected = this.testDMDIIProjectModels;
		List<DMDIIProjectModel> actual = this.testDMDIIProjectService.findDmdiiProjectsByPrimeOrganizationId(1000, 0, 1);
		
		assertTrue(actual.equals(expected));
	}
	
	@Test
	public void testFindOne() {
		when(this.testDMDIIProjectRepository.findOne(any(Integer.class)))
			.thenReturn(this.testDMDIIProject);
		when(this.testDMDIIProjectMapper.mapToModel(any(DMDIIProject.class)))
			.thenReturn(this.testDMDIIProjectModel);
		when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
			.thenReturn(testDMDIIProjectMapper);
		when(this.testDMDIIProjectMapper.mapToModel(this.testDMDIIProject))
			.thenReturn(this.testDMDIIProjectModel);
		
		DMDIIProjectModel expected = this.testDMDIIProjectModel;
		DMDIIProjectModel actual = this.testDMDIIProjectService.findOne(2000);
		
		assertTrue(actual.equals(expected));
		Mockito.verify(testDMDIIProjectRepository).findOne(any(Integer.class));
	}
}
