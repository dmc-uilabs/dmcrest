package org.dmc.services.mappers;

import org.dmc.services.entities.DMDIIMemberFinance;
import org.dmc.services.entities.User;
import org.dmc.services.models.DMDIIMemberFinanceModel;
import org.dmc.services.models.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberFinanceMapper extends AbstractMapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> {

	@Override
	public DMDIIMemberFinance mapToEntity(DMDIIMemberFinanceModel model) {
		DMDIIMemberFinance entity = copyProperties(model, new DMDIIMemberFinance());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		entity.setDmdiiOwner(userMapper.mapToEntity(model.getDmdiiOwner()));
		
		return entity;
	}

	@Override
	public DMDIIMemberFinanceModel mapToModel(DMDIIMemberFinance entity) {
		DMDIIMemberFinanceModel model = copyProperties(entity, new DMDIIMemberFinanceModel());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		model.setDmdiiOwner(userMapper.mapToModel(entity.getDmdiiOwner()));
		
		return model;
	}

	@Override
	public Class<DMDIIMemberFinance> supportsEntity() {
		return DMDIIMemberFinance.class;
	}

	@Override
	public Class<DMDIIMemberFinanceModel> supportsModel() {
		return DMDIIMemberFinanceModel.class;
	}

}
