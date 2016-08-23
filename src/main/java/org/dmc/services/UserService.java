package org.dmc.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.dmc.services.data.entities.OnboardingStatus;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserToken;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserTokenModel;
import org.dmc.services.data.repositories.OnboardingStatusRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.data.repositories.UserTokenRepository;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.roleassignment.UserRoleAssignmentService;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.security.UserPrincipalService;
import org.dmc.services.users.VerifyUserResponse;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

	private static final String DEFAULT_PASSWORD = "password";

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private OnboardingStatusRepository onboardingStatusRepository;

	@Inject
	private OrganizationUserService orgUserService;

	@Inject
	private UserRoleAssignmentService userRoleAssignmentService;

	@Inject
	private UserPrincipalService userPrincipalService;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserTokenRepository userTokenRepository;

	public UserModel findOne(Integer id) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findOne(id));
	}

	public UserModel save(UserModel userModel) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.save(mapper.mapToEntity(userModel)));
	}

	public List<UserModel> findByOrganizationId(Integer organizationId) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findByOrganizationUserOrganizationId(organizationId));
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

		if(numberOfUsersVerified == 1) {
			userRoleAssignmentService.grantRoleToUserForOrg(SecurityRoles.ADMIN, userId, orgUserModel.getOrganizationId(), true);
		}
		else if (numberOfUsersVerified > 1) {
			userRoleAssignmentService.grantRoleToUserForOrg(SecurityRoles.MEMBER, userId, orgUserModel.getOrganizationId());
		}

		return new VerifyUserResponse(0, "Successfully verified user.");
	}

	private VerifyUserResponse incorrectToken(UserToken tokenEntity) {
		tokenEntity.setAttemptsMade(tokenEntity.getAttemptsMade() + 1);
		userTokenRepository.save(tokenEntity);
		return new VerifyUserResponse(1000,
				"Tokens did not match, " + (5 - tokenEntity.getAttemptsMade()) + " attempts remaining.");
	}

	public UserModel readOrCreateUser(String userEPPN, String userFirstName, String userSurname, String userFullname,
			String userEmail) {
		User user = userRepository.findByUsername(userEPPN);
		if (user == null) {
			user = createUser(userEPPN, userFirstName, userSurname, userFullname, userEmail);
			OnboardingStatus onboardingStatus = createOnboardingStatus(user.getId());
		}
		final Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		final UserModel userModel = mapper.mapToModel(user);
		return userModel;
	}

	private OnboardingStatus createOnboardingStatus(Integer id) {
		OnboardingStatus status = new OnboardingStatus();
		status.setId(id);
		status.setAccount(false);
		status.setCompany(false);
		status.setProfile(false);
		status.setStorefront(false);
		return onboardingStatusRepository.save(status);
	}

	private User createUser(String userEPPN, String firstName, String lastName, String fullName, String email) {
		User user = new User();
		user.setUsername(userEPPN);
		user.setPassword(DEFAULT_PASSWORD);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setRealname(fullName);
		user.setEmail(email);
		user.setAddDate(0);
		return userRepository.save(user);
	}

	public List<UserModel> findAllWhereDmdiiMemberExpiryDateIsAfterNow() {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findAllWhereDmdiiMemberExpiryDateIsAfterNow());
	}

	public UserModel readOrCreateUser(String userEPPN, String userFirstName, String userSurname, String userFullname,
			String userEmail) {
		User user = userRepository.findByUsername(userEPPN);

		if (user == null) {
			user = createUserAndOnboardingStatus(userEPPN, userFirstName, userSurname, userFullname, userEmail);
		}
		final Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);

		final UserModel userModel = mapper.mapToModel(user);
		updateRolesAndDmdiiMembership(userModel);

		return userModel;
	}

	private void updateRolesAndDmdiiMembership(UserModel userModel) {
		UserPrincipal userPrincipal = (UserPrincipal) userPrincipalService.loadUserByUsername(userModel.getUsername());
		userModel.setIsDMDIIMember(userPrincipal.hasAuthority(SecurityRoles.DMDII_MEMBER));
		userModel.setRoles(userPrincipal.getAllRoles());
		return;
	}

	private User createUserAndOnboardingStatus(String userEPPN, String firstName, String lastName, String fullName,
			String email) {
		final User user = createUser(userEPPN, firstName, lastName, fullName, email);
		createOnboardingStatus(user.getId());
		return user;
	}

	private User createUser(String userEPPN, String firstName, String lastName, String fullName, String email) {
		User user = new User();
		user.setUsername(userEPPN);
		user.setPassword(DEFAULT_PASSWORD);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setRealname(fullName);
		user.setEmail(email);
		user.setAddDate(0);
		return userRepository.save(user);
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
}
