package org.dmc.services.payments;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.data.entities.PaymentReceipt;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.PaymentParentType;
import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.PaymentPlanType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.PaymentStatus;
import org.dmc.services.data.models.ServicePayment;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.PaymentPlanRepository;
import org.dmc.services.data.repositories.PaymentReceiptRepository;
import org.dmc.services.data.repositories.ServiceUsePermitRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.exceptions.TooManyAttemptsException;
import org.dmc.services.organization.OrganizationService;
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
	private final Integer MAX_ATTEMPTS = 10;

	@Inject
	private PaymentReceiptRepository receiptRepository;

	@Inject
	private ServiceUsePermitRepository serviceUsePermitRepo;

	@Inject
	private UserRepository userRepository;

	@Inject
	private PaymentPlanRepository payPlanRepo;

	@Inject
	private OrganizationRepository orgRepo;

	@Inject
	private OrganizationService orgService;

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

	// Charge for provided amount/token
	public Charge createCharge(String token, Integer amount, String description) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		params.put("currency", "usd");
		params.put("description", description);
		params.put("source", token);
		return Charge.create(params);
	}

	public PaymentStatus processOrganizationPayment(OrgCreation oc) throws StripeException, TooManyAttemptsException {
		String token = oc.getStripeToken();
		OrganizationModel orgModel = oc.getOrganizationModel();
		return processOrganizationPayment(token, orgModel);
	}

	public PaymentStatus processOrganizationPayment(String token, OrganizationModel orgModel)
			throws StripeException, TooManyAttemptsException {
		checkPaymentAttempts();
		PaymentStatus ps = new PaymentStatus();
		Charge charge = new Charge();

		orgModel = orgService.save(orgModel);

		if (token == null || orgModel == null) {
			ps.setReason("Token and organization must not be null.");
		} else if (orgModel.getIsPaid()) {
			ps.setReason("Organization already paid!");
		} else {
			try {
				charge = createCharge(token, T_3_PRICE,
						"Registration charge for " + orgModel.getName() + " with id: " + orgModel.getId());
			} catch (StripeException e) {
				orgService.delete(orgModel.getId());
				savePaymentReceipt(orgModel.getId(), PaymentParentType.SERVICE, FAILED, T_3_PRICE, null, e.getMessage(),
						null);
				throw e;
			}
			orgService.updatePaymentStatus(orgModel, charge.getPaid());
			ps.setStatus(SUCCESS);
			ps.setReason("Payment successfully completed!");
			savePaymentReceipt(orgModel.getId(), PaymentParentType.SERVICE, charge.getStatus(), T_3_PRICE,
					charge.getId(), charge.getDescription(), null);
		}
		return ps;
	}

	public PaymentStatus processStripePayment(ServicePayment sp)
			throws StripeException, ArgumentNotFoundException, TooManyAttemptsException {
		String token = sp.getStripeToken();
		Integer planId = sp.getPlanId();
		return processStripePayment(token, planId);
	}

	public PaymentStatus processStripePayment(String token, Integer planId)
			throws StripeException, ArgumentNotFoundException, TooManyAttemptsException {
		checkPaymentAttempts();
		PaymentStatus ps = new PaymentStatus();
		ServiceUsePermit sup = new ServiceUsePermit();
		Charge charge = new Charge();
		PaymentPlan plan = new PaymentPlan();

		if (token == null || planId == null) {
			ps.setReason("Token and planId must not be null.");
		} else {
			plan = payPlanRepo.findOne(planId);
			if (plan == null) {
				throw new ArgumentNotFoundException("Could not find payment plan with given ID " + planId);
			}
			sup = serviceUsePermitRepo.save(createPermitFromPlan(plan));
			try {
				charge = createCharge(token, plan.getPrice(), "Charge for service: " + plan.getServiceId());
			} catch (StripeException e) {
				if (sup != null) {
					serviceUsePermitRepo.delete(sup);
				}
				savePaymentReceipt(plan.getServiceId(), PaymentParentType.SERVICE, FAILED, plan.getPrice(), null,
						e.getMessage(), plan);
				throw e;
			}
			ps.setStatus(SUCCESS);
			ps.setReason("Service payment successfully completed!");
			savePaymentReceipt(charge, plan, PaymentParentType.SERVICE);
		}
		return ps;
	}

	public PaymentStatus processInternalPayment(ServicePayment sp) throws ArgumentNotFoundException {
		PaymentStatus ps = new PaymentStatus();
		ServiceUsePermit sup = new ServiceUsePermit();
		PaymentPlan plan = new PaymentPlan();

		if (sp.getStripeToken() == null || sp.getPlanId() == null) {
			ps.setReason("Token and planId must not be null.");
		} else {
			plan = payPlanRepo.findOne(sp.getPlanId());
			if (plan == null) {
				throw new ArgumentNotFoundException("Could not find payment plan with given ID " + sp.getPlanId());
			}
			sup = serviceUsePermitRepo.save(createPermitFromPlan(plan));
			Organization org = getCurrentOrg();
			Integer newBalance = org.getAccountBalance() - plan.getPrice();
			if (newBalance > 0) {
				org.setAccountBalance(newBalance);
			} else {
				ps.setReason("INSUFFICIENT FUNDS AVAILABLE : $" + BigDecimal.valueOf(plan.getPrice(), 2)
						+ " REQUIRED. $" + BigDecimal.valueOf(org.getAccountBalance(), 2) + " AVAILABLE.");
				return ps;
			}
			try {
				orgRepo.save(org);
			} catch (Exception e) {
				if (sup != null) {
					serviceUsePermitRepo.delete(sup);
				}
				savePaymentReceipt(plan.getServiceId(), PaymentParentType.SERVICE, FAILED, plan.getPrice(), null,
						e.getMessage(), plan);
			}
			ps.setStatus(SUCCESS);
			ps.setReason("Service payment successfully completed!");
			savePaymentReceipt(plan.getServiceId(), PaymentParentType.SERVICE, SUCCESS, plan.getPrice(), null,
					"INTERNAL charge for service with id: " + plan.getServiceId(), plan);
		}
		return ps;
	}

	public PaymentStatus addFundsToAccount(String token) throws StripeException, TooManyAttemptsException {
		checkPaymentAttempts();
		Integer price = T_3_PRICE;
		Organization org = getCurrentUser().getOrganizationUser().getOrganization();
		org.setAccountBalance(org.getAccountBalance() + price);
		orgRepo.save(org);
		try {
			Charge charge = createCharge(token, price, "Adding funds to organization account: " + org.getId());
			savePaymentReceipt(org.getId(), PaymentParentType.ACCOUNT, SUCCESS, price, charge.getId(),
					charge.getDescription(), null);
		} catch (StripeException e) {
			org.setAccountBalance(org.getAccountBalance() - price);
			orgRepo.save(org);
			savePaymentReceipt(org.getId(), PaymentParentType.ACCOUNT, FAILED, price, null, e.getMessage(), null);
			throw e;
		}

		return new PaymentStatus(SUCCESS,
				"Organization account balance successfully updated to: " + org.getAccountBalance());
	}

	/* Payment Receipt methods BEGIN */
	public void savePaymentReceipt(Charge charge, PaymentPlan plan, PaymentParentType parentType) {
		savePaymentReceipt(plan.getServiceId(), parentType, charge.getStatus(), Math.toIntExact(charge.getAmount()),
				charge.getId(), charge.getDescription(), plan);
	}

	public void savePaymentReceipt(Integer parentId, PaymentParentType parentType, String status, Integer amount,
			String chargeId, String desc, PaymentPlan plan) {
		Integer userId = getCurrentUser().getId();
		Organization org = getCurrentOrg();
		PaymentReceipt receipt = new PaymentReceipt(userId, org.getId(), parentId, parentType, status, amount, chargeId,
				desc, plan, org.getAccountBalance());
		receiptRepository.save(receipt);
	}
	/* Payment Receipt methods END */

	/* Utility methods BEGIN */
	private void checkPaymentAttempts() throws TooManyAttemptsException {
		if (receiptRepository.countByUserIdAndDateAndStatus(getCurrentUser().getId(), new Date(),
				FAILED) > MAX_ATTEMPTS) {
			throw new TooManyAttemptsException();
		}
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

	private Organization getCurrentOrg() {
		return getCurrentUser().getOrganizationUser().getOrganization();
	}
	/* Utility methods END */

}