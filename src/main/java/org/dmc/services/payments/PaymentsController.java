package org.dmc.services.payments;

import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.PaymentsStatus;
import org.dmc.services.organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.dmc.services.exceptions.InvalidFilterParameterException;

@RestController
public class PaymentsController {

	@Autowired
	private PaymentsService paymentsService;

	@Autowired
	private OrganizationService organizationService;

	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public ResponseEntity<PaymentsStatus> makePayment(@RequestBody OrgCreation orgCreation) {

		OrganizationModel orgModel = organizationService.save(orgCreation.getOrganizationModel());
		Charge charge = new Charge();
		charge.setStatus("failed");

		if (orgModel != null) {
			String token = orgCreation.getStripeToken();
			try {
				//Will throw an exception if payment fails
				charge = paymentsService.createCharge(token, orgModel.getName());
				//Should be true if payment was successful
				if(charge.getPaid()) {
					organizationService.updatePayment(orgModel, true);
				}
				return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), "Payment Successful!"), HttpStatus.OK);
			} catch (StripeException e) {
				try {
					organizationService.delete(orgModel.getId());
				} catch (InvalidFilterParameterException fpe) {
					return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), "Payment unsuccessful, org could not be deleted"), HttpStatus.OK);
				}
				return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), e.getMessage()), HttpStatus.OK);
			}

		} else {
			return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), "Org creation failed!"), HttpStatus.OK);
		}

	}

}
