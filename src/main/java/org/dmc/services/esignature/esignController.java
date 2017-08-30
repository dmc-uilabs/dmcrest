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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import org.dmc.services.ServiceLogger;

//RestController for esignController.
@RestController
public class esignController {

	private final String logTag = esignController.class.getName();

	@Autowired
	private ESignService eSignService;

	//Match front end call /rest/esignDoc with POST method.
	@RequestMapping(value = "/esignDoc", method = RequestMethod.POST)
	public ResponseEntity<eSignStatus> signDocument(@RequestBody String CompanyInfo) {

			String response = "";
			ServiceLogger.log(logTag, "From frontend : " + CompanyInfo);

      try {
					//Call eSignService.eSignField for execute the API call.
  				//Will throw an exception if esign fails.
  				response = eSignService.eSignField(CompanyInfo);
					ServiceLogger.log(logTag, "Response from API :" + response);

					try{
						 //Jsonify the response string.
						 JSONObject jsonObj = new JSONObject(response);
						 //if 'fillable_form_id' not exists in object means an error call.
						 if (!jsonObj.has("fillable_form_id")){
							 	return new ResponseEntity<eSignStatus>(new eSignStatus("eSignature Failed!", response), HttpStatus.BAD_REQUEST);
						 }
						 else{
							 	JSONObject resultJSONObject = new JSONObject();
								//abstract 'fillable_form_id' and 'url' to resultJSONObject and return to front for use
								resultJSONObject.put("template_id", jsonObj.get("fillable_form_id"));
								resultJSONObject.put("url", jsonObj.getString("url"));
								return new ResponseEntity<eSignStatus>(new eSignStatus("eSignature Successful!", resultJSONObject.toString()), HttpStatus.OK);
						 }
					}catch (Exception e) {
							e.printStackTrace();
				      return new ResponseEntity<eSignStatus>(new eSignStatus("eSignature Failed!", response), HttpStatus.BAD_REQUEST);
					}

  		} catch (Exception e) {
					return new ResponseEntity<eSignStatus>(new eSignStatus("eSignature Failed!", null), HttpStatus.BAD_REQUEST);
  		}
	}

	//Match front end call /rest/esignCheck/{LinkToFillID} with GET method.
	@RequestMapping(value = "/esignCheck/{LinkToFillID}", method = RequestMethod.GET)
	public ResponseEntity<eSignStatus> signCheck(@PathVariable("LinkToFillID") String LinkToFillID,
																							 @RequestHeader(value = "AJP_eppn", required = true) String UserEPPN) {

			String response = "";

      try {
					//Call eSignService.eSignCheck for execute the API call.
  				//Will throw an exception if esign fails
  				response = eSignService.eSignCheck(LinkToFillID);

					try{
						 //Jsonify the response string.
						 JSONObject jsonObj = new JSONObject(response);
						 JSONObject resultJsonObject = new JSONObject();

						 //if 'errors' in object means an error call.
						 if (jsonObj.has("errors")){
							 	return new ResponseEntity<eSignStatus>(new eSignStatus("eSignCheck Failed!", jsonObj.getString("errors")), HttpStatus.BAD_REQUEST);
						 }
						 //if 'null' in object means an exception call.
						 else if (response == "null"){
							 	return new ResponseEntity<eSignStatus>(new eSignStatus("eSignCheck Failed!", "Error when calling the API"), HttpStatus.BAD_REQUEST);
						 }
						 else{
							 	//'items' contain signature informations as an array of objects.
							 	JSONArray eSignItems = jsonObj.getJSONArray("items");
								JSONArray eSignResultItems = new JSONArray();
								resultJsonObject.put("total", jsonObj.get("total"));
								//Check total signature quantities
							 	if ((int)jsonObj.get("total") >= 1){

										//If more than 1, compare with each of the 'token.data.userEPPN' field in 'items' to mark verified user
										for (int i = 0; i < eSignItems.length(); i++){
												JSONObject iterative = eSignItems.getJSONObject(i);
												String signatureToken = "";
												if (!iterative.getJSONObject("token").isNull("data")){
														signatureToken = iterative.getJSONObject("token").getJSONObject("data").getString("userEPPN");
												}
												iterative.remove("token");
												iterative.remove("additional_documents");
												//If token info with userEPPN matches, mark as same, else mark as different
												if (signatureToken.equals(UserEPPN)){
														iterative.put("user", "same");
												}
												else{
														iterative.put("user", "different");
												}
												eSignResultItems.put(iterative);
										}
								}
								resultJsonObject.put("items", eSignResultItems);
								return new ResponseEntity<eSignStatus>(new eSignStatus("eSignCheck Successful!", resultJsonObject.toString()), HttpStatus.OK);
						 }

					}catch (Exception e) {
						 e.printStackTrace();
				     return new ResponseEntity<eSignStatus>(new eSignStatus("eSignCheck Failed!", response), HttpStatus.BAD_REQUEST);
					}

  		} catch (Exception e) {
					return new ResponseEntity<eSignStatus>(new eSignStatus("eSignCheck Failed!", "Failed to call API!"), HttpStatus.BAD_REQUEST);
  		}
	}

	//Match front end call /esignToken with GET method.
	@RequestMapping(value = "/esignToken", method = RequestMethod.GET)
	public ResponseEntity<eSignStatus> signToken(@RequestHeader(value = "AJP_eppn", required = true) String UserEPPN) {

			String response = "";

      try {
					//Call eSignService.eSignToken for execute the API call.
  				//Will throw an exception if esign fails
  				response = eSignService.eSignToken(UserEPPN);

					try{
						 JSONObject jsonObj = new JSONObject(response);
						 //if 'hash' contained in the response, return this token to front.
						 if (jsonObj.has("hash")){
						 		return new ResponseEntity<eSignStatus>(new eSignStatus("eSignToken Successful!", jsonObj.getString("hash")), HttpStatus.OK);
						 }
						 else{
							  return new ResponseEntity<eSignStatus>(new eSignStatus("eSignToken Failed!", response), HttpStatus.BAD_REQUEST);
						 }
					}catch (Exception e) {
						 e.printStackTrace();
				     return new ResponseEntity<eSignStatus>(new eSignStatus("eSignToken Failed!", response), HttpStatus.BAD_REQUEST);
					}

  		} catch (Exception e) {
					return new ResponseEntity<eSignStatus>(new eSignStatus("eSignToken Failed!", "eSignToken Failed!"), HttpStatus.BAD_REQUEST);
  		}
	}

	// Comment /rest/esignCallback if needed in future.
	// @RequestMapping(value = "/esignCallback", method = RequestMethod.POST)
	// public ResponseEntity<eSignStatus> signCallback(@RequestBody String documentID) {
	//
	// 		String response = "";
	//
  //     try {
  // 				//Will throw an exception if esign fails
  // 				response = eSignService.eSignCallback(documentID);
	// 				System.out.println("response " + response);
	// 				return new ResponseEntity<eSignStatus>(new eSignStatus(response, "esignCallback Successful!"), HttpStatus.OK);
  // 		} catch (Exception e) {
	// 				// System.out.println("failed");
	// 				return new ResponseEntity<eSignStatus>(new eSignStatus(response, "esignCallback Failed!"), HttpStatus.BAD_REQUEST);
  // 		}
	// }
}
