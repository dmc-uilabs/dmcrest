package org.dmc.services.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DMCRequestHeaderAuthenticationFilterTest {

	@InjectMocks
	private DMCRequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter;

	@Mock
	protected HttpServletRequest httpServletRequest;

	private String appToken;
	private String principle;

	@Before
	public void before(){
		appToken = RandomStringUtils.randomAlphanumeric(25);
		principle = RandomStringUtils.randomAlphanumeric(25);

		requestHeaderAuthenticationFilter.setAppToken(appToken);

		when(httpServletRequest.getHeader("AJP_eppn")).thenReturn(principle);
		when(httpServletRequest.getHeader("APP_TOKEN")).thenReturn(appToken);
	}

	@Test
	public void getPreAuthenticatedPrincipal() {
		String actual = (String) requestHeaderAuthenticationFilter.getPreAuthenticatedPrincipal(httpServletRequest);
		assertEquals(principle, actual);
	}

	@Test(expected = PreAuthenticatedCredentialsNotFoundException.class)
	public void getPreAuthenticatedPrincipal_NullPrincipal(){
		when(httpServletRequest.getHeader("AJP_eppn")).thenReturn(null);
		requestHeaderAuthenticationFilter.getPreAuthenticatedPrincipal(httpServletRequest);
	}

	@Test(expected = PreAuthenticatedCredentialsNotFoundException.class)
	public void getPreAuthenticatedPrincipal_NullToken(){
		requestHeaderAuthenticationFilter.setAppToken(null);
		requestHeaderAuthenticationFilter.getPreAuthenticatedPrincipal(httpServletRequest);
	}

	@Test(expected = PreAuthenticatedCredentialsNotFoundException.class)
	public void getPreAuthenticatedPrincipal_InvalidToken(){
		when(httpServletRequest.getHeader("APP_TOKEN")).thenReturn(appToken.substring(1));
		requestHeaderAuthenticationFilter.getPreAuthenticatedPrincipal(httpServletRequest);
	}

	@Test
	public void setAppToken() {
		requestHeaderAuthenticationFilter.setAppToken(appToken);
		String actual = (String) ReflectionTestUtils.getField(requestHeaderAuthenticationFilter, "appToken");
		assertEquals(appToken, actual);
	}

}