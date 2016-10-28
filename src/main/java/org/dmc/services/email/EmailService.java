package org.dmc.services.email;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EmailService {

	private static final String EMAIL_URL;

	static {
		String host = System.getenv("verifyURL");
		if (host == null) {
			host = "localhost";
		}
		EMAIL_URL = "http://" + host + ":4000/";
	}

	public HttpStatus sendEmail(EmailModel emailModel) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<EmailModel> request = new HttpEntity<>(emailModel);
		ResponseEntity<String> response = restTemplate.exchange(EMAIL_URL, HttpMethod.POST, request, String.class);
		return response.getStatusCode();
	}

}
