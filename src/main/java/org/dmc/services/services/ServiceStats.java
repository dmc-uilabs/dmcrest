package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceStats  {
  
  private String id = null;
  private String serviceId = null;
  private ServiceStatsItem successRate = null;
  private ServiceStatsItem successfulRuns = null;
  private ServiceStatsItem incompleteRuns = null;
  private ServiceStatsItem unavailableRuns = null;
  private ServiceStatsItem runsByUsers = null;
  private ServiceStatsItem uniqueUsers = null;
  private ServiceStatsItem averageTime = null;

  
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
  @JsonProperty("success_rate")
  public ServiceStatsItem getSuccessRate() {
    return successRate;
  }
  public void setSuccessRate(ServiceStatsItem successRate) {
    this.successRate = successRate;
  }

  
  /**
   **/
  @JsonProperty("successful_runs")
  public ServiceStatsItem getSuccessfulRuns() {
    return successfulRuns;
  }
  public void setSuccessfulRuns(ServiceStatsItem successfulRuns) {
    this.successfulRuns = successfulRuns;
  }

  
  /**
   **/
  @JsonProperty("incomplete_runs")
  public ServiceStatsItem getIncompleteRuns() {
    return incompleteRuns;
  }
  public void setIncompleteRuns(ServiceStatsItem incompleteRuns) {
    this.incompleteRuns = incompleteRuns;
  }

  
  /**
   **/
  @JsonProperty("unavailable_runs")
  public ServiceStatsItem getUnavailableRuns() {
    return unavailableRuns;
  }
  public void setUnavailableRuns(ServiceStatsItem unavailableRuns) {
    this.unavailableRuns = unavailableRuns;
  }

  
  /**
   **/
  @JsonProperty("runs_by_users")
  public ServiceStatsItem getRunsByUsers() {
    return runsByUsers;
  }
  public void setRunsByUsers(ServiceStatsItem runsByUsers) {
    this.runsByUsers = runsByUsers;
  }

  
  /**
   **/
  @JsonProperty("unique_users")
  public ServiceStatsItem getUniqueUsers() {
    return uniqueUsers;
  }
  public void setUniqueUsers(ServiceStatsItem uniqueUsers) {
    this.uniqueUsers = uniqueUsers;
  }

  
  /**
   **/
  @JsonProperty("average_time")
  public ServiceStatsItem getAverageTime() {
    return averageTime;
  }
  public void setAverageTime(ServiceStatsItem averageTime) {
    this.averageTime = averageTime;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceStats serviceStats = (ServiceStats) o;
    return Objects.equals(id, serviceStats.id) &&
        Objects.equals(serviceId, serviceStats.serviceId) &&
        Objects.equals(successRate, serviceStats.successRate) &&
        Objects.equals(successfulRuns, serviceStats.successfulRuns) &&
        Objects.equals(incompleteRuns, serviceStats.incompleteRuns) &&
        Objects.equals(unavailableRuns, serviceStats.unavailableRuns) &&
        Objects.equals(runsByUsers, serviceStats.runsByUsers) &&
        Objects.equals(uniqueUsers, serviceStats.uniqueUsers) &&
        Objects.equals(averageTime, serviceStats.averageTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceId, successRate, successfulRuns, incompleteRuns, unavailableRuns, runsByUsers, uniqueUsers, averageTime);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceStats {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  successRate: ").append(successRate).append("\n");
    sb.append("  successfulRuns: ").append(successfulRuns).append("\n");
    sb.append("  incompleteRuns: ").append(incompleteRuns).append("\n");
    sb.append("  unavailableRuns: ").append(unavailableRuns).append("\n");
    sb.append("  runsByUsers: ").append(runsByUsers).append("\n");
    sb.append("  uniqueUsers: ").append(uniqueUsers).append("\n");
    sb.append("  averageTime: ").append(averageTime).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
