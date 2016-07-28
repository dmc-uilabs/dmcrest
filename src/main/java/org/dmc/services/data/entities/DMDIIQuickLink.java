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

	
}
