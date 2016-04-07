package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceSpecialSpecifications  {
  
  private String specification = null;
  private String data = null;
  private String specificationId = null;

  
  /**
   **/
  @JsonProperty("specification")
  public String getSpecification() {
    return specification;
  }
  public void setSpecification(String specification) {
    this.specification = specification;
  }

  
  /**
   **/
  @JsonProperty("data")
  public String getData() {
    return data;
  }
  public void setData(String data) {
    this.data = data;
  }

  
  /**
   **/
  @JsonProperty("specificationId")
  public String getSpecificationId() {
    return specificationId;
  }
  public void setSpecificationId(String specificationId) {
    this.specificationId = specificationId;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceSpecialSpecifications serviceSpecialSpecifications = (ServiceSpecialSpecifications) o;
    return Objects.equals(specification, serviceSpecialSpecifications.specification) &&
        Objects.equals(data, serviceSpecialSpecifications.data) &&
        Objects.equals(specificationId, serviceSpecialSpecifications.specificationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(specification, data, specificationId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceSpecialSpecifications {\n");
    
    sb.append("  specification: ").append(specification).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  specificationId: ").append(specificationId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
