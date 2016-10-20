package org.dmc.services.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="servers")
public class DomeServer extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="server_id")
	private Integer id;
	
	@Column(name="url")
	@JsonProperty("ip")
	private String serverURL;
	
	@Column(name="alias")
	private String name;
	
	@Column(name="user_id")
	@JsonProperty("accountId")
	private Integer userId;
	
	private Integer port;
	
	@Column(name="local_dome_user")
	@JsonIgnore
	private String userName;
	
	@Column(name="dome_user_space")
	@JsonIgnore
	private String userSpace;
	
	@Column(name="local_dome_user_password")
	@JsonIgnore
	private String userPass;
	
	private String status;

	@ManyToMany
	@JoinTable(name = "server_in_server_access_group",
			   joinColumns = @JoinColumn(name="server_id"),
			   inverseJoinColumns = @JoinColumn(name="server_access_group_id"))
	@JsonIgnore
	private List<ServerAccess> accessList;
	
	//helper json property to denote pub/private status
	@JsonProperty("public")
	private Boolean isPublic(){
		return accessList.stream().anyMatch(g->"global".equals(g.getName()));
	}
		
	@Override
	public Integer getId()
	{
		return this.id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	public String getServerURL()
	{
		return this.serverURL;
	}
	public void setServerURL(String url)
	{
		this.serverURL = url;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	
	public Integer getUserId()
	{
		return this.userId;
	}
	public void setUserId(Integer id)
	{
		this.userId = id;
	}
	
	public Integer getPort(){
		return this.port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}	
	
	public String getUserName(){
		return this.userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}	
	
	public String getUserSpace(){
		return this.userSpace;
	}
	public void setUserSpace(String userSpace)
	{
		this.userSpace = userSpace;
	}	
	
	public String getUserPass(){
		return this.userPass;
	}
	public void setUserPass(String userPass)
	{
		this.userPass = userPass;
	}	
	
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((serverURL == null) ? 0 : serverURL.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userSpace == null) ? 0 : userSpace.hashCode());
		result = prime * result + ((userPass == null) ? 0 : userPass.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((accessList == null) ? 0 : accessList.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomeServer other = (DomeServer) obj;
		if (serverURL == null) {
			if (other.serverURL != null)
				return false;
		} else if (!serverURL.equals(other.serverURL))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userSpace == null) {
			if (other.userSpace != null)
				return false;
		} else if (!userSpace.equals(other.userSpace))
			return false;
		if (userPass == null) {
			if (other.userPass != null)
				return false;
		} else if (!userPass.equals(other.userPass))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (accessList == null) {
			if (other.accessList != null)
				return false;
		} else if (!accessList.equals(other.accessList))
			return false;
		
		return true;
	}
	
}

