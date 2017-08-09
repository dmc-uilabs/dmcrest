package org.dmc.services.esignature;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.dmc.services.data.models.eSignStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStreamReader;


@RestController
public class esignController {

	@Autowired
	private ESignService eSignService;

	@RequestMapping(value = "/esignDoc", method = RequestMethod.POST)
	public ResponseEntity<eSignStatus> signDocument(@RequestBody String CompanyInfo) {

			String response = "";

      try {
  				//Will throw an exception if esign fails
  				response = eSignService.eSignField(CompanyInfo);
					System.out.println("response " + response);
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "eSignature Successful!"), HttpStatus.OK);
  		} catch (Exception e) {
					// System.out.println("failed");
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "eSignature Failed!"), HttpStatus.BAD_REQUEST);
  		}
	}

	@RequestMapping(value = "/esignCheck", method = RequestMethod.GET)
	public ResponseEntity<eSignStatus> signCheck(@RequestBody String documentID) {

			String response = "";

      try {
  				//Will throw an exception if esign fails
  				response = eSignService.eSignCheck(documentID);
					System.out.println("response " + response);
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "eSignCheck Successful!"), HttpStatus.OK);
  		} catch (Exception e) {
					// System.out.println("failed");
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "eSignCheck Failed!"), HttpStatus.BAD_REQUEST);
  		}
	}

	@RequestMapping(value = "/esignCallback", method = RequestMethod.POST)
	public ResponseEntity<eSignStatus> signCallback(@RequestBody String documentID) {

			String response = "";

      try {
  				//Will throw an exception if esign fails
  				response = eSignService.eSignCallback(documentID);
					System.out.println("response " + response);
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "esignCallback Successful!"), HttpStatus.OK);
  		} catch (Exception e) {
					// System.out.println("failed");
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "esignCallback Failed!"), HttpStatus.BAD_REQUEST);
  		}
	}
}
