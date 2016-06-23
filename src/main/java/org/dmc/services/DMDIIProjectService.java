package org.dmc.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

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
	private MapperFactory mapperFactory;

	public List<DMDIIProjectModel> filter(Map<String, String> filterParams, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
		return mapper.mapToModel(dmdiiProjectRepository.findAll(where, new PageRequest(pageNumber, pageSize)).getContent());
	}

	private Collection<Predicate> getFilterExpressions(Map<String, String> filterParams) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<Predicate>();

		expressions.add(statusIdFilter(filterParams.get("statusId")));
		expressions.add(focusIdFilter(filterParams.get("focusId")));
		expressions.add(thrustIdFilter(filterParams.get("thrustId")));
		expressions.add(rootNumberFilter(filterParams.get("rootNumber")));
		expressions.add(callNumberFilter(filterParams.get("callNumber")));

		return expressions;
	}

	private Predicate statusIdFilter(String statusId) throws InvalidFilterParameterException {
		if (statusId == null) {
			return null;
		}

		Integer statusIdInt = null;
		try {
			statusIdInt = Integer.parseInt(statusId);
		} catch (NumberFormatException e) {
			throw new InvalidFilterParameterException("statusId", Integer.class);
		}

		return QDMDIIProject.dMDIIProject.projectStatus().id.eq(statusIdInt);
	}

	private Predicate focusIdFilter(String focusId) throws InvalidFilterParameterException {
		if (focusId == null) {
			return null;
		}

		Integer focusIdInt = null;
		try {
			focusIdInt = Integer.parseInt(focusId);
		} catch (NumberFormatException e) {
			throw new InvalidFilterParameterException("focusId", Integer.class);
		}

		return QDMDIIProject.dMDIIProject.projectFocusArea().id.eq(focusIdInt);
	}

	private Predicate thrustIdFilter(String thrustId) throws InvalidFilterParameterException {
		if (thrustId == null) {
			return null;
		}

		Integer thrustIdInt = null;
		try {
			thrustIdInt = Integer.parseInt(thrustId);
		} catch (NumberFormatException e) {
			throw new InvalidFilterParameterException("thrustId", Integer.class);
		}

		return QDMDIIProject.dMDIIProject.projectThrust().id.eq(thrustIdInt);
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

	public List<DMDIIProjectModel> findDmdiiProjectsByPrimeOrganizationId (Integer primeOrganizationId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByPrimeOrganizationId(new PageRequest(pageNumber, pageSize), primeOrganizationId).getContent());
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByAwardedDate(Date awardedDate, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByAwardedDate(new PageRequest(pageNumber, pageSize), awardedDate).getContent());
	}

	public List<DMDIIProjectModel> findByTitle(String title, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByProjectTitleLikeIgnoreCase(new PageRequest(pageNumber, pageSize), "%"+title+"%").getContent());
	}

	public DMDIIProjectModel findOne(Integer id) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findOne(id));
	}

	public DMDIIProjectModel save(DMDIIProjectModel project) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);

		DMDIIProject projectEntity = mapper.mapToEntity(project);

		projectEntity = dmdiiProjectRepository.save(projectEntity);

		return mapper.mapToModel(projectEntity);
	}

	public List<DMDIIMemberModel> findContributingCompanyByProjectId(Integer projectId) {
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
}
