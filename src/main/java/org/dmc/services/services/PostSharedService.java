package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class PostSharedService  {
  
  private String accountId = null;
  private String profileId = null;
  private String serviceId = null;

  
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
  @JsonProperty("profileId")
  public String getProfileId() {
    return profileId;
  }
  public void setProfileId(String profileId) {
    this.profileId = profileId;
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostSharedService postSharedService = (PostSharedService) o;
    return Objects.equals(accountId, postSharedService.accountId) &&
        Objects.equals(profileId, postSharedService.profileId) &&
        Objects.equals(serviceId, postSharedService.serviceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, profileId, serviceId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostSharedService {\n");
    
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  profileId: ").append(profileId).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
