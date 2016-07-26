package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-07-22T17:42:57.404Z")
public class RunDomeModelInput  {
  
  private String serviceId = "";
 
  @JsonProperty("serviceId")
  public String getServiceId() {
    return serviceId;
  }
  public void setServiceId(String id) {
    this.serviceId = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RunDomeModelInput response = (RunDomeModelInput) o;
    return (response.getServiceId()==serviceId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n").append("serviceId: ").append(serviceId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
