package org.dmc.services.payments;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.data.entities.PaymentReceipt;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.entities.PaymentParentType;
import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.PaymentPlanType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.PaymentStatus;
import org.dmc.services.data.models.ServicePayment;
import org.dmc.services.data.repositories.PaymentPlanRepository;
import org.dmc.services.data.repositories.PaymentReceiptRepository;
import org.dmc.services.data.repositories.ServiceUsePermitRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.exceptions.ArgumentNotFoundException;
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
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Service
public class PaymentService {

	private final String FAILED = "failed";
	private final String SUCCESS = "succeeded";
	private final Integer T_3_PRICE = 50000;

	@Inject
	private PaymentReceiptRepository receiptRepository;

	@Inject
	private ServiceUsePermitRepository serviceUsePermitRepo;

	@Inject
	private UserRepository userRepository;

	@Inject
	private PaymentPlanRepository payPlanRepo;

	@Value("${STRIPE_SKEY:empty}")
	private String stripeSKey;

	@Value("${STRIPE_T_SKEY}")
	private String stripeTSKey;

	@PostConstruct
	public void init() {
		if ("empty".equals(stripeSKey)) {
			Stripe.apiKey = stripeTSKey;
		} else {
			Stripe.apiKey = stripeSKey;
		}
	}

	public Charge createOrgCharge(String token, String description) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", T_3_PRICE);
		params.put("currency", "usd");
		params.put("description", description);
		params.put("source", token);
		return Charge.create(params);

	}

	public Charge createCharge(String token, Integer amount, String description) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		params.put("currency", "usd");
		params.put("description", description);
		params.put("source", token);
		return Charge.create(params);
	}

	public void savePaymentReceipt(Charge charge, PaymentPlan plan, PaymentParentType parentType) {
		Integer userId = getCurrentUser().getId();
		PaymentReceipt receipt = new PaymentReceipt(userId, parentType, charge, plan);
		receiptRepository.save(receipt);
	}

	public void savePaymentReceipt(Charge charge, Integer parentId, PaymentParentType parentType) {
		Integer userId = getCurrentUser().getId();
		PaymentReceipt receipt = new PaymentReceipt(userId, parentId, parentType, charge);
		receiptRepository.save(receipt);
	}
	
	public void saveErrorReceipt(String errorDesc, PaymentPlan plan, PaymentParentType parentType) {
		Integer userId = getCurrentUser().getId();
		PaymentReceipt receipt = new PaymentReceipt(userId, parentType, FAILED, errorDesc, plan);
		receiptRepository.save(receipt);
	}

	public PaymentStatus processServicePayment(ServicePayment sp) throws StripeException, ArgumentNotFoundException {
		String token = sp.getStripeToken();
		Integer planId = sp.getPlanId();
		return processServicePayment(token, planId);
	}

	public PaymentStatus processServicePayment(String token, Integer planId) throws StripeException, ArgumentNotFoundException {
		PaymentStatus ps = new PaymentStatus();
		ps.setStatus(FAILED);
		ServiceUsePermit sup = new ServiceUsePermit();
		Charge charge = new Charge();
		PaymentPlan plan = new PaymentPlan();

		if (token == null || planId == null) {
			ps.setReason("Token and planId must not be null.");
		} else {
			plan = payPlanRepo.findOne(planId);
			if(plan == null) {
				throw new ArgumentNotFoundException("Could not find payment plan with given ID " + planId);
			}
				sup = serviceUsePermitRepo.save(createPermitFromPlan(plan));
			try {
				charge = createCharge(token, plan.getPrice(), "Charge for service: " + plan.getServiceId());
			} catch (StripeException e) {
				if(sup != null) {
					serviceUsePermitRepo.delete(sup);
				}
				saveErrorReceipt(e.getMessage(), plan, PaymentParentType.SERVICE);
				throw e;
			}
			ps.setStatus(SUCCESS);
			ps.setReason("Service payment successfully completed!");
			savePaymentReceipt(charge, plan, PaymentParentType.SERVICE);
		}
		return ps;
	}
	
	private ServiceUsePermit createPermitFromPlan(PaymentPlan plan) {
		User user = getCurrentUser();
		ServiceUsePermit sup = new ServiceUsePermit();
		sup.setOrganizationId(user.getOrganizationUser().getOrganization().getId());
		sup.setUserId(user.getId());
		sup.setServiceId(plan.getServiceId());
		if (plan.getPlanType() == PaymentPlanType.PAY_FOR_TIME) {
			sup.setUses(-1);
			sup.setExpirationDate(addDaysToCurrentDate(plan.getUses()));
		} else {
			sup.setUses(plan.getUses());
			// Expiration Date should be null/empty
		}
		return sup;
	}

	private Date addDaysToCurrentDate(Integer days) {
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);

		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	private User getCurrentUser() {
		return userRepository.findOne(
				((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
	}

}