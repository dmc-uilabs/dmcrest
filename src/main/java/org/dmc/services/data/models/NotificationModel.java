package org.dmc.services.data.models;

import java.util.Date;

public class NotificationModel extends BaseModel {

	private String type;
	private MiniUserModel createdBy;
	private MiniUserModel createdFor;
	private Date created;
	private String message;
	private boolean unread;
	private boolean isDeleted;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	public boolean isDeleted(){
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted){
		this.isDeleted = isDeleted;
	}
}
