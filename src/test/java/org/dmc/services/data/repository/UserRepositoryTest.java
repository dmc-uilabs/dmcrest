package org.dmc.services.data.repository;

import org.dmc.services.config.JpaTestConfig;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIMemberContact;
import org.dmc.services.data.entities.DMDIIMemberUser;
import org.dmc.services.data.entities.DMDIIRole;
import org.dmc.services.data.entities.DMDIITypeCategory;
import org.dmc.services.data.entities.Entities;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.DMDIIMemberRepository;
import org.dmc.services.data.repositories.DMDIIMemberUserRepository;
import org.dmc.services.data.repositories.DMDIIRoleRepository;
import org.dmc.services.data.repositories.DMDIITypeCategoryRepository;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.dmdiitype.DMDIIType;
import org.dmc.services.dmdiitype.DMDIITypeDao;
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
	private DMDIIMemberUserRepository dmdiiMemberUserRepository;

	@Inject
	private DMDIIRoleRepository dmdiiRoleRepository;

	@Inject
	private DMDIITypeCategoryRepository dmdiiTypeCatRepo;

	@Inject
	private DMDIITypeDao dmdiiTypeRepo;

	@Inject
	private OrganizationRepository orgRepo;

	@Inject
	private OrganizationUserRepository orgUserRepo;

	@Inject
	private UserRepository userRepo;

	private DMDIIMember member;

	private DMDIIMemberContact dmdiiMemberContact;

	private DMDIIRole dmdiiRole;

	private DMDIIMemberUser dmdiiMemberUser;

	private DMDIIType dmdiiType;

	private DMDIITypeCategory dmdiiTypeCategory;

	private Organization org;

	private OrganizationUser orgUser;

	private User user;

	@Before
	public void before() throws Exception {
		dmdiiTypeCategory = dmdiiTypeCatRepo.saveAndFlush(Entities.dmdiiTypeCategory());
		dmdiiType = dmdiiTypeRepo.saveAndFlush(Entities.dmdiiType(dmdiiTypeCategory));
		org = orgRepo.saveAndFlush(Entities.organization());

		user = Entities.user();
		user.setOrganizationUser(null);
		user = userRepo.saveAndFlush(user);

		orgUser = orgUserRepo.saveAndFlush(Entities.organizationUser(org, user));

		dmdiiRole = dmdiiRoleRepository.saveAndFlush(Entities.dmdiiRole());

		member = dmdiiMemberRepo.saveAndFlush(Entities.dmdiiMember(org, dmdiiType, dmdiiMemberUser));
		dmdiiMemberUser = dmdiiMemberUserRepository.saveAndFlush(Entities.dmdiiMemberUser(member, user, dmdiiRole));

		user.setOrganizationUser(orgUser);
		user = userRepo.saveAndFlush(user);
	}

	@Test
	public void findAllWhereDmdiiMemberExpiryDateIsAfterNow() {
		final List<User> users = userRepo.findAllWhereDmdiiMemberExpiryDateIsAfterNow();
		assertNotNull(users);
		assertEquals(1, users.size());
		assertTrue(users.contains(user));
	}

	@Test
	public void findByUsername_exists() {
		final User foundUser = userRepo.findByUsername(user.getUsername());
		assertEquals(user, foundUser);
	}
}
