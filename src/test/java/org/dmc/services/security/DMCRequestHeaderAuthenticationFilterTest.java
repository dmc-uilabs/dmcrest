package org.dmc.services.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DMCRequestHeaderAuthenticationFilterTest {

	@InjectMocks
	private DMCRequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter;

	@Mock
	protected HttpServletRequest httpServletRequest;

	private String principal;

	@Before
	public void before() {
		principal = RandomStringUtils.randomAlphanumeric(25);
		when(httpServletRequest.getHeader("AJP_eppn")).thenReturn(principal);
	}

	@Test
	public void getPreAuthenticatedPrincipal() {
		String actual = (String) requestHeaderAuthenticationFilter.getPreAuthenticatedPrincipal(httpServletRequest);
		assertEquals(principal, actual);
	}

	@Test(expected = PreAuthenticatedCredentialsNotFoundException.class)
	public void getPreAuthenticatedPrincipal_NullPrincipal() {
		when(httpServletRequest.getHeader("AJP_eppn")).thenReturn(null);
		requestHeaderAuthenticationFilter.getPreAuthenticatedPrincipal(httpServletRequest);
	}

}