package org.dmc.services.email;

import java.util.HashMap;
import java.io.IOException;

import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;

@Component
public class EmailService {

	private static final String EMAIL_URL;

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Inject
	private UserRepository userRepository;

	static {
		String host = System.getenv("verifyURL");
		if (host == null) {
			host = "localhost";
		}
		EMAIL_URL = "http://" + host + ":4000/";
	}

	public ResponseEntity sendEmail(User user, Integer template, HashMap params) {
		if (user.getEmail() == null) return ResponseEntity.badRequest().body("User does not have an email!");

		String jsonParams = "";

		try {
			ObjectMapper mapperObj = new ObjectMapper();
			jsonParams = mapperObj.writeValueAsString(params);
		} catch (IOException e) {
			logger.warn("Failed to parse params to JSON");
		}

		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User requester = this.userRepository.findByUsername(userPrincipal.getUsername());

		EmailModel emailModel = new EmailModel();
		emailModel.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));
		emailModel.setEmail(user.getEmail());
		// emailModel.setToken(token);
		emailModel.setParams(jsonParams);
		emailModel.setTemplate(template);
		emailModel.setRequester(requester.getEmail());

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<EmailModel> request = new HttpEntity<>(emailModel);
		ResponseEntity<String> response = restTemplate.exchange(EMAIL_URL, HttpMethod.POST, request, String.class);

		if (!HttpStatus.OK.equals(response.getStatusCode())) {
			logger.warn("Email for user token was not sent for user: {}", user.getEmail());
		}

		return response;
	}

}
