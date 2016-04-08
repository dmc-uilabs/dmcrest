package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceDocument  {
  
  private String id = null;
  private String serviceId = null;
  private String serviceDocumentId = null;
  private String owner = null;
  private String ownerId = null;
  private String title = null;
  private String modifed = null;
  private String size = null;
  private String file = null;

  
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
  @JsonProperty("serviceId")
  public String getServiceId() {
    return serviceId;
  }
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  
  /**
   **/
  @JsonProperty("service-documentId")
  public String getServiceDocumentId() {
    return serviceDocumentId;
  }
  public void setServiceDocumentId(String serviceDocumentId) {
    this.serviceDocumentId = serviceDocumentId;
  }

  
  /**
   **/
  @JsonProperty("owner")
  public String getOwner() {
    return owner;
  }
  public void setOwner(String owner) {
    this.owner = owner;
  }

  
  /**
   **/
  @JsonProperty("ownerId")
  public String getOwnerId() {
    return ownerId;
  }
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  
  /**
   **/
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   **/
  @JsonProperty("modifed")
  public String getModifed() {
    return modifed;
  }
  public void setModifed(String modifed) {
    this.modifed = modifed;
  }

  
  /**
   **/
  @JsonProperty("size")
  public String getSize() {
    return size;
  }
  public void setSize(String size) {
    this.size = size;
  }

  
  /**
   **/
  @JsonProperty("file")
  public String getFile() {
    return file;
  }
  public void setFile(String file) {
    this.file = file;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceDocument serviceDocument = (ServiceDocument) o;
    return Objects.equals(id, serviceDocument.id) &&
        Objects.equals(serviceId, serviceDocument.serviceId) &&
        Objects.equals(serviceDocumentId, serviceDocument.serviceDocumentId) &&
        Objects.equals(owner, serviceDocument.owner) &&
        Objects.equals(ownerId, serviceDocument.ownerId) &&
        Objects.equals(title, serviceDocument.title) &&
        Objects.equals(modifed, serviceDocument.modifed) &&
        Objects.equals(size, serviceDocument.size) &&
        Objects.equals(file, serviceDocument.file);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceId, serviceDocumentId, owner, ownerId, title, modifed, size, file);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceDocument {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  serviceDocumentId: ").append(serviceDocumentId).append("\n");
    sb.append("  owner: ").append(owner).append("\n");
    sb.append("  ownerId: ").append(ownerId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  modifed: ").append(modifed).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  file: ").append(file).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
