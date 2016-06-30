package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.UserModel;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public UserModel findOne(Integer id) {
		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);
		return mapper.mapToModel(userRepository.findOne(id));
	}

}
