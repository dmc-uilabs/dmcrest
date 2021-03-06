package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-08-24T16:43:27.555-04:00")
public class PostCompareService {

	private String serviceId = null;
	private String profileId = null;

	/**
	 **/
	@JsonProperty("serviceId")
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PostCompareService postCompareService = (PostCompareService) o;
		return Objects.equals(serviceId, postCompareService.serviceId)
				&& Objects.equals(profileId, postCompareService.profileId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceId, profileId);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PostCompareService {\n");

		sb.append("  serviceId: ").append(serviceId).append("\n");
		sb.append("  profileId: ").append(profileId).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
