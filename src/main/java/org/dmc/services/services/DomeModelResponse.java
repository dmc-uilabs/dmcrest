package org.dmc.services.services;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomeModelResponse {
	private String status = null;
	private DomeModelResponsePkg pkg = null;

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("pkg")
	public DomeModelResponsePkg getPkg() {
		return pkg;
	}

	public void setPkg(DomeModelResponsePkg pkg) {
		this.pkg = pkg;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DomeModelResponse domeModelResponse = (DomeModelResponse) o;
		return Objects.equals(status, domeModelResponse.status) && Objects.equals(pkg, domeModelResponse.pkg);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeModelResponse {\n");

		sb.append("  status: ").append(status).append("\n");
		sb.append("  pkg: ").append(pkg.toString()).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

}
