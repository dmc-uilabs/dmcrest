package org.dmc.services.discussions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionComment  {
  
  private String id = null;
  private String individualDiscussionId = null;
  private String accountId = null;
  private String fullName = null;
  private String avatar = null;
  private String text = null;
  private BigDecimal createdAt = null;
  private Boolean reply = null;
  private String commentId = null;
  private BigDecimal like = null;
  private BigDecimal dislike = null;
  private BigDecimal projectId = null;

  
  /**
   **/
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   **/
  @JsonProperty("individual-discussionId")
  public String getIndividualDiscussionId() {
    return individualDiscussionId;
  }
  public void setIndividualDiscussionId(String individualDiscussionId) {
    this.individualDiscussionId = individualDiscussionId;
  }

  
  /**
   **/
  @JsonProperty("accountId")
  public String getAccountId() {
    return accountId;
  }
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  
  /**
   **/
  @JsonProperty("full_name")
  public String getFullName() {
    return fullName;
  }
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  
  /**
   **/
  @JsonProperty("avatar")
  public String getAvatar() {
    return avatar;
  }
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  
  /**
   **/
  @JsonProperty("text")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

  
  /**
   **/
  @JsonProperty("created_at")
  public BigDecimal getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(BigDecimal createdAt) {
    this.createdAt = createdAt;
  }

  
  /**
   **/
  @JsonProperty("reply")
  public Boolean getReply() {
    return reply;
  }
  public void setReply(Boolean reply) {
    this.reply = reply;
  }

  
  /**
   **/
  @JsonProperty("commentId")
  public String getCommentId() {
    return commentId;
  }
  public void setCommentId(String commentId) {
    this.commentId = commentId;
  }

  
  /**
   **/
  @JsonProperty("like")
  public BigDecimal getLike() {
    return like;
  }
  public void setLike(BigDecimal like) {
    this.like = like;
  }

  
  /**
   **/
  @JsonProperty("dislike")
  public BigDecimal getDislike() {
    return dislike;
  }
  public void setDislike(BigDecimal dislike) {
    this.dislike = dislike;
  }

  
  /**
   **/
  @JsonProperty("projectId")
  public BigDecimal getProjectId() {
    return projectId;
  }
  public void setProjectId(BigDecimal projectId) {
    this.projectId = projectId;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndividualDiscussionComment individualDiscussionComment = (IndividualDiscussionComment) o;
    return Objects.equals(id, individualDiscussionComment.id) &&
        Objects.equals(individualDiscussionId, individualDiscussionComment.individualDiscussionId) &&
        Objects.equals(accountId, individualDiscussionComment.accountId) &&
        Objects.equals(fullName, individualDiscussionComment.fullName) &&
        Objects.equals(avatar, individualDiscussionComment.avatar) &&
        Objects.equals(text, individualDiscussionComment.text) &&
        Objects.equals(createdAt, individualDiscussionComment.createdAt) &&
        Objects.equals(reply, individualDiscussionComment.reply) &&
        Objects.equals(commentId, individualDiscussionComment.commentId) &&
        Objects.equals(like, individualDiscussionComment.like) &&
        Objects.equals(dislike, individualDiscussionComment.dislike) &&
        Objects.equals(projectId, individualDiscussionComment.projectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, individualDiscussionId, accountId, fullName, avatar, text, createdAt, reply, commentId, like, dislike, projectId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndividualDiscussionComment {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  individualDiscussionId: ").append(individualDiscussionId).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  fullName: ").append(fullName).append("\n");
    sb.append("  avatar: ").append(avatar).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  createdAt: ").append(createdAt).append("\n");
    sb.append("  reply: ").append(reply).append("\n");
    sb.append("  commentId: ").append(commentId).append("\n");
    sb.append("  like: ").append(like).append("\n");
    sb.append("  dislike: ").append(dislike).append("\n");
    sb.append("  projectId: ").append(projectId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
