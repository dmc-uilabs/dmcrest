package org.dmc.services.payments;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.PaymentStatus;
import org.dmc.services.data.models.ServicePayment;
import org.dmc.services.data.models.ServiceUsePermitModel;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.exceptions.TooManyAttemptsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;

@RestController
public class PaymentsController {

	private final String logTag = PaymentsController.class.getName();

	@Autowired
	private PaymentService paymentsService;

	@RequestMapping(value = "/payment/organization", method = RequestMethod.POST)
	public ResponseEntity<PaymentStatus> makeOrgPayment(@RequestBody OrgCreation orgCreation,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws StripeException, TooManyAttemptsException {
		ServiceLogger.log(logTag, "organizationPaymentPOST, userEPPN: " + userEPPN);
		return new ResponseEntity<PaymentStatus>(paymentsService.processOrganizationPayment(orgCreation), HttpStatus.OK);
	}

	@RequestMapping(value = "/payment/plan/stripe", method = RequestMethod.POST)
	public ResponseEntity<PaymentStatus> makeStripePayment(@RequestBody ServicePayment sp,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
					throws StripeException, ArgumentNotFoundException, TooManyAttemptsException {
		ServiceLogger.log(logTag, "servicePaymentPOST, userEPPN: " + userEPPN);
		return new ResponseEntity<PaymentStatus>(paymentsService.processStripeServicePayment(sp), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/payment/plan/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> makeServicePayment(@PathVariable("id") Integer planId,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
					throws StripeException, ArgumentNotFoundException {
		ServiceLogger.log(logTag, "servicePaymentPOST, userEPPN: " + userEPPN);
		return new ResponseEntity<ServiceUsePermitModel>(paymentsService.processInternalPayment(planId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/payment/account", method = RequestMethod.POST)
	public ResponseEntity<PaymentStatus> addFundsToAccount(@RequestParam (value = "stripeToken", required = true) String token,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws StripeException, TooManyAttemptsException {
		ServiceLogger.log(logTag, "Adding funds to Org account for user: " + userEPPN);
		return new ResponseEntity<PaymentStatus>(paymentsService.addFundsToAccount(token), HttpStatus.OK);
	}
	
	/* Exception Handling */
	@ExceptionHandler(StripeException.class)
	public ResponseEntity<?> handleStripeException(StripeException e) {
		ServiceLogger.logException(logTag, new DMCServiceException(DMCError.PaymentError, e.getMessage()));
		PaymentStatus status = new PaymentStatus("failed", e.getMessage());
		return new ResponseEntity<PaymentStatus>(status, HttpStatus.OK);
	}
	
	@ExceptionHandler(TooManyAttemptsException.class)
	public ResponseEntity<?> handleTMAException(TooManyAttemptsException e) {
		ServiceLogger.logException(logTag, new DMCServiceException(DMCError.PaymentError, e.getMessage()));
		PaymentStatus status = new PaymentStatus("failed", e.getMessage());
		return new ResponseEntity<PaymentStatus>(status, HttpStatus.OK);
	}
	
	@ExceptionHandler(ArgumentNotFoundException.class)
	public ResponseEntity<?> handleANFException(ArgumentNotFoundException e) {
		ServiceLogger.logException(logTag, new DMCServiceException(DMCError.NotFound, e.getMessage()));
		PaymentStatus status = new PaymentStatus("failed", e.getMessage());
		return new ResponseEntity<PaymentStatus>(status, HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		ServiceLogger.logException(logTag, new DMCServiceException(DMCError.Generic, e.getMessage()));
		PaymentStatus status = new PaymentStatus("failed", e.getMessage());
		return new ResponseEntity<PaymentStatus>(status, HttpStatus.OK);
	}
	
}
