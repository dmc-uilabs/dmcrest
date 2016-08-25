package org.dmc.services.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

import org.dmc.services.reviews.Review;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProductReview extends Review {
  
  private String productId = null;
  public enum ProductTypeEnum {
     services,  components, 
  };
  
  private ProductTypeEnum productType = null;

  public String getEntityId() {
    return getProductId();
  }

  /**
   **/
  @JsonProperty("productId")
  public String getProductId() {
    return productId;
  }
  public void setProductId(String productId) {
    this.productId = productId;
  }

  
  /**
   **/
  @JsonProperty("productType")
  public ProductTypeEnum getProductType() {
    return productType;
  }
  public void setProductType(ProductTypeEnum productType) {
    this.productType = productType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductReview productReview = (ProductReview) o;
    return super.equals(productReview) &&
        Objects.equals(productId, productReview.productId) &&
        Objects.equals(productType, productReview.productType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), productId, productType);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductReview {\n");
    sb.append(super.toString());
    sb.append("  productId: ").append(productId).append("\n");
    sb.append("  productType: ").append(productType).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
