package org.dmc.services.payments;

import org.dmc.services.ServiceLogger;
import org.dmc.services.data.entities.PaymentParentType;
import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.PaymentStatus;
import org.dmc.services.data.models.ServicePayment;
import org.dmc.services.organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@RestController
public class PaymentsController {

	private final String logTag = PaymentsController.class.getName();

	@Autowired
	private PaymentService paymentsService;

	@Autowired
	private OrganizationService organizationService;

	@RequestMapping(value = "/payment/organization", method = RequestMethod.POST)
	public ResponseEntity<PaymentStatus> makeOrgPayment(@RequestBody OrgCreation orgCreation,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "organizationPaymentPOST, userEPPN: " + userEPPN);
		Charge charge = new Charge();
		OrganizationModel orgModel = organizationService.save(orgCreation.getOrganizationModel());
		String token = orgCreation.getStripeToken();
		if (orgModel == null) {
			ServiceLogger.log(logTag,
					"Database failed to create org named " + orgCreation.getOrganizationModel().getName());
			return new ResponseEntity<PaymentStatus>(
					new PaymentStatus(charge.getStatus(), "Organization creation failed!"), HttpStatus.OK);
		}
		if (orgModel.getIsPaid()) {
			ServiceLogger.log(logTag, orgModel.getName() + " is already paid for.");
			return new ResponseEntity<PaymentStatus>(
					new PaymentStatus(charge.getStatus(), "Organization already paid!"), HttpStatus.OK);
		}
		try {
			charge = paymentsService.createOrgCharge(token, "Registration charge for " + orgModel.getName());
			organizationService.updatePaymentStatus(orgModel, charge.getPaid());
			paymentsService.savePaymentReceipt(charge, orgModel.getId(), PaymentParentType.ORGANIZATION);
			return new ResponseEntity<PaymentStatus>(new PaymentStatus(charge.getStatus(), "Payment Successful!"), HttpStatus.OK);
		} catch (StripeException e) {
			ServiceLogger.log(logTag, "Stripe Exception Occurred: " + e.getMessage());
			organizationService.delete(orgModel.getId());
			return new ResponseEntity<PaymentStatus>(new PaymentStatus(charge.getStatus(), e.getMessage()), HttpStatus.OK);
		} catch (DataAccessException dae) {
			ServiceLogger.log(logTag, "Database Exception Occurred: " + dae.getMessage());
			return new ResponseEntity<PaymentStatus>(new PaymentStatus(charge.getStatus(),
					"There was a problem in the database, please contact support."), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/payment/service", method = RequestMethod.POST)
	public ResponseEntity<PaymentStatus> makeServicePayment(@RequestBody ServicePayment sp,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "servicePaymentPOST, userEPPN: " + userEPPN);
		return new ResponseEntity<PaymentStatus>(paymentsService.processServicePayment(sp), HttpStatus.OK);
	}
}
