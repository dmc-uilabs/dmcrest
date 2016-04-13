package org.dmc.services.announcement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CommunityAnnouncemntComment  {
  
  private String id = null;
  private String accountId = null;
  private String announcementId = null;
  private String text = null;
  private String createdAt = null;

  
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
  @JsonProperty("accountId")
  public String getAccountId() {
    return accountId;
  }
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  
  /**
   **/
  @JsonProperty("announcementId")
  public String getAnnouncementId() {
    return announcementId;
  }
  public void setAnnouncementId(String announcementId) {
    this.announcementId = announcementId;
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
  public String getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommunityAnnouncemntComment communityAnnouncemntComment = (CommunityAnnouncemntComment) o;
    return Objects.equals(id, communityAnnouncemntComment.id) &&
        Objects.equals(accountId, communityAnnouncemntComment.accountId) &&
        Objects.equals(announcementId, communityAnnouncemntComment.announcementId) &&
        Objects.equals(text, communityAnnouncemntComment.text) &&
        Objects.equals(createdAt, communityAnnouncemntComment.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, announcementId, text, createdAt);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommunityAnnouncemntComment {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  announcementId: ").append(announcementId).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  createdAt: ").append(createdAt).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
