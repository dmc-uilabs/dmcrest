package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceStatsItem  {
  
  private BigDecimal today = null;
  private BigDecimal week = null;
  private BigDecimal all = null;

  
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
  @JsonProperty("all")
  public BigDecimal getAll() {
    return all;
  }
  public void setAll(BigDecimal all) {
    this.all = all;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceStatsItem serviceStatsItem = (ServiceStatsItem) o;
    return Objects.equals(today, serviceStatsItem.today) &&
        Objects.equals(week, serviceStatsItem.week) &&
        Objects.equals(all, serviceStatsItem.all);
  }

  @Override
  public int hashCode() {
    return Objects.hash(today, week, all);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceStatsItem {\n");
    
    sb.append("  today: ").append(today).append("\n");
    sb.append("  week: ").append(week).append("\n");
    sb.append("  all: ").append(all).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
