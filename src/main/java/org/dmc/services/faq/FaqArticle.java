package org.dmc.services.faq;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class FaqArticle  {
  
  private String id = null;
  private String faqSubcategoryId = null;
  private String title = null;
  private String text = null;

  
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
  @JsonProperty("faq_subcategoryId")
  public String getFaqSubcategoryId() {
    return faqSubcategoryId;
  }
  public void setFaqSubcategoryId(String faqSubcategoryId) {
    this.faqSubcategoryId = faqSubcategoryId;
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
  @JsonProperty("text")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FaqArticle faqArticle = (FaqArticle) o;
    return Objects.equals(id, faqArticle.id) &&
        Objects.equals(faqSubcategoryId, faqArticle.faqSubcategoryId) &&
        Objects.equals(title, faqArticle.title) &&
        Objects.equals(text, faqArticle.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, faqSubcategoryId, title, text);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FaqArticle {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  faqSubcategoryId: ").append(faqSubcategoryId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
