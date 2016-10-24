package org.dmc.services.data.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="server_access_group")
public class ServerAccess extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;
	
	@ManyToMany
	@JoinTable(name = "user_in_server_access_group",
			   joinColumns = @JoinColumn(name="server_access_group_id"),
			   inverseJoinColumns = @JoinColumn(name="user_id"))
	@JsonIgnore
	private List<User> users;
	
	@ManyToMany
	@JoinTable(name = "server_in_server_access_group",
			   joinColumns = @JoinColumn(name="server_access_group_id"),
			   inverseJoinColumns = @JoinColumn(name="server_id"))
	@JsonIgnore
	private List<DomeServer> servers;

    ServerAccess() {
    }
    public ServerAccess(String name) {
        this.name = name;
    }

	@Override
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
		this.id = id;
	}
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
		this.name = name;
	}
    
    public List<DomeServer> getServers(){
    	return servers;
    }
    public void setServers(List<DomeServer> servers){
    	this.servers=servers;
    }
    
    public List<User> getUsers(){
    	return users;
    }
    public void setUsers(List<User> users){
    	this.users=users;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((servers == null) ? 0 : servers.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		
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
		ServerAccess other = (ServerAccess) obj;
		if (servers == null) {
			if (other.servers != null)
				return false;
		} else if (!servers.equals(other.servers))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
				
		return true;
	}
}