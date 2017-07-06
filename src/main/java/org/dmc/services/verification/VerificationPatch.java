package org.dmc.services.verification;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class VerificationPatch {

	private int id;
	private String url;
	private String table;
	private String folder;
	private String resourceType;
	private String userEPPN;
	private boolean verified;
	private String urlColumn;
	private String idColumn;
	private String scanLog;
	private String restIP;
	private String sha256;
	private Long scanDate;
	private String encryptionType;


    @JsonProperty("id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

    @JsonProperty("url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

		@JsonProperty("scanDate")
	public Long getScanDate() {
		return scanDate;
	}

	public void setScanDate(Long scanDate){
		this.scanDate = scanDate;
	}


	@JsonProperty("encryptionType")
public String getEncryptionType() {
	return encryptionType;
}

public void setEncryptionType(String encryptionType){
	this.encryptionType = encryptionType;
}


    @JsonProperty("table")
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}

    @JsonProperty("folder")
    public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}

    @JsonProperty("resourceType")
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

    @JsonProperty("userEPPN")
	public String getUserEPPN() {
		return userEPPN;
	}
	public void setUserEPPN(String userEPPN) {
		this.userEPPN = userEPPN;
	}
	@JsonProperty("verified")
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@JsonProperty("urlColumn")
	public String getUrlColumn() {
		return urlColumn;
	}
	public void setUrlColumn(String urlColumn) {
		this.urlColumn = urlColumn;
	}

	@JsonProperty("idColumn")
	public String getIdColumn() {
		return idColumn;
	}
	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}
	@JsonProperty("scanLog")
	public String getScanLog() {
		return scanLog;
	}
	public void setScanLog(String scanLog) {
		this.scanLog = scanLog;
	}

	@JsonProperty("restIP")
	public String getRestIP() {
		return restIP;
	}
	public void setRestIP(String restIP) {
		this.restIP = restIP;
	}


	@JsonProperty("sha256")
	public String getSha256() {
		return sha256;
	}
	public void setSha256(String sha256){
		this.sha256 = sha256;
	}



	@Override
	public String toString() {
		return "VerificationPatch{" +
				"id=" + id +
				", url='" + url + '\'' +
				", table='" + table + '\'' +
				", folder='" + folder + '\'' +
				", resourceType='" + resourceType + '\'' +
				", userEPPN='" + userEPPN + '\'' +
				", verified=" + verified +
				", urlColumn='" + urlColumn + '\'' +
				", idColumn='" + idColumn + '\'' +
				", scanLog='" + scanLog + '\'' +
				", restIP='" + restIP + '\'' +
				", sha256='" + sha256 + '\'' +
				", scanDate='" + scanDate + '\'' +
				", encryptionType='" + encryptionType + '\'' +
				'}';
	}
}
