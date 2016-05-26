package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class DomeModelEntity extends DomeEntity {

	private BigDecimal dateModified = null;
	private String description = null;
	private String modelId = null;
	private BigDecimal version = null;
	
	
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
		DomeModelEntity domeEntity = (DomeModelEntity) o;
		return Objects.equals(getType(), domeEntity.getType()) && Objects.equals(getName(), domeEntity.getName())
				&& Objects.equals(getPath(), domeEntity.getPath())
				&& Objects.equals(getDomeServer(), domeEntity.getDomeServer())
				&& Objects.equals(dateModified, domeEntity.dateModified) && Objects.equals(description, domeEntity.description)
				&& Objects.equals(modelId, domeEntity.modelId) && Objects.equals(version, domeEntity.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getType(), getName(), getPath(), getDomeServer(), dateModified, description, modelId, version);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeModelEntity {\n");

		sb.append("  type: ").append(getType()).append("\n");
		sb.append("  name: ").append(getName()).append("\n");
		sb.append("  path: ").append(getPath()).append("\n");
		sb.append("  domeServer: ").append(getDomeServer()).append("\n");
		sb.append("  dateModified: ").append(dateModified).append("\n");
		sb.append("  description: ").append(description).append("\n");
		sb.append("  modelId: ").append(modelId).append("\n");
		sb.append("  version: ").append(version).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
