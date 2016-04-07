package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class SharedService  {
  
  private String id = null;
  private String accountId = null;
  private String profileId = null;
  private String serviceId = null;

  
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
    SharedService sharedService = (SharedService) o;
    return Objects.equals(id, sharedService.id) &&
        Objects.equals(accountId, sharedService.accountId) &&
        Objects.equals(profileId, sharedService.profileId) &&
        Objects.equals(serviceId, sharedService.serviceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, profileId, serviceId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SharedService {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  profileId: ").append(profileId).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
