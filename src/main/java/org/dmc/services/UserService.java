package org.dmc.services;

import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserToken;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
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
	private MapperFactory mapperFactory;

	public UserModel findOne(Integer id) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findOne(id));
	}

	public UserModel save(UserModel userModel) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.save(mapper.mapToEntity(userModel)));
	}

//	public UserTokenModel save(Integer userId) {
//		Mapper<UserToken, UserTokenModel> mapper = mapperFactory.mapperFor(UserToken.class, UserTokenModel.class);
//		UserToken token = new UserToken();
//		token.setUserId(userId);
//		token.set
//		token.setToken(DigestUtils.sha256Hex(token.getToken()));
//		return mapper.mapToModel(userTokenRepository.save(token));
//	}

	public VerifyUserResponse verifyUser(Integer userId, String token) {
		Mapper<UserToken, UserTokenModel> mapper = mapperFactory.mapperFor(UserToken.class, UserTokenModel.class);
		VerifyUserResponse response = new VerifyUserResponse();
		UserToken tokenEntity = userTokenRepository.findByUserId(userId);

		if(tokenEntity == null) {
			response.setResponseCode(1000);
			response.setResponseDescription("No tokens found for user.");
			return response;
		}

		if(tokenEntity.getAttemptsMade() <= 5) {
			userTokenRepository.delete(tokenEntity.getId());
			response.setResponseCode(1000);
			response.setResponseDescription("Too many unsuccessful attempts made to validate, please contact your administrator.");
		} else {

			String sha256Token = DigestUtils.sha256Hex(token);

			if(tokenEntity.getToken().equals(sha256Token)) {
				//TODO update organization_user.is_verified column to true
				response.setResponseCode(0);
				response.setResponseDescription("Successfully verified user.");
			} else {
				tokenEntity.setAttemptsMade(tokenEntity.getAttemptsMade() + 1);
				userTokenRepository.save(tokenEntity);
				response.setResponseCode(1000);
				response.setResponseDescription("Tokens did not match, " + tokenEntity.getAttemptsMade() + " attempts remaining.");
			}
		}

		return response;

	}


}
