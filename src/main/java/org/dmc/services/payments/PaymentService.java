package org.dmc.services.payments;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.data.entities.ServiceUsePermit;
import org.apache.commons.lang3.time.DateUtils;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.OrganizationUserService;
import org.dmc.services.ServiceUsePermitService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.PaymentParentType;
import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.PaymentPlanType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.PaymentStatus;
import org.dmc.services.data.models.ServicePayment;
import org.dmc.services.data.models.ServiceUsePermitModel;
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
	private final Integer MINIMUM_FUND_ADD = 2000;

	@Inject
	private ServiceUsePermitRepository serviceUsePermitRepo;

	@Inject
	private UserService userService;

	@Inject
	private PaymentPlanRepository payPlanRepo;

	@Inject
	private PaymentReceiptService receiptService;

	@Inject
	private OrganizationRepository orgRepo;

	@Inject
	private OrganizationService orgService;
	
	@Inject
	private OrganizationUserService orgUserService;

	@Inject
	private ServiceUsePermitService supService;
	
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
		
		PaymentStatus ps = new PaymentStatus();
		Charge charge = new Charge();
		
		if (token == null || orgModel == null) {
			ps.setReason("Token and organization must not be null.");
		} else if (orgModel.getIsPaid()) {
			ps.setReason("Organization already paid!");
		} else {
		
		User user = userService.getCurrentUser();
		userService.canCreateOrg(user.getId());
		
		if(receiptService.getPaymentFailureCount(user) > MAX_ATTEMPTS) {
			throw new TooManyAttemptsException();
		}
		
		if(orgModel.getId() == null) {
			Integer existingOrgId = orgService.findByUser().getId();
			if(existingOrgId != null) {
				orgModel.setId(existingOrgId);
			}
		}
		
		orgModel = orgService.save(orgModel);

		
			try {
				charge = createCharge(token, T_3_PRICE,
						"Registration charge for " + orgModel.getName() + " with id: " + orgModel.getId());
			} catch (StripeException e) {
				orgService.delete(orgModel.getId());
				orgUserService.verifyUnverifyExistingUser(user.getId(), false);
				receiptService.savePaymentReceipt(user.getId(), getCurrentOrg(user), orgModel.getId(),
						PaymentParentType.ORGANIZATION, FAILED, T_3_PRICE, null, e.getMessage(), null);
				throw e;
			}
			orgService.updatePaymentStatus(orgModel, charge.getPaid());
			ps.setStatus(SUCCESS);
			ps.setReason("Payment successfully completed!");
			receiptService.savePaymentReceipt(user.getId(), getCurrentOrg(user), orgModel.getId(),
					PaymentParentType.ORGANIZATION, charge.getStatus(), T_3_PRICE, charge.getId(),
					charge.getDescription(), null);
		}
		return ps;
	}

	public ServiceUsePermitModel processInternalPayment(Integer planId, Integer quantity) throws ArgumentNotFoundException {
		ServiceUsePermit sup = new ServiceUsePermit();
		PaymentPlan plan = new PaymentPlan();
		
		User user = userService.getCurrentUser();

		if (planId == null) {
			throw new DMCServiceException(DMCError.InvalidArgument, "Plan must not be null!");
		} else {
			plan = payPlanRepo.findOne(planId);
			if (plan == null) {
				throw new ArgumentNotFoundException("Could not find payment plan with given ID " + planId);
			}
			sup = serviceUsePermitRepo.save(processPermitFromPlan(plan, quantity));
			Organization org = getCurrentOrg(userService.getCurrentUser());
			int totalPrice = plan.getPrice() * quantity;
			Integer newBalance = org.getAccountBalance() - (totalPrice);
			if (newBalance > 0) {
				org.setAccountBalance(newBalance);
			} else {
				serviceUsePermitRepo.delete(sup);
				throw new DMCServiceException(DMCError.PaymentError,
						"INSUFFICIENT FUNDS AVAILABLE : $" + BigDecimal.valueOf(totalPrice, 2) + " REQUIRED. $"
								+ BigDecimal.valueOf(org.getAccountBalance(), 2) + " AVAILABLE.");
			}
			try {
				orgRepo.save(org);
			} catch (Exception e) {
				if (sup != null) {
					serviceUsePermitRepo.delete(sup);
				}
				receiptService.savePaymentReceipt(user.getId(), getCurrentOrg(user), plan.getServiceId(),
						PaymentParentType.SERVICE, FAILED, totalPrice, null, e.getMessage(), plan);
				throw e;
			}
			receiptService.savePaymentReceipt(user.getId(), getCurrentOrg(user), plan.getServiceId(),
					PaymentParentType.SERVICE, SUCCESS, totalPrice, null,
					"INTERNAL charge for service with id: " + plan.getServiceId(), plan);
		}
		return supService.mapToModel(sup);
	}

	public PaymentStatus addFundsToAccount(String token, int amount) throws StripeException, TooManyAttemptsException {
		
		User user = userService.getCurrentUser();
		
		if(amount < MINIMUM_FUND_ADD) {
			throw new DMCServiceException(DMCError.InvalidArgument, "Minimum amount of $20.00 required!");
		}

		if(receiptService.getPaymentFailureCount(user) > MAX_ATTEMPTS) {
			throw new TooManyAttemptsException();
		}
		checkValidOrg();

		Organization org = user.getOrganizationUser().getOrganization();
		org.setAccountBalance(org.getAccountBalance() + amount);
		orgRepo.save(org);
		try {
			Charge charge = createCharge(token, amount, "Adding funds to organization account: " + org.getId());
			receiptService.savePaymentReceipt(user.getId(), getCurrentOrg(user), org.getId(),
					PaymentParentType.ACCOUNT, SUCCESS, amount, charge.getId(), charge.getDescription(), null);
		} catch (StripeException e) {
			org.setAccountBalance(org.getAccountBalance() - amount);
			orgRepo.save(org);
			receiptService.savePaymentReceipt(user.getId(), getCurrentOrg(user), org.getId(),
					PaymentParentType.ACCOUNT, FAILED, amount, null, e.getMessage(), null);
			throw e;
		}

		return new PaymentStatus(SUCCESS, "Organization account balance successfully updated to: $"
				+ BigDecimal.valueOf(org.getAccountBalance(), 2));
	}

	public PaymentStatus processStripeServicePayment(ServicePayment sp)
			throws StripeException, ArgumentNotFoundException, TooManyAttemptsException {
		String token = sp.getStripeToken();
		Integer planId = sp.getPlanId();
		return processStripeServicePayment(token, planId);
	}

	public PaymentStatus processStripeServicePayment(String token, Integer planId)
			throws StripeException, TooManyAttemptsException {
		
		User user = userService.getCurrentUser();

		if(receiptService.getPaymentFailureCount(user) > MAX_ATTEMPTS) {
			throw new TooManyAttemptsException();
		}
		checkValidOrg();

		PaymentStatus ps = new PaymentStatus();
		ServiceUsePermit sup = new ServiceUsePermit();
		Charge charge = new Charge();
		PaymentPlan plan = new PaymentPlan();

		if (token == null || planId == null) {
			ps.setReason("Token and planId must not be null.");
		} else {
			plan = payPlanRepo.findOne(planId);
			if (plan == null) {
				throw new DMCServiceException(DMCError.NoContentInQuery,
						"Could not find payment plan with given ID " + planId);
			}
			sup = serviceUsePermitRepo.save(processPermitFromPlan(plan, 1));
			try {
				charge = createCharge(token, plan.getPrice(), "Charge for service: " + plan.getServiceId());
			} catch (StripeException e) {
				if (sup != null) {
					serviceUsePermitRepo.delete(sup);
				}
				receiptService.savePaymentReceipt(user.getId(), getCurrentOrg(user), plan.getServiceId(),
						PaymentParentType.SERVICE, FAILED, plan.getPrice(), null, e.getMessage(), plan);
				throw e;
			}
			ps.setStatus(SUCCESS);
			ps.setReason("Service payment successfully completed!");
			receiptService.savePaymentReceipt(user.getId(), getCurrentOrg(user), charge, plan,
					PaymentParentType.SERVICE);
		}
		return ps;
	}

	/* Utility methods BEGIN */
	private void checkValidOrg() {
		User user = userService.getCurrentUser();
		if (!getCurrentOrg(user).getIsPaid()) {
			throw new DMCServiceException(DMCError.OrganizationNotPaid,
					"Unauthorized. Organization with id: " + getCurrentOrg(user).getId() + " not paid!");
		}
	}

	private ServiceUsePermit processPermitFromPlan(PaymentPlan plan, Integer quantity) {
		User user = userService.getCurrentUser();

		//Get existing permit if available
		ServiceUsePermit sup = supService.getServiceUsePermitByOrganizationIdAndServiceId(getCurrentOrg(user).getId(),
				plan.getServiceId());
		
		//Create new permit
		if (sup == null) {
			sup = new ServiceUsePermit();
			sup.setOrganizationId(getCurrentOrg(user).getId());
			sup.setUserId(user.getId());
			sup.setServiceId(plan.getServiceId());
		} else if(sup.getUses() == -1) {
				throw new DMCServiceException(DMCError.PaymentError, "Organization already possesses unlimited uses of service!");
		}

		if (plan.getPlanType() == PaymentPlanType.PAY_FOR_TIME) {
			sup.setExpirationDate(addDaysToDate((plan.getUses() * quantity), sup.getExpirationDate()));
		}
		//Unlimited uses
		else if (plan.getPlanType() == PaymentPlanType.PAY_ONCE) {
			sup.setUses(plan.getUses());
		} else {
			Integer uses = sup.getUses() == null ? 0 : sup.getUses();
			uses += (plan.getUses() * quantity);
			sup.setUses(uses);
		}

		return sup;
	}

	private Date addDaysToDate(Integer days, Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	private Organization getCurrentOrg(User user) {
		OrganizationUser orgUser = user.getOrganizationUser();
		if(orgUser != null) {
			return orgUser.getOrganization();
		} else {
			return null;
		}
	}

	/* Utility methods END */

}