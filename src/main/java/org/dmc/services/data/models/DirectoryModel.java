package org.dmc.services.data.models;

public class DirectoryModel extends BaseModel {

	Integer id;
	String name;
	String fullPath;
	DirectoryModel parent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public DirectoryModel getParent() {
		return parent;
	}

	public void setParent(DirectoryModel parent) {
		this.parent = parent;
	}

}
