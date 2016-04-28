package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CompanyImage  {
  
  private String id = null;
  private String companyId = null;
  private String url = null;
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
  @JsonProperty("companyId")
  public String getCompanyId() {
    return companyId;
  }
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  
  /**
   **/
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
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
    CompanyImage companyImage = (CompanyImage) o;
    return Objects.equals(id, companyImage.id) &&
        Objects.equals(companyId, companyImage.companyId) &&
        Objects.equals(url, companyImage.url) &&
        Objects.equals(title, companyImage.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, companyId, url, title);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanyImage {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  companyId: ").append(companyId).append("\n");
    sb.append("  url: ").append(url).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
