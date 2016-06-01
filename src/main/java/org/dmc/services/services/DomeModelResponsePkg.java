package org.dmc.services.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomeModelResponsePkg {
	protected DomeEntity interFace;
	protected Map<String, DomeModelParam> inParams = new HashMap<String, DomeModelParam>();
	protected Map<String, DomeModelParam> outParams = new HashMap<String, DomeModelParam>();
	protected String modelName;
	protected String modelDescription;
	protected DomeServer server;

	@JsonProperty("interFace")
	public DomeEntity getInterface() {
		return this.interFace;
	}

	public void setInterface(DomeEntity interFace) {
		this.interFace = interFace;
	}

	@JsonProperty("inParams")
	public Map<String, DomeModelParam> getInParams() {
		return this.inParams;
	}

	public void setInParams(Map<String, DomeModelParam> inParams) {
		this.inParams = inParams;
	}

	@JsonProperty("outParams")
	public Map<String, DomeModelParam> getOutParams() {
		return this.outParams;
	}

	public void setOutParams(Map<String, DomeModelParam> outParams) {
		this.outParams = outParams;
	}

	@JsonProperty("modelName")
	public String getName() {
		return this.modelName;
	}

	public void setName(String friendlyName) {
		this.modelName = friendlyName;
	}

	@JsonProperty("modelDescription")
	public String getDescription() {
		return this.modelDescription;
	}

	public void setDescription(String description) {
		this.modelDescription = description;
	}

	@JsonProperty("server")
	public DomeServer getServer() {
		return server;
	}

	public void setServer(DomeServer server) {
		this.server = server;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DomeModelResponsePkg domeModelResponsePkg = (DomeModelResponsePkg) o;
		return Objects.equals(interFace, domeModelResponsePkg.interFace)
				&& Objects.equals(inParams, domeModelResponsePkg.inParams)
				&& Objects.equals(outParams, domeModelResponsePkg.outParams)
				&& Objects.equals(modelName, domeModelResponsePkg.modelName)
				&& Objects.equals(modelDescription, domeModelResponsePkg.modelDescription)
				&& Objects.equals(server, domeModelResponsePkg.server);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeModelResponsePkg {\n");

		sb.append("  interFace: ").append(interFace.toString()).append("\n");
		sb.append("  inParams: ").append(inParams.toString()).append("\n");
		sb.append("  outParams: ").append(outParams.toString()).append("\n");
		sb.append("  modelName: ").append(modelName).append("\n");
		sb.append("  modelDescription: ").append(modelDescription).append("\n");
		sb.append("  server: ").append(server.toString()).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

}
