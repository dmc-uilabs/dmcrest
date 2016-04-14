package org.dmc.services.company;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.User;
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
import java.util.List;

import javax.xml.ws.http.HTTPException;

@RestController
public class CompanyController {

    private final String logTag = CompanyController.class.getName();

    private CompanyDao companyDao = new CompanyDao();
    private CompanySkillDao skillDao = new CompanySkillDao();
    private CompanyVideoDao videoDao = new CompanyVideoDao();

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
    
    @RequestMapping(value = "/companies/{id}/delete", method = RequestMethod.DELETE)
    public Id deleteCompany(@PathVariable("id") int id, @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
    	ServiceLogger.log(logTag, "deleteCompany, id: " + id);
    	return  companyDao.deleteCompany(id, userEPPN);
    }

    /**
	 * Retrieve company videos
	 * 
	 * @param id
	 * @param userEPPN
	 * @return
	 */
	@RequestMapping(value = "/companies/{id}/company_videos", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity getCompanyVideos(@PathVariable("id") int id, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
		
		ServiceLogger.log(logTag, "getCompanyVideos, userEPPN: " + userEPPN);
		int statusCode = HttpStatus.OK.value();
		ArrayList<CompanyVideo> videos = null;

		try {
			videos = videoDao.getCompanyVideos(id, userEPPN);
			return new ResponseEntity<ArrayList<CompanyVideo>>(videos, HttpStatus.valueOf(statusCode));
		} catch (HTTPException e) {
			ServiceLogger.log(logTag, e.getMessage());
			statusCode = e.getStatusCode();
			ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
			return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
		}
	}
	
    /**
	 * Create a company video
	 * @param video
	 * @param userEPPN
	 * @return
	 */
	
	@RequestMapping(value = "/company_videos", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public ResponseEntity createCompanyVideo(@RequestBody CompanyVideo video, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
		
		ServiceLogger.log(logTag, "createCompanyVideo");
		int statusCode = HttpStatus.OK.value();
		Id id = null;

		try {
			id = videoDao.createCompanyVideo(video, userEPPN);
			return new ResponseEntity<CompanyVideo>(video, HttpStatus.valueOf(statusCode));
		} catch (HTTPException e) {
			statusCode = e.getStatusCode();
			ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
			return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
		}
	}
	
	/* Delete a company video
	 * @param id
	 * @param userEPPN
	 * @return
	 */
	@RequestMapping(value = "/company_videos/{id}", method = RequestMethod.DELETE, produces = { "application/json" })
	public ResponseEntity deleteCompanyVideos(@PathVariable("id") int videoId, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
		
		ServiceLogger.log(logTag, "deleteCompanyVideos, userEPPN: " + userEPPN);
		int statusCode = HttpStatus.OK.value();
		Id id = null;

		try {
			id = videoDao.deleteCompanyVideo(-1, videoId, userEPPN);
			return new ResponseEntity<Id>(id, HttpStatus.valueOf(statusCode));
		} catch (HTTPException e) {
			ServiceLogger.log(logTag, e.getMessage());
			statusCode = e.getStatusCode();
			ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
			return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
		}
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

	// /companies/{companyID}/company_members
	@RequestMapping(value = "/companies/{companyID}/company_members", method = RequestMethod.GET)
	public ResponseEntity getCompanyMembers (@PathVariable("companyID") int companyID, @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
		ServiceLogger.log(logTag, "getCompanyMembers, companyID: " + companyID);
		int statusCode = HttpStatus.OK.value();

		try {
			List<User> members = companyDao.getCompanyMembers(companyID, userEPPN);
			return new ResponseEntity<List<User>>(members, HttpStatus.valueOf(statusCode));
		} catch (HTTPException e) {
			statusCode = e.getStatusCode();
			ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
			return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
		}
	}

}