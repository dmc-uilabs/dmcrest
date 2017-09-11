package org.dmc.services.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

public class UserFavorite {

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "content_id")
	private Integer contentId;

	@Column(name = "content_type")
	private FavoriteContentType contentType;

	@JsonProperty("userId")
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@JsonProperty("contentId")
	public Integer getContentId() {
		return contentId;
	}
	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@JsonProperty("contentType")
	public FavoriteContentType getContentType() {
		return contentType;
	}
	public void setContentType(FavoriteContentType contentType) {
		this.contentType = contentType;
	}

}
