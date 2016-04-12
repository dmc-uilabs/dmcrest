package org.dmc.services.discussions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-04T18:05:28.094Z")
public class FollowingIndividualDiscussion  {
  
  private String id = null;
  private String accountId = null;
  private String individualDiscussionId = null;

  
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
  @JsonProperty("individual-discussionId")
  public String getIndividualDiscussionId() {
    return individualDiscussionId;
  }
  public void setIndividualDiscussionId(String individualDiscussionId) {
    this.individualDiscussionId = individualDiscussionId;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FollowingIndividualDiscussion followingIndividualDiscussion = (FollowingIndividualDiscussion) o;
    return Objects.equals(id, followingIndividualDiscussion.id) &&
        Objects.equals(accountId, followingIndividualDiscussion.accountId) &&
        Objects.equals(individualDiscussionId, followingIndividualDiscussion.individualDiscussionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, individualDiscussionId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FollowingIndividualDiscussion {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  individualDiscussionId: ").append(individualDiscussionId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
