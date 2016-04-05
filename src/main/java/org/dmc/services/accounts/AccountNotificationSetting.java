package org.dmc.services.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-22T14:57:06.776Z")
public class AccountNotificationSetting  {
  
  private String id = null;
  private String accountId = null;
  public enum SectionEnum {
     website,  email, 
  };
  private SectionEnum section = null;
  private String accountNotificationCategoryItemId = null;
  private Boolean selected = null;

  
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
  @JsonProperty("section")
  public SectionEnum getSection() {
    return section;
  }
  public void setSection(SectionEnum section) {
    this.section = section;
  }

  
  /**
   **/
  @JsonProperty("account-notification-category-itemId")
  public String getAccountNotificationCategoryItemId() {
    return accountNotificationCategoryItemId;
  }
  public void setAccountNotificationCategoryItemId(String accountNotificationCategoryItemId) {
    this.accountNotificationCategoryItemId = accountNotificationCategoryItemId;
  }

  
  /**
   **/
  @JsonProperty("selected")
  public Boolean getSelected() {
    return selected;
  }
  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountNotificationSetting accountNotificationSetting = (AccountNotificationSetting) o;
    return Objects.equals(id, accountNotificationSetting.id) &&
        Objects.equals(accountId, accountNotificationSetting.accountId) &&
        Objects.equals(section, accountNotificationSetting.section) &&
        Objects.equals(accountNotificationCategoryItemId, accountNotificationSetting.accountNotificationCategoryItemId) &&
        Objects.equals(selected, accountNotificationSetting.selected);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, section, accountNotificationCategoryItemId, selected);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountNotificationSetting {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  section: ").append(section).append("\n");
    sb.append("  accountNotificationCategoryItemId: ").append(accountNotificationCategoryItemId).append("\n");
    sb.append("  selected: ").append(selected).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
