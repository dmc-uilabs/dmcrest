package org.dmc.services.data.models;

import java.sql.Date;

public class DMDIIInstituteInvolvementModel extends BaseModel {

	private String staticLineItem;
	private Date date;
	private UserModel user;
	
	public String getStaticLineItem() {
		return staticLineItem;
	}
	public void setStaticLineItem(String staticLineItem) {
		this.staticLineItem = staticLineItem;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
}
