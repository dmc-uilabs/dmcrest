package org.dmc.services.data.models;

public class ESignModel extends BaseModel {

  private int documentId;
  private boolean documentStatus;
  private String downloadLink;
  private Integer userId;
  private int fillableFormId;
  private int organizationId;

  public void setDocumentId(int id){
    this.documentId = id;
  }

  public int getDocumentId(){
    return documentId;
  }

  public void setDocumentStatus(boolean status){
    this.documentStatus = status;
  }

  public boolean getDocumentStatus(){
    return documentStatus;
  }

  public void setDownloadLink(String link){
    this.downloadLink = link;
  }

  public String getDownloadLink(){
    return downloadLink;
  }

  public void setUserId(Integer id){
    this.userId = id;
  }

  public Integer getUserId(){
    return userId;
  }

  public void setFillableFormId(int id){
    this.fillableFormId = id;
  }

  public Integer getFillableFormId(){
    return fillableFormId;
  }

  public void setOrganizationId(int id){
    this.organizationId = id;
  }

  public Integer getOrganizationId(){
    return organizationId;
  }
}
