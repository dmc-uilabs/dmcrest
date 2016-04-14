package org.dmc.services.faq;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class FaqCategory  {
  
  private String id = null;
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
    FaqCategory faqCategory = (FaqCategory) o;
    return Objects.equals(id, faqCategory.id) &&
        Objects.equals(title, faqCategory.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FaqCategory {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
