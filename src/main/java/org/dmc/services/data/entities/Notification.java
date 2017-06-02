package org.dmc.services.data.entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "notification")
@Where(clause = "is_deleted='FALSE'")
@SQLDelete(sql="UPDATE notification SET is_deleted = 'TRUE' WHERE id = ?")
public class Notification extends BaseEntity {

	public enum NotificationType {
		NEW_USER_JOINED_ORGANIZATION,
		USER_REQUESTS_VERIFICATION,
		DOCUMENT_SHARED,
		DOCUMENT_SHARED_WITH_WORKSPACE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "created_for")
	private User createdFor;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	private String message;

	private boolean unread = true;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	@PrePersist
	protected void onCreate() {
		this.created = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getCreatedFor() {
		return createdFor;
	}

	public void setCreatedFor(User createdFor) {
		this.createdFor = createdFor;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
