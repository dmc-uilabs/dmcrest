package org.dmc.services.data.models;

public class DMDIITypeModel extends BaseModel {

	private DMDIITypeCategoryModel dmdiiTypeCategory;
	private Integer tier;

	public DMDIITypeCategoryModel getDmdiiTypeCategory() {
		return dmdiiTypeCategory;
	}

	public void setDmdiiTypeCategory(DMDIITypeCategoryModel dmdiiTypeCategory) {
		this.dmdiiTypeCategory = dmdiiTypeCategory;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}


}
