package org.dmc.services.data.models;

import java.util.Date;
import java.util.List;

public class DMDIIDocumentModel extends BaseModel {

	private String documentName;

	private String documentUrl;

	private String path;

	private Integer dmdiiProjectId;

	private Integer userId;

	private List<DMDIIDocumentModel> tags;

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

	public String getPath() {
		return path;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
}
