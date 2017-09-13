package org.dmc.services.data.mappers;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectContact;
import org.dmc.services.data.entities.DMDIIProjectFocusArea;
import org.dmc.services.data.entities.DMDIIProjectStatus;
import org.dmc.services.data.entities.DMDIIProjectThrust;
import org.dmc.services.data.entities.User;
import org.dmc.services.ServiceLogger;

import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIPrimeOrganizationModel;
import org.dmc.services.data.models.DMDIIProjectContactModel;
import org.dmc.services.data.models.DMDIIProjectFocusAreaModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectStatusModel;
import org.dmc.services.data.models.DMDIIProjectThrustModel;
import org.dmc.services.data.models.UserModel;

import org.dmc.services.dmdiimember.DMDIIMemberService;
import org.dmc.services.UserService;
import org.springframework.stereotype.Component;


@Component
public class DMDIIProjectMapper extends AbstractMapper<DMDIIProject, DMDIIProjectModel> {

	@Inject
	private DMDIIMemberService dmdiiMemberService;

	@Inject
	private UserService userService;

	@Override
	public DMDIIProject mapToEntity(DMDIIProjectModel model) {
		if (model == null) return null;

		DMDIIProject entity = copyProperties(model, new DMDIIProject());


		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<DMDIIProjectContact, DMDIIProjectContactModel> contactMapper = mapperFactory.mapperFor(DMDIIProjectContact.class, DMDIIProjectContactModel.class);
		Mapper<DMDIIProjectStatus, DMDIIProjectStatusModel> statusMapper = mapperFactory.mapperFor(DMDIIProjectStatus.class, DMDIIProjectStatusModel.class);
		Mapper<DMDIIProjectFocusArea, DMDIIProjectFocusAreaModel> focusMapper = mapperFactory.mapperFor(DMDIIProjectFocusArea.class, DMDIIProjectFocusAreaModel.class);
		Mapper<DMDIIProjectThrust, DMDIIProjectThrustModel> thrustMapper = mapperFactory.mapperFor(DMDIIProjectThrust.class, DMDIIProjectThrustModel.class);

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);

		List<DMDIIMemberModel> contributingCompanyModels = model.getContributingCompanyIds()
		.stream()
		.map(e -> dmdiiMemberService.findOne(e))
		.collect(Collectors.toList());

		entity.setContributingCompanies(memberMapper.mapToEntity(contributingCompanyModels));

		if (!model.getIsEvent()) {
			if (model.getPrimeOrganization().getId() != null) {
				entity.setPrimeOrganization(memberMapper.mapToEntity(dmdiiMemberService.findOne(model.getPrimeOrganization().getId())));
			}
			entity.setPrincipalInvestigator(contactMapper.mapToEntity(model.getPrincipalInvestigator()));
			entity.setProjectStatus(statusMapper.mapToEntity(model.getProjectStatus()));
			entity.setProjectFocusArea(focusMapper.mapToEntity(model.getProjectFocusArea()));
			entity.setProjectThrust(thrustMapper.mapToEntity(model.getProjectThrust()));
		}


		entity.setPrincipalPointOfContact(userMapper.mapToEntity(model.getPrincipalPointOfContact()));


		try{
			if (model.getAwardedDate() != null) {
				entity.setAwardedDate(format.parse(model.getAwardedDate()));
			}
			if (model.getEndDate() != null) {
				entity.setEndDate(format.parse(model.getEndDate()));
			}
		} catch (Exception e){
			throw new DMCServiceException(DMCError.ParseError, e.getMessage());
		}


		return entity;
	}

	@Override
	public DMDIIProjectModel mapToModel(DMDIIProject entity) {
		if (entity == null) return null;

		DMDIIProjectModel model = copyProperties(entity, new DMDIIProjectModel());

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<DMDIIProjectContact, DMDIIProjectContactModel> contactMapper = mapperFactory.mapperFor(DMDIIProjectContact.class, DMDIIProjectContactModel.class);
		Mapper<DMDIIProjectStatus, DMDIIProjectStatusModel> statusMapper = mapperFactory.mapperFor(DMDIIProjectStatus.class, DMDIIProjectStatusModel.class);
		Mapper<DMDIIProjectFocusArea, DMDIIProjectFocusAreaModel> focusMapper = mapperFactory.mapperFor(DMDIIProjectFocusArea.class, DMDIIProjectFocusAreaModel.class);
		Mapper<DMDIIProjectThrust, DMDIIProjectThrustModel> thrustMapper = mapperFactory.mapperFor(DMDIIProjectThrust.class, DMDIIProjectThrustModel.class);


		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);

		List<Integer> contributingCompanyIds = entity.getContributingCompanies()
		.stream()
		.map(e -> e.getId())
		.collect(Collectors.toList());

		model.setContributingCompanies(contributingCompanyIds);

		if (!entity.getIsEvent()) {
			if (entity.getPrimeOrganization() != null) {
				model.setPrimeOrganization(new DMDIIPrimeOrganizationModel(entity.getPrimeOrganization().getId(), entity.getPrimeOrganization().getOrganization().getName()));
			}
			model.setPrincipalInvestigator(contactMapper.mapToModel(entity.getPrincipalInvestigator()));
			model.setProjectStatus(statusMapper.mapToModel(entity.getProjectStatus()));
			model.setProjectFocusArea(focusMapper.mapToModel(entity.getProjectFocusArea()));
			model.setProjectThrust(thrustMapper.mapToModel(entity.getProjectThrust()));
		}

		model.setPrincipalPointOfContact(userMapper.mapToModel(entity.getPrincipalPointOfContact()));

		if(entity.getAwardedDate() != null){
			model.setAwardedDate(format.format(entity.getAwardedDate()));
		}
		if(entity.getEndDate() != null){
			model.setEndDate(format.format(entity.getEndDate()));
		}

		return model;
	}

	@Override
	public Class<DMDIIProject> supportsEntity() {
		return DMDIIProject.class;
	}

	@Override
	public Class<DMDIIProjectModel> supportsModel() {
		return DMDIIProjectModel.class;
	}

}
