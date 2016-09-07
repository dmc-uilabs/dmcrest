package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

import org.dmc.services.reviews.Review;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CompanyReview extends Review {

  private String companyId = null;

  public String getEntityId() {
    return getCompanyId();
  }
    
  public void setEntityId(String entityId) {
    setCompanyId(entityId);
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
    CompanyReview companyReview = (CompanyReview) o;
    return super.equals(companyReview) &&
        Objects.equals(companyId, companyReview.companyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), companyId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanyReview {\n");
    sb.append(super.toString());
    sb.append("  companyId: ").append(companyId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
