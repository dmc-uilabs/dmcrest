package org.dmc.services.data.entities;

import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;

import org.dmc.services.ParentDocumentService;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "document")
@Where(clause = "is_deleted='false'")
public class Document extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String documentName;

	@Column(name = "url")
	private String documentUrl;

	@Column(name = "parent_type")
	@Enumerated(EnumType.STRING)
	private DocumentParentType parentType;

	@Column(name = "parent_id")
	private Integer parentId;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "document_tag_join",
			joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "document_tag_id", referencedColumnName = "id"))
	private List<DocumentTag> tags;

	@Column(name = "modified")
	private Timestamp modified;

	@Column(name = "expires")
	private Timestamp expires;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;
	
	@Column(name = "resource_type_id")
	private Integer resourceTypeId;
	
	@Column(name = "doc_class")
	@Enumerated(EnumType.STRING)
	private DocumentClass docClass;

	@Column(name = "verified")
	private Boolean verified = false;
	
	@ManyToMany
	@JoinTable(name = "resource_in_resource_group",
				joinColumns = @JoinColumn(name = "resource_id"),
				inverseJoinColumns = @JoinColumn(name = "resource_group_id"))
	@Where(clause = "resource_type_id = 1")
	@JsonIgnore
	private List<ResourceGroup> resourceGroups;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<DocumentTag> getTags() {
		return tags;
	}

	public void setTags(List<DocumentTag> tags) {
		this.tags = tags;
	}

	public Timestamp getModified() {
		return modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public Timestamp getExpires() {
		return expires;
	}

	public void setExpires(Timestamp expires) {
		this.expires = expires;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Integer resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public DocumentClass getDocClass() {
		return docClass;
	}

	public void setDocClass(DocumentClass docClass) {
		this.docClass = docClass;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public List<ResourceGroup> getResourceGroups() {
		return resourceGroups;
	}

	public void setResourceGroups(List<ResourceGroup> resourceGroups) {
		this.resourceGroups = resourceGroups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docClass == null) ? 0 : docClass.hashCode());
		result = prime * result + ((documentName == null) ? 0 : documentName.hashCode());
		result = prime * result + ((documentUrl == null) ? 0 : documentUrl.hashCode());
		result = prime * result + ((expires == null) ? 0 : expires.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result + ((parentType == null) ? 0 : parentType.hashCode());
		result = prime * result + ((resourceGroups == null) ? 0 : resourceGroups.hashCode());
		result = prime * result + ((resourceTypeId == null) ? 0 : resourceTypeId.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((verified == null) ? 0 : verified.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (docClass != other.docClass)
			return false;
		if (documentName == null) {
			if (other.documentName != null)
				return false;
		} else if (!documentName.equals(other.documentName))
			return false;
		if (documentUrl == null) {
			if (other.documentUrl != null)
				return false;
		} else if (!documentUrl.equals(other.documentUrl))
			return false;
		if (expires == null) {
			if (other.expires != null)
				return false;
		} else if (!expires.equals(other.expires))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isDeleted == null) {
			if (other.isDeleted != null)
				return false;
		} else if (!isDeleted.equals(other.isDeleted))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (parentType != other.parentType)
			return false;
		if (resourceGroups == null) {
			if (other.resourceGroups != null)
				return false;
		} else if (!resourceGroups.equals(other.resourceGroups))
			return false;
		if (resourceTypeId == null) {
			if (other.resourceTypeId != null)
				return false;
		} else if (!resourceTypeId.equals(other.resourceTypeId))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (verified == null) {
			if (other.verified != null)
				return false;
		} else if (!verified.equals(other.verified))
			return false;
		return true;
	}


}
