package org.dmc.services.security;

public final class SecurityRoles {

	private SecurityRoles() {}
	
	public static final String SUPERADMIN = "SUPERADMIN";
	public static final String ADMIN = "ADMIN";
	public static final String VIP = "VIP";
	public static final String MEMBER = "MEMBER";
	
	public static final String REQUIRED_ROLE_SUPERADMIN = "hasRole('" + SUPERADMIN + "')";
	public static final String REQUIRED_ROLE_ADMIN = "hasRole('" + ADMIN + "')";
	public static final String REQUIRED_ROLE_VIP = "hasRole('" + VIP + "')";
	public static final String REQUIRED_ROLE_MEMBER = "hasRole('" + MEMBER + "')";
	
}
