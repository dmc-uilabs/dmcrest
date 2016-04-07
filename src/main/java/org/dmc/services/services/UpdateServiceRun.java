package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class UpdateServiceRun  {
  
  private String serviceId = null;
  private String accountId = null;
  private String runBy = null;
  private BigDecimal status = null;
  private BigDecimal percentCompleted = null;
  private String startDate = null;
  private String startTime = null;
  private SimplifiedProject project = null;
  private String stopDate = null;
  private String stopTime = null;
  private ModelInterface _interface = null;

  
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
  @JsonProperty("status")
  public BigDecimal getStatus() {
    return status;
  }
  public void setStatus(BigDecimal status) {
    this.status = status;
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
  public String getStartDate() {
    return startDate;
  }
  public void setStartDate(String startDate) {
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
  @JsonProperty("project")
  public SimplifiedProject getProject() {
    return project;
  }
  public void setProject(SimplifiedProject project) {
    this.project = project;
  }

  
  /**
   **/
  @JsonProperty("stopDate")
  public String getStopDate() {
    return stopDate;
  }
  public void setStopDate(String stopDate) {
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
    UpdateServiceRun updateServiceRun = (UpdateServiceRun) o;
    return Objects.equals(serviceId, updateServiceRun.serviceId) &&
        Objects.equals(accountId, updateServiceRun.accountId) &&
        Objects.equals(runBy, updateServiceRun.runBy) &&
        Objects.equals(status, updateServiceRun.status) &&
        Objects.equals(percentCompleted, updateServiceRun.percentCompleted) &&
        Objects.equals(startDate, updateServiceRun.startDate) &&
        Objects.equals(startTime, updateServiceRun.startTime) &&
        Objects.equals(project, updateServiceRun.project) &&
        Objects.equals(stopDate, updateServiceRun.stopDate) &&
        Objects.equals(stopTime, updateServiceRun.stopTime) &&
        Objects.equals(_interface, updateServiceRun._interface);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceId, accountId, runBy, status, percentCompleted, startDate, startTime, project, stopDate, stopTime, _interface);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateServiceRun {\n");
    
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  runBy: ").append(runBy).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("  percentCompleted: ").append(percentCompleted).append("\n");
    sb.append("  startDate: ").append(startDate).append("\n");
    sb.append("  startTime: ").append(startTime).append("\n");
    sb.append("  project: ").append(project).append("\n");
    sb.append("  stopDate: ").append(stopDate).append("\n");
    sb.append("  stopTime: ").append(stopTime).append("\n");
    sb.append("  _interface: ").append(_interface).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
