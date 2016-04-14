package org.dmc.services.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-04T18:05:28.094Z")
public class AccountNotificationCategory  {
  
  private String id = null;
  private String title = null;
  private BigDecimal position = null;
  private List<AccountNotificationCategoryItem> accountNotificationCategoryItems = new ArrayList<AccountNotificationCategoryItem>();

  
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
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   **/
  @JsonProperty("position")
  public BigDecimal getPosition() {
    return position;
  }
  public void setPosition(BigDecimal position) {
    this.position = position;
  }

  
  /**
   **/
  @JsonProperty("account-notification-category-items")
  public List<AccountNotificationCategoryItem> getAccountNotificationCategoryItems() {
    return accountNotificationCategoryItems;
  }
  public void setAccountNotificationCategoryItems(List<AccountNotificationCategoryItem> accountNotificationCategoryItems) {
    this.accountNotificationCategoryItems = accountNotificationCategoryItems;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountNotificationCategory accountNotificationCategory = (AccountNotificationCategory) o;
    return Objects.equals(id, accountNotificationCategory.id) &&
        Objects.equals(title, accountNotificationCategory.title) &&
        Objects.equals(position, accountNotificationCategory.position) &&
        Objects.equals(accountNotificationCategoryItems, accountNotificationCategory.accountNotificationCategoryItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, position, accountNotificationCategoryItems);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountNotificationCategory {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("  accountNotificationCategoryItems: ").append(accountNotificationCategoryItems).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
