package org.dmc.services.payments;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Service
public class PaymentsService {

	@Value("${STRIPE_SKEY}")
	private String stripeSKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = stripeSKey;
	}

	public Charge createCharge(String token) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", 50000);
		params.put("currency", "usd");
		params.put("description", "Example charge");
		params.put("source", token);
		//Will throw an exception if payment fails
		return Charge.create(params);
		
	}
	
}