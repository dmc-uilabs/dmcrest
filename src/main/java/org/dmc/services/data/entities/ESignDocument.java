package org.dmc.services.data.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "esign_status")
public class ESignDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "document_id")
	  private int documentId;

    @Column(name = "status")
	  private boolean documentStatus;

    @Column(name = "download_link")
	  private String downloadLink;

    @Column(name = "user_id")
	  private Integer userId;

    @Column(name = "fillable_form_id")
	  private int fillableFormId;

    @Column(name = "organization_id")
	  private int organizationId;

    public void setId(Integer id) {
  		this.id = id;
  	}

    public Integer getId() {
  		return id;
  	}

    public void setDocumentId(int id){
      this.documentId = id;
    }

    public Integer getDocumentId(){
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

    @Override
    public boolean equals(Object obj){
      if (this == obj) {
  			return true;
  		}
  		if (obj == null) {
  			return false;
  		}
  		if (getClass() != obj.getClass()) {
  			return false;
  		}

      ESignDocument other = (ESignDocument) obj;
      if (other.id != id)
        return false;
      if (other.documentId != documentId)
        return false;
      if (other.documentStatus != documentStatus)
        return false;
      if (!other.downloadLink.equals(downloadLink))
        return false;
      if (other.userId != userId)
        return false;
      if (other.fillableFormId != fillableFormId)
        return false;
      if (other.organizationId != organizationId)
        return false;

      return true;

    }
}
