package org.dmc.services.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-07-22T17:42:57.404Z")
public class RunDomeModelInput {

	private String serviceId = "";
	private Map<String, DomeModelParam> inParams = new HashMap<String, DomeModelParam>();
	private Map<String, DomeModelParam> outParams = new HashMap<String, DomeModelParam>();

	@JsonProperty("serviceId")
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String id) {
		this.serviceId = id;
	}

	@JsonProperty("inParams")
	public Map<String, DomeModelParam> getInParams() {
		return inParams;
	}

	public void setInParams(Map<String, DomeModelParam> inParams) {
		this.inParams = inParams;
	}

	@JsonProperty("outParams")
	public Map<String, DomeModelParam> getOutParams() {
		return outParams;
	}

	public void setOutParams(Map<String, DomeModelParam> outParams) {
		this.outParams = outParams;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RunDomeModelInput response = (RunDomeModelInput) o;
		return (Objects.equals(serviceId, response.getServiceId()) && Objects.equals(inParams, response.inParams) && Objects.equals(outParams, response.outParams));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n").append("serviceId: ").append(serviceId).append("\n");
		sb.append("  inParams: ").append(inParams).append("\n");
		sb.append("  outParams: ").append(outParams).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
