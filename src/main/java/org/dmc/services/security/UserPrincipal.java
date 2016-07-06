package org.dmc.services.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
	
	private Integer id;
	private String username;
	private Map<Integer, String> rolesByOrgId = new HashMap<Integer, String>();
	private Boolean isSuperAdmin = false;
	
	public UserPrincipal(Integer id, String username) {
		this.id = id;
		this.username = username;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void addRole(Integer orgId, String role) {
		this.rolesByOrgId.put(orgId, role);
	}
	public String getRole(Integer orgId) {
		return this.rolesByOrgId.get(orgId);
	}
	public Map<Integer, String> getAllRoles() {
		return this.rolesByOrgId;
	}
	
	public Boolean getIsSuperAdmin() {
		return this.isSuperAdmin;
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
