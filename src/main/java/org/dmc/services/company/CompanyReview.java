package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CompanyReview  {
  
  private String id = null;
  private String companyId = null;
  private String name = null;
  private Boolean reply = null;
  private String reviewId = null;
  private Boolean status = null;
  private BigDecimal date = null;
  private Integer rating = null;
  private Integer like = null;
  private Integer dislike = null;
  private String comment = null;
  private String accountId = null;

  
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
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   **/
  @JsonProperty("reply")
  public Boolean getReply() {
    return reply;
  }
  public void setReply(Boolean reply) {
    this.reply = reply;
  }

  
  /**
   **/
  @JsonProperty("reviewId")
  public String getReviewId() {
    return reviewId;
  }
  public void setReviewId(String reviewId) {
    this.reviewId = reviewId;
  }

  
  /**
   **/
  @JsonProperty("status")
  public Boolean getStatus() {
    return status;
  }
  public void setStatus(Boolean status) {
    this.status = status;
  }

  
  /**
   **/
  @JsonProperty("date")
  public BigDecimal getDate() {
    return date;
  }
  public void setDate(BigDecimal date) {
    this.date = date;
  }

  
  /**
   **/
  @JsonProperty("rating")
  public Integer getRating() {
    return rating;
  }
  public void setRating(Integer rating) {
    this.rating = rating;
  }

  
  /**
   **/
  @JsonProperty("like")
  public Integer getLike() {
    return like;
  }
  public void setLike(Integer like) {
    this.like = like;
  }

  
  /**
   **/

  @JsonProperty("dislike")
  public Integer getDislike() {
    return dislike;
  }
  public void setDislike(Integer dislike) {
    this.dislike = dislike;
  }

  
  /**
   **/

  @JsonProperty("comment")
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanyReview companyReview = (CompanyReview) o;
    return Objects.equals(id, companyReview.id) &&
        Objects.equals(companyId, companyReview.companyId) &&
        Objects.equals(name, companyReview.name) &&
        Objects.equals(reply, companyReview.reply) &&
        Objects.equals(reviewId, companyReview.reviewId) &&
        Objects.equals(status, companyReview.status) &&
        Objects.equals(date, companyReview.date) &&
        Objects.equals(rating, companyReview.rating) &&
        Objects.equals(like, companyReview.like) &&
        Objects.equals(dislike, companyReview.dislike) &&
        Objects.equals(comment, companyReview.comment) &&
        Objects.equals(accountId, companyReview.accountId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, companyId, name, reply, reviewId, status, date, rating, like, dislike, comment, accountId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanyReview {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  companyId: ").append(companyId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  reply: ").append(reply).append("\n");
    sb.append("  reviewId: ").append(reviewId).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("  date: ").append(date).append("\n");
    sb.append("  rating: ").append(rating).append("\n");
    sb.append("  like: ").append(like).append("\n");
    sb.append("  dislike: ").append(dislike).append("\n");
    sb.append("  comment: ").append(comment).append("\n");
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
