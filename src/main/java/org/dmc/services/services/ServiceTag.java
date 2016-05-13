package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Objects;

import org.dmc.services.services.Service.ServiceBuilder;
import org.dmc.services.sharedattributes.FeatureImage;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceTag  {

  private String id = null;
  private String serviceId = null;
  private String name = null;

  private ServiceTag(ServiceTagBuilder builder) {
      this.id = builder.id;
      this.serviceId = builder.serviceId;
      this.name = builder.name;
  }

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
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceTag serviceTag = (ServiceTag) o;
    return Objects.equals(id, serviceTag.id) &&
        Objects.equals(serviceId, serviceTag.serviceId) &&
        Objects.equals(name, serviceTag.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceId, name);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceTag {\n");

    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }

  //Service Tag Builder
  public static class ServiceTagBuilder {

		private final String id;
    	private final String serviceId;
    	private final String name;

  	public ServiceTagBuilder(String id, String serviceId, String name) {
  		this.id = id;
  		this.serviceId = serviceId;
  		this.name = name;
  	}

  	public ServiceTag build() {
  		return new ServiceTag(this);
  	}
  }
}
