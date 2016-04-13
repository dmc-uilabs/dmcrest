package org.dmc.services.projects;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class GetProjectTag  {
  
  private String id = null;
  private String projectId = null;
  private String name = null;

  
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
    GetProjectTag getProjectTag = (GetProjectTag) o;
    return Objects.equals(id, getProjectTag.id) &&
        Objects.equals(projectId, getProjectTag.projectId) &&
        Objects.equals(name, getProjectTag.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, projectId, name);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetProjectTag {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  projectId: ").append(projectId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
