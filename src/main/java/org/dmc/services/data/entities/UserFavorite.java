package org.dmc.services.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserFavorite {

	private Integer userId;

	private Integer contentId;

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
