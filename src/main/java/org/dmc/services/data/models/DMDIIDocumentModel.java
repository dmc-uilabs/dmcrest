package org.dmc.services.data.models;

import java.util.Date;

public class DMDIIDocumentModel extends BaseModel {

	private String documentName;
	
	private String documentUrl;
	
	private DMDIIProjectModel dmdiiProject;
	
	private UserModel owner;
	
	private Date modified;
	
	private Date expires;
	
	private Boolean isDeleted;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public DMDIIProjectModel getDmdiiProject() {
		return dmdiiProject;
	}

	public void setDmdiiProject(DMDIIProjectModel dmdiiProject) {
		this.dmdiiProject = dmdiiProject;
	}

	public UserModel getOwner() {
		return owner;
	}

	public void setOwner(UserModel owner) {
		this.owner = owner;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
