package org.dmc.services.data.models;

import java.io.Serializable;

public class BaseModel implements Serializable {
	
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
