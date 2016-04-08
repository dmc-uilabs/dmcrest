package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceAuthor  {
  
  private String id = null;
  private String serviceId = null;
  private String displayName = null;
  private String jobTitle = null;
  private Boolean follow = null;
  private String avatar = null;
  private String company = null;

  
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
  @JsonProperty("serviceId")
  public String getServiceId() {
    return serviceId;
  }
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  
  /**
   **/
  @JsonProperty("display_name")
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  
  /**
   **/
  @JsonProperty("jobTitle")
  public String getJobTitle() {
    return jobTitle;
  }
  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  
  /**
   **/
  @JsonProperty("follow")
  public Boolean getFollow() {
    return follow;
  }
  public void setFollow(Boolean follow) {
    this.follow = follow;
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
  @JsonProperty("company")
  public String getCompany() {
    return company;
  }
  public void setCompany(String company) {
    this.company = company;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceAuthor serviceAuthor = (ServiceAuthor) o;
    return Objects.equals(id, serviceAuthor.id) &&
        Objects.equals(serviceId, serviceAuthor.serviceId) &&
        Objects.equals(displayName, serviceAuthor.displayName) &&
        Objects.equals(jobTitle, serviceAuthor.jobTitle) &&
        Objects.equals(follow, serviceAuthor.follow) &&
        Objects.equals(avatar, serviceAuthor.avatar) &&
        Objects.equals(company, serviceAuthor.company);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceId, displayName, jobTitle, follow, avatar, company);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceAuthor {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("  jobTitle: ").append(jobTitle).append("\n");
    sb.append("  follow: ").append(follow).append("\n");
    sb.append("  avatar: ").append(avatar).append("\n");
    sb.append("  company: ").append(company).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
