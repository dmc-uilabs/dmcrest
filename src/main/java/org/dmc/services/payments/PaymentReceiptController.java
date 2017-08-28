package org.dmc.services.payments;

import java.util.List;

import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.PaymentReceiptModel;
import org.dmc.services.data.models.PaymentStatus;
import org.dmc.services.exceptions.TooManyAttemptsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;

@RestController
public class PaymentReceiptController {
	
	private final String logTag = PaymentReceiptController.class.getName();
	
	@Autowired
	private PaymentReceiptService receiptService;

	@RequestMapping(value = "/receipt/organization/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<PaymentReceiptModel>> getPaymentReceipts(@PathVariable("id") Integer orgId,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "Retrieving receipts for: " + userEPPN + " with Org Id: " + orgId);
		return new ResponseEntity<List<PaymentReceiptModel>>(receiptService.getPaymentReceiptsByOrgId(orgId), HttpStatus.OK);
	}
	
}
