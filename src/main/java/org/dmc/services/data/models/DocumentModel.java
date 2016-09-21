package org.dmc.services.data.models;

import java.util.Date;
import java.util.List;

public class DocumentModel extends BaseModel {

	private String documentName;

	private String documentUrl;
	
	private String parentType;
	
	private Integer parentId;

	private Integer ownerId;

	private List<DocumentTagModel> tags;

	private Date modified;

	private Date expires;

	private Boolean isDeleted;
	
	private Integer docClass;
	
	private Boolean verified;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public List<DocumentTagModel> getTags() {
		return tags;
	}

	public void setTags(List<DocumentTagModel> tags) {
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

	public Integer getDocClass() {
		return docClass;
	}

	public void setDocClass(Integer docClass) {
		this.docClass = docClass;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

}
