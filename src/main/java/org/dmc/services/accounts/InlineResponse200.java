package org.dmc.services.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import org.dmc.services.components.Component;
import org.dmc.services.services.Service;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-22T14:57:06.776Z")
public class InlineResponse200  {
  
  private Integer accountId = null;
  private Component component = null;
  private Integer componentId = null;
  private Service service = null;
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
  @JsonProperty("component")
  public Component getComponent() {
    return component;
  }
  public void setComponent(Component component) {
    this.component = component;
  }

  
  /**
   **/
  @JsonProperty("componentId")
  public Integer getComponentId() {
    return componentId;
  }
  public void setComponentId(Integer componentId) {
    this.componentId = componentId;
  }

  
  /**
   **/
  @JsonProperty("service")
  public Service getService() {
    return service;
  }
  public void setService(Service service) {
    this.service = service;
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
    InlineResponse200 inlineResponse200 = (InlineResponse200) o;
    return Objects.equals(accountId, inlineResponse200.accountId) &&
        Objects.equals(component, inlineResponse200.component) &&
        Objects.equals(componentId, inlineResponse200.componentId) &&
        Objects.equals(service, inlineResponse200.service) &&
        Objects.equals(id, inlineResponse200.id) &&
        Objects.equals(serviceId, inlineResponse200.serviceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, component, componentId, service, id, serviceId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse200 {\n");
    
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  component: ").append(component).append("\n");
    sb.append("  componentId: ").append(componentId).append("\n");
    sb.append("  service: ").append(service).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
