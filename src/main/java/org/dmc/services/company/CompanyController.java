package org.dmc.services.company;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

@RestController
public class CompanyController {

    private final String logTag = CompanyController.class.getName();

    private CompanyDao companyDao = new CompanyDao();
    private CompanySkillDao skillDao = new CompanySkillDao();

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

    @RequestMapping(value = "/companies/{id}", method = RequestMethod.PATCH, produces = { "application/json" })
    public ResponseEntity updateCompany(@PathVariable("id") int id,
    					//@RequestBody Company company, 
    					@RequestBody String payload, 
    					@RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
    	
    	ServiceLogger.log(logTag, "UpdateCompany, ID: " + id + " Payload: " + payload);

        int statusCode = HttpStatus.OK.value();
        Id retrievedId = null;
        
        try {
            retrievedId = companyDao.updateCompany(id, /*company,*/ payload, userEPPN);
            return new ResponseEntity<Id>(retrievedId, HttpStatus.valueOf(statusCode));
        } catch(HTTPException e) {
    		statusCode = e.getStatusCode();
    		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
    		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
        }  
    }
    
    @RequestMapping(value = "/companies/{id}/delete", method = RequestMethod.GET)
    public Id deleteCompany(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "deleteCompany, id: " + id);
    	return  companyDao.deleteCompany(id);
    }
	
    /**
     * Add an administrator for a company
     * @param id
     * @param userId
     * @param userEPPN
     * @return id of the organization_admin entry or -9999
     */
    @RequestMapping(value = "/companies/{id}/admin/{userId}", method = RequestMethod.POST)
    public ResponseEntity addCompanyAdministrator (@PathVariable("id") int id, @PathVariable("userId") int userId, @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
	ServiceLogger.log(logTag, "addCompanyAdministrator, id: " + id + ", userId: " + userId);
	
	int statusCode = HttpStatus.OK.value();
	Id retrievedId = null;
	
	try {
	    retrievedId = companyDao.addAdministrator(id, userId, userEPPN);
	    return new ResponseEntity<Id>(retrievedId, HttpStatus.valueOf(statusCode));
	} catch (HTTPException e) {
	    statusCode = e.getStatusCode();
	    ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
	    return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
	}
    }

    /**
     * Add an member for a company
     * @param id
     * @param userId
     * @param userEPPN
     * @return id of the organization_user entry or -9999
     */
    @RequestMapping(value = "/companies/{id}/member/{userId}", method = RequestMethod.POST)
    public ResponseEntity addCompanyMember (@PathVariable("id") int id, @PathVariable("userId") int userId, @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
	ServiceLogger.log(logTag, "addCompanyMember, id: " + id + ", userId: " + userId);
	
	int statusCode = HttpStatus.OK.value();
	Id retrievedId = null;
	
	try {
	    retrievedId = companyDao.addMember(id, userId, userEPPN);
	    return new ResponseEntity<Id>(retrievedId, HttpStatus.valueOf(statusCode));
	} catch (HTTPException e) {
	    statusCode = e.getStatusCode();
	    ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
	    return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
	}
    }
}