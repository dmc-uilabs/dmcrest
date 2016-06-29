package org.dmc.services.discussions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionCommentHelpful  {
  
  private String id = null;
  private String commentId = null;
  private String accountId = null;
  private Boolean helpful = null;

  
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
  @JsonProperty("commentId")
  public String getCommentId() {
    return commentId;
  }
  public void setCommentId(String commentId) {
    this.commentId = commentId;
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
  @JsonProperty("helpful")
  public Boolean getHelpful() {
    return helpful;
  }
  public void setHelpful(Boolean helpful) {
    this.helpful = helpful;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndividualDiscussionCommentHelpful individualDiscussionCommentHelpful = (IndividualDiscussionCommentHelpful) o;
    return Objects.equals(id, individualDiscussionCommentHelpful.id) &&
        Objects.equals(commentId, individualDiscussionCommentHelpful.commentId) &&
        Objects.equals(accountId, individualDiscussionCommentHelpful.accountId) &&
        Objects.equals(helpful, individualDiscussionCommentHelpful.helpful);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, commentId, accountId, helpful);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndividualDiscussionCommentHelpful {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  commentId: ").append(commentId).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  helpful: ").append(helpful).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
