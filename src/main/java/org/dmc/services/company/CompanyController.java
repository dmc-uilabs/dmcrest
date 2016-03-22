package org.dmc.services.company;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.Project;
import org.dmc.services.projects.ProjectDao;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.AbstractList;
import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

@RestController
public class CompanyController {

	private final String logTag = CompanyController.class.getName();
	
    private CompanyDao companyDao = new CompanyDao(); 
	
    /**
     Return a list of companies
     **/
    @RequestMapping(value = "/companies", method = RequestMethod.GET, produces = { "application/json"})
    public ResponseEntity getCompanies(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
    	ServiceLogger.log(logTag, "getCompanys, userEPPN: " + userEPPN);
    	int statusCode = HttpStatus.OK.value();
    	ArrayList<Company> companies = null;
    	
    	try {
            companies = companyDao.getCompanies(userEPPN);
            return new ResponseEntity<ArrayList<Company>>(companies, HttpStatus.valueOf(statusCode));
    	} catch (HTTPException e) {
    		ServiceLogger.log(logTag, e.getMessage());
    		statusCode = e.getStatusCode();
    		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
    		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
    	}
    }
    
    @RequestMapping(value = "/companies/{id}", method = RequestMethod.GET, produces = { "application/json"})
    public ResponseEntity getCompany(@PathVariable("id") int id, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
    	ServiceLogger.log(logTag, "getCompany, id: " + id);
    	int statusCode = HttpStatus.OK.value();
    	Company company = null; 
    	
    	try {
    	  company = companyDao.getCompany(id, userEPPN);
    	  return new ResponseEntity<Company>(company, HttpStatus.valueOf(statusCode));
    	} catch (HTTPException e) {
    		ServiceLogger.log(logTag, e.getMessage());
    		statusCode = e.getStatusCode();
    		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
    		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
    	}
    }
    
    @RequestMapping(value = "/companies/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    @ResponseBody
    public Id createCompany(@RequestBody String payload, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
    	ServiceLogger.log(logTag, "CreateCompany, Payload: " + payload);	
    	return companyDao.createCompany(payload, userEPPN);
    }

    @RequestMapping(value = "/companies/{id}/update", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    @ResponseBody
    public Id updateCompany(@PathVariable("id") int id, @RequestBody String payload) {
    	ServiceLogger.log(logTag, "UpdateCompany, ID: " + id + " Payload: " + payload);	
    	return companyDao.updateCompany(id, payload);
    }
    @RequestMapping(value = "/companies/{id}/delete", method = RequestMethod.GET)
    public Id deleteCompany(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "deleteCompany, id: " + id);
    	return  companyDao.deleteCompany(id);
    }
    
	private CompanySkillDao skills = new CompanySkillDao(); 
	
    @RequestMapping(value = "/companies/{companyID}/company_skills", method = RequestMethod.GET)
    public ArrayList<CompanySkill> getCompanySkills(@PathVariable("companyID") int companyID,
    						  @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {

    	ServiceLogger.log(this.logTag, "In getCompanySkills, companyID: " + companyID + " as user " + userEPPN);
    	return this.skills.getCompanySkills(userEPPN, companyID);
    }
    
    @RequestMapping(value = "/company_skills", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    public int createCompanySkills(@RequestBody String payload, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)
    {
    	return 0;
    }


}