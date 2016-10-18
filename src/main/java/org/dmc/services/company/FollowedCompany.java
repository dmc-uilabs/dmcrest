package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FollowedCompany  {
  
  private Integer accountId = null;
  private Integer companyId = null;
  private Integer id = null;

  
  /**
   **/
  @JsonProperty("accountId")
  public Integer getAccountId() {
    return accountId;
  }
  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  
  /**
   **/
  @JsonProperty("companyId")
  public Integer getCompanyId() {
    return companyId;
  }
  public void setCompanyId(Integer companyId) {
    this.companyId = companyId;
  }

  
  /**
   **/
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FollowedCompany inlineResponse201 = (FollowedCompany) o;
    return Objects.equals(accountId, inlineResponse201.accountId) &&
        Objects.equals(companyId, inlineResponse201.companyId) &&
        Objects.equals(id, inlineResponse201.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, companyId, id);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse201 {\n");
    
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  companyId: ").append(companyId).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
