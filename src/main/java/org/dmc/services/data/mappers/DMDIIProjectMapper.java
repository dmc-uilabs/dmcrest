package org.dmc.services.data.mappers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectContact;
import org.dmc.services.data.entities.DMDIIProjectFocusArea;
import org.dmc.services.data.entities.DMDIIProjectStatus;
import org.dmc.services.data.entities.DMDIIProjectThrust;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIPrimeOrganizationModel;
import org.dmc.services.data.models.DMDIIProjectContactModel;
import org.dmc.services.data.models.DMDIIProjectFocusAreaModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectStatusModel;
import org.dmc.services.data.models.DMDIIProjectThrustModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.dmdiimember.DMDIIMemberService;
import org.springframework.stereotype.Component;


@Component
public class DMDIIProjectMapper extends AbstractMapper<DMDIIProject, DMDIIProjectModel> {

	@Inject
	private DMDIIMemberService dmdiiMemberService;

	@Override
	public DMDIIProject mapToEntity(DMDIIProjectModel model) {
		if (model == null) return null;

		DMDIIProject entity = copyProperties(model, new DMDIIProject());

		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<DMDIIProjectContact, DMDIIProjectContactModel> contactMapper = mapperFactory.mapperFor(DMDIIProjectContact.class, DMDIIProjectContactModel.class);
		Mapper<DMDIIProjectStatus, DMDIIProjectStatusModel> statusMapper = mapperFactory.mapperFor(DMDIIProjectStatus.class, DMDIIProjectStatusModel.class);
		Mapper<DMDIIProjectFocusArea, DMDIIProjectFocusAreaModel> focusMapper = mapperFactory.mapperFor(DMDIIProjectFocusArea.class, DMDIIProjectFocusAreaModel.class);
		Mapper<DMDIIProjectThrust, DMDIIProjectThrustModel> thrustMapper = mapperFactory.mapperFor(DMDIIProjectThrust.class, DMDIIProjectThrustModel.class);

		List<DMDIIMemberModel> contributingCompanyModels = model.getContributingCompanyIds()
				.stream()
				.map(e -> dmdiiMemberService.findOne(e))
				.collect(Collectors.toList());
		entity.setPrimeOrganization(memberMapper.mapToEntity(dmdiiMemberService.findOne(model.getPrimeOrganization().getId())));
		entity.setPrincipalInvestigator(contactMapper.mapToEntity(model.getPrincipalInvestigator()));
		entity.setPrincipalPointOfContact(contactMapper.mapToEntity(model.getPrincipalPointOfContact()));
		entity.setProjectStatus(statusMapper.mapToEntity(model.getProjectStatus()));
		entity.setProjectFocusArea(focusMapper.mapToEntity(model.getProjectFocusArea()));
		entity.setProjectThrust(thrustMapper.mapToEntity(model.getProjectThrust()));
		entity.setContributingCompanies(memberMapper.mapToEntity(contributingCompanyModels));

		return entity;
	}

	@Override
	public DMDIIProjectModel mapToModel(DMDIIProject entity) {
		if (entity == null) return null;

		DMDIIProjectModel model = copyProperties(entity, new DMDIIProjectModel());

		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<DMDIIProjectContact, DMDIIProjectContactModel> contactMapper = mapperFactory.mapperFor(DMDIIProjectContact.class, DMDIIProjectContactModel.class);
		Mapper<DMDIIProjectStatus, DMDIIProjectStatusModel> statusMapper = mapperFactory.mapperFor(DMDIIProjectStatus.class, DMDIIProjectStatusModel.class);
		Mapper<DMDIIProjectFocusArea, DMDIIProjectFocusAreaModel> focusMapper = mapperFactory.mapperFor(DMDIIProjectFocusArea.class, DMDIIProjectFocusAreaModel.class);
		Mapper<DMDIIProjectThrust, DMDIIProjectThrustModel> thrustMapper = mapperFactory.mapperFor(DMDIIProjectThrust.class, DMDIIProjectThrustModel.class);

		List<Integer> contributingCompanyIds = entity.getContributingCompanies()
				.stream()
				.map(e -> e.getId())
				.collect(Collectors.toList());
		model.setPrimeOrganization(new DMDIIPrimeOrganizationModel(entity.getPrimeOrganization().getId(), entity.getPrimeOrganization().getOrganization().getName()));
		model.setPrincipalInvestigator(contactMapper.mapToModel(entity.getPrincipalInvestigator()));
		model.setPrincipalPointOfContact(contactMapper.mapToModel(entity.getPrincipalPointOfContact()));
		model.setProjectStatus(statusMapper.mapToModel(entity.getProjectStatus()));
		model.setProjectFocusArea(focusMapper.mapToModel(entity.getProjectFocusArea()));
		model.setProjectThrust(thrustMapper.mapToModel(entity.getProjectThrust()));
		model.setContributingCompanies(contributingCompanyIds);

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
