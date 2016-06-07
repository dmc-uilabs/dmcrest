package org.dmc.services.mappers;

import org.dmc.services.dmdiitype.DMDIIType;
import org.dmc.services.entities.DMDIIAreaOfExpertise;
import org.dmc.services.entities.DMDIIAward;
import org.dmc.services.entities.DMDIIInstituteInvolvement;
import org.dmc.services.entities.DMDIIMember;
import org.dmc.services.entities.DMDIIMemberContact;
import org.dmc.services.entities.DMDIIMemberFinance;
import org.dmc.services.entities.DMDIIMemberUser;
import org.dmc.services.entities.DMDIIRndFocus;
import org.dmc.services.entities.DMDIISkill;
import org.dmc.services.entities.Organization;
import org.dmc.services.models.DMDIIAreaOfExpertiseModel;
import org.dmc.services.models.DMDIIAwardModel;
import org.dmc.services.models.DMDIIInstituteInvolvementModel;
import org.dmc.services.models.DMDIIMemberContactModel;
import org.dmc.services.models.DMDIIMemberFinanceModel;
import org.dmc.services.models.DMDIIMemberModel;
import org.dmc.services.models.DMDIIMemberUserModel;
import org.dmc.services.models.DMDIIRndFocusModel;
import org.dmc.services.models.DMDIISkillModel;
import org.dmc.services.models.DMDIITypeModel;
import org.dmc.services.models.OrganizationModel;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberMapper extends AbstractMapper<DMDIIMember, DMDIIMemberModel> {

	@Override
	public DMDIIMember mapToEntity(DMDIIMemberModel model) {
		DMDIIMember entity = copyProperties(model, new DMDIIMember());

		Mapper<DMDIIType, DMDIITypeModel> typeMapper = mapperFactory.mapperFor(DMDIIType.class, DMDIITypeModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class,	OrganizationModel.class);
		Mapper<DMDIIAward, DMDIIAwardModel> awardMapper = mapperFactory.mapperFor(DMDIIAward.class, DMDIIAwardModel.class);
		Mapper<DMDIIAreaOfExpertise, DMDIIAreaOfExpertiseModel> aoeMapper = mapperFactory.mapperFor(DMDIIAreaOfExpertise.class, DMDIIAreaOfExpertiseModel.class);
		Mapper<DMDIIMemberContact, DMDIIMemberContactModel> contactMapper = mapperFactory.mapperFor(DMDIIMemberContact.class, DMDIIMemberContactModel.class);
		Mapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> financeMapper = mapperFactory.mapperFor(DMDIIMemberFinance.class, DMDIIMemberFinanceModel.class);
		Mapper<DMDIIInstituteInvolvement, DMDIIInstituteInvolvementModel> involvementMapper = mapperFactory.mapperFor(DMDIIInstituteInvolvement.class, DMDIIInstituteInvolvementModel.class);
		Mapper<DMDIIRndFocus, DMDIIRndFocusModel> rndMapper = mapperFactory.mapperFor(DMDIIRndFocus.class, DMDIIRndFocusModel.class);
		Mapper<DMDIISkill, DMDIISkillModel> skillMapper = mapperFactory.mapperFor(DMDIISkill.class,	DMDIISkillModel.class);
		Mapper<DMDIIMemberUser, DMDIIMemberUserModel> userMapper = mapperFactory.mapperFor(DMDIIMemberUser.class, DMDIIMemberUserModel.class);

		entity.setDmdiiType(typeMapper.mapToEntity(model.getDmdiiType()));
		entity.setOrganization(orgMapper.mapToEntity(model.getOrganization()));
		entity.setAwards(awardMapper.mapToEntity(model.getAwards()));
		entity.setAreasOfExpertise(aoeMapper.mapToEntity(model.getAreasOfExpertise()));
		entity.setContacts(contactMapper.mapToEntity(model.getContacts()));
		entity.setFinances(financeMapper.mapToEntity(model.getFinances()));
		entity.setInstituteInvolvement(involvementMapper.mapToEntity(model.getInstituteInvolvement()));
		entity.setRndFocus(rndMapper.mapToEntity(model.getRndFocus()));
		entity.setSkills(skillMapper.mapToEntity(model.getSkills()));
		entity.setUsers(userMapper.mapToEntity(model.getUsers()));

		return entity;
	}

	@Override
	public DMDIIMemberModel mapToModel(DMDIIMember entity) {
		DMDIIMemberModel model = copyProperties(entity, new DMDIIMemberModel());

		Mapper<DMDIIType, DMDIITypeModel> typeMapper = mapperFactory.mapperFor(Organization.class, DMDIITypeModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class,	OrganizationModel.class);
		Mapper<DMDIIAward, DMDIIAwardModel> awardMapper = mapperFactory.mapperFor(DMDIIAward.class, DMDIIAwardModel.class);
		Mapper<DMDIIAreaOfExpertise, DMDIIAreaOfExpertiseModel> aoeMapper = mapperFactory.mapperFor(DMDIIAreaOfExpertise.class, DMDIIAreaOfExpertiseModel.class);
		Mapper<DMDIIMemberContact, DMDIIMemberContactModel> contactMapper = mapperFactory.mapperFor(DMDIIMemberContact.class, DMDIIMemberContactModel.class);
		Mapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> financeMapper = mapperFactory.mapperFor(DMDIIMemberFinance.class, DMDIIMemberFinanceModel.class);
		Mapper<DMDIIInstituteInvolvement, DMDIIInstituteInvolvementModel> involvementMapper = mapperFactory.mapperFor(DMDIIInstituteInvolvement.class, DMDIIInstituteInvolvementModel.class);
		Mapper<DMDIIRndFocus, DMDIIRndFocusModel> rndMapper = mapperFactory.mapperFor(DMDIIRndFocus.class, DMDIIRndFocusModel.class);
		Mapper<DMDIISkill, DMDIISkillModel> skillMapper = mapperFactory.mapperFor(DMDIISkill.class,	DMDIISkillModel.class);
		Mapper<DMDIIMemberUser, DMDIIMemberUserModel> userMapper = mapperFactory.mapperFor(DMDIIMemberUser.class, DMDIIMemberUserModel.class);

		model.setDmdiiType(typeMapper.mapToModel(entity.getDmdiiType()));
		model.setOrganization(orgMapper.mapToModel(entity.getOrganization()));
		model.setAwards(awardMapper.mapToModel(entity.getAwards()));
		model.setAreasOfExpertise(aoeMapper.mapToModel(entity.getAreasOfExpertise()));
		model.setContacts(contactMapper.mapToModel(entity.getContacts()));
		model.setFinances(financeMapper.mapToModel(entity.getFinances()));
		model.setInstituteInvolvement(involvementMapper.mapToModel(entity.getInstituteInvolvement()));
		model.setRndFocus(rndMapper.mapToModel(entity.getRndFocus()));
		model.setSkills(skillMapper.mapToModel(entity.getSkills()));
		model.setUsers(userMapper.mapToModel(entity.getUsers()));

		return model;
	}

	@Override
	public Class<DMDIIMember> supportsEntity() {
		return DMDIIMember.class;
	}

	@Override
	public Class<DMDIIMemberModel> supportsModel() {
		return DMDIIMemberModel.class;
	}

}
