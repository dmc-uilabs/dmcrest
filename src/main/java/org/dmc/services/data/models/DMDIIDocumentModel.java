package org.dmc.services.data.models;

public class DMDIIDocumentModel extends BaseModel {

	private String documentName;
	
	private String documentUrl;
	
	private Integer dmdiiProjectId;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public Integer getDmdiiProjectId() {
		return dmdiiProjectId;
	}

	public void setDmdiiProjectId(Integer dmdiiProjectId) {
		this.dmdiiProjectId = dmdiiProjectId;
	}
}
