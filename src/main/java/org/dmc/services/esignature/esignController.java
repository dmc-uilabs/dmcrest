package org.dmc.services.esignature;

import java.util.HashMap;
import java.util.Map;

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
import java.net.URL;
import java.net.URLConnection;


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
					System.out.println("response" + response);
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "eSignature Successful!"), HttpStatus.OK);
  				//Should be true if payment was successful
  				// if(response == 200)
  				//     return new ResponseEntity<eSignStatus>(new eSignStatus(charge.getStatus(), "eSignature Successful!"), HttpStatus.OK);
          // else
          //     return new ResponseEntity<eSignStatus>(new eSignStatus(charge.getStatus(), "eSignature Failed!"), HttpStatus.OK);
  		} catch (Exception e) {
  				// return new ResponseEntity<eSignStatus>(new eSignStatus(charge.getStatus(), "eSignature failed!"), HttpStatus.OK);
					System.out.println("failed");
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "eSignature Failed!"), HttpStatus.OK);
  		}
	}

}
