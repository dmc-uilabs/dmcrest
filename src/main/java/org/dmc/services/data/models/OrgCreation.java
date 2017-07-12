package org.dmc.services.data.models;

public class OrgCreation {
	
	private String stripeToken;
	private OrganizationModel organizationModel;
	
	public String getStripeToken() {
		return stripeToken;
	}
	
	public void setStripeToken(String stripeToken) {
		this.stripeToken = stripeToken;
	}
	
	public OrganizationModel getOrganizationModel() {
		return organizationModel;
	}
	
	public void setOrgModel(OrganizationModel orgModel) {
		this.organizationModel = orgModel;
	}

}
