package org.dmc.services.data.models;

public class ServiceInterfaceParameterModel extends BaseModel {
	
	private String type;
	private String name;
	private String unit;
	private String category;
	private String parameterid;
	private String instancename;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getParameterid() {
		return parameterid;
	}
	public void setParameterid(String parameterid) {
		this.parameterid = parameterid;
	}
	public String getInstancename() {
		return instancename;
	}
	public void setInstancename(String instancename) {
		this.instancename = instancename;
	}

}
