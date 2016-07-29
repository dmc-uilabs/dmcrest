package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.entities.Role;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.RoleModel;
import org.dmc.services.data.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	
	@Inject
	private RoleRepository roleRepository;
	
	@Inject
	private MapperFactory mapperFactory;

	public RoleModel findOne(Integer id) {
		Mapper<Role, RoleModel> mapper = mapperFactory.mapperFor(Role.class, RoleModel.class);
		return mapper.mapToModel(roleRepository.findOne(id));
	}
	
	public RoleModel save (RoleModel roleModel) {
		Mapper<Role, RoleModel> mapper = mapperFactory.mapperFor(Role.class, RoleModel.class);
		return mapper.mapToModel(roleRepository.save(mapper.mapToEntity(roleModel)));
	}
}
