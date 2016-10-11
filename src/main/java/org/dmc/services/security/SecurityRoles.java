package org.dmc.services.security;

public final class SecurityRoles {

	private SecurityRoles() {}
	
	public static final String SUPERADMIN = "SUPERADMIN";
	public static final String ADMIN = "ADMIN";
	public static final String VIP = "VIP";
	public static final String MEMBER = "MEMBER";
	public static final String DMDII_MEMBER = "DMDII_MEMBER";
	public static final String ORGANIZATION_MEMBER = "ORGANIZATION_MEMBER";
	public static final String PROJECT_MEMBER = "PROJECT_MEMBER";
	public static final String SERVICE_MEMBER = "SERVICE_MEMBER";
	
	public static final String REQUIRED_ROLE_SUPERADMIN = "hasRole('" + SUPERADMIN + "')";
	public static final String REQUIRED_ROLE_ADMIN = "hasRole('" + ADMIN + "')";
	public static final String REQUIRED_ROLE_VIP = "hasRole('" + VIP + "')";
	public static final String REQUIRED_ROLE_MEMBER = "hasRole('" + MEMBER + "')";
	public static final String REQUIRED_ROLE_DMDII_MEMBER = "hasRole('" + DMDII_MEMBER + "')";
	public static final String REQUIRED_ROLE_ORGANIZATION_MEMBER = "hasRole('" + ORGANIZATION_MEMBER + "')";
	public static final String REQUIRED_ROLE_PROJECT_MEMBER = "hasRole('" + PROJECT_MEMBER + "')";
	public static final String REQUIRED_ROLE_SERVICE_MEMBER = "hasRole('" + SERVICE_MEMBER + "')";
}
