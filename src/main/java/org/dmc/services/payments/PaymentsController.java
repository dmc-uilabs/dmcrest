package org.dmc.services.payments;

import org.dmc.services.data.entities.PaymentParentType;
import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.PaymentsStatus;
import org.dmc.services.organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@RestController
public class PaymentsController {

	@Autowired
	private PaymentsService paymentsService;
	
	@Autowired
	private OrganizationService organizationService;

	@RequestMapping(value = "/payment/organization", method = RequestMethod.POST)
	public ResponseEntity<PaymentsStatus> makePayment(@RequestBody OrgCreation orgCreation) {

		OrganizationModel orgModel = organizationService.save(orgCreation.getOrganizationModel());
		Charge charge = new Charge();
		charge.setStatus("failed");
		
		if (orgModel != null) {
			if(!orgModel.getIsPaid()) {
				String token = orgCreation.getStripeToken();
				try {
					//Will throw an exception if payment fails
					charge = paymentsService.createCharge(token, "Registration charge for " + orgModel.getName());
					organizationService.updatePaymentStatus(orgModel, charge.getPaid());
					paymentsService.savePayment(charge, orgModel.getId(), PaymentParentType.ORGANIZATION);
					return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), "Payment Successful!"), HttpStatus.OK);
				} catch (StripeException e) {
					organizationService.delete(orgModel.getId());
					return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), e.getMessage()), HttpStatus.OK);
				} catch (DataAccessException dae) {
					return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), "There was a problem in the database, please contact support."), HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), "Organization already paid!"), HttpStatus.OK);
			}
		
		} else {
			return new ResponseEntity<PaymentsStatus>(new PaymentsStatus(charge.getStatus(), "Organization creation failed!"), HttpStatus.OK);
		}

	}
	
	//@RequestMapping(value = "/payment/services/{id}", method = RequestMethod.POST)

}
