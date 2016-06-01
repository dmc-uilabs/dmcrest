package org.dmc.services.models;

import java.io.Serializable;
import java.util.Date;

public class BaseModel implements Serializable {
	
	private Integer id;
	
	private Date updatedTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
