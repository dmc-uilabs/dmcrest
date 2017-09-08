package org.dmc.services.dmdiimember;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;
import org.apache.commons.lang3.BooleanUtils;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIMemberEvent;
import org.dmc.services.data.entities.DMDIIMemberNews;
import org.dmc.services.data.entities.QDMDIIMember;
import org.dmc.services.data.entities.QDMDIIProject;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.AreaOfExpertiseModel;
import org.dmc.services.data.models.DMDIIMemberAutocompleteModel;
import org.dmc.services.data.models.DMDIIMemberEventModel;
import org.dmc.services.data.models.DMDIIMemberMapEntryModel;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIMemberNewsModel;
import org.dmc.services.data.repositories.DMDIIMemberEventRepository;
import org.dmc.services.data.repositories.DMDIIMemberNewsRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.organization.OrganizationService;
import org.dmc.services.recentupdates.RecentUpdateController;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.dmc.services.ServiceLogger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	public List<DMDIIMemberModel> findByNameOrTags(String name, Integer pageNumber, Integer pageSize, String[] tiers, String[] types, String[] activeProjects) throws InvalidFilterParameterException {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);

		Predicate where = ExpressionUtils.allOf(getFilterExpressions(tiers, types, activeProjects, name));
		ServiceLogger.log("where", where.toString());
		return mapper.mapToModel(dmdiiMemberDao.findAll(where, new PageRequest(pageNumber, pageSize)).getContent());


		//return mapper.mapToModel(dmdiiMemberDao.findDistinctByOrganizationNameLikeIgnoreCaseOrOrganizationAreasOfExpertiseNameContainsIgnoreCaseOrOrganizationDesiredAreasOfExpertiseNameContainsIgnoreCase(new PageRequest(pageNumber, pageSize), "%"+name+"%", name, name).getContent());
	}

	public Long countByNameOrTags(String name) {
		return dmdiiMemberDao.countDistinctByOrganizationNameLikeIgnoreCaseOrOrganizationAreasOfExpertiseNameContainsIgnoreCaseOrOrganizationDesiredAreasOfExpertiseNameContainsIgnoreCase("%"+name+"%", name, name);
	}

	@Transactional
	public DMDIIMemberModel save(DMDIIMemberModel memberModel) throws DuplicateDMDIIMemberException {
		if (memberModel.getId() == null && dmdiiMemberDao.existsByOrganizationId(memberModel.getOrganization().getId())) {
			throw new DuplicateDMDIIMemberException("This organization is already a DMDII member");
		}

		// Remove non-DMDII tags from getting saved through DMDIIMember
		List<AreaOfExpertiseModel> aTags = memberModel.getOrganization().getAreasOfExpertise();
		List<AreaOfExpertiseModel> dTags = memberModel.getOrganization().getDesiredAreasOfExpertise();

		for(Iterator<AreaOfExpertiseModel> iterator = aTags.iterator(); iterator.hasNext(); ) {
			AreaOfExpertiseModel temp = iterator.next();

			if(temp.getId() == null || !temp.getIsDmdii()) {
				iterator.remove();
			}
		}

		for(Iterator<AreaOfExpertiseModel> iterator = dTags.iterator(); iterator.hasNext(); ) {
			AreaOfExpertiseModel temp = iterator.next();

			if(temp.getId() == null || !temp.getIsDmdii()) {
				iterator.remove();
			}
		}

		memberModel.getOrganization().setAreasOfExpertise(aTags);
		memberModel.getOrganization().setDesiredAreasOfExpertise(dTags);


		// Save organization separately
		memberModel.setOrganization(organizationService.save(memberModel.getOrganization()));

		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		DMDIIMember memberEntity = memberMapper.mapToEntity(memberModel);

		// Projects on DMDII member entity are not mapped to/from model
		// So if this is an existing DMDII member being updated, we need to get any existing projects and add them to the member for data integrity
		if (memberEntity.getId() != null) {
			DMDIIMember originalEntity = dmdiiMemberDao.findOne(memberEntity.getId());
			memberEntity.setProjects(originalEntity.getProjects());
		}

		RecentUpdateController recentUpdateController = new RecentUpdateController();
		recentUpdateController.addRecentUpdate(memberEntity);

		return memberMapper.mapToModel(dmdiiMemberDao.save(memberEntity));
	}

	public List<DMDIIMemberModel> filter(Integer pageNumber, Integer pageSize, String[] tiers, String[] types, String[] activeProjects) throws InvalidFilterParameterException {
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(tiers, types, activeProjects, null));
		ServiceLogger.log("where", where.toString());
		return mapper.mapToModel(dmdiiMemberDao.findAll(where, new PageRequest(pageNumber, pageSize)).getContent());
	}

	// public Long count(Map filterParams) throws InvalidFilterParameterException {
	// 	Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
	// 	return dmdiiMemberDao.count(where);
	// }

	public List<DMDIIMemberAutocompleteModel> getAllMembers() {
		Mapper<DMDIIMember, DMDIIMemberAutocompleteModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberAutocompleteModel.class);
		return mapper.mapToModel(dmdiiMemberDao.findAll());
	}

	private Collection<Predicate> getFilterExpressions(String[] tiers, String[] types, String[] activeProjects, String searchTerm) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<Predicate>();

		expressions.add(tierFilter(tiers));
		expressions.add(categoryIdFilter(types));
		expressions.add(hasActiveProjectsFilter(activeProjects));
		expressions.add(searchFilter(searchTerm));

		ServiceLogger.log("expressions", expressions.toString());

		//expressions.addAll(tagFilter(filterParams.get("expertiseTags"), "expertiseTags"));
		//expressions.addAll(tagFilter(filterParams.get("desiredExpertiseTags"), "desiredExpertiseTags"));

		return expressions;
	}

	private Predicate searchFilter(String searchTerm){
		if (searchTerm.equals("")) {
			return null;
		}

		// Generate list of predicates for each field
		Collection<Predicate> expressions = new ArrayList<Predicate>();
		expressions.add(QDMDIIMember.dMDIIMember.organization().name.like(searchTerm));
		expressions.add(QDMDIIMember.dMDIIMember.organization().description.containsIgnoreCase(searchTerm));
		expressions.add(QDMDIIMember.dMDIIMember.organization().industry.containsIgnoreCase(searchTerm));

		// return any of for these predicates
		return ExpressionUtils.anyOf(expressions);
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
				returnValue.add(QDMDIIMember.dMDIIMember.organization().areasOfExpertise.any().id.eq(tagIdInt));
			} else if (tagType.equals("desiredExpertiseTags")) {
				returnValue.add(QDMDIIMember.dMDIIMember.organization().desiredAreasOfExpertise.any().id.eq(tagIdInt));
			}
		}
		return returnValue;
	}

	private Predicate categoryIdFilter(String[] categoryIds) throws InvalidFilterParameterException {
		if (categoryIds == null) {
			return null;
		}

		List<Integer> categoryIdInts = new ArrayList<Integer>();

		for(String categoryId: categoryIds){
			Integer categoryIdInt = null;
			try {
				categoryIdInt = Integer.parseInt(categoryId);
				categoryIdInts.add(categoryIdInt);
			} catch (NumberFormatException e) {
				throw new InvalidFilterParameterException("categoryId", Integer.class);
			}
		}

		return QDMDIIMember.dMDIIMember.dmdiiType().dmdiiTypeCategory().id.in(categoryIdInts);
	}

	private Predicate tierFilter(String[] tiers) throws InvalidFilterParameterException {
		if (tiers == null) {
			return null;
		}

		List<Integer> tierInts = new ArrayList<Integer>();

		for(String tier: tiers){
			Integer tierInt = null;
			try {
				tierInt = Integer.parseInt(tier);
				tierInts.add(tierInt);
			} catch (NumberFormatException e) {
				throw new InvalidFilterParameterException("tier", Integer.class);
			}
		}

		return QDMDIIMember.dMDIIMember.dmdiiType().tier.in(tierInts);
	}

	private Predicate hasActiveProjectsFilter(String[] hasActiveProjectsList) {
		if (hasActiveProjectsList == null) {
			return null;
		}
		if(hasActiveProjectsList.length != 1){
			return null;
		}

		String hasActiveProjects = hasActiveProjectsList[0];

		Date today = new Date();
		QDMDIIProject qdmdiiProject = QDMDIIProject.dMDIIProject;
		ListSubQuery subQuery = new JPASubQuery().from(qdmdiiProject).where(qdmdiiProject.awardedDate.before(today), qdmdiiProject.endDate.after(today)).list(qdmdiiProject.id);
		if (BooleanUtils.toBoolean(hasActiveProjects)) {
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
