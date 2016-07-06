package org.dmc.services.data.models;

import java.util.Date;
import java.util.List;

public class ResourceBayModel extends BaseModel {
	
	private Integer id; 
	private String title; 
	private String image; 
	private String description; 
	private String dateCreated; 
	private String link; 
	private String contact; 
	private boolean highlighted;
	//private List<ResourceMachineModel> machines;

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public boolean isHighlighted() {
		return highlighted;
	}
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	/*
	public List<ResourceMachineModel> getMachines() {
		return machines;
	}
	public void setMachines(List<ResourceMachineModel> machines) {
		this.machines = machines;
	} 
	*/

}
