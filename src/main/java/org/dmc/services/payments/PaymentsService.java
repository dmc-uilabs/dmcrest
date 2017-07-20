package org.dmc.services.payments;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

@Service
public class PaymentsService {

	@Value("${STRIPE_SKEY}")
	private String stripeSKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = stripeSKey;
	}

	public Charge createCharge(Map<String, Object> chargeParams) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {
		
		return Charge.create(chargeParams);
		
	}
}
