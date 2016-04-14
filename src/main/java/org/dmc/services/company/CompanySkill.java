package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CompanySkill  {
  
  private String id = null;
  private String companyId = null;
  private String name = null;

  
  @JsonProperty("id")
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }


  @JsonProperty("companyId")
  public String getCompanyId() {
    return this.companyId;
  }
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  
  @JsonProperty("name")
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }

}
