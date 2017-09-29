package org.dmc.services.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {


	private Integer id;
	private String username;
	private Integer myOrg;
	private Boolean isUpperTierMember;
	private Map<Integer, String> rolesByOrgId = new HashMap<Integer, String>();
	private Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<SimpleGrantedAuthority>();

	public UserPrincipal() {
	}
	public UserPrincipal(Integer id, String username) {
		this.id = id;
		this.username = username;
	}
	public UserPrincipal(Integer id, String username, Integer myOrg) {
		this.id = id;
		this.username = username;
		this.myOrg = myOrg;
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

	public Integer getmyOrg() {
		return myOrg;
	}
	public void setmyOrg(Integer myOrg) {
		this.myOrg = myOrg;
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	public void addAuthority(String authority) {
		this.grantedAuthorities.add(new SimpleGrantedAuthority(authority));
	}

	public void addAuthorities(Collection<String> authorities) {
		authorities.stream().forEach((n) -> addAuthority(n));
	}

	public boolean hasAuthority(String authority) {
		return this.grantedAuthorities.contains(new SimpleGrantedAuthority(authority));
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

	public Boolean getIsUpperTierMember() {
		return isUpperTierMember;
	}

	public void setIsUpperTierMember(Boolean isUpperTierMember) {
		this.isUpperTierMember = isUpperTierMember;
	}

}
