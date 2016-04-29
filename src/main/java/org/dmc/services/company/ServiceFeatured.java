package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;



@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ServiceFeatured  {
  
  private String id = null;
  private String companyId = null;
  private Integer position = null;
  private String serviceId = null;
  private ServiceFeaturedService service = null;

  
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

  @JsonProperty("companyId")
  public String getCompanyId() {
    return companyId;
  }
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  
  /**
   **/

  @JsonProperty("position")
  public Integer getPosition() {
    return position;
  }
  public void setPosition(Integer position) {
    this.position = position;
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

  @JsonProperty("service")
  public ServiceFeaturedService getService() {
    return service;
  }
  public void setService(ServiceFeaturedService service) {
    this.service = service;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceFeatured serviceFeatured = (ServiceFeatured) o;
    return Objects.equals(id, serviceFeatured.id) &&
        Objects.equals(companyId, serviceFeatured.companyId) &&
        Objects.equals(position, serviceFeatured.position) &&
        Objects.equals(serviceId, serviceFeatured.serviceId) &&
        Objects.equals(service, serviceFeatured.service);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, companyId, position, serviceId, service);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceFeatured {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  companyId: ").append(companyId).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  service: ").append(service).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
