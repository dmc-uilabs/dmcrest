package org.dmc.services.dmdiimember;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIMemberEvent;
import org.dmc.services.data.entities.DMDIIMemberNews;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.QDMDIIMember;
import org.dmc.services.data.entities.QDMDIIProject;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIMemberEventModel;
import org.dmc.services.data.models.DMDIIMemberMapEntryModel;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIMemberNewsModel;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.repositories.DMDIIMemberEventRepository;
import org.dmc.services.data.repositories.DMDIIMemberNewsRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.organization.OrganizationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.query.ListSubQuery;

@Service
public class DMDIIMemberService {

	@Inject
	private DMDIIMemberDao dmdiiMemberDao;

	@Inject
	private DMDIIMemberNewsRepository dmdiiMemberNewsRepository;

	@Inject
	private DMDIIMemberEventRepository dmdiiMemberEventRepository;

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private OrganizationService organizationService;

	public DMDIIMemberModel findOne(Integer id) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findOne(id));
	}

	public List<DMDIIMemberMapEntryModel> getMapEntries() {
		Mapper<DMDIIMember, DMDIIMemberMapEntryModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberMapEntryModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findAll());
	}

	public List<DMDIIMemberModel> findByName(String name, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findByOrganizationNameLikeIgnoreCase(new PageRequest(pageNumber, pageSize), "%"+name+"%").getContent());
	}

	public Long countByName(String name) {
		return dmdiiMemberDao.countByOrganizationNameLikeIgnoreCase("%"+name+"%");
	}

	public DMDIIMemberModel save(DMDIIMemberModel memberModel) throws DuplicateDMDIIMemberException {
		if (memberModel.getId() == null && dmdiiMemberDao.existsByOrganizationId(memberModel.getOrganization().getId())) {
			throw new DuplicateDMDIIMemberException("This organization is already a DMDII member");
		}
		
		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);

		DMDIIMember memberEntity = memberMapper.mapToEntity(memberModel);
		Organization organizationEntity = orgMapper.mapToEntity(organizationService.findOne(memberModel.getOrganization().getId()));
		memberEntity.setOrganization(organizationEntity);

		memberEntity = dmdiiMemberDao.save(memberEntity);

		return memberMapper.mapToModel(memberEntity);
	}

	public List<DMDIIMemberModel> filter(Map filterParams, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
		return mapper.mapToModel(dmdiiMemberDao.findAll(where, new PageRequest(pageNumber, pageSize)).getContent());
	}

	public Long count(Map filterParams) throws InvalidFilterParameterException {
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
		return dmdiiMemberDao.count(where);
	}

	private Collection<Predicate> getFilterExpressions(Map<String, String> filterParams) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<Predicate>();

		expressions.add(categoryIdFilter(filterParams.get("categoryId")));
		expressions.add(tierFilter(filterParams.get("tier")));
		expressions.add(hasActiveProjectsFilter(filterParams.get("hasActiveProjects")));
		expressions.addAll(tagFilter(filterParams.get("expertiseTags"), "expertiseTags"));
		expressions.addAll(tagFilter(filterParams.get("desiredExpertiseTags"), "desiredExpertiseTags"));

		return expressions;
	}

	private Collection<Predicate> tagFilter(String tagIds, String tagType) throws InvalidFilterParameterException {
		if(tagIds == null)
			return new ArrayList<Predicate>();

		Collection<Predicate> returnValue = new ArrayList<Predicate>();
		String[] tags = tagIds.split(",");
		Integer tagIdInt = null;

		for(String tag: tags) {
			try{
				tagIdInt = Integer.parseInt(tag);
			} catch(NumberFormatException e) {
				throw new InvalidFilterParameterException(tagType, Integer.class);
			}

			if(tagType.equals("expertiseTags")) {
				returnValue.add(QDMDIIMember.dMDIIMember.areasOfExpertise.any().id.eq(tagIdInt));
			} else if (tagType.equals("desiredExpertiseTags")) {
				returnValue.add(QDMDIIMember.dMDIIMember.desiredAreasOfExpertise.any().id.eq(tagIdInt));
			}
		}
		return returnValue;
	}

	private Predicate categoryIdFilter(String categoryId) throws InvalidFilterParameterException {
		if (categoryId == null) {
			return null;
		}

		Integer categoryIdInt = null;
		try {
			categoryIdInt = Integer.parseInt(categoryId);
		} catch (NumberFormatException e) {
			throw new InvalidFilterParameterException("categoryId", Integer.class);
		}

		return QDMDIIMember.dMDIIMember.dmdiiType().dmdiiTypeCategory().id.eq(categoryIdInt);
	}

	private Predicate tierFilter(String tier) throws InvalidFilterParameterException {
		if (tier == null) {
			return null;
		}

		Integer tierInt = null;
		try {
			tierInt = Integer.parseInt(tier);
		} catch (NumberFormatException e) {
			throw new InvalidFilterParameterException("tier", Integer.class);
		}

		return QDMDIIMember.dMDIIMember.dmdiiType().tier.eq(tierInt);
	}

	private Predicate hasActiveProjectsFilter(String hasActiveProjects) {
		if (hasActiveProjects == null) {
			return null;
		}

		Date today = new Date();
		QDMDIIProject qdmdiiProject = QDMDIIProject.dMDIIProject;
		ListSubQuery subQuery = new JPASubQuery().from(qdmdiiProject).where(qdmdiiProject.awardedDate.before(today), qdmdiiProject.endDate.after(today)).list(qdmdiiProject.id);
		if (Boolean.valueOf(hasActiveProjects)) {
			return QDMDIIMember.dMDIIMember.projects.any().in(subQuery);
		} else {
			return new BooleanBuilder().and(QDMDIIMember.dMDIIMember.projects.any().in(subQuery)).not().getValue();
		}
	}

	public List<DMDIIMemberNewsModel> getDmdiiMemberNews(Integer limit) {
		Mapper<DMDIIMemberNews, DMDIIMemberNewsModel> mapper = mapperFactory.mapperFor(DMDIIMemberNews.class, DMDIIMemberNewsModel.class);
		return mapper.mapToModel(dmdiiMemberNewsRepository.findAllByOrderByDateCreatedDesc(new PageRequest(0, limit)).getContent());
	}

	public List<DMDIIMemberEventModel> getDmdiiMemberEvents(Integer limit) {
		Mapper<DMDIIMemberEvent, DMDIIMemberEventModel> mapper = mapperFactory.mapperFor(DMDIIMemberEvent.class, DMDIIMemberEventModel.class);
		return mapper.mapToModel(dmdiiMemberEventRepository.findFutureEvents(new PageRequest(0, limit)).getContent());
	}
	
	public class DuplicateDMDIIMemberException extends Exception {
		public DuplicateDMDIIMemberException(String message) {
			super(message);
		}
	}

}
