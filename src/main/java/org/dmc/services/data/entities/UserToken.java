package org.dmc.services.data.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_token")
public class UserToken extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "issued")
	private Date dateIssued;

	@Column(name = "token")
	private String token;

	@Column(name = "attempts_made")
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
