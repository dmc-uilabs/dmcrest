package org.dmc.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.OnboardingStatus;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationAuthorizedIdp;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserContactInfo;
import org.dmc.services.data.entities.UserMemberPortalContactInfo;
import org.dmc.services.data.entities.UserPublicContactInfo;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.entities.UserSkill;
import org.dmc.services.data.entities.UserToken;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.models.SimpleUserModel;
import org.dmc.services.data.models.UserContactInfoModel;
import org.dmc.services.data.models.UserMemberPortalContactInfoModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserPublicContactInfoModel;
import org.dmc.services.data.models.UserTokenModel;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.OnboardingStatusRepository;
import org.dmc.services.data.repositories.OrganizationAuthorizedIdpRepository;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.dmc.services.data.repositories.ServerAccessRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.data.repositories.UserTokenRepository;
import org.dmc.services.email.EmailService;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.notification.NotificationService;
import org.dmc.services.roleassignment.UserRoleAssignmentService;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.security.UserPrincipalService;
import org.dmc.services.users.VerifyUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.dmc.services.predicates.Predicates.buildPredicate;
import static org.dmc.services.predicates.UserPredicates.likeDisplayName;
import static org.dmc.services.predicates.UserPredicates.likeFirstName;
import static org.dmc.services.predicates.UserPredicates.likeLastName;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private OnboardingStatusRepository onboardingStatusRepository;

	@Inject
	private OrganizationUserService orgUserService;

	@Inject
	private OrganizationUserRepository orgUserRepo;

	@Inject
	private OrganizationRepository organizationRepository;

	@Inject
	private UserRoleAssignmentService userRoleAssignmentService;

	@Inject
	private UserPrincipalService userPrincipalService;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserTokenRepository userTokenRepository;

	@Inject
	private NotificationService notificationService;

	@Inject
	private OrganizationAuthorizedIdpRepository idpRepository;

	@Inject
	private ServerAccessRepository accessRepo;

	@Inject
	private DocumentRepository documentRepository;

	@Inject
	private EmailService emailService;

	public UserModel findOne(Integer id) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findOne(id));
	}

	public UserModel findByUsername(String username) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findByUsername(username));
	}

	public Page<SimpleUserModel> findAll(PageRequest pageRequest, List<String> firstNameFilter, List<String> lastNameFilter, List<String> displayNameFilter) {
		Mapper<User, SimpleUserModel> mapper = mapperFactory.mapperFor(User.class, SimpleUserModel.class);
		Page<User> users = userRepository.findAll(
						buildPredicate(likeFirstName(firstNameFilter), likeLastName(lastNameFilter), likeDisplayName(displayNameFilter)), pageRequest);
		List<SimpleUserModel> simpleUsers = mapper.mapToModel(users.getContent());
		return new PageImpl<>(simpleUsers, pageRequest, users.getTotalElements());
	}

	public UserModel save(UserModel userModel, String userEPPN) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		User user = mapper.mapToEntity(userModel);
		user.setUsername(userEPPN);
		this.updateUserProfileLogo(user);
		return mapper.mapToModel(userRepository.save(user));
	}

	public List<UserModel> findByOrganizationId(Integer organizationId) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findByOrganizationUserOrganizationId(organizationId));
	}

	public List<UserModel> findByOrganizationIdAndRole(Integer organizaitonId, String role) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findByOrganizationIdAndRole(organizaitonId, role));
	}

	@Transactional
	public UserTokenModel createToken(Integer userId) {
		Mapper<UserToken, UserTokenModel> mapper = mapperFactory.mapperFor(UserToken.class, UserTokenModel.class);
		UserToken token = userTokenRepository.findByUserId(userId);
		User userEntity = userRepository.findOne(userId);

		java.util.Date todayDate = new java.util.Date();
		java.sql.Date todayTimestamp = new java.sql.Date(todayDate.getTime());
		String unhashedToken = userEntity.getFirstName() + userEntity.getLastName() + todayTimestamp.getTime();
		String hashedToken = DigestUtils.sha256Hex(unhashedToken);

		// If null, createUser a new token, else update existing
		if (token == null) {
			token = new UserToken();
			token.setUserId(userId);
			token.setDateIssued(todayTimestamp);
			token.setToken(hashedToken);
			token.setAttemptsMade(0);
			token = userTokenRepository.save(token);
		} else {
			token.setDateIssued(todayTimestamp);
			token.setToken(hashedToken);
			token = userTokenRepository.save(token);
		}

		return mapper.mapToModel(token);
	}

	public ResponseEntity emailToken(Integer userId, String token) {
		User user = this.userRepository.findOne(userId);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		return this.emailService.sendEmail(user, 1, params);
	}

	@Transactional
	public VerifyUserResponse verifyUser(Integer userId, String token) throws ArgumentNotFoundException {
		Mapper<UserToken, UserTokenModel> mapper = mapperFactory.mapperFor(UserToken.class, UserTokenModel.class);
		VerifyUserResponse response = new VerifyUserResponse();
		UserToken tokenEntity = userTokenRepository.findByUserId(userId);

		if (tokenEntity == null) {
			response.setResponseCode(1000);
			response.setResponseDescription("No tokens found for user.");
			return response;
		}

		if (tokenEntity.getAttemptsMade() >= 5) {
			response = tooManyAttempts(tokenEntity);
		} else {
			response = (tokenEntity.getToken().equals(token)) ?
					correctToken(userId, tokenEntity) :
					incorrectToken(tokenEntity);
		}

		return response;
	}

	@Transactional
	public VerifyUserResponse unverifyUser(Integer userId) {
		VerifyUserResponse response = new VerifyUserResponse();

		OrganizationUserModel orgUserModel = orgUserService.getOrganizationUserByUserId(userId);
		orgUserModel.setIsVerified(false);
		orgUserService.saveOrganizationUser(orgUserModel);

		userRoleAssignmentService.deleteByUserIdAndOrganizationId(userId, orgUserModel.getOrganizationId());

		response.setResponseCode(0);
		response.setResponseDescription("Successfully unverified user.");

		return response;
	}

	@Transactional
	public VerifyUserResponse declineUser(Integer userId, Integer organizationId) {
		VerifyUserResponse response;

		Integer rowsDeleted = orgUserRepo.deleteByUserIdAndOrganizationId(userId, organizationId);

		if (rowsDeleted > 0) {
			response = new VerifyUserResponse(0, "Successfully declined user.");
		} else {
			response = new VerifyUserResponse(1000,
					"User with ID " + userId + " could not be declined from organization with ID " + organizationId
							+ ".");
		}

		return response;
	}

	private VerifyUserResponse tooManyAttempts(UserToken tokenEntity) {
		userTokenRepository.delete(tokenEntity.getId());
		return new VerifyUserResponse(1000,
				"Too many unsuccessful attempts made to validate, please contact your administrator.");
	}

	private VerifyUserResponse correctToken(Integer userId, UserToken tokenEntity) throws ArgumentNotFoundException {
		userTokenRepository.delete(tokenEntity.getId());

		OrganizationUserModel orgUserModel = orgUserService.getOrganizationUserByUserId(userId);
		orgUserModel.setIsVerified(true);
		orgUserService.saveOrganizationUser(orgUserModel);

		// if this user is the only verified user of this organization, they're defaulted to company admin, else defaulted to member
		Integer numberOfUsersVerified = orgUserService.getNumberOfVerifiedUsers(orgUserModel.getOrganizationId());

		if (numberOfUsersVerified == 1) {
			userRoleAssignmentService
					.grantRoleToUserForOrg(SecurityRoles.ADMIN, userId, orgUserModel.getOrganizationId(), true);
		} else if (numberOfUsersVerified > 1) {
			userRoleAssignmentService
					.grantRoleToUserForOrg(SecurityRoles.MEMBER, userId, orgUserModel.getOrganizationId(), true);
		}

		return new VerifyUserResponse(0, "Successfully verified user.");
	}

	private VerifyUserResponse incorrectToken(UserToken tokenEntity) {
		tokenEntity.setAttemptsMade(tokenEntity.getAttemptsMade() + 1);
		userTokenRepository.save(tokenEntity);
		return new VerifyUserResponse(1000,
				"Tokens did not match, " + (5 - tokenEntity.getAttemptsMade()) + " attempts remaining.");
	}

	@Transactional
	public UserModel readOrCreateUser(String userEPPN, String userFirstName, String userSurname, String userFullname,
	                                  String userEmail) {
		final Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		User user = userRepository.findByUsername(userEPPN);
		UserModel userModel;

		if (user == null) {
			user = createUserAndOnboardingStatus(userEPPN, userFirstName, userSurname, userFullname, userEmail);
			userModel = mapper.mapToModel(user);
		} else {
			userModel = mapper.mapToModel(user);
			updateRolesAndDmdiiMembership(userModel, userEPPN);
		}

		return userModel;
	}

	private void updateRolesAndDmdiiMembership(UserModel userModel, String userEPPN) {
		UserPrincipal userPrincipal = (UserPrincipal) userPrincipalService.loadUserByUsername(userEPPN);
		userModel.setIsDMDIIMember(userPrincipal.hasAuthority(SecurityRoles.DMDII_MEMBER));
		return;
	}

	private User createUserAndOnboardingStatus(String userEPPN, String firstName, String lastName, String fullName,
	                                           String email) {
		final User user = createUser(userEPPN, firstName, lastName, fullName, email);
		final OnboardingStatus onboardingStatus = createOnboardingStatus(user.getId());
		user.setOnboarding(onboardingStatus);
		return user;
	}

	private User createUser(String userEPPN, String firstName, String lastName, String fullName, String email) {
		User user = new User();
		user.setUsername(userEPPN);
		user.setPassword("password");
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setRealname(fullName);
		user.setEmail(email);
		user.setAddDate(0L);
		user.setAccessList(Arrays.asList(accessRepo.findOneByName("global")));
		user.setUserContactInfo(new UserContactInfo());
		user = userRepository.save(user);

		String idpDomain = userEPPN.substring(userEPPN.indexOf('@') + 1);
		OrganizationAuthorizedIdp idp = idpRepository.findByIdpDomain(idpDomain);

		if (idp != null) {
			OrganizationUser orgUser = orgUserRepo.save(new OrganizationUser(user, idp.getOrganization(), true));
			user.setOrganizationUser(orgUser);
			UserRoleAssignment role = userRoleAssignmentService.setUserAsMemberForAuthorizedIdps(user, user.getOrganizationUser().getOrganization());
			user.setRoles(Arrays.asList(role));
		}

		return user;
	}

	private OnboardingStatus createOnboardingStatus(Integer userId) {
		OnboardingStatus status = new OnboardingStatus();
		status.setId(userId);
		status.setAccount(false);
		status.setCompany(false);
		status.setProfile(false);
		status.setStorefront(false);
		return onboardingStatusRepository.save(status);
	}

	private Boolean containsSkill(List<UserSkill> unmanagedSkills, UserSkill managedSkill) {
		for(UserSkill unmanagedSkill: unmanagedSkills) {
			if(managedSkill.getId().equals(unmanagedSkill.getId())) {
				return true;
			}
		}

		return false;
	}

	@Transactional
	public UserModel patch(String userEPPN, UserModel patchUser) {
		Assert.notNull(userEPPN, "Missing required parameter userEPPN");
		Assert.notNull(patchUser, "Missing required parameter patchUser");

		User currentUser = userRepository.findByUsername(userEPPN);
		Assert.notNull(currentUser, "Provided user name is invalid");

		Assert.isTrue(currentUser.getId().equals(patchUser.getId()),
				"User ID from username does not match user ID " + "from request body");

		final Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);

		User patchUserEntity = userMapper.mapToEntity(patchUser);

		currentUser.setRealname(patchUser.getDisplayName());
		currentUser.setTitle(patchUser.getTitle());
		currentUser.setAddress(patchUser.getAddress());
		currentUser.setOnboarding(patchUserEntity.getOnboarding());

		// remove skills that aren't in new list from managed list of skills
		Iterator<UserSkill> iterator = currentUser.getSkills().iterator();
		while(iterator.hasNext()) {
			UserSkill managedSkill = iterator.next();

			if( !containsSkill(patchUserEntity.getSkills(), managedSkill) ) {
				iterator.remove();
			}
		}

		// add new skills to the managed list
		for(UserSkill newSkill: patchUserEntity.getSkills()) {
			if(newSkill.getId() == null) {
				currentUser.getSkills().add(newSkill);
			}
		}

		this.patchUserContactInfo(currentUser, patchUser);

		currentUser.setTimezone(patchUser.getTimezone());
		currentUser.setAboutMe(patchUser.getAboutMe());

		// If a user is updating their primary user info, un-verify them from their current organization if they have one
		if (!currentUser.getFirstName().equals(patchUser.getFirstName()) ||
				!currentUser.getLastName().equals(patchUser.getLastName()) ||
				!currentUser.getEmail().equals(patchUser.getEmail())) {
			OrganizationUserModel orgUserModel = orgUserService.getOrganizationUserByUserId(currentUser.getId());

			if (orgUserModel != null) {
				orgUserModel.setIsVerified(false);
				orgUserService.saveOrganizationUser(orgUserModel);
				userRoleAssignmentService.deleteByUserIdAndOrganizationId(currentUser.getId(), orgUserModel.getOrganizationId());
			}

			currentUser.setFirstName(patchUser.getFirstName());
			currentUser.setLastName(patchUser.getLastName());
			currentUser.setEmail(patchUser.getEmail());
		}

		currentUser.setOrganizationUser(createOrganizationUser(currentUser, patchUser.getCompanyId()));

		this.updateUserProfileLogo(currentUser);

		return userMapper.mapToModel(userRepository.save(currentUser));
	}

	private void patchUserContactInfo(User currentUser, UserModel patchUser) {
		if (currentUser.getUserContactInfo() != null && patchUser.getUserContactInfo() != null) {

			UserContactInfo currentUserContactInfo = currentUser.getUserContactInfo();
			UserMemberPortalContactInfo currentUserMemberPortalContactInfo = currentUserContactInfo.getUserMemberPortalContactInfo();
			UserPublicContactInfo currentUserPublicContactInfo = currentUserContactInfo.getUserPublicContactInfo();

			UserContactInfoModel newUserContactInfo = patchUser.getUserContactInfo();
			UserMemberPortalContactInfoModel newUserMemberPortalContactInfo = newUserContactInfo.getUserMemberPortalContactInfo();
			UserPublicContactInfoModel newUserPublicContactInfo = newUserContactInfo.getUserPublicContactInfo();

			if (newUserMemberPortalContactInfo != null) {
				if (currentUserMemberPortalContactInfo == null) {
					currentUserMemberPortalContactInfo = new UserMemberPortalContactInfo();
				}

				BeanUtils.copyProperties(newUserMemberPortalContactInfo, currentUserMemberPortalContactInfo, new String[]{"id"});
				currentUser.getUserContactInfo().setUserMemberPortalContactInfo(currentUserMemberPortalContactInfo);

			}

			if (newUserPublicContactInfo != null) {
				if (currentUserPublicContactInfo == null) {
					currentUserPublicContactInfo = new UserPublicContactInfo();
				}

				BeanUtils.copyProperties(newUserPublicContactInfo, currentUserPublicContactInfo, new String[]{"id"});
				currentUser.getUserContactInfo().setUserPublicContactInfo(currentUserPublicContactInfo);
			}
		}
	}

	private OrganizationUser createOrganizationUser(User user, Integer organizationId) {
		if (organizationId == null) return null;

		Organization organization = this.organizationRepository.findOne(organizationId);
		OrganizationUser organizationUser = this.orgUserRepo.findByUserId(user.getId());

		if (organizationUser == null && organization != null) {
			notificationService.notifyOrgAdminsOfNewUser(organizationId, user);
			organizationUser = orgUserRepo.save(new OrganizationUser(user, organization, false));
		}

		return organizationUser;
	}

	private void updateUserProfileLogo(User user) {
		if (user.getId() != null) {
			Document document = this.documentRepository.
					findFirstByParentTypeAndDocClassAndOwnerOrderByModifiedDesc(DocumentParentType.USER, DocumentClass.IMAGE, user);
			if (document != null) {
				user.setImage(document.getDocumentUrl());
			}
		}
	}
}
