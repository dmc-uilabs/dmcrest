package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class GetDomeInterface {

	private String id = null;
	private BigDecimal version = null;
	private String modelId = null;
	private String interfaceId = null;
	private String type = null;
	private String name = null;
	private List<BigDecimal> path = new ArrayList<BigDecimal>();
	private BigDecimal serviceId = null;
	private String domeServer = null;
	private List<DomeModelParam> inParams = new ArrayList<DomeModelParam>();
	private List<DomeModelParam> outParams = new ArrayList<DomeModelParam>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("version")
	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	@JsonProperty("modelId")
	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	@JsonProperty("interfaceId")
	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("path")
	public List<BigDecimal> getPath() {
		return path;
	}

	public void setPath(List<BigDecimal> path) {
		this.path = path;
	}

	@JsonProperty("serviceId")
	public BigDecimal getServiceId() {
		return serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	@JsonProperty("domeServer")
	public String getDomeServer() {
		return domeServer;
	}

	public void setDomeServer(String domeServer) {
		this.domeServer = domeServer;
	}

	@JsonProperty("inParams")
	public List<DomeModelParam> getInParams() {
		return inParams;
	}

	public void setInParams(List<DomeModelParam> inParams) {
		this.inParams = inParams;
	}

	@JsonProperty("outParams")
	public List<DomeModelParam> getOutParams() {
		return outParams;
	}

	public void setOutParams(List<DomeModelParam> outParams) {
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
		GetDomeInterface getDomeInterface = (GetDomeInterface) o;
		return Objects.equals(id, getDomeInterface.id) && Objects.equals(version, getDomeInterface.version) && Objects.equals(modelId, getDomeInterface.modelId)
				&& Objects.equals(interfaceId, getDomeInterface.interfaceId) && Objects.equals(type, getDomeInterface.type) && Objects.equals(name, getDomeInterface.name)
				&& Objects.equals(path, getDomeInterface.path) && Objects.equals(serviceId, getDomeInterface.serviceId) && Objects.equals(domeServer, getDomeInterface.domeServer)
				&& Objects.equals(inParams, getDomeInterface.inParams) && Objects.equals(outParams, getDomeInterface.outParams);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, version, modelId, interfaceId, type, name, path, serviceId, domeServer);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GetDomeInterface {\n");

		sb.append("  id: ").append(id).append("\n");
		sb.append("  version: ").append(version).append("\n");
		sb.append("  modelId: ").append(modelId).append("\n");
		sb.append("  interfaceId: ").append(interfaceId).append("\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  name: ").append(name).append("\n");
		sb.append("  path: ").append(path).append("\n");
		sb.append("  serviceId: ").append(serviceId).append("\n");
		sb.append("  domeServer: ").append(domeServer).append("\n");
		sb.append("  inParams: ").append(inParams).append("\n");
		sb.append("  outParams: ").append(outParams).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
