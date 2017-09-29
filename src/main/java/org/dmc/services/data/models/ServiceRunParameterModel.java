package org.dmc.services.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceRunParameterModel extends BaseModel {
	
	@JsonIgnore
	private Integer id;
	
	@JsonIgnore
	private Integer parameterId;
	
	@JsonIgnore
	private Integer runId;
	
	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public Integer getRunId() {
		return runId;
	}

	public void setRunId(Integer runId) {
		this.runId = runId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
