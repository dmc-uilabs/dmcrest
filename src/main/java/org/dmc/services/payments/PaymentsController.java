package org.dmc.services.payments;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@RestController
public class PaymentsController {
	
	@Autowired
	private PaymentsService paymentsService;
	
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public ResponseEntity makePayment(@RequestParam (value = "stripeToken", required = true) String token)
		throws StripeException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", 1000);
		params.put("currency", "usd");
		params.put("description", "Example charge");
		params.put("source", token);
		
		Charge charge = paymentsService.createCharge(params);
		
		return new ResponseEntity(charge.getStatus(), HttpStatus.OK);
		
	}
	
	@ExceptionHandler(StripeException.class)
	public ResponseEntity handleError(StripeException e) {
		return new ResponseEntity(e.getMessage(), HttpStatus.valueOf(e.getStatusCode()));
	}

}
