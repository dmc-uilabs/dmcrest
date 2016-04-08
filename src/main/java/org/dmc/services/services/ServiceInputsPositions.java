package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceInputsPositions  {
  
  private String id = null;
  private String serviceId = null;
  private List<ServiceInputPosition> positions = new ArrayList<ServiceInputPosition>();

  
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
  @JsonProperty("positions")
  public List<ServiceInputPosition> getPositions() {
    return positions;
  }
  public void setPositions(List<ServiceInputPosition> positions) {
    this.positions = positions;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceInputsPositions serviceInputsPositions = (ServiceInputsPositions) o;
    return Objects.equals(id, serviceInputsPositions.id) &&
        Objects.equals(serviceId, serviceInputsPositions.serviceId) &&
        Objects.equals(positions, serviceInputsPositions.positions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceId, positions);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceInputsPositions {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  positions: ").append(positions).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
