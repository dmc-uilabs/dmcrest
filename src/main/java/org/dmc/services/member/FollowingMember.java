package org.dmc.services.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FollowingMember  {
  
  private String id = "";
  private String follower = "";
  private String followed = "";


  /**
   **/
  @JsonProperty("id")
  public String getId() {
    fixId();
    return id;
  }
  public void setId(String id) {
    this.id = id;
    String[] parts = id.split("-");
    if (parts.length != 2) {
        throw new DMCServiceException(DMCError.IncorrectType, "invalid id for FollowingMember: " + id);
    }
    this.follower = parts[0];
    this.followed = parts[1];
  }

  
  /**
   **/
  @JsonProperty("accountId")
  public String getFollower() {
    return follower;
  }
  public void setFollower(String accountId) {
    this.follower = accountId;
    fixId();
  }

  
  /**
   **/
  @JsonProperty("profileId")
  public String getFollowed() {
    return followed;
  }
  public void setFollowed(String profileId) {
    this.followed = profileId;
    fixId();
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FollowingMember followingMember = (FollowingMember) o;
    return Objects.equals(id, followingMember.id) &&
        Objects.equals(follower, followingMember.follower) &&
        Objects.equals(followed, followingMember.followed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, follower, followed);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FollowingMember {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  follower: ").append(follower).append("\n");
    sb.append("  followed: ").append(followed).append("\n");
    sb.append("}\n");
    return sb.toString();
  }

  private void fixId()
  {
      id = follower + "-" + followed;
  }
}
