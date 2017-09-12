package org.dmc.services.data.models;

import java.io.Serializable;

import org.dmc.services.utils.RestViews;

import com.fasterxml.jackson.annotation.JsonView;

public class BaseModel implements Serializable {
	
	@JsonView(RestViews.Public.class)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
