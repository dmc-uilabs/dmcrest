package org.dmc.services.data.models;

import java.util.Date;

public class NotificationModel extends BaseModel {

	private String type;
<<<<<<< HEAD
	private MiniUserModel createdBy;
	private MiniUserModel createdFor;
=======
	private UserModel createdBy;
	private UserModel createdFor;
>>>>>>> Add base for new notification system
	private Date created;
	private String message;
	private boolean unread;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
<<<<<<< HEAD
	public MiniUserModel getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(MiniUserModel createdBy) {
		this.createdBy = createdBy;
	}
	public MiniUserModel getCreatedFor() {
		return createdFor;
	}
	public void setCreatedFor(MiniUserModel createdFor) {
=======
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
>>>>>>> Add base for new notification system
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
