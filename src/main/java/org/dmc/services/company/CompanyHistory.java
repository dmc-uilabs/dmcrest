package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CompanyHistory  {
  
  private String id = null;
  private String companyId = null;
  private String title = null;
  private String date = null;
  private String profileId = null;
  private String user = null;
  private String link = null;
  public enum SectionEnum {
     publicName,  mutual
  };
  
  private SectionEnum section = null;
  public enum PeriodEnum {
     today,  week
  };
  
  private PeriodEnum period = null;
  public enum TypeEnum {
     completed,  rated,  added,  worked 
  };
  
  private TypeEnum type = null;

  
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
  @JsonProperty("companyId")
  public String getCompanyId() {
    return companyId;
  }
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
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
  @JsonProperty("profileId")
  public String getProfileId() {
    return profileId;
  }
  public void setProfileId(String profileId) {
    this.profileId = profileId;
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

  
  /**
   **/
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }
  public void setType(TypeEnum type) {
    this.type = type;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanyHistory companyHistory = (CompanyHistory) o;
    return Objects.equals(id, companyHistory.id) &&
        Objects.equals(companyId, companyHistory.companyId) &&
        Objects.equals(title, companyHistory.title) &&
        Objects.equals(date, companyHistory.date) &&
        Objects.equals(profileId, companyHistory.profileId) &&
        Objects.equals(user, companyHistory.user) &&
        Objects.equals(link, companyHistory.link) &&
        Objects.equals(section, companyHistory.section) &&
        Objects.equals(period, companyHistory.period) &&
        Objects.equals(type, companyHistory.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, companyId, title, date, profileId, user, link, section, period, type);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanyHistory {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  companyId: ").append(companyId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  date: ").append(date).append("\n");
    sb.append("  profileId: ").append(profileId).append("\n");
    sb.append("  user: ").append(user).append("\n");
    sb.append("  link: ").append(link).append("\n");
    sb.append("  section: ").append(section).append("\n");
    sb.append("  period: ").append(period).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
