package org.dmc.services.data.models;

import java.util.Date;

public class NotificationModel extends BaseModel {

	private String type;
	private UserModel createdBy;
	private UserModel createdFor;
	private Date created;
	private String message;
	private boolean unread;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public UserModel getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserModel createdBy) {
		this.createdBy = createdBy;
	}
	public UserModel getCreatedFor() {
		return createdFor;
	}
	public void setCreatedFor(UserModel createdFor) {
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
	
}
