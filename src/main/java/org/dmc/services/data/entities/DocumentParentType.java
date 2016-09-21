package org.dmc.services.data.entities;

public enum DocumentParentType {
	DMDII ("dmdii_project"),
	SERVICE ("service"),
	PROJECT ("group"),
	ORGANIZATION ("organization"),
	USER ("users");
	
	private final String targetTable;
	
	DocumentParentType (String targetTable) {
		this.targetTable = targetTable;
	}
	
	public String targetTable() {
		return targetTable;
	}
}
