package org.dmc.services.data.models;

public class DMDIIMemberAutocompleteModel extends BaseModel {

	Integer id;

	String name;

	public DMDIIMemberAutocompleteModel(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
