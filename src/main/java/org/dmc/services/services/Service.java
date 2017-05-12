package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dmc.services.sharedattributes.FeatureImage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-05-03T15:13:20.207Z")

public class Service  {

    private final String logTag = Service.class.getName();
	
	private int id = -1;
	private String companyId = null;
	private String title = null;
	private String description = null;
	private String owner = null;
	private String profileId = null;
	private Date releaseDate = null;
	private String serviceType = null;
	private List<String> tags = new ArrayList<String>();
	private String specifications = null;
	private FeatureImage featureImage = null;
	private ServiceCurrentStatus currentStatus = null;
	private String projectId = null;
	private String from = null;
	private String type = null;
	private String parent = null;
	private Boolean published = null;
	private String averageRun = null;
  private int isDeleted = null;
	
	
	/**
	 **/
	@JsonProperty("id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	@JsonProperty("owner")
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
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
	@JsonProperty("serviceType")
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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
	@JsonProperty("specifications")
	public String getSpecifications() {
		return specifications;
	}
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
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
	@JsonProperty("currentStatus")
	public ServiceCurrentStatus getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(ServiceCurrentStatus currentStatus) {
		this.currentStatus = currentStatus;
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
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
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
	@JsonProperty("parent")
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	
	/**
	 **/
	@JsonProperty("published")
	public Boolean getPublished() {
		return published;
	}
	public void setPublished(Boolean published) {
		this.published = published;
	}
	
	
	/**
	 **/
	@JsonProperty("averageRun")
	public String getAverageRun() {
		return averageRun;
	}
	public void setAverageRun(String averageRun) {
		this.averageRun = averageRun;
	}
	
  
  /**
	 **/
	@JsonProperty("isDeleted")
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
  
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Service service = (Service) o;
		return Objects.equals(id, service.id) &&
        Objects.equals(companyId, service.companyId) &&
        Objects.equals(title, service.title) &&
        Objects.equals(description, service.description) &&
        Objects.equals(owner, service.owner) &&
        Objects.equals(profileId, service.profileId) &&
        Objects.equals(releaseDate, service.releaseDate) &&
        Objects.equals(serviceType, service.serviceType) &&
        Objects.equals(tags, service.tags) &&
        Objects.equals(specifications, service.specifications) &&
        Objects.equals(featureImage, service.featureImage) &&
        Objects.equals(currentStatus, service.currentStatus) &&
        Objects.equals(projectId, service.projectId) &&
        Objects.equals(from, service.from) &&
        Objects.equals(type, service.type) &&
        Objects.equals(parent, service.parent) &&
        Objects.equals(published, service.published) &&
        Objects.equals(averageRun, service.averageRun) &&
        Objects.equals(isDeleted, service.isDeleted);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, companyId, title, description, owner, profileId, releaseDate, serviceType, tags, specifications, featureImage, currentStatus, projectId, from, type, parent, published, averageRun, isDeleted);
	}
	
	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class Service {\n");
		
		sb.append("  id: ").append(id).append("\n");
		sb.append("  companyId: ").append(companyId).append("\n");
		sb.append("  title: ").append(title).append("\n");
		sb.append("  description: ").append(description).append("\n");
		sb.append("  owner: ").append(owner).append("\n");
		sb.append("  profileId: ").append(profileId).append("\n");
		sb.append("  releaseDate: ").append(releaseDate).append("\n");
		sb.append("  serviceType: ").append(serviceType).append("\n");
		sb.append("  tags: ").append(tags).append("\n");
		sb.append("  specifications: ").append(specifications).append("\n");
		sb.append("  featureImage: ").append(featureImage).append("\n");
		sb.append("  currentStatus: ").append(currentStatus).append("\n");
		sb.append("  projectId: ").append(projectId).append("\n");
		sb.append("  from: ").append(from).append("\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  parent: ").append(parent).append("\n");
		sb.append("  published: ").append(published).append("\n");
		sb.append("  averageRun: ").append(averageRun).append("\n");
    sb.append("  isDeleted: ").append(isDeleted).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}