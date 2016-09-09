package org.dmc.services.security;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

public class DMCRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	private static final String PRINCIPAL_REQUEST_HEADER = "AJP_eppn";
	private static final String APP_TOKEN_HEADER = "APP_TOKEN";
	private String appToken;

	/**
	 * Read and returns the header named by {@code principalRequestHeader} from the request.
	 * Also looks at the header named by {@code appTokenHeader} from the request for the application token.
	 *
	 * @throws PreAuthenticatedCredentialsNotFoundException if the headers are missing or application token is not valid.
	 */
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String principal = request.getHeader(PRINCIPAL_REQUEST_HEADER);
		String token = request.getHeader(APP_TOKEN_HEADER);

		if (principal == null) {
			throw new PreAuthenticatedCredentialsNotFoundException(this.PRINCIPAL_REQUEST_HEADER + " header not found in request.");
		} else if (token == null || !token.equals(appToken)) {
			throw new PreAuthenticatedCredentialsNotFoundException(this.APP_TOKEN_HEADER + " header not found or invalid in request.");
		}
		return principal;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}
}
