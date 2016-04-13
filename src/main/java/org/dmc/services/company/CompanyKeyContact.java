package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CompanyKeyContact  {
  
  private String id = null;
  private String companyId = null;
  private int type = -1;
  private String name = null;
  private String phoneNumber = null;
  private String title = null;
  private String email = null;

  /**
   **/
  @JsonProperty("id")
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   **/
  @JsonProperty("companyId")
  public String getCompanyId() {
    return this.companyId;
  }
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  
  /**
   **/
  @JsonProperty("name")
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
  
  
  @JsonProperty("type")
  public int getType() {
	return type;
  }
  public void setType(int type) {
	this.type = type;
  }
  
  @JsonProperty("phoneNumber")
  public String getPhoneNumber() {
	return phoneNumber;
  }
  public void setPhoneNumber(String phoneNumber) {
	  this.phoneNumber = phoneNumber;
  }
  
  @JsonProperty("title")
  public String getTitle() {
	  return title;
  }
  public void setTitle(String title) {
	this.title = title;
  }
  
  @JsonProperty("email")
  public String getEmail() {
	return email;
  }
  public void setEmail(String email) {
	this.email = email;
  }
}
