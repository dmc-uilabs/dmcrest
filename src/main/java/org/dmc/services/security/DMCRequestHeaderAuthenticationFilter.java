package org.dmc.services.security;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

public class DMCRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	private static final String PRINCIPAL_REQUEST_HEADER = "AJP_eppn";

	/**
	 * Read and returns the header named by {@code principalRequestHeader} from the request.
	 *
	 * @throws PreAuthenticatedCredentialsNotFoundException if the header is missing.
	 */
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String principal = request.getHeader(PRINCIPAL_REQUEST_HEADER);

		if (principal == null) {
			throw new PreAuthenticatedCredentialsNotFoundException(this.PRINCIPAL_REQUEST_HEADER + " header not found in request.");
		}
		return principal;
	}
}
