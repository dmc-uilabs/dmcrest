package org.dmc.services.discussions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class Discussion  {
  
  private String id = null;
  private String title = null;
  private String message = null;
  private String createdBy = null;
  private BigDecimal createdAt = null;
  private String accountId = null;
  private String projectId = null;

  public Discussion() {
      this.id = new String();
      this.title = new String();
      this.message = new String();
      this.createdBy = new String();
      this.createdAt = new java.math.BigDecimal("11120000");
      this.accountId = new String();
      this.projectId = new String();
  }
  
  public Discussion(DiscussionBuilder builder) {
	this.id = builder.id;
	this.title = builder.title;
	this.message = builder.message;
	this.createdBy = builder.createdBy;
	this.createdAt = builder.createdAt;
	this.accountId = builder.accountId;
	this.projectId = builder.projectId;
  }
  
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  @JsonProperty("message")
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  
  @JsonProperty("created_by")
  public String getCreatedBy() {
    return createdBy;
  }
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonProperty("created_at")
  public BigDecimal getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(BigDecimal createdAt) {
    this.createdAt = createdAt;
  }
  
  @JsonProperty("accountId")
  public String getAccountId() {
    return accountId;
  }
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  @JsonProperty("projectId")
  public String getProjectId() {
    return projectId;
  }
  public void setProjectId(String projectId) {
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
    Discussion discussion = (Discussion) o;
    return Objects.equals(id, discussion.id) &&
        Objects.equals(title, discussion.title) &&
        Objects.equals(message, discussion.message) &&
        Objects.equals(createdBy, discussion.createdBy) &&
        Objects.equals(createdAt, discussion.createdAt) &&
        Objects.equals(accountId, discussion.accountId) &&
        Objects.equals(projectId, discussion.projectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, message, createdBy, createdAt, accountId, projectId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndividualDiscussion {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("  createdBy: ").append(createdBy).append("\n");
    sb.append("  createdAt: ").append(createdAt).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  projectId: ").append(projectId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
  
  
  public static class DiscussionBuilder {
      
      private String id = null;
      private String title = null;
      private String message = null;
      private String createdBy = null;
      private BigDecimal createdAt = null;
      private String accountId = null;
      private String projectId = null;

      public DiscussionBuilder (String id) {
          this.id = id;
      }

      public DiscussionBuilder (String id, String title) {
          this.id = id;
          this.title = title;
      }

      public  DiscussionBuilder message (String message) {
          this.message = message;
          return this;
      }

      public DiscussionBuilder createdBy (String createdBy) {
          this.createdBy = createdBy;
          return this;
      }
      
      public DiscussionBuilder createdAt (BigDecimal createdAt) {
          this.createdAt = createdAt;
          return this;
      }
      
      public DiscussionBuilder accountId (String accountId) {
          this.accountId = accountId;
          return this;
      }
      
      public DiscussionBuilder projectId (String projectId) {
          this.projectId = projectId;
          return this;
      }

      public Discussion build() {
          return new Discussion(this);
      }
  }
}
