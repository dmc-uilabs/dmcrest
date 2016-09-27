package org.dmc.services.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class PostFollowingMember  {
  
  private String id = null;
  private String accountId = null;
  private String profileId = null;

  
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFollowingMember postFollowingMemeber = (PostFollowingMember) o;
    return Objects.equals(id, postFollowingMemeber.id) &&
        Objects.equals(accountId, postFollowingMemeber.accountId) &&
        Objects.equals(profileId, postFollowingMemeber.profileId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, profileId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostFollowingMemeber {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  profileId: ").append(profileId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
