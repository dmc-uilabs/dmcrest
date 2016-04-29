package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FeatureImage  {
  
  private String thumbnail = null;
  private String large = null;

  
  /**
   **/
  @JsonProperty("thumbnail")
  public String getThumbnail() {
    return thumbnail;
  }
  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  
  /**
   **/
  @JsonProperty("large")
  public String getLarge() {
    return large;
  }
  public void setLarge(String large) {
    this.large = large;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeatureImage featureImage = (FeatureImage) o;
    return Objects.equals(thumbnail, featureImage.thumbnail) &&
        Objects.equals(large, featureImage.large);
  }

  @Override
  public int hashCode() {
    return Objects.hash(thumbnail, large);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FeatureImage {\n");
    
    sb.append("  thumbnail: ").append(thumbnail).append("\n");
    sb.append("  large: ").append(large).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
