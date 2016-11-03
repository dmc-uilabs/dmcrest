package org.dmc.services.data.models;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.web.validator.AWSLink;

public class DocumentModel extends BaseModel {

	private String documentName;

	@AWSLink
	private String documentUrl;
	
	private DocumentParentType parentType;
	
	private Integer parentId;

	@NotNull
	private Integer ownerId;

	private List<DocumentTagModel> tags;

	private Date modified;

	private Date expires;
	
	private DocumentClass docClass;
	
	private String accessLevel;
	
	private List<Integer> vipIds;
	
	private Boolean isPublic;
	
	private Integer version;

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

	public List<Integer> getVipIds() {
		return vipIds;
	}

	public void setVips(List<Integer> vipIds) {
		this.vipIds = vipIds;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
