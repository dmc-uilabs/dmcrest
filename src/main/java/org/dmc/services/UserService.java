package org.dmc.services;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserToken;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserTokenModel;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.data.repositories.UserTokenRepository;
import org.dmc.services.users.VerifyUserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserTokenRepository userTokenRepository;

	@Inject
	private OrganizationUserService orgUserService;

	@Inject
	private UserRoleService userRoleService;

	@Inject
	private MapperFactory mapperFactory;

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	public UserModel findOne(Integer id) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findOne(id));
	}

	public UserModel save(UserModel userModel) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.save(mapper.mapToEntity(userModel)));
	}

	public UserTokenModel createToken(Integer userId) {
		Mapper<UserToken, UserTokenModel> mapper = mapperFactory.mapperFor(UserToken.class, UserTokenModel.class);
		UserToken token = userTokenRepository.findByUserId(userId);
		User userEntity = userRepository.findOne(userId);

		java.util.Date todayDate = new java.util.Date();
		java.sql.Date todayTimestamp = new java.sql.Date(todayDate.getTime());
		String unhashedToken = userEntity.getFirstName() + userEntity.getLastName() + todayTimestamp.getTime();
		String hashedToken = DigestUtils.sha256Hex(unhashedToken);

		// If null, create a new token, else update existing
		if(token == null) {
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

	public VerifyUserResponse verifyUser(Integer userId, String token) {
		Mapper<UserToken, UserTokenModel> mapper = mapperFactory.mapperFor(UserToken.class, UserTokenModel.class);
		VerifyUserResponse response = new VerifyUserResponse();
		UserToken tokenEntity = userTokenRepository.findByUserId(userId);

		if(tokenEntity == null) {
			response.setResponseCode(1000);
			response.setResponseDescription("No tokens found for user.");
			return response;
		}

		if(tokenEntity.getAttemptsMade() >= 5) {
			userTokenRepository.delete(tokenEntity.getId());
			response.setResponseCode(1000);
			response.setResponseDescription("Too many unsuccessful attempts made to validate, please contact your administrator.");
		} else {
			if(tokenEntity.getToken().equals(token)) {
				userTokenRepository.delete(tokenEntity.getId());

				OrganizationUserModel orgUserModel = orgUserService.getOrganizationUserByUserId(userId);
				orgUserModel.setIsVerified(true);
				orgUserService.saveOrganizationUser(orgUserModel);

				// TODO: make user a company admin if they're the first verified user
				if(orgUserService.getNumberOfVerifiedUsers(orgUserModel.getOrganizationId()) == 0) {

				}

				response.setResponseCode(0);
				response.setResponseDescription("Successfully verified user.");
			} else {
				tokenEntity.setAttemptsMade(tokenEntity.getAttemptsMade() + 1);
				userTokenRepository.save(tokenEntity);
				response.setResponseCode(1000);
				response.setResponseDescription("Tokens did not match, " + (5 - tokenEntity.getAttemptsMade()) + " attempts remaining.");
			}
		}

		return response;

	}


}
