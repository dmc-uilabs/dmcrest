package org.dmc.services.data.models;


//Models are the return object of Hiberante. They are what the entities will map back to
//Implement these like regular POJOs
public class ResourceMachineModel extends BaseModel {


//As you can see, there  is no id as in the ResourceMachine entity. This is because of the special bidirectional relationship with bay
//normal relationships normally mirror the variables in the entity class 
	private String title;
	private String image;
	private String description;
	private String dateCreated;
	private String link;
	private String contact;
	private boolean highlighted;
	private ResourceBayModel bay;

	public ResourceBayModel getBay() {
		return bay;
	}
	public void setBay(ResourceBayModel bay) {
		this.bay = bay;
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






}
