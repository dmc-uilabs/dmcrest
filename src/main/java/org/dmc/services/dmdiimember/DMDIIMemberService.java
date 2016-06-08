package org.dmc.services.dmdiimember;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DMDIIMemberService {

	@Inject
	private DMDIIMemberDao dmdiiMemberDao;

	@Inject
	private MapperFactory mapperFactory;

	public List<DMDIIMemberModel> findPage(Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		List<DMDIIMember> members = dmdiiMemberDao.findAll(new PageRequest(pageNumber, pageSize)).getContent();
		return mapper.mapToModel(members);
	}

	public DMDIIMemberModel findOne(Integer id) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findOne(id));
	}

	public List<DMDIIMemberModel> findByTypeId(Integer typeId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		List<DMDIIMember> members = dmdiiMemberDao.findByDmdiiTypeId(new PageRequest(pageNumber, pageSize), typeId).getContent();
		return mapper.mapToModel(members);
	}

	public DMDIIMemberModel save(DMDIIMemberModel memberModel) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		return mapper.mapToModel(dmdiiMemberDao.save(mapper.mapToEntity(memberModel)));
	}


}
