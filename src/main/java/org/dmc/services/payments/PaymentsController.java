package org.dmc.services.payments;

import java.util.HashMap;
import java.util.Map;

import org.dmc.services.data.models.OrgCreation;
import org.dmc.services.data.models.OrganizationModel;
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

@RestController
public class PaymentsController {

	@Autowired
	private PaymentsService paymentsService;
	
	@Autowired
	private OrganizationService organizationService;

	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public ResponseEntity<String> makePayment(@RequestBody OrgCreation orgCreation) {

		OrganizationModel orgModel = organizationService.save(orgCreation.getOrganizationModel());
		
		if (orgModel != null) {
			String token = orgCreation.getStripeToken();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("amount", 50000);
			params.put("currency", "usd");
			params.put("description", "Example charge");
			params.put("source", token);
			try {
				//Will throw an exception if payment fails
				Charge charge = paymentsService.createCharge(params);
				//Should be true if payment was successful
				if(charge.getPaid()) {
					organizationService.updatePayment(orgModel, true);
				}
				return new ResponseEntity<String>(charge.getStatus(), HttpStatus.OK);
			} catch (StripeException e) {
				organizationService.delete(orgModel.getId());
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
			}
		
		} else {
			return new ResponseEntity<String>("Organization creation failed!", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
