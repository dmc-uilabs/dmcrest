package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceInputPosition  {
  
  private String name = null;
  private BigDecimal position = null;

  
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
  @JsonProperty("position")
  public BigDecimal getPosition() {
    return position;
  }
  public void setPosition(BigDecimal position) {
    this.position = position;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceInputPosition serviceInputPosition = (ServiceInputPosition) o;
    return Objects.equals(name, serviceInputPosition.name) &&
        Objects.equals(position, serviceInputPosition.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, position);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceInputPosition {\n");
    
    sb.append("  name: ").append(name).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
