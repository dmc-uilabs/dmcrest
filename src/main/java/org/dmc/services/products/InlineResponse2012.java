package org.dmc.services.products;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class InlineResponse2012  {
  
  private Integer accountId = null;
  private Integer id = null;
  private Integer serviceId = null;

  
  /**
   **/
  @JsonProperty("accountId")
  public Integer getAccountId() {
    return accountId;
  }
  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
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
    InlineResponse2012 inlineResponse2012 = (InlineResponse2012) o;
    return Objects.equals(accountId, inlineResponse2012.accountId) &&
        Objects.equals(id, inlineResponse2012.id) &&
        Objects.equals(serviceId, inlineResponse2012.serviceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, id, serviceId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2012 {\n");
    
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
