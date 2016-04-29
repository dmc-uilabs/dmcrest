package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;


import java.util.Objects;

import org.dmc.services.services.ModelInterface;
import org.dmc.services.services.SimplifiedProject;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CurrentStatus  {
  
  private String id = null;
  private BigDecimal status = null;
  private String accountId = null;
  private String runBy = null;
  private String serviceId = null;
  private BigDecimal percentCompleted = null;
  private Date startDate = null;
  private String startTime = null;
  private Date stopDate = null;
  private String stopTime = null;
  private SimplifiedProject project = null;
  private ModelInterface _interface = null;

  
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
  @JsonProperty("status")
  public BigDecimal getStatus() {
    return status;
  }
  public void setStatus(BigDecimal status) {
    this.status = status;
  }

  
  /**
   **/
  @JsonProperty("accountId")
  public String getAccountId() {
    return accountId;
  }
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  
  /**
   **/
  @JsonProperty("runBy")
  public String getRunBy() {
    return runBy;
  }
  public void setRunBy(String runBy) {
    this.runBy = runBy;
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
  @JsonProperty("percentCompleted")
  public BigDecimal getPercentCompleted() {
    return percentCompleted;
  }
  public void setPercentCompleted(BigDecimal percentCompleted) {
    this.percentCompleted = percentCompleted;
  }

  
  /**
   **/
  @JsonProperty("startDate")
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  
  /**
   **/
  @JsonProperty("startTime")
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  
  /**
   **/
  @JsonProperty("stopDate")
  public Date getStopDate() {
    return stopDate;
  }
  public void setStopDate(Date stopDate) {
    this.stopDate = stopDate;
  }

  
  /**
   **/
  @JsonProperty("stopTime")
  public String getStopTime() {
    return stopTime;
  }
  public void setStopTime(String stopTime) {
    this.stopTime = stopTime;
  }

  
  /**
   **/
  @JsonProperty("project")
  public SimplifiedProject getProject() {
    return project;
  }
  public void setProject(SimplifiedProject project) {
    this.project = project;
  }

  
  /**
   **/
  @JsonProperty("interface")
  public ModelInterface getInterface() {
    return _interface;
  }
  public void setInterface(ModelInterface _interface) {
    this._interface = _interface;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrentStatus currentStatus = (CurrentStatus) o;
    return Objects.equals(id, currentStatus.id) &&
        Objects.equals(status, currentStatus.status) &&
        Objects.equals(accountId, currentStatus.accountId) &&
        Objects.equals(runBy, currentStatus.runBy) &&
        Objects.equals(serviceId, currentStatus.serviceId) &&
        Objects.equals(percentCompleted, currentStatus.percentCompleted) &&
        Objects.equals(startDate, currentStatus.startDate) &&
        Objects.equals(startTime, currentStatus.startTime) &&
        Objects.equals(stopDate, currentStatus.stopDate) &&
        Objects.equals(stopTime, currentStatus.stopTime) &&
        Objects.equals(project, currentStatus.project) &&
        Objects.equals(_interface, currentStatus._interface);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, status, accountId, runBy, serviceId, percentCompleted, startDate, startTime, stopDate, stopTime, project, _interface);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrentStatus {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  runBy: ").append(runBy).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  percentCompleted: ").append(percentCompleted).append("\n");
    sb.append("  startDate: ").append(startDate).append("\n");
    sb.append("  startTime: ").append(startTime).append("\n");
    sb.append("  stopDate: ").append(stopDate).append("\n");
    sb.append("  stopTime: ").append(stopTime).append("\n");
    sb.append("  project: ").append(project).append("\n");
    sb.append("  _interface: ").append(_interface).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
