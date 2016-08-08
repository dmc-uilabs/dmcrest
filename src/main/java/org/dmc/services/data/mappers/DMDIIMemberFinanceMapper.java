package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIMemberFinance;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIMemberFinanceModel;
import org.dmc.services.data.models.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberFinanceMapper extends AbstractMapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> {

	@Override
	public DMDIIMemberFinance mapToEntity(DMDIIMemberFinanceModel model) {
		if (model == null) return null;
		
		DMDIIMemberFinance entity = copyProperties(model, new DMDIIMemberFinance());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		entity.setDmdiiOwner(userMapper.mapToEntity(model.getDmdiiOwner()));
		
		return entity;
	}

	@Override
	public DMDIIMemberFinanceModel mapToModel(DMDIIMemberFinance entity) {
		if (entity == null) return null;
		
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
