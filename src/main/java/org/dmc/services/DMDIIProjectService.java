package org.dmc.services;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.StringExpression;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectEvent;
import org.dmc.services.data.entities.DMDIIProjectNews;
import org.dmc.services.data.entities.QDMDIIProject;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIProjectEventModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectNewsModel;
import org.dmc.services.data.repositories.DMDIIProjectEventsRepository;
import org.dmc.services.data.repositories.DMDIIProjectNewsRepository;
import org.dmc.services.data.repositories.DMDIIProjectRepository;
import org.dmc.services.dmdiimember.DMDIIMemberDao;
import org.dmc.services.dmdiimember.DMDIIMemberService;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.recentupdates.RecentUpdateController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class DMDIIProjectService {

	@Inject
	private DMDIIProjectRepository dmdiiProjectRepository;

	@Inject
	private DMDIIMemberDao dmdiiMemberDao;

	@Inject
	private DMDIIProjectNewsRepository dmdiiProjectNewsRepository;

	@Inject
	private DMDIIProjectEventsRepository dmdiiProjectEventsRepository;

	@Inject
	private DMDIIMemberService dmdiiMemberService;

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private DMDIIDocumentService dmdiiDocumentService;

	public List<DMDIIProjectModel> filter(String[] focusIds, String[] statuses, String[] thrustIds, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(focusIds, statuses, thrustIds, null));
		PageRequest pageRequest = new PageRequest(pageNumber, pageSize,
				new Sort(
						new Order(Direction.ASC, "rootNumber"),
						new Order(Direction.ASC, "callNumber"),
						new Order(Direction.ASC, "projectNumber")
				)
		);

		if(where != null){
			ServiceLogger.log("where", where.toString());
		}

		return mapper.mapToModel(dmdiiProjectRepository.findAll(where, pageRequest).getContent());
	}

	public List<DMDIIProjectModel> filter(String isEvent, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		Collection<Predicate> expressions = new ArrayList<Predicate>();
		expressions.add(isEventFilter(isEvent));
		Predicate where = ExpressionUtils.allOf(expressions);
		PageRequest pageRequest = new PageRequest(pageNumber, pageSize,
				new Sort(
						new Order(Direction.ASC, "rootNumber"),
						new Order(Direction.ASC, "callNumber"),
						new Order(Direction.ASC, "projectNumber")
				)
		);
		return mapper.mapToModel(dmdiiProjectRepository.findAll(where, pageRequest).getContent());
	}

	public Long count(String[] focusIds, String[] statuses, String[] thrustIds, String searchTerm) throws InvalidFilterParameterException {
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(focusIds, statuses, thrustIds, searchTerm));
		return dmdiiProjectRepository.count(where);
	}

	public Long count(String isEvent) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<Predicate>();
		expressions.add(isEventFilter(isEvent));
		Predicate where = ExpressionUtils.allOf(expressions);
		return dmdiiProjectRepository.count(where);
	}

	private Collection<Predicate> getFilterExpressions(String[] focusIds, String[] statuses, String[] thrustIds, String searchTerm) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<Predicate>();

		expressions.add(statusIdFilter(statuses));
		expressions.add(focusIdFilter(focusIds));
		expressions.add(thrustIdFilter(thrustIds));
		expressions.add(searchFilter(searchTerm));
		expressions.add(isEventFilter("false"));

		// expressions.add(rootNumberFilter(filterParams.get("rootNumber")));
		// expressions.add(callNumberFilter(filterParams.get("callNumber")));

		return expressions;
	}

	private Predicate searchFilter(String searchTerm){
		if (searchTerm == null){
			return null;
		}
		if (searchTerm.equals("")) {
			return null;
		}

		// Generate list of predicates for each field
		Collection<Predicate> expressions = new ArrayList<Predicate>();
		expressions.add(QDMDIIProject.dMDIIProject.projectTitle.containsIgnoreCase(searchTerm));
		expressions.add(QDMDIIProject.dMDIIProject.projectSummary.containsIgnoreCase(searchTerm));
		expressions.add(QDMDIIProject.dMDIIProject.projectNumberString.containsIgnoreCase(parseProjectNumberSearchString(searchTerm)));
		//expressions.add(QDMDIIProject.dMDIIProject.rootNumber.toString().containsIgnoreCase(parseProjectNumberSearchString(searchTerm)));

		// return any of for these predicates
		return ExpressionUtils.anyOf(expressions);
	}

	private Predicate statusIdFilter(String[] statusIds) throws InvalidFilterParameterException {
		if (statusIds == null) {
			return null;
		}

		List<Integer> statusInts = new ArrayList<Integer>();

		for(String statusId: statusIds){
			Integer statusInt = null;
			try {
				statusInt = Integer.parseInt(statusId);
				statusInts.add(statusInt);
			} catch (NumberFormatException e) {
				throw new InvalidFilterParameterException("statusId", Integer.class);
			}
		}

		return QDMDIIProject.dMDIIProject.projectStatus().id.in(statusInts);
	}

	private Predicate focusIdFilter(String[] focusIds) throws InvalidFilterParameterException {
		if (focusIds == null) {
			return null;
		}

		List<Integer> focusInts = new ArrayList<Integer>();

		for(String focusId: focusIds){
			Integer focusInt = null;
			try {
				focusInt = Integer.parseInt(focusId);
				focusInts.add(focusInt);
			} catch (NumberFormatException e) {
				throw new InvalidFilterParameterException("focusId", Integer.class);
			}
		}

		return QDMDIIProject.dMDIIProject.projectFocusArea().id.in(focusInts);
	}

	private Predicate thrustIdFilter(String[] thrustIds) throws InvalidFilterParameterException {
		if (thrustIds == null) {
			return null;
		}

		List<Integer> thrustInts = new ArrayList<Integer>();

		for(String thrustId: thrustIds){
			Integer thrustInt = null;
			try {
				thrustInt = Integer.parseInt(thrustId);
				thrustInts.add(thrustInt);
			} catch (NumberFormatException e) {
				throw new InvalidFilterParameterException("thrustId", Integer.class);
			}
		}

		return QDMDIIProject.dMDIIProject.projectThrust().id.in(thrustInts);
	}

	private Predicate rootNumberFilter(String rootNumber) throws InvalidFilterParameterException {
		if (rootNumber == null) {
			return null;
		}

		Integer rootNumberInt = null;
		try {
			rootNumberInt = Integer.parseInt(rootNumber);
		} catch (NumberFormatException e) {
			throw new InvalidFilterParameterException("rootNumber", Integer.class);
		}

		return QDMDIIProject.dMDIIProject.rootNumber.eq(rootNumberInt);
	}

	private Predicate callNumberFilter(String callNumber) throws InvalidFilterParameterException {
		if (callNumber == null) {
			return null;
		}

		Integer callNumberInt = null;
		try {
			callNumberInt = Integer.parseInt(callNumber);
		} catch (NumberFormatException e) {
			throw new InvalidFilterParameterException("callNumber", Integer.class);
		}

		return QDMDIIProject.dMDIIProject.callNumber.eq(callNumberInt);
	}

	private Predicate isEventFilter(String isEvent) throws InvalidFilterParameterException {
		if (isEvent == null) {
			return null;
		}

		return QDMDIIProject.dMDIIProject.isEvent.eq(Boolean.valueOf(isEvent));
	}

	public List<DMDIIProjectModel> findDmdiiProjectsByPrimeOrganizationId (Integer primeOrganizationId, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException {
		Assert.notNull(primeOrganizationId);
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByPrimeOrganizationId(new PageRequest(pageNumber, pageSize), primeOrganizationId).getContent());
	}


	public Long countDMDIIProjectsByPrimeOrganizationIdAndIsActive(Integer dmdiiMemberId) {
		Assert.notNull(dmdiiMemberId);
		return dmdiiProjectRepository.countByPrimeOrganizationIdAndIsActive(dmdiiMemberId);
	}

	public Long countDmdiiProjectsByPrimeOrganizationId(Integer dmdiiMemberId) throws InvalidFilterParameterException {
		Assert.notNull(dmdiiMemberId);
		return dmdiiProjectRepository.countByPrimeOrganizationId(dmdiiMemberId);
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByAwardedDate(Date awardedDate, Integer pageNumber, Integer pageSize) {
		Assert.notNull(awardedDate);
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByAwardedDate(new PageRequest(pageNumber, pageSize), awardedDate).getContent());
	}

	public Long countDMDIIProjectsByAwardedDate(Date awardedDate) {
		Assert.notNull(awardedDate);
		return dmdiiProjectRepository.countByAwardedDate(awardedDate);
	}

	public List<DMDIIProjectModel> findByTitle(String title, Integer pageNumber, Integer pageSize) {
		Assert.notNull(title);
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByProjectTitleLikeIgnoreCase(new PageRequest(pageNumber, pageSize), "%"+title+"%").getContent());
	}

	public Long countByTitle(String title) {
		Assert.notNull(title);
		return dmdiiProjectRepository.countByProjectTitleLikeIgnoreCase("%"+title+"%");
	}

	public List<DMDIIProjectModel> findByTitleOrProjectNumber(String searchTerm, Integer pageNumber, Integer pageSize, String[] focusIds, String[] statuses, String[] thrustIds) throws InvalidFilterParameterException {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(focusIds, statuses, thrustIds, searchTerm));
		PageRequest pageRequest = new PageRequest(pageNumber, pageSize,
				new Sort(
						new Order(Direction.ASC, "rootNumber"),
						new Order(Direction.ASC, "callNumber"),
						new Order(Direction.ASC, "projectNumber")
				)
		);

		if(where != null){
			ServiceLogger.log("where", where.toString());
		}

		return mapper.mapToModel(dmdiiProjectRepository.findAll(where, pageRequest).getContent());

		// Assert.notNull(searchTerm);
		// Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		// return mapper.mapToModel(dmdiiProjectRepository.findByProjectTitleLikeIgnoreCaseOrProjectNumberContainsIgnoreCase(new PageRequest(pageNumber, pageSize), "%"+parseProjectNumberSearchString(searchTerm)+"%").getContent());
	}

	public Long countByTitleOrProjectNumber(String searchTerm) {
		Assert.notNull(searchTerm);
		return dmdiiProjectRepository.countByProjectTitleLikeIgnoreCaseOrProjectNumberContainsIgnoreCase("%"+parseProjectNumberSearchString(searchTerm)+"%");
	}

	private String parseProjectNumberSearchString(String searchTerm) {

		String brokenString[] = searchTerm.split("-");

		for(int i = 0; i < brokenString.length; i++) {
			if (isNumeric(brokenString[i]) && brokenString[i].length() > 1 && Integer.parseInt(brokenString[i].substring(0,1)) == 0) {
				brokenString[i] = brokenString[i].substring(1);
			}
		}

		return Arrays.stream(brokenString).collect(
				Collectors.joining("-"));
	}

	private static boolean isNumeric(String str)
	{
		try
		{
			double d = Double.parseDouble(str);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}

	public DMDIIProjectModel findOne(Integer id) {
		Assert.notNull(id);
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findOne(id));
	}

	public DMDIIProjectModel save(DMDIIProjectModel project) {
		Assert.notNull(project);

		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);

		DMDIIProject projectEntity = projectMapper.mapToEntity(project);


		if(projectEntity.getPrimeOrganization() != null) {
			DMDIIMember memberEntity = memberMapper.mapToEntity(dmdiiMemberService.findOne(project.getPrimeOrganization().getId()));
			projectEntity.setPrimeOrganization(memberEntity);
		}

		projectEntity = dmdiiProjectRepository.save(projectEntity);

		// Insert update for newly-created project
		if (!project.getIsEvent()) {
			RecentUpdateController recentUpdateController = new RecentUpdateController();
			recentUpdateController.addRecentUpdate(projectEntity);
		}

		return projectMapper.mapToModel(projectEntity);
	}

	public DMDIIProjectModel update (DMDIIProjectModel project) {
		Assert.notNull(project);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);

		DMDIIProject projectEntity = projectMapper.mapToEntity(project);
		DMDIIProject oldEntity = dmdiiProjectRepository.findOne(project.getId());
		Assert.notNull(oldEntity);

		if(project.getPrimeOrganization() != null) {
			DMDIIMember memberEntity = memberMapper.mapToEntity(dmdiiMemberService.findOne(project.getPrimeOrganization().getId()));
			projectEntity.setPrimeOrganization(memberEntity);
		}

		// Insert update for all modified fields
		if (!project.getIsEvent()) {
			RecentUpdateController recentUpdateController = new RecentUpdateController();
			recentUpdateController.addRecentUpdate(projectEntity, oldEntity);
		}

		projectEntity = dmdiiProjectRepository.save(projectEntity);

		return projectMapper.mapToModel(projectEntity);
	}

	public List<DMDIIMemberModel> findContributingCompanyByProjectId(Integer projectId) {
		Assert.notNull(projectId);
		Mapper<DMDIIMember, DMDIIMemberModel> mapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);

		return mapper.mapToModel(dmdiiMemberDao.findByDMDIIProjectContributingCompanyDMDIIProject(projectId));
	}

	public List<DMDIIProjectNewsModel> getDmdiiProjectNews(Integer limit) {
		Mapper<DMDIIProjectNews, DMDIIProjectNewsModel> mapper = mapperFactory.mapperFor(DMDIIProjectNews.class, DMDIIProjectNewsModel.class);
		return mapper.mapToModel(dmdiiProjectNewsRepository.findAllByOrderByDateCreatedDesc(new PageRequest(0, limit)).getContent());
	}

	public List<DMDIIProjectEventModel> getDmdiiProjectEvents(Integer limit) {
		Mapper<DMDIIProjectEvent, DMDIIProjectEventModel> mapper = mapperFactory.mapperFor(DMDIIProjectEvent.class, DMDIIProjectEventModel.class);
		return mapper.mapToModel(dmdiiProjectEventsRepository.findAllByOrderByEventDateDesc(new PageRequest(0, limit)).getContent());
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByContributingCompany(Integer dmdiiMemberId) {
		Assert.notNull(dmdiiMemberId);
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByContributingCompanyId(dmdiiMemberId));
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByPrimeOrganizationIdAndIsActive(Integer dmdiiMemberId, Integer pageNumber, Integer pageSize) {
		Assert.notNull(dmdiiMemberId);
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByPrimeOrganizationIdAndIsActive(new PageRequest(pageNumber, pageSize), dmdiiMemberId).getContent());
	}

	public List<DMDIIProjectModel> findActiveDMDIIProjectsByContributingCompany(Integer dmdiiMemberId) {
		Assert.notNull(dmdiiMemberId);
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		dmdiiProjectRepository.findActiveByContributingCompanyId(dmdiiMemberId);
		return mapper.mapToModel(dmdiiProjectRepository.findActiveByContributingCompanyId(dmdiiMemberId));
	}

	@Transactional
	public void delete(Integer dmdiiProjectId) {
		dmdiiDocumentService.deleteDMDIIDocumentsByDMDIIProjectId(dmdiiProjectId);
		dmdiiProjectRepository.delete(dmdiiProjectId);
	}

}
