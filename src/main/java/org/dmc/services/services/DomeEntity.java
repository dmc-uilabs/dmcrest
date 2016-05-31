package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class DomeEntity {

	private String type = null;
	private String name = null;
	private List<BigDecimal> path = new ArrayList<BigDecimal>();
	private String domeServer = null;
	private List<DomeEntity> children = new ArrayList<DomeEntity>();
	private BigDecimal dateModified = null;
	private String description = null;
	private String modelId = null;
	private String interfaceId = null;
	private BigDecimal version = null;

	/**
	 **/
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 **/
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 **/
	@JsonProperty("path")
	public List<BigDecimal> getPath() {
		return path;
	}

	public void setPath(List<BigDecimal> path) {
		this.path = path;
	}
	
	/**
	 **/
	@JsonProperty("domeServer")
	public String getDomeServer() {
		return domeServer;
	}

	public void setDomeServer(String domeServer) {
		this.domeServer = domeServer;
	}
	
	/**
	 **/
	@JsonProperty("children")
	public List<DomeEntity> getChildren() {
		return children;
	}

	public void setChildren(List<DomeEntity> children) {
		this.children = children;
	}
	
	/**
	 **/
	@JsonProperty("dateModified")
	public BigDecimal getDateModified() {
		return dateModified;
	}

	public void setDateModified(BigDecimal dateModified) {
		this.dateModified = dateModified;
	}
	
	/**
	 **/
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 **/
	@JsonProperty("modelId")
	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	
	/**
	 **/
	@JsonProperty("interfaceId")
	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 **/
	@JsonProperty("version")
	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DomeEntity domeEntity = (DomeEntity) o;
		return Objects.equals(type, domeEntity.type) && Objects.equals(name, domeEntity.name)
				&& Objects.equals(path, domeEntity.path)
				&& Objects.equals(domeServer, domeEntity.domeServer)
				&& Objects.equals(dateModified, domeEntity.dateModified) && Objects.equals(description, domeEntity.description)
				&& Objects.equals(modelId, domeEntity.modelId) && Objects.equals(version, domeEntity.version) && Objects.equals(interfaceId, domeEntity.interfaceId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, name, path, domeServer, dateModified, description, modelId, interfaceId, version);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeEntity {\n");

		sb.append("  type: ").append(type).append("\n");
		sb.append("  name: ").append(name).append("\n");
		sb.append("  path: ").append(path).append("\n");
		sb.append("  domeServer: ").append(domeServer).append("\n");
		sb.append("  dateModified: ").append(dateModified).append("\n");
		sb.append("  description: ").append(description).append("\n");
		sb.append("  modelId: ").append(modelId).append("\n");
		sb.append("  interfaceId: ").append(interfaceId).append("\n");
		sb.append("  version: ").append(version).append("\n");
		sb.append("  children: \n");
		sb.append(children.toString()).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
	
}
