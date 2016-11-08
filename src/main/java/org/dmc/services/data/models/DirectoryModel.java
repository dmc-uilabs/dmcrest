package org.dmc.services.data.models;

import java.util.List;

public class DirectoryModel extends BaseModel {

	Integer id;
	String name;
	Integer parent;
	List <DirectoryModel> children;

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

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public List<DirectoryModel> getChildren() {
		return children;
	}

	public void setChildren(List<DirectoryModel> children) {
		this.children = children;
	}

}
