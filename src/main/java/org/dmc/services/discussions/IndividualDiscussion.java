package org.dmc.services.discussions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussion  {
  
  private String id = null;
  private String title = null;
  private String createdBy = null;
  private BigDecimal createdAt = null;
  private BigDecimal accountId = null;
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
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   **/
  @JsonProperty("created_by")
  public String getCreatedBy() {
    return createdBy;
  }
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
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
  @JsonProperty("accountId")
  public BigDecimal getAccountId() {
    return accountId;
  }
  public void setAccountId(BigDecimal accountId) {
    this.accountId = accountId;
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
    IndividualDiscussion individualDiscussion = (IndividualDiscussion) o;
    return Objects.equals(id, individualDiscussion.id) &&
        Objects.equals(title, individualDiscussion.title) &&
        Objects.equals(createdBy, individualDiscussion.createdBy) &&
        Objects.equals(createdAt, individualDiscussion.createdAt) &&
        Objects.equals(accountId, individualDiscussion.accountId) &&
        Objects.equals(projectId, individualDiscussion.projectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, createdBy, createdAt, accountId, projectId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndividualDiscussion {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  createdBy: ").append(createdBy).append("\n");
    sb.append("  createdAt: ").append(createdAt).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  projectId: ").append(projectId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
