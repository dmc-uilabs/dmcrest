package org.dmc.services.data.repository;

import org.dmc.services.config.JpaTestConfig;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIType;
import org.dmc.services.data.entities.DMDIITypeCategory;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.DMDIIMemberRepository;
import org.dmc.services.data.repositories.DMDIITypeCategoryRepository;
import org.dmc.services.data.repositories.DMDIITypeRepository;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by kskronek on 8/10/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaTestConfig.class)
@Transactional
public class UserRepositoryTest {

	@Inject
	private DMDIIMemberRepository dmdiiMemberRepo;

	@Inject
	DMDIITypeCategoryRepository dmdiiTypeCatRepo;

	@Inject
	DMDIITypeRepository dmdiiTypeRepo;

	@Inject
	private OrganizationRepository orgRepo;

	@Inject
	private OrganizationUserRepository orgUserRepo;

	@Inject
	private UserRepository userRepo;

	private DMDIIMember member;

	private DMDIIType dmdiiType;

	private DMDIITypeCategory dmdiiTypeCategory;

	private Organization org;

	private OrganizationUser orgUser;

	private User user;

	@Before
	public void before() {
		dmdiiTypeCategory = dmdiiTypeCatRepo.saveAndFlush(Entities.dmdiiTypeCategory());
		dmdiiType = dmdiiTypeRepo.saveAndFlush(Entities.dmdiiType(dmdiiTypeCategory));
		org = orgRepo.saveAndFlush(Entities.organization());
		user = userRepo.saveAndFlush(Entities.user());
		orgUser = orgUserRepo.saveAndFlush(Entities.organizationUser(org, user));
		member = dmdiiMemberRepo.saveAndFlush(Entities.member(org, dmdiiType));
	}

	@Test
	public void findAllWhereDmdiiMemberExpiryDateIsAfterNow() {
		final List<User> users = userRepo.findAllWhereDmdiiMemberExpiryDateIsAfterNow();
		assertNotNull(users);
		assertEquals(1, users.size());
		assertTrue(users.contains(user));
	}
}
