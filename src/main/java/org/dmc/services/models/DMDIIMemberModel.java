package org.dmc.services.models;

import java.sql.Date;
import java.util.List;

public class DMDIIMemberModel extends BaseModel {

	private DMDIITypeModel dmdiiType;
	private OrganizationModel organization;

	private Date startDate;

	private Date expireDate;

	private List<DMDIIAwardModel> awards;

	private List<DMDIIAreaOfExpertiseModel> areasOfExpertise;

	private List<DMDIIMemberContactModel> contacts;

	private List<DMDIIMemberCustomerModel> customers;

	private List<DMDIIMemberFinanceModel> finances;

	private List<DMDIIInstituteInvolvementModel> instituteInvolvement;

	private List<DMDIIRndFocusModel> rndFocus;

	private List<DMDIISkillModel> skills;

	private List<DMDIIMemberUserModel> users;
}
