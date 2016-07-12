package org.dmc.services.data.entities;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="dmdii_document")
public class DMDIIDocument extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String documentName;
	
	@Column(name = "url")
	private String documentUrl;
	
	@Column(name = "path")
	private String path;
	
	@ManyToOne
	@JoinColumn(name = "dmdii_project_id")
	private DMDIIProject dmdiiProject;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;
	
	@Column(name = "modified")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;
	
	@Column(name = "expires")
	private Timestamp expires;
	
	@Column(name = "is_deleted")
	private Boolean isDeleted;

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
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public DMDIIProject getDMDIIProject() {
		return dmdiiProject;
	}

	public void setDMDIIProject(DMDIIProject dmdiiProject) {
		this.dmdiiProject = dmdiiProject;
	}

	public DMDIIProject getDmdiiProject() {
		return dmdiiProject;
	}

	public void setDmdiiProject(DMDIIProject dmdiiProject) {
		this.dmdiiProject = dmdiiProject;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((documentName == null) ? 0 : documentName.hashCode());
		result = prime * result + ((documentUrl == null) ? 0 : documentUrl.hashCode());
		return result;
	}
	
	@Override
	public boolean equals (Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		DMDIIDocument other = (DMDIIDocument) obj;
		if(documentName == null) {
			if(other.documentName != null)
				return false;
		} else if (!documentName.equals(other.documentName))
			return false;
		if(id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if(documentUrl == null) {
			if (other.documentUrl != null)
				return false;
		} else if (!documentUrl.equals(other.documentUrl))
			return false;
		
		return true;
	}
}
