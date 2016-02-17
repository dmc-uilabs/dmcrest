package org.dmc.services.profile;

import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

	private final String logTag = ProfileController.class.getName();
	
    private ProfileDao profileDao = new ProfileDao(); 
	
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET)
    public Profile getProfile(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "getProfile, id: " + id);
    	return profileDao.getProfile(id);
    }
    
    @RequestMapping(value = "/profiles/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    @ResponseBody
    public Id createProfile(@RequestBody String payload,  @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
    	ServiceLogger.log(logTag, "================================CreateProfile================, Payload: " + payload);	
    	return profileDao.createProfile(payload, userEPPN);
    }
    
    @RequestMapping(value = "/profiles/{id}/delete", method = RequestMethod.GET)
    public Id deleteProfile(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "deleteProfile, id: " + id);
    	return profileDao.deleteProfile(id);
    }
    
}