package org.dmc.services.dmdiimember;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.entities.DMDIIMember;
import org.dmc.services.mappers.Mapper;
import org.dmc.services.mappers.MapperFactory;
import org.dmc.services.models.DMDIIMemberModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DMDIIMemberService {

	@Inject
	private DMDIIMemberDao dmdiiMemberDao;
	
	@Inject
	private MapperFactory mapperFactory;

	public List<DMDIIMember> findPage(Integer pageNumber, Integer pageSize) {
		Page<DMDIIMember> page = dmdiiMemberDao.findAll(new PageRequest(pageNumber, pageSize));
		return page.getContent();
	}

	public DMDIIMemberModel findOne(Integer id) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findOne(id));
	}

	public List<DMDIIMember> findByTypeId(Integer typeId, Integer pageNumber, Integer pageSize) {
		Page<DMDIIMember> page = dmdiiMemberDao.findByDmdiiTypeId(new PageRequest(pageNumber, pageSize), typeId);
		return page.getContent();
	}

	public DMDIIMember save(DMDIIMember member) {
		//get all the areas of expertise
		return dmdiiMemberDao.save(member);
	}


}
