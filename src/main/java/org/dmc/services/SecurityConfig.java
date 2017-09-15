package org.dmc.services;

import org.dmc.services.security.DMCRequestHeaderAuthenticationFilter;
import org.dmc.services.security.UserPrincipalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Inject
	private UserPrincipalService userPrincipalService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper
				= new UserDetailsByNameServiceWrapper<>(userPrincipalService);
		PreAuthenticatedAuthenticationProvider preAuthenticatedProvider = new PreAuthenticatedAuthenticationProvider();
		preAuthenticatedProvider.setPreAuthenticatedUserDetailsService(wrapper);
		auth.authenticationProvider(preAuthenticatedProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterAfter(dmcRequestHeaderAuthenticationFilter(), ExceptionTranslationFilter.class)
				.authorizeRequests().anyRequest().permitAll()
				.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
				.and().httpBasic().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/users/create",
				"/user",
				"/companies",
				"/swagger-ui.html",
				"/webjars/springfox-swagger-ui/**",
				"/configuration/security",
				"/configuration/ui",
				"/swagger-resources",
				"/v2/api-docs");
	}
	
	//Uncomment to show SQL in console
//	@Bean
//	public JpaVendorAdapter jpaVendorAdapter() {
//		HibernateJpaVendorAdapter jpaVA = new HibernateJpaVendorAdapter();
//		jpaVA.setGenerateDdl(true);
//		jpaVA.setShowSql(true);
//		
//		return jpaVA;
//	}
	
	private DMCRequestHeaderAuthenticationFilter dmcRequestHeaderAuthenticationFilter() throws Exception {
		DMCRequestHeaderAuthenticationFilter authFilter = new DMCRequestHeaderAuthenticationFilter();
		authFilter.setCheckForPrincipalChanges(true);
		authFilter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
		authFilter.setAuthenticationManager(authenticationManager());
		return authFilter;
	}

	private AuthenticationEntryPoint authenticationEntryPoint(){
		return (request, response, authException) -> {
			HttpServletResponse httpResponse = response;
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Request!");
		};
	}
}
