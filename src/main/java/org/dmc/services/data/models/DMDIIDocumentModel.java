package org.dmc.services.data.models;

import java.util.Date;
import java.util.List;

public class DMDIIDocumentModel extends BaseModel {

	private String documentName;

	private String documentUrl;

	private Integer dmdiiProjectId;

	private Integer ownerId;

	private List<DMDIIDocumentTagModel> tags;

	private Date modified;

	private Date expires;

	private Boolean isDeleted;

	private Integer fileType;

	private Boolean verified;

	private String accessLevel;

	private Integer version;

	private String sha256;

	public String getSha256(){
		return sha256;
	}

	public void setSha256(String sha256){
		this.sha256 = sha256;
	}


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

	public List<DMDIIDocumentTagModel> getTags() {
		return tags;
	}

	public void setTags(List<DMDIIDocumentTagModel> tags) {
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

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}


}
