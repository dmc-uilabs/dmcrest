package org.dmc.services.data.entities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="dmdii_quick_link")
public class DMDIIQuickLink extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="text")
	private String text;
	
	@Column(name="link")
	private String link;
	
	@ManyToOne
	@JoinColumn(name="dmdii_document_id")
	private DMDIIDocument doc;
	
	@Column(name="created")
	private Date created;
	
	@Column(name="display_name")
	private String displayName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public DMDIIDocument getDoc() {
		return doc;
	}

	public void setDoc(DMDIIDocument doc) {
		this.doc = doc;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((doc == null) ? 0 : doc.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DMDIIQuickLink other = (DMDIIQuickLink) obj;
		if(id == null) {
			if(other.id != null)
				return false;
		} else if(!id.equals(other.id))
			return false;
		if(text == null) {
			if(other.text != null)
				return false;
		} else if(!text.equals(other.text))
			return false;
		if(link == null) {
			if(other.link != null)
				return false;
		} else if(!link.equals(other.link))
			return false;
		if(doc == null) {
			if(other.doc != null)
				return false;
		} else if(!doc.equals(other.doc))
			return false;
		if(created == null) {
			if(other.created != null)
				return false;
		} else if(!created.equals(other.created))
			return false;
		if(displayName == null) {
			if(other.displayName != null)
				return false;
		} else if(!displayName.equals(other.displayName))
			return false;
		return true;
	}

	
}
