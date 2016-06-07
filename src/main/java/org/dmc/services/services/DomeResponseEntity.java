package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class DomeResponseEntity {

	private String status = null;
	private DomeEntity pkg = null;


	@JsonProperty("status")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}


	@JsonProperty("pkg")
	public DomeEntity getPkg() {
		return pkg;
	}
	
	public void setPkg(DomeEntity pkg) {
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
		DomeResponseEntity domeResponseEntity = (DomeResponseEntity) o;
		return Objects.equals(status, domeResponseEntity.status) && Objects.equals(pkg, domeResponseEntity.pkg);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeResponseEntity {\n");

		sb.append("  status: ").append(status).append("\n");
		sb.append("  pkg: ").append(pkg.toString()).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

}
