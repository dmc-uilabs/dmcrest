package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.SimpleUserModel;
import org.springframework.stereotype.Component;

@Component
public class SimpleUserMapper extends AbstractMapper<User, SimpleUserModel>  {

	@Override
	public User mapToEntity(SimpleUserModel model) {
		return copyProperties(model, new User());
	}

	@Override
	public SimpleUserModel mapToModel(User entity) {
		SimpleUserModel model = copyProperties(entity, new SimpleUserModel(),
				new String[]{"email", "companyName", "companyId"});

		if(entity.getUserContactInfo() != null && entity.getUserContactInfo().getUserPublicContactInfo() != null){
			model.setEmail(entity.getUserContactInfo().getUserPublicContactInfo().getEmail());
		}

		if(entity.getOrganizationUser() != null) {
			model.setCompanyId(entity.getOrganizationUser().getOrganization().getId());
			model.setCompanyName(entity.getOrganizationUser().getOrganization().getName());
		}

		return model;
	}

	@Override
	public Class<User> supportsEntity() {
		return User.class;
	}

	@Override
	public Class<SimpleUserModel> supportsModel() {
		return SimpleUserModel.class;
	}
}
