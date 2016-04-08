package org.dmc.services.projects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class PostProjectTag  {
  
  private String projectId = null;
  private String name = null;

  
  /**
   **/
  @JsonProperty("projectId")
  public String getProjectId() {
    return projectId;
  }
  public void setProjectId(String projectId) {
    this.projectId = projectId;
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostProjectTag postProjectTag = (PostProjectTag) o;
    return Objects.equals(projectId, postProjectTag.projectId) &&
        Objects.equals(name, postProjectTag.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectId, name);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostProjectTag {\n");
    
    sb.append("  projectId: ").append(projectId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
