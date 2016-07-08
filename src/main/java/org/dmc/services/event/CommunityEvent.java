package org.dmc.services.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Date;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CommunityEvent  {
  
  private String id = null;
  private String title = null;
  private String date = null;
  private String startTime = null;
  private String endTime = null;
  private String address = null;
  private String description = null;

  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
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
  @JsonProperty("startTime")
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  
  /**
   **/
  @JsonProperty("endTime")
  public String getEndTime() {
    return endTime;
  }
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  
  /**
   **/
  @JsonProperty("address")
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommunityEvent communityEvent = (CommunityEvent) o;
    return Objects.equals(id, communityEvent.id) &&
        Objects.equals(title, communityEvent.title) &&
        Objects.equals(date, communityEvent.date) &&
        Objects.equals(startTime, communityEvent.startTime) &&
        Objects.equals(endTime, communityEvent.endTime) &&
        Objects.equals(address, communityEvent.address) &&
        Objects.equals(description, communityEvent.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, date, startTime, endTime, address, description);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommunityEvent {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  date: ").append(date).append("\n");
    sb.append("  startTime: ").append(startTime).append("\n");
    sb.append("  endTime: ").append(endTime).append("\n");
    sb.append("  address: ").append(address).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
