package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceSpecifications  {
  
  private String id = null;
  private String serviceId = null;
  private String description = null;
  private BigDecimal input = null;
  private BigDecimal output = null;
  private List<ServiceSpecialSpecifications> special = new ArrayList<ServiceSpecialSpecifications>();
  private UsageStats usageStats = null;
  private RunStats runStats = null;

  
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
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   **/
  @JsonProperty("input")
  public BigDecimal getInput() {
    return input;
  }
  public void setInput(BigDecimal input) {
    this.input = input;
  }

  
  /**
   **/
  @JsonProperty("output")
  public BigDecimal getOutput() {
    return output;
  }
  public void setOutput(BigDecimal output) {
    this.output = output;
  }

  
  /**
   **/
  @JsonProperty("special")
  public List<ServiceSpecialSpecifications> getSpecial() {
    return special;
  }
  public void setSpecial(List<ServiceSpecialSpecifications> special) {
    this.special = special;
  }

  
  /**
   **/
  @JsonProperty("usageStats")
  public UsageStats getUsageStats() {
    return usageStats;
  }
  public void setUsageStats(UsageStats usageStats) {
    this.usageStats = usageStats;
  }

  
  /**
   **/
  @JsonProperty("runStats")
  public RunStats getRunStats() {
    return runStats;
  }
  public void setRunStats(RunStats runStats) {
    this.runStats = runStats;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceSpecifications serviceSpecifications = (ServiceSpecifications) o;
    return Objects.equals(id, serviceSpecifications.id) &&
        Objects.equals(serviceId, serviceSpecifications.serviceId) &&
        Objects.equals(description, serviceSpecifications.description) &&
        Objects.equals(input, serviceSpecifications.input) &&
        Objects.equals(output, serviceSpecifications.output) &&
        Objects.equals(special, serviceSpecifications.special) &&
        Objects.equals(usageStats, serviceSpecifications.usageStats) &&
        Objects.equals(runStats, serviceSpecifications.runStats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceId, description, input, output, special, usageStats, runStats);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceSpecifications {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  input: ").append(input).append("\n");
    sb.append("  output: ").append(output).append("\n");
    sb.append("  special: ").append(special).append("\n");
    sb.append("  usageStats: ").append(usageStats).append("\n");
    sb.append("  runStats: ").append(runStats).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
