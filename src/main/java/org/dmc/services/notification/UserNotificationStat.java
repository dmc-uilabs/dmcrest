package org.dmc.services.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class UserNotificationStat  {
  
  private String id = null;
  private String name = null;
  private List<String> events = new ArrayList<String>();
  private BigDecimal today = null;
  private BigDecimal week = null;
  private BigDecimal month = null;

  
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
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   **/
  @JsonProperty("events")
  public List<String> getEvents() {
    return events;
  }
  public void setEvents(List<String> events) {
    this.events = events;
  }

  
  /**
   **/
  @JsonProperty("today")
  public BigDecimal getToday() {
    return today;
  }
  public void setToday(BigDecimal today) {
    this.today = today;
  }

  
  /**
   **/
  @JsonProperty("week")
  public BigDecimal getWeek() {
    return week;
  }
  public void setWeek(BigDecimal week) {
    this.week = week;
  }

  
  /**
   **/
  @JsonProperty("month")
  public BigDecimal getMonth() {
    return month;
  }
  public void setMonth(BigDecimal month) {
    this.month = month;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserNotificationStat userNotificationStat = (UserNotificationStat) o;
    return Objects.equals(id, userNotificationStat.id) &&
        Objects.equals(name, userNotificationStat.name) &&
        Objects.equals(events, userNotificationStat.events) &&
        Objects.equals(today, userNotificationStat.today) &&
        Objects.equals(week, userNotificationStat.week) &&
        Objects.equals(month, userNotificationStat.month);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, events, today, week, month);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserNotificationStat {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  events: ").append(events).append("\n");
    sb.append("  today: ").append(today).append("\n");
    sb.append("  week: ").append(week).append("\n");
    sb.append("  month: ").append(month).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
