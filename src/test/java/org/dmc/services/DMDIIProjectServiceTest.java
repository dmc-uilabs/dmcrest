package org.dmc.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectEvent;
import org.dmc.services.data.entities.DMDIIProjectNews;
import org.dmc.services.data.entities.Entities;
import org.dmc.services.data.mappers.DMDIIMemberMapper;
import org.dmc.services.data.mappers.DMDIIProjectEventMapper;
import org.dmc.services.data.mappers.DMDIIProjectMapper;
import org.dmc.services.data.mappers.DMDIIProjectNewsMapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIProjectEventModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectNewsModel;
import org.dmc.services.data.models.Models;
import org.dmc.services.data.repositories.DMDIIProjectEventsRepository;
import org.dmc.services.data.repositories.DMDIIProjectNewsRepository;
import org.dmc.services.data.repositories.DMDIIProjectRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.dmdiimember.DMDIIMemberDao;
import org.dmc.services.dmdiimember.DMDIIMemberService;
import org.dmc.services.dmdiimember.DMDIIMemberService.DuplicateDMDIIMemberException;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mysema.query.types.Predicate;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DMDIIProjectServiceTest {

	@InjectMocks
	private final DMDIIProjectService testDMDIIProjectService = new DMDIIProjectService();
	
	@Mock
	private DMDIIProjectRepository testDMDIIProjectRepository;
	
	@Mock
	private DMDIIProjectMapper testDMDIIProjectMapper;
	
	@Mock
	private DMDIIMemberService testDMDIIMemberService;
	
	@Mock
	private DMDIIMemberMapper testDMDIIMemberMapper;
	
	@Mock
	private DMDIIMemberDao testDMDIIMemberDao;
	
	@Mock
	private UserRepository testUserRepository;
	
	@Mock
	private DMDIIProjectNewsRepository testDMDIIProjectNewsRepository;
	
	@Mock
	private DMDIIProjectEventsRepository testDMDIIProjectEventsRepository;
	
	@Mock
	private DMDIIProjectNewsMapper testDMDIIProjectNewsMapper;
	
	@Mock
	private DMDIIProjectEventMapper testDMDIIProjectEventMapper;
	
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
	private DMDIIMemberModel testDMDIIMemberModel;
	private List<DMDIIMemberModel> testDMDIIMemberModels;
	private Page<DMDIIProject> testDMDIIProjectsPage;
	private DMDIIProjectNews testDMDIIProjectNews;
	private DMDIIProjectNewsModel testDMDIIProjectNewsModel;
	private List<DMDIIProjectNews> testDMDIIProjectNewsList;
	private Page<DMDIIProjectNews> testDMDIIProjectNewsPage;
	private List<DMDIIProjectNewsModel> testDMDIIProjectNewsModels;
	private DMDIIProjectEvent testDMDIIProjectEvent;
	private DMDIIProjectEventModel testDMDIIProjectEventModel;
	private List<DMDIIProjectEvent> testDMDIIProjectEvents;
	private List<DMDIIProjectEventModel> testDMDIIProjectEventModels;
	private Page<DMDIIProjectEvent> testDMDIIProjectEventPage;
	
	
	@Before
	public void setup() throws Exception {
	
		MockitoAnnotations.initMocks(this);
		this.testDMDIIProject = Entities.dmdiiProject();
		this.testDMDIIProject1 = Entities.dmdiiProject1();
		this.testDMDIIProject2 = Entities.dmdiiProject2();
		this.testDMDIIProjects = Arrays.asList(testDMDIIProject, testDMDIIProject1, testDMDIIProject2);
		this.testDMDIIProjectsPage = new PageImpl<DMDIIProject>(testDMDIIProjects);
		this.testDMDIIProjectModel = Models.dmdiiProjectModel();
		this.testDMDIIProjectModels = Arrays.asList(testDMDIIProjectModel);
		this.testDMDIIMemberModel = Models.dmdiiMemberModel();
		this.testDMDIIMemberModels = Arrays.asList(testDMDIIMemberModel);
		this.testDMDIIProjectNewsModel  = Models.dmdiiProjectNews();
		this.testDMDIIProjectNewsModels = Arrays.asList(testDMDIIProjectNewsModel);
		this.testDMDIIProjectEventModel = Models.dmdiiProjectEvent();
		this.testDMDIIProjectEventModels = Arrays.asList(testDMDIIProjectEventModel);
		this.testDMDIIProjectNewsList = Arrays.asList(testDMDIIProjectNews);
		this.testDMDIIProjectNewsPage = new PageImpl<DMDIIProjectNews>(testDMDIIProjectNewsList);
		this.testDMDIIProjectEvents = Arrays.asList(testDMDIIProjectEvent);
		this.testDMDIIProjectEventPage = new PageImpl<DMDIIProjectEvent>(testDMDIIProjectEvents);
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
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindOneByNull() {
    	this.testDMDIIProjectService.findOne(null);
    }
	
	@Test
	public void testFindByTitle() {
		when(this.testDMDIIProjectRepository.findByProjectTitleLikeIgnoreCase(any(Pageable.class), any(String.class)))
			.thenReturn(this.testDMDIIProjectsPage);
		List<DMDIIProject> list = any();
		when(this.testDMDIIProjectMapper.mapToModel(list))
			.thenReturn(this.testDMDIIProjectModels);
		when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
		.thenReturn(testDMDIIProjectMapper);
		when(this.testDMDIIProjectMapper.mapToModel(this.testDMDIIProjects))
		.thenReturn(this.testDMDIIProjectModels);
		
		List<DMDIIProjectModel> expected = this.testDMDIIProjectModels;
		List<DMDIIProjectModel> actual = this.testDMDIIProjectService.findByTitle("", 0, 1);
		assertTrue(actual.equals(expected));
	}
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindByTitleByNull() {
    	this.testDMDIIProjectService.findByTitle(null, 0, 1);
    }
    
    @Test
    public void testCountDMDIIProjectsByTitle() {
		when(this.testDMDIIProjectRepository.countByProjectTitleLikeIgnoreCase(any(String.class)))
			.thenReturn((long) this.testDMDIIProjects.size());
		
		Long expected = (long) this.testDMDIIProjects.size();
		Long actual = this.testDMDIIProjectService.countByTitle("ProjectTitle");
		assertTrue(actual.equals(expected));
    }
	
	@Test
	public void testFindDMDIIProjectsByAwardedDate() {
		when(this.testDMDIIProjectRepository.findByAwardedDate(any(Pageable.class), any(Date.class)))
			.thenReturn(testDMDIIProjectsPage);
		List<DMDIIProject> list = any();
		when(this.testDMDIIProjectMapper.mapToModel(list))
			.thenReturn(this.testDMDIIProjectModels);
		when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
		.thenReturn(testDMDIIProjectMapper);
		when(this.testDMDIIProjectMapper.mapToModel(this.testDMDIIProjects))
		.thenReturn(this.testDMDIIProjectModels);
		
		List<DMDIIProjectModel> expected = this.testDMDIIProjectModels;
		List<DMDIIProjectModel> actual = this.testDMDIIProjectService.findDMDIIProjectsByAwardedDate(new Date(), 0, 1);
		assertTrue(actual.equals(expected));		
	}
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindDMDIIProjectsByAwardedDateByNull() {
    	this.testDMDIIProjectService.findDMDIIProjectsByAwardedDate(null, 0, 1);
    }
    
    @Test
    public void testCountDMDIIProjectsByAwardedDate() {
		when(this.testDMDIIProjectRepository.countByAwardedDate(any(Date.class)))
			.thenReturn((long) this.testDMDIIProjects.size());
		
		Long expected = (long) this.testDMDIIProjects.size();
		Long actual = this.testDMDIIProjectService.countDMDIIProjectsByAwardedDate(new Date());
		assertTrue(actual.equals(expected));
    }
	
	@Test
	public void testFindDMDIIProjectsByPrimeOrganizationIdAndIsActive() {
		when(this.testDMDIIProjectRepository.findByPrimeOrganizationIdAndIsActive(any(Pageable.class), any(Integer.class)))
			.thenReturn(testDMDIIProjectsPage);
		List<DMDIIProject> list = any();
		when(this.testDMDIIProjectMapper.mapToModel(list))
			.thenReturn(this.testDMDIIProjectModels);
		when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
		.thenReturn(testDMDIIProjectMapper);
		when(this.testDMDIIProjectMapper.mapToModel(this.testDMDIIProjects))
		.thenReturn(this.testDMDIIProjectModels);
		
		List<DMDIIProjectModel> expected = this.testDMDIIProjectModels;
		List<DMDIIProjectModel> actual = this.testDMDIIProjectService.findDMDIIProjectsByPrimeOrganizationIdAndIsActive(2000, 0, 1);
		assertTrue(actual.equals(expected));		
	}
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindDMDIIProjectsByPrimeOrganizationIdByNull() {
    	this.testDMDIIProjectService.findDmdiiProjectsByPrimeOrganizationId(null, 0, 1);
    }
    
    @Test
    public void testCountDMDIIProjectsByPrimeOrganizationId() {
		when(this.testDMDIIProjectRepository.countByPrimeOrganizationId(any(Integer.class)))
			.thenReturn((long) this.testDMDIIProjects.size());
		
		Long expected = (long) this.testDMDIIProjects.size();
		Long actual = this.testDMDIIProjectService.countDmdiiProjectsByPrimeOrganizationId(2000);
		assertTrue(actual.equals(expected));
    }
	
	@Test
	public void testFindDMDIIProjectsByPrimeOrganizationId() {
		when(this.testDMDIIProjectRepository.findByPrimeOrganizationId(any(Pageable.class), any(Integer.class)))
			.thenReturn(testDMDIIProjectsPage);
		List<DMDIIProject> list = any();
		when(this.testDMDIIProjectMapper.mapToModel(list))
			.thenReturn(this.testDMDIIProjectModels);
		when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
		.thenReturn(testDMDIIProjectMapper);
		when(this.testDMDIIProjectMapper.mapToModel(this.testDMDIIProjects))
		.thenReturn(this.testDMDIIProjectModels);
		
		List<DMDIIProjectModel> expected = this.testDMDIIProjectModels;
		List<DMDIIProjectModel> actual = this.testDMDIIProjectService.findDmdiiProjectsByPrimeOrganizationId(2000, 0, 1);
		assertTrue(actual.equals(expected));
	}
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindDMDIIProjectsByPrimeOrganizationIdAndIsActiveByNull() {
    	this.testDMDIIProjectService.findDMDIIProjectsByPrimeOrganizationIdAndIsActive(null, 0, 1);
    }
    
    @Test
    public void testCountDMDIIProjectsByPrimeOrganizationIdAndIsActive() {
		when(this.testDMDIIProjectRepository.countByPrimeOrganizationIdAndIsActive(any(Integer.class)))
			.thenReturn((long) this.testDMDIIProjects.size());
		
		Long expected = (long) this.testDMDIIProjects.size();
		Long actual = this.testDMDIIProjectService.countDMDIIProjectsByPrimeOrganizationIdAndIsActive(2000);
		assertTrue(actual.equals(expected));
    }
    
    @Test
    public void testSave() throws DuplicateDMDIIMemberException {
    	when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
    		.thenReturn(testDMDIIProjectMapper);
    	when(this.mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class))
    		.thenReturn(testDMDIIMemberMapper);
    	when(this.testDMDIIMemberMapper.mapToEntity(any(DMDIIMemberModel.class)))
    		.thenReturn(this.testDMDIIMember);
    	when(this.testDMDIIMemberMapper.mapToModel(any(DMDIIMember.class)))
    		.thenReturn(this.testDMDIIMemberModel);
    	when(this.testDMDIIProjectMapper.mapToModel(any(DMDIIProject.class)))
    		.thenReturn(this.testDMDIIProjectModel);
    	when(this.testDMDIIProjectMapper.mapToEntity(any(DMDIIProjectModel.class)))
    		.thenReturn(this.testDMDIIProject);
    	when(this.testDMDIIMemberService.save(any(DMDIIMemberModel.class)))
    		.thenReturn(this.testDMDIIMemberModel);
    	when(this.testDMDIIMemberDao.save(any(DMDIIMember.class)))
    	 	.thenReturn(this.testDMDIIMember);
    	
    	DMDIIProjectModel expected = this.testDMDIIProjectModel;
    	DMDIIProjectModel actual = this.testDMDIIProjectService.save(this.testDMDIIProjectModel);
    	assertTrue(actual.equals(expected));
    	Mockito.verify(testDMDIIProjectRepository).save(any(DMDIIProject.class));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSaveByNull() {
    	this.testDMDIIProjectService.save(null);
    }
    
    @Test
    public void testFilter() throws InvalidFilterParameterException {
    	when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
			.thenReturn(testDMDIIProjectMapper);
    	when(this.testDMDIIProjectMapper.mapToModel(any(DMDIIProject.class)))
			.thenReturn(this.testDMDIIProjectModel);
		when(this.testDMDIIProjectMapper.mapToEntity(any(DMDIIProjectModel.class)))
			.thenReturn(this.testDMDIIProject);
    	when(this.testDMDIIProjectRepository.findAll(any(Predicate.class), any(Pageable.class)))
    		.thenReturn(testDMDIIProjectsPage);
    	List<DMDIIProject> list = any();
    	when(this.testDMDIIProjectMapper.mapToModel(list))
    		.thenReturn(this.testDMDIIProjectModels);
    	
    	Map<String, String> filterParams = new HashMap<String, String>();
    	
    	filterParams.put("statusId", "1");
    	List<DMDIIProjectModel> expected = this.testDMDIIProjectModels;
    	List<DMDIIProjectModel> actual = this.testDMDIIProjectService.filter(filterParams, 0, 1);
    	assertTrue(actual.equals(expected));
    	
    	filterParams.remove("statusId");
    	
    	filterParams.put("focusId", "1");
    	expected = this.testDMDIIProjectModels;
    	actual = this.testDMDIIProjectService.filter(filterParams, 0, 1);
    	assertTrue(actual.equals(expected));
    	
    	filterParams.remove("focusId");
    	
    	filterParams.put("thrustId", "1");
    	expected = this.testDMDIIProjectModels;
    	actual = this.testDMDIIProjectService.filter(filterParams, 0, 1);
    	assertTrue(actual.equals(expected));
    	
    	filterParams.remove("thrustId");
    	
    	filterParams.put("rootNumber", "2016");
    	expected = this.testDMDIIProjectModels;
    	actual = this.testDMDIIProjectService.filter(filterParams, 0, 1);
    	assertTrue(actual.equals(expected));
    	
    	filterParams.remove("rootNumber");
    	
    	filterParams.put("callNumber", "07");
    	expected = this.testDMDIIProjectModels;
    	actual = this.testDMDIIProjectService.filter(filterParams, 0, 1);
    	assertTrue(actual.equals(expected));
    }
    
    @Test(expected = InvalidFilterParameterException.class)
    public void testFilterInvalidFilterParameter() throws InvalidFilterParameterException {
    	when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
			.thenReturn(testDMDIIProjectMapper);
    	when(this.testDMDIIProjectMapper.mapToModel(any(DMDIIProject.class)))
			.thenReturn(this.testDMDIIProjectModel);
		when(this.testDMDIIProjectMapper.mapToEntity(any(DMDIIProjectModel.class)))
			.thenReturn(this.testDMDIIProject);
    	when(this.testDMDIIProjectRepository.findAll(any(Predicate.class), any(Pageable.class)))
    		.thenReturn(testDMDIIProjectsPage);
    	List<DMDIIProject> list = any();
    	when(this.testDMDIIProjectMapper.mapToModel(list))
    		.thenReturn(this.testDMDIIProjectModels);
    	
    	Map<String, String> filterParams = new HashMap<String, String>();
    	
    	filterParams.put("statusId", "zoo");
    	List<DMDIIProjectModel> expected = this.testDMDIIProjectModels;
    	List<DMDIIProjectModel> actual = this.testDMDIIProjectService.filter(filterParams, 0, 1);
    	assertTrue(actual.equals(expected));
    	
    }
    
    @Test
    public void testCount() throws InvalidFilterParameterException {
		when(this.testDMDIIProjectRepository.count(any(Predicate.class)))
			.thenReturn(this.testDMDIIProjectsPage.getTotalElements());
    	when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
			.thenReturn(testDMDIIProjectMapper);
    	when(this.testDMDIIProjectMapper.mapToModel(any(DMDIIProject.class)))
			.thenReturn(this.testDMDIIProjectModel);
		when(this.testDMDIIProjectMapper.mapToEntity(any(DMDIIProjectModel.class)))
			.thenReturn(this.testDMDIIProject);
    	when(this.testDMDIIProjectRepository.findAll(any(Predicate.class), any(Pageable.class)))
    		.thenReturn(testDMDIIProjectsPage);
    	List<DMDIIProject> list = any();
    	when(this.testDMDIIProjectMapper.mapToModel(list))
    		.thenReturn(this.testDMDIIProjectModels);
    	
    	Map<String, String> filterParams = new HashMap<String, String>();
    	
    	filterParams.put("statusId", "2000");
    	Long expected = this.testDMDIIProjectsPage.getTotalElements();
    	Long actual = testDMDIIProjectService.count(filterParams);
    	assertTrue(actual.equals(expected));
    }
    
    @Test
    public void testFindContributingCompanyByProjectId() {
		when(this.testDMDIIMemberDao.findByDMDIIProjectContributingCompanyDMDIIProject(any(Integer.class)))
			.thenReturn(testDMDIIMembers);
		List<DMDIIMember> list = any();
		when(this.testDMDIIMemberMapper.mapToModel(list))
			.thenReturn(this.testDMDIIMemberModels);
		when(this.mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class))
			.thenReturn(testDMDIIMemberMapper);
		when(this.testDMDIIMemberMapper.mapToModel(this.testDMDIIMembers))
			.thenReturn(this.testDMDIIMemberModels);
		
		List<DMDIIMemberModel> expected = this.testDMDIIMemberModels;
		List<DMDIIMemberModel> actual = this.testDMDIIProjectService.findContributingCompanyByProjectId(2000);
		assertTrue(actual.equals(expected));
    	
    }
    
    @Test
    public void testGetDmdiiProjectNews() {
		when(this.testDMDIIProjectNewsRepository.findAllByOrderByDateCreatedDesc(any(Pageable.class)))
			.thenReturn(testDMDIIProjectNewsPage);
		List<DMDIIProjectNews> list = any();
		when(this.testDMDIIProjectNewsMapper.mapToModel(list))
			.thenReturn(this.testDMDIIProjectNewsModels);
		when(this.mapperFactory.mapperFor(DMDIIProjectNews.class, DMDIIProjectNewsModel.class))
			.thenReturn(testDMDIIProjectNewsMapper);
		when(this.testDMDIIProjectNewsMapper.mapToModel(this.testDMDIIProjectNewsList))
			.thenReturn(this.testDMDIIProjectNewsModels);
	
	List<DMDIIProjectNewsModel> expected = this.testDMDIIProjectNewsModels;
	List<DMDIIProjectNewsModel> actual = this.testDMDIIProjectService.getDmdiiProjectNews(5);
	assertTrue(actual.equals(expected));
    	
    }
    
    @Test
    public void testGetDmdiiProjectEvents() {
		when(this.testDMDIIProjectEventsRepository.findAllByOrderByEventDateDesc(any(Pageable.class)))
		.thenReturn(testDMDIIProjectEventPage);
	List<DMDIIProjectEvent> list = any();
	when(this.testDMDIIProjectEventMapper.mapToModel(list))
		.thenReturn(this.testDMDIIProjectEventModels);
	when(this.mapperFactory.mapperFor(DMDIIProjectEvent.class, DMDIIProjectEventModel.class))
		.thenReturn(testDMDIIProjectEventMapper);
	when(this.testDMDIIProjectEventMapper.mapToModel(this.testDMDIIProjectEvents))
		.thenReturn(this.testDMDIIProjectEventModels);

	List<DMDIIProjectEventModel> expected = this.testDMDIIProjectEventModels;
	List<DMDIIProjectEventModel> actual = this.testDMDIIProjectService.getDmdiiProjectEvents(5);
	assertTrue(actual.equals(expected));
    	
    }
    
    @Test
    public void testFindDMDIIProjectsByContributingCompany() {
		when(this.testDMDIIProjectRepository.findByContributingCompanyId(any(Integer.class)))
			.thenReturn(testDMDIIProjects);
		List<DMDIIProject> list = any();
		when(this.testDMDIIProjectMapper.mapToModel(list))
			.thenReturn(this.testDMDIIProjectModels);
		when(this.mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class))
			.thenReturn(testDMDIIProjectMapper);
		when(this.testDMDIIProjectMapper.mapToModel(this.testDMDIIProjects))
			.thenReturn(this.testDMDIIProjectModels);
	
	List<DMDIIProjectModel> expected = this.testDMDIIProjectModels;
	List<DMDIIProjectModel> actual = this.testDMDIIProjectService.findDMDIIProjectsByContributingCompany(2000);
	assertTrue(actual.equals(expected));
    	
    }
}
