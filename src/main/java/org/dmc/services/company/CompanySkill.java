package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CompanySkill  {
  
  private String id = null;
  private String companyId = null;
  private String name = null;

  
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanySkill companySkill = (CompanySkill) o;
    return Objects.equals(this.id, companySkill.id) &&
        Objects.equals(this.companyId, companySkill.companyId) &&
        Objects.equals(this.name, companySkill.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.companyId, this.name);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanySkill {\n");
    
    sb.append("  id: ").append(this.id).append("\n");
    sb.append("  companyId: ").append(this.companyId).append("\n");
    sb.append("  name: ").append(this.name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
