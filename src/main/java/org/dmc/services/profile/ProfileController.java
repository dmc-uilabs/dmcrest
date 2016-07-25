package org.dmc.services.profile;

import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.member.FollowingMemeber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


import java.util.List;

import javax.xml.ws.http.HTTPException;

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
	
	@RequestMapping(value = "/profiles", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity createProfile(@RequestBody Profile profile,
										@RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
		ServiceLogger.log(logTag, "createProfile, profile: " + profile.toString());
		
		int httpStatusCode = HttpStatus.OK.value();
		Id retrivedId = null;
		
		try{
			retrivedId = profileDao.createProfile(profile, userEPPN);
		} catch(DMCServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
		
		return new ResponseEntity<Id>(retrivedId, HttpStatus.valueOf(httpStatusCode));
	}
	
	
	@RequestMapping(value = "/profiles/{id}", method = RequestMethod.PATCH, produces = { "application/json" })
	public ResponseEntity updateProfile(@PathVariable("id") int id,
										@RequestBody Profile profile,
										@RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
		ServiceLogger.log(logTag, "updateProfile, profile: " + profile.toString());
		
		int httpStatusCode = HttpStatus.OK.value();
		Id retrivedId = null;
		
		try{
			retrivedId = profileDao.updateProfile(id, profile, userEPPN);
		} catch(DMCServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
		
		return new ResponseEntity<Id>(retrivedId, HttpStatus.valueOf(httpStatusCode));
	}
	
	
	@RequestMapping(value = "/profiles/{id}/delete", method = RequestMethod.GET)
	public Id deleteProfile(@PathVariable("id") int id,
							@RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
		ServiceLogger.log(logTag, "deleteProfile, id: " + id);
		
		return profileDao.deleteProfile(id, userEPPN);
	}
	
 /////newly added methods
	@RequestMapping(value = "/profiles", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<?> profilesGet(@RequestHeader(value="AJP_eppn", required=true) String userEPPN,
													 @RequestParam(value = "limit", defaultValue="100") Integer limit,
													 @RequestParam(value = "order", defaultValue="DESC") String order,
													 @RequestParam(value = "sort", defaultValue="realname") String sort,
													 @RequestParam(value = "id", required = false) List<String> id){
		
		
		if(null != id) {
			ServiceLogger.log(logTag, "getProfile, with ids: " + id.toString());
		} else {
			ServiceLogger.log(logTag, "getProfile");
		}

		int httpStatusCode = HttpStatus.OK.value();
		List<Profile> profiles = null;
		
		try{
			profiles = profileDao.getProfiles(userEPPN, limit, order, sort, id);
			return new ResponseEntity<List<Profile>>(profiles, HttpStatus.valueOf(httpStatusCode));
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}
	
	
	
	@RequestMapping(value = "/profiles/{profileID}/profile_history", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<List<ProfileHistory>> profilesProfileIDProfileHistoryGet(@PathVariable("profileID") String profileID,
																				   @RequestParam(value = "section", required = false) String section,
																				   @RequestParam(value = "limit", required = false) Integer limit) {
		// do some magic!
		return new ResponseEntity<List<ProfileHistory>>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	
	@RequestMapping(value = "/profiles/{profileID}/profile_reviews", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<List<ProfileReview>> profilesProfileIDProfileReviewsGet(@PathVariable("profileID") String profileID,
																				  @RequestParam(value = "reviewId", required = true) String reviewId,
																				  @RequestParam(value = "limit", required = false) Integer limit,
																				  @RequestParam(value = "order", required = false) String order,
																				  @RequestParam(value = "sort", required = false) String sort,
																				  @RequestParam(value = "rating", required = false) Integer rating,
																				  @RequestParam(value = "status", required = false) Boolean status){
		// do some magic!
		return new ResponseEntity<List<ProfileReview>>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	
	@RequestMapping(value = "/profiles/{profileId}/following_members", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<List<FollowingMemeber>> profilesProfileIdFollowingMembersGet(@PathVariable("profileId") String profileId,
																					   @RequestParam(value = "limit", required = false) Integer limit,
																					   @RequestParam(value = "order", required = false) String order,
																					   @RequestParam(value = "sort", required = false) String sort){
		// do some magic!
		return new ResponseEntity<List<FollowingMemeber>>(HttpStatus.NOT_IMPLEMENTED);
	}
	
}