package org.dmc.services.services;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomeServer {
	private String name = null;
	private String port = null;
	private String user = null;
	private String pw = null;
	private String space = null;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("port")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@JsonProperty("pw")
	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	@JsonProperty("space")
	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DomeServer domeServer = (DomeServer) o;
		return Objects.equals(name, domeServer.name) && Objects.equals(port, domeServer.port)
				&& Objects.equals(user, domeServer.user) && Objects.equals(pw, domeServer.pw)
				&& Objects.equals(space, domeServer.space);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DomeServer {\n");

		sb.append("  name: ").append(name).append("\n");
		sb.append("  port: ").append(port).append("\n");
		sb.append("  user: ").append(user).append("\n");
		sb.append("  pw: ").append(pw).append("\n");
		sb.append("  space: ").append(space).append("\n");
		sb.append("}\n");
		return sb.toString();
	}

}
