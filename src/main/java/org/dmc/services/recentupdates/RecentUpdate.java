package org.dmc.services.recentupdates;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import org.dmc.services.sharedattributes.FeatureImage;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-05-03T15:13:20.207Z")

public class RecentUpdate {

    private final String logTag = RecentUpdate.class.getName();

  	private int id;
  	private String updateDate;
  	private String updateType;
  	private int updateId;
  	private int parentId;
    private String description;
    private String parentTitle;

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
  	@JsonProperty("updateDate")
  	public String getUpdateDate() {
  		return updateDate;
  	}
  	public void setUpdateDate(String updateDate) {
  		this.updateDate = updateDate;
  	}


  	/**
  	 **/
  	@JsonProperty("updateType")
  	public String getUpdateType() {
  		return updateType;
  	}
  	public void setUpdateType(String updateType) {
  		this.updateType = updateType;
  	}


  	/**
  	 **/
  	@JsonProperty("updateId")
  	public int getUpdateId() {
  		return updateId;
  	}
  	public void setUpdateId(int updateId) {
  		this.updateId = updateId;
  	}


  	/**
  	 **/
  	@JsonProperty("parentId")
  	public int getParentId() {
  		return parentId;
  	}
  	public void setParentId(int parentId) {
  		this.parentId = parentId;
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
    @JsonProperty("parentTitle")
    public String getParentTitle() {
      return parentTitle;
    }
    public void setParentTitle(String parentTitle) {
      this.parentTitle = parentTitle;
    }
  	// @Override
  	// public int hashCode() {
  	// 	return Objects.hash(id, updateDate, updateType, updateId, parentId);
  	// }

}
