package org.dmc.services.mappers;

import org.dmc.services.dmdiitype.DMDIIType;
import org.dmc.services.entities.DMDIIAreaOfExpertise;
import org.dmc.services.entities.DMDIIInstituteInvolvement;
import org.dmc.services.entities.DMDIIMember;
import org.dmc.services.entities.DMDIIMemberContact;
import org.dmc.services.entities.DMDIIMemberFinance;
import org.dmc.services.entities.DMDIIMemberUser;
import org.dmc.services.entities.DMDIIRndFocus;
import org.dmc.services.entities.DMDIISkill;
import org.dmc.services.models.DMDIIAreaOfExpertiseModel;
import org.dmc.services.models.DMDIIInstituteInvolvementModel;
import org.dmc.services.models.DMDIIMemberContactModel;
import org.dmc.services.models.DMDIIMemberFinanceModel;
import org.dmc.services.models.DMDIIMemberModel;
import org.dmc.services.models.DMDIIMemberUserModel;
import org.dmc.services.models.DMDIIRndFocusModel;
import org.dmc.services.models.DMDIISkillModel;
import org.dmc.services.models.DMDIITypeModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberMapper extends AbstractMapper<DMDIIMember, DMDIIMemberModel> {

	@Override
	public DMDIIMember mapToEntity(DMDIIMemberModel model) {
		return null;
	}

	@Override
	public DMDIIMemberModel mapToModel(DMDIIMember entity) {
		DMDIIMemberModel model = copyProperties(entity, new DMDIIMemberModel());
		BeanUtils.copyProperties(model, entity);
		
		Mapper<DMDIIType, DMDIITypeModel> typeMapper = mapperFactory.mapperFor(DMDIIType.class, DMDIITypeModel.class);
		Mapper<DMDIIAreaOfExpertise, DMDIIAreaOfExpertiseModel> aoeMapper = mapperFactory.mapperFor(DMDIIAreaOfExpertise.class, DMDIIAreaOfExpertiseModel.class);
		Mapper<DMDIIMemberContact, DMDIIMemberContactModel> contactMapper = mapperFactory.mapperFor(DMDIIMemberContact.class, DMDIIMemberContactModel.class);
		Mapper<DMDIIMemberFinance, DMDIIMemberFinanceModel> financeMapper = mapperFactory.mapperFor(DMDIIMemberFinance.class, DMDIIMemberFinanceModel.class);
		Mapper<DMDIIInstituteInvolvement, DMDIIInstituteInvolvementModel> involvementMapper = mapperFactory.mapperFor(DMDIIInstituteInvolvement.class, DMDIIInstituteInvolvementModel.class);
		Mapper<DMDIIRndFocus, DMDIIRndFocusModel> rndMapper = mapperFactory.mapperFor(DMDIIRndFocus.class, DMDIIRndFocusModel.class);
		Mapper<DMDIISkill, DMDIISkillModel> skillMapper = mapperFactory.mapperFor(DMDIISkill.class, DMDIISkillModel.class);
		Mapper<DMDIIMemberUser, DMDIIMemberUserModel> userMapper = mapperFactory.mapperFor(DMDIIMemberUser.class, DMDIIMemberUserModel.class);
		
		// Map type
		model.setDmdiiType(typeMapper.mapToModel(entity.getDmdiiType()));
		
		// Map areas of expertise
		model.setAreasOfExpertise(aoeMapper.mapToModel(entity.getAreasOfExpertise()));
		
		// Map member contacts
		model.setContacts(contactMapper.mapToModel(entity.getContacts()));
		
		// Map member finances
		model.setFinances(financeMapper.mapToModel(entity.getFinances()));
		
		// Map member institute involvement
		model.setInstituteInvolvement(involvementMapper.mapToModel(entity.getInstituteInvolvement()));
		
		// Map member rnd focus
		model.setRndFocus(rndMapper.mapToModel(entity.getRndFocus()));
		
		// Map member skills
		model.setSkills(skillMapper.mapToModel(entity.getSkills()));
		
		// Map member users
		model.setUsers(userMapper.mapToModel(entity.getUsers()));
		
		return null;
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
