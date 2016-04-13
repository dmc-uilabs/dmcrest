package org.dmc.services.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-22T14:57:06.776Z")
public class UserAccountServer  {
  
  private String id = null;
  private String accountId = null;
  private String name = null;
  private String ip = null;
  private String status = null;

  
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
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   **/
  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }

  
  /**
   **/
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAccountServer userAccountServer = (UserAccountServer) o;
    return Objects.equals(id, userAccountServer.id) &&
        Objects.equals(accountId, userAccountServer.accountId) &&
        Objects.equals(name, userAccountServer.name) &&
        Objects.equals(ip, userAccountServer.ip) &&
        Objects.equals(status, userAccountServer.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, name, ip, status);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAccountServer {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  ip: ").append(ip).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
