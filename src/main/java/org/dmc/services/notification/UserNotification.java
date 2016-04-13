package org.dmc.services.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class UserNotification  {
  
  private String id = null;
  private String image = null;
  public enum PeriodEnum {
     today,  week,  month, 
  };
  
  private PeriodEnum period = null;
  public enum TypeEnum {
     profile,  discussion,  service,  company,  marketplace,  community,  project,  task, 
  };
  
  private TypeEnum type = null;
  public enum EventEnum {
     ACCEPT_INVITATION,  ANNOUNCEMENT_DMC,  ANNOUNCEMENT_SYSTEM,  EVENT_DMC,  FAVORITED_COMPONENT,  FAVORITED_SERVICE,  FOLLOW_COMPANY,  FOLLOW_DISCUSSION,  FOLLOW_USER,  JOIN_PUBLIC_PROJECT,  PROJECT_DUE,  NEW_DISCUSSION,  REJECT_INVITATION,  REPLY_DISCUSSION,  REPLY_REVIEW,  REVIEW_COMPANY,  REVIEW_COMPONENT,  REVIEW_SERVICE,  REVIEW_USER,  SERVICE_ERROR,  SERVICE_FINISH,  SERVICE_SHARED,  SERVICE_RUN,  SUBSCRIBE_SERVICE_TO_YOUR_PROJECT,  SUBSCRIBE_USER_TO_COMPANY,  SUBSCRIBE_YOUR_SERVICE_TO_PROJECT,  TASK_ASSIGN,  TASK_DUE,  TASK_UPDATE,  UPDATE_COMPANY_PROFILE,  UPDATE_COMPANY_STOREFRONT,  UPDATE_PROJECT,  UPDATE_SERVICE,  UPDATE_USER_PROFILE,  UNSUBSCRIBE_SERVICE_TO_YOUR_PROJECT, 
  };
  
  private EventEnum event = null;
  private String date = null;
  private Boolean read = null;
  private Boolean cleared = null;
  private Object linkParams = null;

  
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
  @JsonProperty("image")
  public String getImage() {
    return image;
  }
  public void setImage(String image) {
    this.image = image;
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

  
  /**
   **/
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }
  public void setType(TypeEnum type) {
    this.type = type;
  }

  
  /**
   **/
  @JsonProperty("event")
  public EventEnum getEvent() {
    return event;
  }
  public void setEvent(EventEnum event) {
    this.event = event;
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
  @JsonProperty("read")
  public Boolean getRead() {
    return read;
  }
  public void setRead(Boolean read) {
    this.read = read;
  }

  
  /**
   **/
  @JsonProperty("cleared")
  public Boolean getCleared() {
    return cleared;
  }
  public void setCleared(Boolean cleared) {
    this.cleared = cleared;
  }

  
  /**
   **/
  @JsonProperty("linkParams")
  public Object getLinkParams() {
    return linkParams;
  }
  public void setLinkParams(Object linkParams) {
    this.linkParams = linkParams;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserNotification userNotification = (UserNotification) o;
    return Objects.equals(id, userNotification.id) &&
        Objects.equals(image, userNotification.image) &&
        Objects.equals(period, userNotification.period) &&
        Objects.equals(type, userNotification.type) &&
        Objects.equals(event, userNotification.event) &&
        Objects.equals(date, userNotification.date) &&
        Objects.equals(read, userNotification.read) &&
        Objects.equals(cleared, userNotification.cleared) &&
        Objects.equals(linkParams, userNotification.linkParams);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, image, period, type, event, date, read, cleared, linkParams);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserNotification {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  image: ").append(image).append("\n");
    sb.append("  period: ").append(period).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  event: ").append(event).append("\n");
    sb.append("  date: ").append(date).append("\n");
    sb.append("  read: ").append(read).append("\n");
    sb.append("  cleared: ").append(cleared).append("\n");
    sb.append("  linkParams: ").append(linkParams).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
