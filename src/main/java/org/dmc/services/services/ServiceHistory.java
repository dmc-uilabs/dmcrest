package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceHistory  {
  
  private String id = null;
  private String serviceId = null;
  private String title = null;
  private String date = null;
  private String user = null;
  private String link = null;
  public enum SectionEnum {
     project,  marketplace
  };
  
  private SectionEnum section = null;
  public enum PeriodEnum {
     today,  week, month, year
  };
  
  private PeriodEnum period = null;

  
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
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   **/
  @JsonProperty("date")
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }

  
  /**
   **/
  @JsonProperty("user")
  public String getUser() {
    return user;
  }
  public void setUser(String user) {
    this.user = user;
  }

  
  /**
   **/
  @JsonProperty("link")
  public String getLink() {
    return link;
  }
  public void setLink(String link) {
    this.link = link;
  }

  
  /**
   **/
  @JsonProperty("section")
  public SectionEnum getSection() {
    return section;
  }
  public void setSection(SectionEnum section) {
    this.section = section;
  }

  
  /**
   **/
  @JsonProperty("period")
  public PeriodEnum getPeriod() {
    return period;
  }
  public void setPeriod(PeriodEnum period) {
    this.period = period;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceHistory serviceHistory = (ServiceHistory) o;
    return Objects.equals(id, serviceHistory.id) &&
        Objects.equals(serviceId, serviceHistory.serviceId) &&
        Objects.equals(title, serviceHistory.title) &&
        Objects.equals(date, serviceHistory.date) &&
        Objects.equals(user, serviceHistory.user) &&
        Objects.equals(link, serviceHistory.link) &&
        Objects.equals(section, serviceHistory.section) &&
        Objects.equals(period, serviceHistory.period);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceId, title, date, user, link, section, period);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceHistory {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  serviceId: ").append(serviceId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  date: ").append(date).append("\n");
    sb.append("  user: ").append(user).append("\n");
    sb.append("  link: ").append(link).append("\n");
    sb.append("  section: ").append(section).append("\n");
    sb.append("  period: ").append(period).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
