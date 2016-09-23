package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIMemberContact;
import org.dmc.services.data.entities.DMDIIMemberFinance;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.models.DMDIIMemberContactModel;
import org.dmc.services.data.models.DMDIIMemberFinanceModel;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIITypeModel;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.dmdiitype.DMDIIType;
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
		Mapper<DMDIIMemberContact, DMDIIMemberContactModel> contactMapper = mapperFactory.mapperFor(DMDIIMemberContact.class, DMDIIMemberContactModel.class);
		Mapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> financeMapper = mapperFactory.mapperFor(DMDIIMemberFinance.class, DMDIIMemberFinanceModel.class);

		try {
			formatter.setLenient(false);
			entity.setStartDate(formatter.parse(model.getStartDate()));
			entity.setExpireDate(formatter.parse(model.getExpireDate()));
		} catch (ParseException e) {
			throw new DateFormatException("Date is incorrectly formatted, cannot parse.");
		}
		entity.setDmdiiType(typeMapper.mapToEntity(model.getDmdiiType()));
		entity.setOrganization(orgMapper.mapToEntity(model.getOrganization()));
		entity.setContacts(contactMapper.mapToEntity(model.getContacts()));
		entity.setFinances(financeMapper.mapToEntity(model.getFinances()));

		return entity;
	}

	@Override
	public DMDIIMemberModel mapToModel(DMDIIMember entity) {
		if (entity == null) return null;

		DMDIIMemberModel model = copyProperties(entity, new DMDIIMemberModel(), new String[]{"startDate", "expireDate", "customers", "finances", "instituteInvolvement"});

		Mapper<DMDIIType, DMDIITypeModel> typeMapper = mapperFactory.mapperFor(DMDIIType.class, DMDIITypeModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class,	OrganizationModel.class);
		Mapper<DMDIIMemberContact, DMDIIMemberContactModel> contactMapper = mapperFactory.mapperFor(DMDIIMemberContact.class, DMDIIMemberContactModel.class);
		Mapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> financeMapper = mapperFactory.mapperFor(DMDIIMemberFinance.class, DMDIIMemberFinanceModel.class);

		model.setStartDate(formatter.format(entity.getStartDate()));
		model.setExpireDate(formatter.format(entity.getExpireDate()));
		model.setDmdiiType(typeMapper.mapToModel(entity.getDmdiiType()));
		model.setOrganization(orgMapper.mapToModel(entity.getOrganization()));
		model.setContacts(contactMapper.mapToModel(entity.getContacts()));
		model.setFinances(financeMapper.mapToModel(entity.getFinances()));

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
