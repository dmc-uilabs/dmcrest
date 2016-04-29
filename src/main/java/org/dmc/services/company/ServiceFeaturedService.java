package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ServiceFeaturedService  {
  
  private String owner = null;
  private String serviceType = null;
  private Date releaseDate = null;
  private CurrentStatus currentStatus = null;
  private String description = null;
  private String title = null;
  private String type = null;
  private String specifications = null;
  private List<String> tags = new ArrayList<String>();
  private String from = null;
  private String id = null;
  private Integer position = null;
  private FeatureImage featureImage = null;
  private String projectId = null;
  private Boolean favorite = null;
  private Boolean inFeatured = null;
  private String featureId = null;

  
  /**
   **/
  @JsonProperty("owner")
  public String getOwner() {
    return owner;
  }
  public void setOwner(String owner) {
    this.owner = owner;
  }

  
  /**
   **/
  @JsonProperty("serviceType")
  public String getServiceType() {
    return serviceType;
  }
  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  
  /**
   **/
  @JsonProperty("releaseDate")
  public Date getReleaseDate() {
    return releaseDate;
  }
  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  
  /**
   **/
  @JsonProperty("currentStatus")
  public CurrentStatus getCurrentStatus() {
    return currentStatus;
  }
  public void setCurrentStatus(CurrentStatus currentStatus) {
    this.currentStatus = currentStatus;
  }

  
  /**
   **/
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
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
  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  
  /**
   **/
  @JsonProperty("specifications")
  public String getSpecifications() {
    return specifications;
  }
  public void setSpecifications(String specifications) {
    this.specifications = specifications;
  }

  
  /**
   **/
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }
  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  
  /**
   **/
  @JsonProperty("from")
  public String getFrom() {
    return from;
  }
  public void setFrom(String from) {
    this.from = from;
  }

  
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
  @JsonProperty("position")
  public Integer getPosition() {
    return position;
  }
  public void setPosition(Integer position) {
    this.position = position;
  }

  
  /**
   **/
  @JsonProperty("featureImage")
  public FeatureImage getFeatureImage() {
    return featureImage;
  }
  public void setFeatureImage(FeatureImage featureImage) {
    this.featureImage = featureImage;
  }

  
  /**
   **/
  @JsonProperty("projectId")
  public String getProjectId() {
    return projectId;
  }
  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  
  /**
   **/
  @JsonProperty("favorite")
  public Boolean getFavorite() {
    return favorite;
  }
  public void setFavorite(Boolean favorite) {
    this.favorite = favorite;
  }

  
  /**
   **/
  @JsonProperty("inFeatured")
  public Boolean getInFeatured() {
    return inFeatured;
  }
  public void setInFeatured(Boolean inFeatured) {
    this.inFeatured = inFeatured;
  }

  
  /**
   **/
  @JsonProperty("featureId")
  public String getFeatureId() {
    return featureId;
  }
  public void setFeatureId(String featureId) {
    this.featureId = featureId;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceFeaturedService serviceFeaturedService = (ServiceFeaturedService) o;
    return Objects.equals(owner, serviceFeaturedService.owner) &&
        Objects.equals(serviceType, serviceFeaturedService.serviceType) &&
        Objects.equals(releaseDate, serviceFeaturedService.releaseDate) &&
        Objects.equals(currentStatus, serviceFeaturedService.currentStatus) &&
        Objects.equals(description, serviceFeaturedService.description) &&
        Objects.equals(title, serviceFeaturedService.title) &&
        Objects.equals(type, serviceFeaturedService.type) &&
        Objects.equals(specifications, serviceFeaturedService.specifications) &&
        Objects.equals(tags, serviceFeaturedService.tags) &&
        Objects.equals(from, serviceFeaturedService.from) &&
        Objects.equals(id, serviceFeaturedService.id) &&
        Objects.equals(position, serviceFeaturedService.position) &&
        Objects.equals(featureImage, serviceFeaturedService.featureImage) &&
        Objects.equals(projectId, serviceFeaturedService.projectId) &&
        Objects.equals(favorite, serviceFeaturedService.favorite) &&
        Objects.equals(inFeatured, serviceFeaturedService.inFeatured) &&
        Objects.equals(featureId, serviceFeaturedService.featureId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner, serviceType, releaseDate, currentStatus, description, title, type, specifications, tags, from, id, position, featureImage, projectId, favorite, inFeatured, featureId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceFeaturedService {\n");
    
    sb.append("  owner: ").append(owner).append("\n");
    sb.append("  serviceType: ").append(serviceType).append("\n");
    sb.append("  releaseDate: ").append(releaseDate).append("\n");
    sb.append("  currentStatus: ").append(currentStatus).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  specifications: ").append(specifications).append("\n");
    sb.append("  tags: ").append(tags).append("\n");
    sb.append("  from: ").append(from).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("  featureImage: ").append(featureImage).append("\n");
    sb.append("  projectId: ").append(projectId).append("\n");
    sb.append("  favorite: ").append(favorite).append("\n");
    sb.append("  inFeatured: ").append(inFeatured).append("\n");
    sb.append("  featureId: ").append(featureId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
