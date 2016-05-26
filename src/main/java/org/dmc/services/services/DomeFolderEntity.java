package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class DomeFolderEntity extends DomeEntity {

	private List<DomeEntity> children = new ArrayList<DomeEntity>();
	
	/**
	 **/
	@JsonProperty("children")
	public List<DomeEntity> getChildren() {
		return children;
	}

	public void setChildren(List<DomeEntity> children) {
		this.children = children;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DomeFolderEntity domeEntity = (DomeFolderEntity) o;
		return Objects.equals(getType(), domeEntity.getType()) && Objects.equals(getName(), domeEntity.getName())
				&& Objects.equals(getPath(), domeEntity.getPath())
				&& Objects.equals(getDomeServer(), domeEntity.getDomeServer());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getType(), getName(), getPath(), getDomeServer());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeFolderEntity {\n");

		sb.append("  type: ").append(getType()).append("\n");
		sb.append("  name: ").append(getName()).append("\n");
		sb.append("  path: ").append(getPath()).append("\n");
		sb.append("  domeServer: ").append(getDomeServer()).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
