package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class DomeModel {

	private String type = null;
	private String name = null;
	private List<BigDecimal> path = new ArrayList<BigDecimal>();
	private String domeServer = null;
	private String modelId = null;
	private String interfaceId = null;
	private String projectId = null;
	private BigDecimal version = null;


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
	

	@JsonProperty("domeServer")
	public String getDomeServer() {
		return domeServer;
	}

	public void setDomeServer(String domeServer) {
		this.domeServer = domeServer;
	}
	

	@JsonProperty("modelId")
	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}


	@JsonProperty("version")
	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}
	

	@JsonProperty("interfaceId")
	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	

	@JsonProperty("projectId")
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DomeModel domeModel = (DomeModel) o;
		return Objects.equals(type, domeModel.type) && Objects.equals(name, domeModel.name)
				&& Objects.equals(path, domeModel.path)
				&& Objects.equals(domeServer, domeModel.domeServer)
				&& Objects.equals(modelId, domeModel.modelId) && Objects.equals(version, domeModel.version)  && Objects.equals(interfaceId, domeModel.interfaceId)
				&& Objects.equals(projectId, domeModel.projectId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, name, path, domeServer, modelId, interfaceId, projectId, version);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeEntity {\n");

		sb.append("  type: ").append(type).append("\n");
		sb.append("  name: ").append(name).append("\n");
		sb.append("  path: ").append(path).append("\n");
		sb.append("  domeServer: ").append(domeServer).append("\n");
		sb.append("  modelId: ").append(modelId).append("\n");
		sb.append("  version: ").append(version).append("\n");
		sb.append("  interfaceId: ").append(interfaceId).append("\n");
		sb.append("  projectId: ").append(projectId).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
	
}
