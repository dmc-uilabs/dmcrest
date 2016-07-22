package org.dmc.services.data.models;

public class DMDIIMemberFinanceModel extends BaseModel {

	private UserModel dmdiiOwner;
	private String name;
	private String assetUrl;
	
	public UserModel getDmdiiOwner() {
		return dmdiiOwner;
	}
	public void setDmdiiOwner(UserModel dmdiiOwner) {
		this.dmdiiOwner = dmdiiOwner;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAssetUrl() {
		return assetUrl;
	}
	public void setAssetUrl(String assetUrl) {
		this.assetUrl = assetUrl;
	}
}
