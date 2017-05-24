package org.dmc.services.data.models;

import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.web.validator.AWSLink;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class DocumentModel extends BaseModel {

	private String documentName;

	@AWSLink
	private String documentUrl;

	private DocumentParentType parentType;

	private Integer parentId;

	@NotNull
	private Integer ownerId;

	private String ownerDisplayName;

	private List<DocumentTagModel> tags;

	private Date modified;

	private Date expires;

	private DocumentClass docClass;

	private String accessLevel;

	private List<SimpleUserModel> vips;

	private Integer version;

	private Integer directoryId;

	private Integer baseDocId;

	private Boolean hasVersions;

	private String sha256;

	private Boolean isAccepted;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public DocumentParentType getParentType() {
		return parentType;
	}

	public void setParentType(DocumentParentType parentType) {
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

	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}

	public void setOwnerDisplayName(String ownerDisplayName) {
		this.ownerDisplayName = ownerDisplayName;
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

	public DocumentClass getDocClass() {
		return docClass;
	}

	public void setDocClass(DocumentClass docClass) {
		this.docClass = docClass;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public List<SimpleUserModel> getVips() {
		return vips;
	}

	public void setVips(List<SimpleUserModel> vips) {
		this.vips = vips;
	}

	public Integer getDirectoryId() {
		return directoryId;
	}

	public void setDirectoryId(Integer directoryId) {
		this.directoryId = directoryId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getBaseDocId() {
		return baseDocId;
	}

	public void setBaseDocId(Integer baseDocId) {
		this.baseDocId = baseDocId;
	}

	public Boolean getHasVersions() {
		return hasVersions;
	}

	public void setHasVersions(Boolean hasVersions) {
		this.hasVersions = hasVersions;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public Boolean getIsAccepted() { return isAccepted; }

	public void setIsAccepted(Boolean isAccepted) { this.isAccepted = isAccepted; }
}
