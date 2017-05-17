package org.dmc.services.data.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

@Entity
@Table(name="dmdii_document")
@Where(clause = "is_deleted='false'")
public class DMDIIDocument extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String documentName;

	@Column(name = "url")
	private String documentUrl;

	@Column(name = "sha256")
	private String sha256;


	@ManyToOne
	@JoinColumn(name = "dmdii_project_id")
	private DMDIIProject dmdiiProject;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "dmdii_document_tag_join",
			   joinColumns = @JoinColumn(name="dmdii_document_id"),
			   inverseJoinColumns = @JoinColumn(name="dmdii_document_tag_id"))
	private List<DMDIIDocumentTag> tags;

	@Column(name = "modified")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@Column(name = "expires")
	private Timestamp expires;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@Column(name = "access_level")
	// @Enumerated(EnumType.STRING)
	private String accessLevel;

	@Column(name = "file_type_id")
	private Integer fileType;

	@Column(name = "verified")
	private Boolean verified = false;

	@Column(name = "version")
	private Integer version;


	@Column(name="scan_date")
	private Long scanDate;

	public Long getScanDate() {
		return scanDate;
	}

	public void setScanDate(Long scanDate) {
		this.scanDate = scanDate;
	}


	@Column(name="encryption_type")
	private String encryptionType;

	public String getEncryptionType() {
		return encryptionType;
	}

	public void setEncryptionType(String encryptionType) {
		this.encryptionType = encryptionType;
	}



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


	public String getSha256(){
		return sha256;
	}

	public void setSha256(String sha256){
		this.sha256 = sha256;
	}





	public List<DMDIIDocumentTag> getTags() {
		return tags;
	}

	public void setTags(List<DMDIIDocumentTag> tags) {
		this.tags = tags;
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

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessLevel == null) ? 0 : accessLevel.hashCode());
		result = prime * result + ((documentName == null) ? 0 : documentName.hashCode());
		result = prime * result + ((documentUrl == null) ? 0 : documentUrl.hashCode());
		result = prime * result + ((expires == null) ? 0 : expires.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		// result = prime * result + ((sha256 == null) ? 0 : sha256.hashCode());
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
		DMDIIDocument other = (DMDIIDocument) obj;
		if (accessLevel != other.accessLevel)
			return false;
		if (dmdiiProject == null) {
			if (other.dmdiiProject != null)
				return false;
		} else if (!dmdiiProject.equals(other.dmdiiProject))
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

		// if (sha256 ==null){
		// 	if (other.sha256 !=null)
		// 	return false;
		// } else if (!sha256.equals(other.sha256))
		// 	return false;


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
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

}
