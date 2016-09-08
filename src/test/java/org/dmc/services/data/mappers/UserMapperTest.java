package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.Entities;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.Models;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "DBport = 5432", "DBip = 54.172.82.133", "DBuser = gforge", "DBpass = gforge" })
@ContextConfiguration(classes = MapperTestConfig.class)
public class UserMapperTest {

	@Mock
	private OrganizationRepository organizationRepository;

	@Mock
	private OrganizationUserRepository organizationUserRepository;

	@Autowired
	UserMapper mapper;

	User entity;

	UserModel model;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		entity = Entities.user();
		model = Models.user();
	}

	@Test
	public void testEntityToModel() {
		final UserModel model = mapper.mapToModel(entity);
		assertEquals(model.getDisplayName(), entity.getRealname());
		assertEquals(model.getUsername(), entity.getUsername());
		assertEquals(model.getRealname(), entity.getRealname());
		assertEquals(model.getTitle(), entity.getTitle());
		assertEquals(model.getTermsConditions(), entity.getTermsAndCondition() != null);
		assertEquals(model.getFirstName(), entity.getFirstName());
		assertEquals(model.getLastName(), entity.getLastName());
		assertEquals(model.getEmail(), entity.getEmail());
		assertEquals(model.getAddress(), entity.getAddress());
		assertEquals(model.getPhone(), entity.getPhone());
		assertEquals(model.getImage(), entity.getImage());
		assertEquals(model.getOnboarding(), entity.getOnboarding());
		assertEquals(model.getAboutMe(), entity.getAboutMe());
		assertEquals(model.getResume(), entity.getResume());
	}

	@Test
	public void testModelToEntity() {
		final User entity = mapper.mapToEntity(model);
		assertEquals(entity.getRealname(), model.getDisplayName());
		assertEquals(entity.getUsername(), model.getUsername());
		assertEquals(entity.getTitle(), model.getTitle());
		assertEquals(entity.getFirstName(), model.getFirstName());
		assertEquals(entity.getLastName(), model.getLastName());
		assertEquals(entity.getEmail(), model.getEmail());
		assertEquals(entity.getAddress(), model.getAddress());
		assertEquals(entity.getPhone(), model.getPhone());
		assertEquals(entity.getImage(), model.getImage());
		assertEquals(entity.getOnboarding(), model.getOnboarding());
		assertTrue(entity.getUserContactInfo() != null && model.getUserContactInfo() != null);
		assertEquals(entity.getAboutMe(), model.getAboutMe());
		assertEquals(entity.getResume(), model.getResume());
	}
}
