package org.dmc.services.discussions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionTag  {
  
  private String id = null;
  private String name = null;
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
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
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
    IndividualDiscussionTag individualDiscussionTag = (IndividualDiscussionTag) o;
    return Objects.equals(id, individualDiscussionTag.id) &&
        Objects.equals(name, individualDiscussionTag.name) &&
        Objects.equals(individualDiscussionId, individualDiscussionTag.individualDiscussionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, individualDiscussionId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndividualDiscussionTag {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  individualDiscussionId: ").append(individualDiscussionId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
