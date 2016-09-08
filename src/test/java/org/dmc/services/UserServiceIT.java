package org.dmc.services;

import org.dmc.services.data.entities.Entities;
import org.dmc.services.data.entities.OnboardingStatus;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OnboardingStatusModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kskronek on 9/1/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserServiceTestConfig.class)
@Transactional
public class UserServiceIT {

	private static final Random RANDOM = new Random();
	public static final String NEW_DISPLAY_NAME = "NewDisplayName";

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private MapperFactory mapperFactory;

	private User user;

	private OnboardingStatus onboardingStatus;

	private UserModel model;

	@Before
	public void before() {
		user = Entities.user();
		user = userRepo.saveAndFlush(user);

		onboardingStatus = Entities.onboardingStatus(user.getId());
		user.setOnboarding(onboardingStatus);
		user = userRepo.saveAndFlush(user);

		final Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		model = mapper.mapToModel(user);
	}

	@Test
	public void testPatch() {
		assertNotNull(userService);

		model.setDisplayName(NEW_DISPLAY_NAME);

		OnboardingStatusModel status = model.getOnboarding();
		status.setAccount(!status.isAccount());
		status.setCompany(!status.isCompany());
		status.setProfile(!status.isProfile());
		status.setStorefront(!status.isStorefront());

		//patch(String userEPPN, UserModel patchUser)
		final UserModel patched = userService.patch(user.getUsername(), model);
		assertEquals(NEW_DISPLAY_NAME, patched.getDisplayName());
	}
}
