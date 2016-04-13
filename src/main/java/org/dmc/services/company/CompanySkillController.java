package org.dmc.services.company;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.DMCServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;


import static org.springframework.http.MediaType.*;

@RestController
public class CompanySkillController {
	private CompanySkillDao skillDao = new CompanySkillDao();
	private final String logTag = CompanySkillController.class.getName();
	
	@RequestMapping(value = "/company_skills", produces = { "application/json", "text/html" }, consumes = { "application/json",
			"text/xml" }, method = RequestMethod.POST)
	public ResponseEntity<?> companySkillsPost(@RequestBody CompanySkill companySkill,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		int statusCode = HttpStatus.OK.value();

		try {
			ServiceLogger.log(this.logTag, "Before create entry");
			CompanySkill ck = this.skillDao.createCompanySkill(companySkill, userEPPN);
			ServiceLogger.log(this.logTag, "After create entry");
			return new ResponseEntity<CompanySkill>(
					HttpStatus.OK);

		}
		catch (DMCServiceException e) {
			ServiceLogger.log(this.logTag, "Exception:" + e.getErrorMessage());
			HttpHeaders headers = new HttpHeaders();
			return new ResponseEntity<String>("DMCServerException:" + e.getErrorMessage(),headers,HttpStatus.BAD_REQUEST );
		}
		catch (Exception e) {
			ServiceLogger.log(this.logTag, "Exception:" + e.getMessage());
			return new ResponseEntity<CompanySkill>(companySkill, HttpStatus.BAD_REQUEST);
		}
	}
    
	@RequestMapping(value = "/company_skills/{skillID}", produces = { "application/json", "text/html" }, method = RequestMethod.DELETE)
	public ResponseEntity<String> companySkillDelete(@PathVariable("skillID") String skillID, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser")String userEPPN) 
			 {
		ServiceLogger.log(this.logTag, "User: " + userEPPN + " is trying to delete:" + skillID);
		try {
			int status = this.skillDao.deleteCompanySkills(skillID, userEPPN);
			ServiceLogger.log(this.logTag, "User: " + userEPPN + " finish deleting:" + skillID + "with Status:" + status);
			return new ResponseEntity<String>("Delete succeeded.", HttpStatus.OK);
		} catch (Exception e) {
			ServiceLogger.log(this.logTag, "Exception:" + e.getMessage());
			//return new ResponseEntity<String>(HttpStatus.valueOf(e.getStatusCode()));
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}		
	}

	@RequestMapping(value = "/companies/{companyID}/company_skills", method =
	RequestMethod.GET)
	public ArrayList<CompanySkill> getCompanySkills(@PathVariable("companyID") int companyID,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)  throws Exception{

		return this.skillDao.getCompanySkills(userEPPN, companyID);
	}
}
