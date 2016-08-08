package org.dmc.services.data.models;

import java.sql.Date;

public class UserTokenModel extends BaseModel {

	private Integer id;

	private Integer userId;

	private Date dateIssued;

	private String token;

	private Integer attemptsMade;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getAttemptsMade() {
		return attemptsMade;
	}

	public void setAttemptsMade(Integer attemptsMade) {
		this.attemptsMade = attemptsMade;
	}



}
