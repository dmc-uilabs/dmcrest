package org.dmc.services.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_favorite")
public class UserFavorite {
	
	@JoinColumn(name = "user_id")
	@ManyToOne
	private User user;
	
	@Column(name = "content_id")
	private Integer contentId;
	
	@Column(name = "content_type")
	private FavoriteContentType contentType;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public FavoriteContentType getContentType() {
		return contentType;
	}

	public void setContentType(FavoriteContentType contentType) {
		this.contentType = contentType;
	}
	
}
