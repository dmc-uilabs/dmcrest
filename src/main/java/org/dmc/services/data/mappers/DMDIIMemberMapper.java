package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIAreaOfExpertise;
import org.dmc.services.data.entities.DMDIIAward;
import org.dmc.services.data.entities.DMDIIInstituteInvolvement;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIMemberContact;
import org.dmc.services.data.entities.DMDIIMemberCustomer;
import org.dmc.services.data.entities.DMDIIMemberFinance;
import org.dmc.services.data.entities.DMDIIMemberUser;
import org.dmc.services.data.entities.DMDIISkill;
import org.dmc.services.data.entities.DMDIIType;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.models.DMDIIAreaOfExpertiseModel;
import org.dmc.services.data.models.DMDIIAwardModel;
import org.dmc.services.data.models.DMDIIInstituteInvolvementModel;
import org.dmc.services.data.models.DMDIIMemberContactModel;
import org.dmc.services.data.models.DMDIIMemberCustomerModel;
import org.dmc.services.data.models.DMDIIMemberFinanceModel;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIMemberUserModel;
import org.dmc.services.data.models.DMDIISkillModel;
import org.dmc.services.data.models.DMDIITypeModel;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.exceptions.DateFormatException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class DMDIIMemberMapper extends AbstractMapper<DMDIIMember, DMDIIMemberModel> {

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public DMDIIMember mapToEntity(DMDIIMemberModel model) {
		if (model == null) return null;

		DMDIIMember entity = copyProperties(model, new DMDIIMember(),
				new String[] { "startDate", "expireDate", "awards", "contacts", "customers", "finances",
						"instituteInvolvement" });

		Mapper<DMDIIType, DMDIITypeModel> typeMapper = mapperFactory.mapperFor(DMDIIType.class, DMDIITypeModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class,	OrganizationModel.class);
		Mapper<DMDIIAward, DMDIIAwardModel> awardMapper = mapperFactory.mapperFor(DMDIIAward.class, DMDIIAwardModel.class);
		Mapper<DMDIIAreaOfExpertise, DMDIIAreaOfExpertiseModel> aoeMapper = mapperFactory.mapperFor(DMDIIAreaOfExpertise.class, DMDIIAreaOfExpertiseModel.class);
		Mapper<DMDIIMemberContact, DMDIIMemberContactModel> contactMapper = mapperFactory.mapperFor(DMDIIMemberContact.class, DMDIIMemberContactModel.class);
		Mapper<DMDIIMemberCustomer, DMDIIMemberCustomerModel> customerMapper = mapperFactory.mapperFor(DMDIIMemberCustomer.class, DMDIIMemberCustomerModel.class);
		Mapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> financeMapper = mapperFactory.mapperFor(DMDIIMemberFinance.class, DMDIIMemberFinanceModel.class);
		Mapper<DMDIIInstituteInvolvement, DMDIIInstituteInvolvementModel> involvementMapper = mapperFactory.mapperFor(DMDIIInstituteInvolvement.class, DMDIIInstituteInvolvementModel.class);
		Mapper<DMDIISkill, DMDIISkillModel> skillMapper = mapperFactory.mapperFor(DMDIISkill.class,	DMDIISkillModel.class);
		Mapper<DMDIIMemberUser, DMDIIMemberUserModel> userMapper = mapperFactory.mapperFor(DMDIIMemberUser.class, DMDIIMemberUserModel.class);

		try {
			formatter.setLenient(false);
			entity.setStartDate(formatter.parse(model.getStartDate()));
			entity.setExpireDate(formatter.parse(model.getExpireDate()));
		} catch (ParseException e) {
			throw new DateFormatException("Date is incorrectly formatted, cannot parse.");
		}
		entity.setDmdiiType(typeMapper.mapToEntity(model.getDmdiiType()));
		entity.setOrganization(orgMapper.mapToEntity(model.getOrganization()));
		entity.setAwards(awardMapper.mapToEntity(model.getAwards()));
		entity.setAreasOfExpertise(aoeMapper.mapToEntity(model.getAreasOfExpertise()));
		entity.setDesiredAreasOfExpertise(aoeMapper.mapToEntity(model.getDesiredAreasOfExpertise()));
		entity.setContacts(contactMapper.mapToEntity(model.getContacts()));
		entity.setCustomers(customerMapper.mapToEntity(model.getCustomers()));
		entity.setFinances(financeMapper.mapToEntity(model.getFinances()));
		entity.setInstituteInvolvement(involvementMapper.mapToEntity(model.getInstituteInvolvement()));
		entity.setSkills(skillMapper.mapToEntity(model.getSkills()));
		entity.setUsers(userMapper.mapToEntity(model.getUsers()));

		return entity;
	}

	@Override
	public DMDIIMemberModel mapToModel(DMDIIMember entity) {
		if (entity == null) return null;

		DMDIIMemberModel model = copyProperties(entity, new DMDIIMemberModel(),
				new String[] { "startDate", "expireDate", "customers", "finances", "instituteInvolvement" });

		Mapper<DMDIIType, DMDIITypeModel> typeMapper = mapperFactory.mapperFor(DMDIIType.class, DMDIITypeModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class,	OrganizationModel.class);
		Mapper<DMDIIAward, DMDIIAwardModel> awardMapper = mapperFactory.mapperFor(DMDIIAward.class, DMDIIAwardModel.class);
		Mapper<DMDIIAreaOfExpertise, DMDIIAreaOfExpertiseModel> aoeMapper = mapperFactory.mapperFor(DMDIIAreaOfExpertise.class, DMDIIAreaOfExpertiseModel.class);
		Mapper<DMDIIMemberCustomer, DMDIIMemberCustomerModel> customerMapper = mapperFactory.mapperFor(DMDIIMemberCustomer.class, DMDIIMemberCustomerModel.class);
		Mapper<DMDIIMemberContact, DMDIIMemberContactModel> contactMapper = mapperFactory.mapperFor(DMDIIMemberContact.class, DMDIIMemberContactModel.class);
		Mapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> financeMapper = mapperFactory.mapperFor(DMDIIMemberFinance.class, DMDIIMemberFinanceModel.class);
		Mapper<DMDIIInstituteInvolvement, DMDIIInstituteInvolvementModel> involvementMapper = mapperFactory.mapperFor(DMDIIInstituteInvolvement.class, DMDIIInstituteInvolvementModel.class);
		Mapper<DMDIISkill, DMDIISkillModel> skillMapper = mapperFactory.mapperFor(DMDIISkill.class,	DMDIISkillModel.class);
		Mapper<DMDIIMemberUser, DMDIIMemberUserModel> userMapper = mapperFactory.mapperFor(DMDIIMemberUser.class, DMDIIMemberUserModel.class);

		model.setStartDate(formatter.format(entity.getStartDate()));
		model.setExpireDate(formatter.format(entity.getExpireDate()));
		model.setDmdiiType(typeMapper.mapToModel(entity.getDmdiiType()));
		model.setOrganization(orgMapper.mapToModel(entity.getOrganization()));
		model.setAwards(awardMapper.mapToModel(entity.getAwards()));
		model.setAreasOfExpertise(aoeMapper.mapToModel(entity.getAreasOfExpertise()));
		model.setDesiredAreasOfExpertise(aoeMapper.mapToModel(entity.getDesiredAreasOfExpertise()));
		model.setContacts(contactMapper.mapToModel(entity.getContacts()));
		model.setCustomers(customerMapper.mapToModel(entity.getCustomers()));
		model.setFinances(financeMapper.mapToModel(entity.getFinances()));
		model.setInstituteInvolvement(involvementMapper.mapToModel(entity.getInstituteInvolvement()));
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
