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

	@Inject
	private OrganizationService organizationService;

	public List<DMDIIMemberModel> findPage(Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		List<DMDIIMember> members = dmdiiMemberDao.findAll(new PageRequest(pageNumber, pageSize)).getContent();
		return mapper.mapToModel(members);
	}

	public DMDIIMemberModel findOne(Integer id) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		DMDIIMember test = dmdiiMemberDao.findOne(id);
		return mapper.mapToModel(test);
	}
	
	public List<DMDIIMemberModel> findByType(Integer categoryId, Integer tier, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		List<DMDIIMember> members;
		PageRequest page = new PageRequest(pageNumber, pageSize);
		
		if (categoryId != null && tier != null) {
			members = dmdiiMemberDao.findByDmdiiTypeDmdiiTypeCategoryIdAndDmdiiTypeTier(page, categoryId, tier).getContent();
		} else if (categoryId != null) {
			members = dmdiiMemberDao.findByDmdiiTypeDmdiiTypeCategoryId(page, categoryId).getContent();
		} else {
			members = dmdiiMemberDao.findByDmdiiTypeTier(page, tier).getContent();
		}
		
		return mapper.mapToModel(members);
	}
	
	public List<DMDIIMemberModel> findByName(String name, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findByOrganizationNameLikeIgnoreCase(new PageRequest(pageNumber, pageSize), "%"+name+"%").getContent());
	}

	public DMDIIMemberModel save(DMDIIMemberModel member) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);

		organizationService.save(member.getOrganization());
		DMDIIMember memberEntity = mapper.mapToEntity(member);
		memberEntity = dmdiiMemberDao.save(memberEntity);

		return mapper.mapToModel(memberEntity);
	}

	public List<DMDIIMemberModel> findByCategoryId(Integer categoryId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findByDmdiiTypeDmdiiTypeCategoryId(new PageRequest(pageNumber, pageSize), categoryId).getContent());
	}

	public List<DMDIIMemberModel> findByTier(Integer tier, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findByDmdiiTypeTier(new PageRequest(pageNumber, pageSize), tier).getContent());
	}

}
