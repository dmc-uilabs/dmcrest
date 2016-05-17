package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class PostUpdateDomeInterface  {
  
  private Integer version = null;
  private String modelId = null;
  private String interfaceId = null;
  private String type = null;
  private String name = null;
  private List<Integer> path = new ArrayList<Integer>();
  private Integer serviceId = null;
  private String domeServer = null;

  
  /**
   **/
  @JsonProperty("version")
  public Integer getVersion() {
    return version;
  }
  public void setVersion(Integer version) {
    this.version = version;
  }

  
  /**
   **/
  @JsonProperty("modelId")
  public String getModelId() {
    return modelId;
  }
  public void setModelId(String modelId) {
    this.modelId = modelId;
  }

  
  /**
   **/
  @JsonProperty("interfaceId")
  public String getInterfaceId() {
    return interfaceId;
  }
  public void setInterfaceId(String interfaceId) {
    this.interfaceId = interfaceId;
  }

  
  /**
   **/
  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
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
  @JsonProperty("path")
  public List<Integer> getPath() {
    return path;
  }
  public void setPath(List<Integer> path) {
    this.path = path;
  }

  
  /**
   **/
  @JsonProperty("serviceId")
  public Integer getServiceId() {
    return serviceId;
  }
  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  
  /**
   **/
  @JsonProperty("domeServer")
  public String getDomeServer() {
    return domeServer;
  }
  public void setDomeServer(String domeServer) {
    this.domeServer = domeServer;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostUpdateDomeInterface postUpdateDomeInterface = (PostUpdateDomeInterface) o;
    return Objects.equals(version, postUpdateDomeInterface.version) &&
        Objects.equals(modelId, postUpdateDomeInterface.modelId) &&
        Objects.equals(interfaceId, postUpdateDomeInterface.interfaceId) &&
        Objects.equals(type, postUpdateDomeInterface.type) &&
        Objects.equals(name, postUpdateDomeInterface.name) &&
        Objects.equals(path, postUpdateDomeInterface.path) &&
        Objects.equals(serviceId, postUpdateDomeInterface.serviceId) &&
        Objects.equals(domeServer, postUpdateDomeInterface.domeServer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, modelId, interfaceId, type, name, path, serviceId, domeServer);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostUpdateDomeInterface {\n");
    
    sb.append("  version: ").append(version).append("\n");
    sb.append("  modelId: ").append(modelId).append("\n");
    sb.append("  interfaceId: ").append(interfaceId).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  path: ").append(path).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  domeServer: ").append(domeServer).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
