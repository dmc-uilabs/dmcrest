package org.dmc.services.announcement;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CommunityAnnouncemnt  {
  
  private String id = null;
  private String text = null;
  private String createdAt = null;
  private List<CommunityAnnouncemntComment> announcementComments = new ArrayList<CommunityAnnouncemntComment>();

  
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

  
  /**
   **/
  @JsonProperty("announcement_comments")
  public List<CommunityAnnouncemntComment> getAnnouncementComments() {
    return announcementComments;
  }
  public void setAnnouncementComments(List<CommunityAnnouncemntComment> announcementComments) {
    this.announcementComments = announcementComments;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommunityAnnouncemnt communityAnnouncemnt = (CommunityAnnouncemnt) o;
    return Objects.equals(id, communityAnnouncemnt.id) &&
        Objects.equals(text, communityAnnouncemnt.text) &&
        Objects.equals(createdAt, communityAnnouncemnt.createdAt) &&
        Objects.equals(announcementComments, communityAnnouncemnt.announcementComments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, createdAt, announcementComments);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommunityAnnouncemnt {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  createdAt: ").append(createdAt).append("\n");
    sb.append("  announcementComments: ").append(announcementComments).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
