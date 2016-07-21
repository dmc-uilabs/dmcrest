package org.dmc.services.data.models;

import java.util.Date;
import java.util.List;

public class DMDIIDocumentModel extends BaseModel {

	private String documentName;

	private String documentUrl;

	private String path;

	private Integer dmdiiProjectId;

	private Integer ownerId;

	private List<DMDIIDocumentModel> tags;

	private Date modified;

	private Date expires;

	private Boolean isDeleted;
	
	private Integer fileType;

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

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getDmdiiProjectId() {
		return dmdiiProjectId;
	}

	public void setDmdiiProjectId(Integer dmdiiProjectId) {
		this.dmdiiProjectId = dmdiiProjectId;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public List<DMDIIDocumentModel> getTags() {
		return tags;
	}

	public void setTags(List<DMDIIDocumentModel> tags) {
		this.tags = tags;
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

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	
}
