package org.dmc.services.payments;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.data.entities.Payment;
import org.dmc.services.data.entities.PaymentParentType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.PaymentRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	@Inject
	private PaymentRepository paymentRepository;
	
	@Inject
	private UserRepository userRepository;

	@Value("${STRIPE_SKEY:empty}")
	private String stripeSKey;
	
	@Value("${STRIPE_T_SKEY}")
	private String stripeTSKey;

	@PostConstruct
	public void init() {
		if("empty".equals(stripeSKey)) {
			Stripe.apiKey = stripeTSKey;
		} else {
			Stripe.apiKey = stripeSKey;
		}
	}

	public Charge createCharge(String token, String description) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", 50000);
		params.put("currency", "usd");
		params.put("description", description);
		params.put("source", token);
		//Will throw an exception if payment fails
		return Charge.create(params);
		
	}

	public void savePayment(Charge charge, Integer parentId, PaymentParentType parentType) {
		User user = getCurrentUser();
		Payment payment = new Payment(user, parentId, parentType, charge.getStatus(), BigDecimal.valueOf(charge.getAmount(), 2), charge.getId(), null);
		payment.setDescription(charge.getDescription());
		paymentRepository.save(payment);
	}
	
	private User getCurrentUser() {
		return userRepository.findOne(((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
	}
	
}