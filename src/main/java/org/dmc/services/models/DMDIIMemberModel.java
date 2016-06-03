package org.dmc.services.models;

import java.sql.Date;
import java.util.List;

import org.dmc.services.entities.DMDIIAreaOfExpertise;
import org.dmc.services.entities.DMDIIAward;
import org.dmc.services.entities.DMDIIInstituteInvolvement;
import org.dmc.services.entities.DMDIIMemberContact;
import org.dmc.services.entities.DMDIIMemberCustomer;
import org.dmc.services.entities.DMDIIMemberFinance;
import org.dmc.services.entities.DMDIIMemberUser;
import org.dmc.services.entities.DMDIIRndFocus;
import org.dmc.services.entities.DMDIISkill;

public class DMDIIMemberModel extends BaseModel {

	private DMDIITypeModel dmdiiType;
	private OrganizationModel organization;

	private Date startDate;

	private Date expireDate;

	private List<DMDIIAward> awards;

	private List<DMDIIAreaOfExpertise> areasOfExpertise;

	private List<DMDIIMemberContact> contacts;

	private List<DMDIIMemberCustomer> customers;

	private List<DMDIIMemberFinance> finances;

	private List<DMDIIInstituteInvolvement> instituteInvolvement;

	private List<DMDIIRndFocus> rndFocus;

	private List<DMDIISkill> skills;

	private List<DMDIIMemberUser> users;
}
