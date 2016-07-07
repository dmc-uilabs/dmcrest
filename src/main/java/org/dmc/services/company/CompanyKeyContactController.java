package org.dmc.services.company;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.DMCServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpHeaders;

import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

import static org.springframework.http.MediaType.*;

@RestController

public class CompanyKeyContactController {
	private CompanyKeyContactDao keyContactDao = new CompanyKeyContactDao();
	private final String logTag = CompanyKeyContactController.class.getName();

	@RequestMapping(value = "/company_key_contacts", produces = { "application/json"}, method = RequestMethod.POST)
	public ResponseEntity<Id> companyKeyContactsPost(
			@RequestBody CompanyKeyContact companyKeyContact,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		try {
			int id = this.keyContactDao.createCompanyKeyContact(companyKeyContact,
					userEPPN);
			Id retrunId = new Id.IdBuilder(id).build();
			ServiceLogger.log(this.logTag,"Before return.  id=" + id);
			int statusCode = HttpStatus.OK.value();
			return new ResponseEntity<Id>(retrunId, HttpStatus.valueOf(statusCode));
		} catch (DMCServiceException e) {
			ServiceLogger.log(this.logTag, "Exception:" + e.getMessage());
			// HttpHeaders headers = new HttpHeaders();
			//return new ResponseEntity<String>("DMCServerException:"
			//		+ e.getErrorMessage(), headers, HttpStatus.BAD_REQUEST);
			return new ResponseEntity<Id>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ServiceLogger.log(this.logTag, "Exception:" + e.getMessage());
			//return new ResponseEntity<CompanySkill>(companySkill,
			//		HttpStatus.BAD_REQUEST);
			return new ResponseEntity<Id>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/company_key_contacts/{contactID}", produces = {"application/json"}, method = RequestMethod.DELETE)
	public ResponseEntity<Void> companyKeyContactsContactIDDelete(
			@PathVariable("contactID") String contactID,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		try {
			int status = this.keyContactDao.deleteCompanyKeyContact(contactID, userEPPN);
			ServiceLogger.log(this.logTag, "User: " + userEPPN
					+ " deleted:" + contactID + "with Status:" + status);
			return new ResponseEntity<Void>(
					HttpStatus.OK);
		} catch (Exception e) {
			ServiceLogger.log(this.logTag, "Exception:" + e.getMessage());
			// return new
			// ResponseEntity<String>(HttpStatus.valueOf(e.getStatusCode()));
			return new ResponseEntity<Void>(
					HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/company_key_contacts/{contactID}", produces = {
			"application/json"}, method = RequestMethod.PATCH)
	public ResponseEntity<CompanyKeyContact> companyKeyContactsContactIDPatch(
			@PathVariable("contactID") String contactID,
			@RequestBody CompanyKeyContact contact,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		try {
			int status = this.keyContactDao.updateCompanyKeyContact(contactID, contact, userEPPN);
			ServiceLogger.log(this.logTag, "User: " + userEPPN
					+ " updated: " + contactID + " with Status: " + status);
			return new ResponseEntity<CompanyKeyContact>(
					HttpStatus.OK);
		} catch (Exception e) {
			ServiceLogger.log(this.logTag, "Exception:" + e.getMessage());
			// return new
			// ResponseEntity<String>(HttpStatus.valueOf(e.getStatusCode()));
			return new ResponseEntity<CompanyKeyContact>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/companies/{companyID}/company_key_contacts", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<CompanyKeyContact>> getCompanyKeyContacts(
			@PathVariable("companyID") String companyID,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
			throws Exception {
		int cID = new Integer(companyID);
		return new ResponseEntity(this.keyContactDao.getCompanyKeyContacts(userEPPN, cID),HttpStatus.OK);
	}
	
	
}
