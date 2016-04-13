package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class PostServiceInputPosition  {
  
  private String serviceId = null;
  private List<ServiceInputPosition> positions = new ArrayList<ServiceInputPosition>();

  
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
    PostServiceInputPosition postServiceInputPosition = (PostServiceInputPosition) o;
    return Objects.equals(serviceId, postServiceInputPosition.serviceId) &&
        Objects.equals(positions, postServiceInputPosition.positions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceId, positions);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostServiceInputPosition {\n");
    
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  positions: ").append(positions).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
