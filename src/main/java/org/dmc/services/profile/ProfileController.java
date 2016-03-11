package org.dmc.services.profile;

import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.http.HTTPException;
import org.springframework.http.HttpStatus;

@RestController
public class ProfileController {

	private final String logTag = ProfileController.class.getName();
	
    private ProfileDao profileDao = new ProfileDao(); 
	
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET)
    public ResponseEntity<Profile> getProfile(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "getProfile, id: " + id);
        
        int httpStatusCode = HttpStatus.OK.value();
        Profile profile = null;
        
        try{
            profile = profileDao.getProfile(id);
        } catch(HTTPException httpException) {
            httpStatusCode = httpException.getStatusCode();
        }
        
        return new ResponseEntity<Profile>(profile, HttpStatus.valueOf(httpStatusCode));
    }
    
    @RequestMapping(value = "/profiles", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    @ResponseBody
    public Id createProfile(@RequestBody String payload,  @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
    	ServiceLogger.log(logTag, "Payload: " + payload);	
    	return profileDao.createProfile(payload, userEPPN);
    }
    
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.PATCH, produces = { "application/json" })
    public Id updateProfile(@PathVariable("id") int id, @RequestBody String payload, @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
    	ServiceLogger.log(logTag, "updateProfile, Payload: " + payload);
    	return profileDao.updateProfile(id, payload, userEPPN);
    }
    
    @RequestMapping(value = "/profiles/{id}/delete", method = RequestMethod.GET)
    public Id deleteProfile(@PathVariable("id") int id, @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
    	ServiceLogger.log(logTag, "deleteProfile, id: " + id);
    	return profileDao.deleteProfile(id, userEPPN);
    }
    
}