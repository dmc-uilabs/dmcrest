package org.dmc.services.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

import org.dmc.services.reviews.Review;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProfileReview extends Review {
  
  private String profileId = null;
  
  public String getEntityId() {
    return getProfileId();
  }
    
  /**
   **/
  @JsonProperty("profileId")
  public String getProfileId() {
    return profileId;
  }
  public void setProfileId(String profileId) {
    this.profileId = profileId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfileReview profileReview = (ProfileReview) o;
    return super.equals(profileReview) &&
        Objects.equals(profileId, profileReview.profileId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), profileId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProfileReview {\n");
    sb.append(super.toString());
    sb.append("  profileId: ").append(profileId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
