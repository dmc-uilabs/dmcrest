package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class PostServiceRun  {
  
  private String serviceId = null;
  private String accountId = null;
  private String runBy = null;
  private BigDecimal status = null;
  private BigDecimal percentComplete = null;
  private String startDate = null;
  private String startTime = null;
  private SimplifiedProject project = null;

  
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
  @JsonProperty("percentComplete")
  public BigDecimal getPercentComplete() {
    return percentComplete;
  }
  public void setPercentComplete(BigDecimal percentComplete) {
    this.percentComplete = percentComplete;
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostServiceRun postServiceRun = (PostServiceRun) o;
    return Objects.equals(serviceId, postServiceRun.serviceId) &&
        Objects.equals(accountId, postServiceRun.accountId) &&
        Objects.equals(runBy, postServiceRun.runBy) &&
        Objects.equals(status, postServiceRun.status) &&
        Objects.equals(percentComplete, postServiceRun.percentComplete) &&
        Objects.equals(startDate, postServiceRun.startDate) &&
        Objects.equals(startTime, postServiceRun.startTime) &&
        Objects.equals(project, postServiceRun.project);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceId, accountId, runBy, status, percentComplete, startDate, startTime, project);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostServiceRun {\n");
    
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  runBy: ").append(runBy).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("  percentComplete: ").append(percentComplete).append("\n");
    sb.append("  startDate: ").append(startDate).append("\n");
    sb.append("  startTime: ").append(startTime).append("\n");
    sb.append("  project: ").append(project).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
