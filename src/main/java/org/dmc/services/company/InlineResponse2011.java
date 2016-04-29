package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class InlineResponse2011  {
  
  private Integer companyId = null;
  private Integer id = null;
  private Integer position = null;
  private Integer serviceId = null;

  
  /**
   **/
  @JsonProperty("companyId")
  public Integer getCompanyId() {
    return companyId;
  }
  public void setCompanyId(Integer companyId) {
    this.companyId = companyId;
  }

  
  /**
   **/
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
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
  public Integer getServiceId() {
    return serviceId;
  }
  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2011 inlineResponse2011 = (InlineResponse2011) o;
    return Objects.equals(companyId, inlineResponse2011.companyId) &&
        Objects.equals(id, inlineResponse2011.id) &&
        Objects.equals(position, inlineResponse2011.position) &&
        Objects.equals(serviceId, inlineResponse2011.serviceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(companyId, id, position, serviceId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2011 {\n");
    
    sb.append("  companyId: ").append(companyId).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
