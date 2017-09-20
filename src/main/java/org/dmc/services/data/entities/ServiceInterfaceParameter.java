package org.dmc.services.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "service_interface_parameter")
public class ServiceInterfaceParameter extends BaseEntity {
	
	@Id
	@Column(name = "parameter_id")
	private Integer id;
	
	@Column(name = "interface_id")
	private Integer interfaceId;
	
	private String name;
	
	private String type;
	
	private String unit;
	
	private String category;
	
	@Column(name = "default_value")
	private String defaultValue;
	
	@Column(name = "parameter_id_txt")
	private String parameterTxtId;
	
	@Column(name = "parameter_position")
	private Integer parameterPosition;
	
	@Column(name = "input_parameter")
	private Boolean inputParameter;
	
	@Column(name = "instancename")
	private String instanceName;
	
	public Integer getId() {
		return id;
	}

	public void setParameterId(Integer parameterId) {
		this.id = id;
	}

	public Integer getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getParameterTxtId() {
		return parameterTxtId;
	}

	public void setParameterTxtId(String parameterTxtId) {
		this.parameterTxtId = parameterTxtId;
	}

	public Integer getParameterPosition() {
		return parameterPosition;
	}

	public void setParameterPosition(Integer parameterPosition) {
		this.parameterPosition = parameterPosition;
	}

	public Boolean isInputParameter() {
		return inputParameter;
	}

	public void setInputParameter(Boolean inputParameter) {
		this.inputParameter = inputParameter;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

}
