package org.dmc.services.member;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FollowingMemeber  {
  
  private String accountId = null;
  private String profileId = null;

  
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FollowingMemeber followingMemeber = (FollowingMemeber) o;
    return Objects.equals(accountId, followingMemeber.accountId) &&
        Objects.equals(profileId, followingMemeber.profileId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, profileId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FollowingMemeber {\n");
    
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  profileId: ").append(profileId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
