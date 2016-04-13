package org.dmc.services.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-22T14:57:06.776Z")
public class FollowingCompany  {
  
  private String id = null;
  private String accountId = null;
  private String companyId = null;

  
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
  @JsonProperty("accountId")
  public String getAccountId() {
    return accountId;
  }
  public void setAccountId(String accountId) {
    this.accountId = accountId;
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FollowingCompany followingCompany = (FollowingCompany) o;
    return Objects.equals(id, followingCompany.id) &&
        Objects.equals(accountId, followingCompany.accountId) &&
        Objects.equals(companyId, followingCompany.companyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, companyId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FollowingCompany {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  companyId: ").append(companyId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
