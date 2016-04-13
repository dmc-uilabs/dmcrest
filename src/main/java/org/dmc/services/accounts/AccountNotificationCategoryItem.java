package org.dmc.services.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-04T18:05:28.094Z")
public class AccountNotificationCategoryItem  {
  
  private String id = null;
  private String accountNotificationCategoryId = null;
  private String title = null;

  
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
  @JsonProperty("account-notification-categoryId")
  public String getAccountNotificationCategoryId() {
    return accountNotificationCategoryId;
  }
  public void setAccountNotificationCategoryId(String accountNotificationCategoryId) {
    this.accountNotificationCategoryId = accountNotificationCategoryId;
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountNotificationCategoryItem accountNotificationCategoryItem = (AccountNotificationCategoryItem) o;
    return Objects.equals(id, accountNotificationCategoryItem.id) &&
        Objects.equals(accountNotificationCategoryId, accountNotificationCategoryItem.accountNotificationCategoryId) &&
        Objects.equals(title, accountNotificationCategoryItem.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountNotificationCategoryId, title);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountNotificationCategoryItem {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  accountNotificationCategoryId: ").append(accountNotificationCategoryId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
