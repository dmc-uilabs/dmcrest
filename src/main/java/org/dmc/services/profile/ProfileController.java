package org.dmc.services.profile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.dmc.services.utils.SQLUtils.DEFAULT_LIMIT_TEXT;
import static org.dmc.services.utils.SQLUtils.SORT_DESCENDING;

import java.util.List;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.member.FollowingMember;
import org.dmc.services.member.FollowingMemberDao;
import org.dmc.services.services.GetCompareService;
import org.dmc.services.reviews.ReviewDao;
import org.dmc.services.reviews.ReviewType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final String logTag = ProfileController.class.getName();

    private ProfileDao profileDao = new ProfileDao();
    private ReviewDao reviewDao = new ReviewDao(ReviewType.PROFILE);


    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProfile(@PathVariable("id") int id) {
        ServiceLogger.log(logTag, "getProfile, id: " + id);

        int httpStatusCode = HttpStatus.OK.value();
        Profile profile = null;

        try {
            profile = profileDao.getProfile(id);
        } catch (HTTPException httpException) {
            httpStatusCode = httpException.getStatusCode();
        }

        return new ResponseEntity<Profile>(profile, HttpStatus.valueOf(httpStatusCode));
    }

    @RequestMapping(value = "/profiles", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createProfile(@RequestBody Profile profile,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
        ServiceLogger.log(logTag, "createProfile, profile: " + profile.toString());

        int httpStatusCode = HttpStatus.OK.value();
        Id retrivedId = null;

        try {
            retrivedId = profileDao.createProfile(profile, userEPPN);
        } catch (DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }

        return new ResponseEntity<Id>(retrivedId, HttpStatus.valueOf(httpStatusCode));
    }

    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.PATCH, produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateProfile(@PathVariable("id") int id, @RequestBody Profile profile,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
        ServiceLogger.log(logTag, "updateProfile, profile: " + profile.toString());

        int httpStatusCode = HttpStatus.OK.value();
        Id retrivedId = null;

        try {
            retrivedId = profileDao.updateProfile(id, profile, userEPPN);
        } catch (DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }

        return new ResponseEntity<Id>(retrivedId, HttpStatus.valueOf(httpStatusCode));
    }

    @RequestMapping(value = "/profiles/{id}/delete", method = RequestMethod.GET)
    public Id deleteProfile(@PathVariable("id") int id,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
        ServiceLogger.log(logTag, "deleteProfile, id: " + id);

        return profileDao.deleteProfile(id, userEPPN);
    }

    ///// newly added methods
    @RequestMapping(value = "/profiles", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> profilesGet(@RequestHeader(value = "AJP_eppn", required = true) String userEPPN,
            @RequestParam(value = "limit", defaultValue = DEFAULT_LIMIT_TEXT) Integer limit,
            @RequestParam(value = "order", defaultValue = SORT_DESCENDING) String order,
            @RequestParam(value = "sort", defaultValue = "realname") String sort,
            @RequestParam(value = "id", required = false) List<String> id) {
        if (null != id) {
            ServiceLogger.log(logTag, "getProfile, with " + id.size() + " ids: " + id.toString());
        } else {
            ServiceLogger.log(logTag, "getProfile, without ids");
        }

        int httpStatusCode = HttpStatus.OK.value();
        List<Profile> profiles = null;

        try {
            profiles = profileDao.getProfiles(userEPPN, limit, order, sort, id);
            return new ResponseEntity<List<Profile>>(profiles, HttpStatus.valueOf(httpStatusCode));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/profiles/{profileID}/profile_history", produces = {
            APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> profilesProfileIDProfileHistoryGet(
            @PathVariable("profileID") String profileID,
            @RequestParam(value = "section", required = false) String section,
            @RequestParam(value = "limit", required = false) Integer limit) {
        // do some magic!
        return new ResponseEntity<List<ProfileHistory>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/profiles/{profileID}/profile_reviews", produces = {
            APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> profilesProfileIDProfileReviewsGet(
            @PathVariable("profileID") String profileID,
            @RequestParam(value = "reviewId", required = true) String reviewId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
        ServiceLogger.log(logTag, "Query for profile " + profileID + " review id " + reviewId);
        List<ProfileReview> reviews = null;
        int statusCode = HttpStatus.OK.value();
        
        int reviewIdInt = 0;
        try {
            reviewIdInt = Integer.parseInt(reviewId);
        } catch (NumberFormatException nfe) {
            return new ResponseEntity<String>("invalid review id: " + reviewId, HttpStatus.BAD_REQUEST);
        }

        try {
            int profileIdInt = Integer.parseInt(profileID);
            
            if (reviewIdInt == 0) {
                reviews = reviewDao.getReviews(profileIdInt, reviewId, limit, order, sort, rating, status, userEPPN, ProfileReview.class);
                
            } else if (reviewIdInt > 0) {
                reviews = reviewDao.getReviewReplies(profileIdInt, reviewId, limit, order, sort, rating, status, userEPPN, ProfileReview.class);
            }

            return new ResponseEntity<List<ProfileReview>>(reviews, HttpStatus.valueOf(statusCode));
        } catch (NumberFormatException nfe) {
            ServiceLogger.log(logTag, "Invalid companyId: " + profileID + ": " + nfe.getMessage());
            return new ResponseEntity<String>("Invalid companyId: " + profileID, HttpStatus.BAD_REQUEST);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/profile_reviews", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
    public ResponseEntity<?> profileReviewsPost(
            @RequestBody ProfileReview profileReview,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
        
        int statusCode = HttpStatus.OK.value();
        
        try {
            Id id = reviewDao.createReview(profileReview, userEPPN);
            return new ResponseEntity<Id>(id, HttpStatus.valueOf(statusCode));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }

    }

    @RequestMapping(value = "/profiles/{profileId}/following_members", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> profilesProfileIdFollowingMembersGet(
            @PathVariable("profileId") String profileId, 
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        final FollowingMemberDao dao = new FollowingMemberDao();
        return new ResponseEntity<List<FollowingMember>>(dao.followingMembersGet(profileId, null, null, limit, start, order, sort, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/profiles/{profileID}/compare_services",produces = { APPLICATION_JSON_VALUE,
        TEXT_HTML_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> profilesProfileIDCompareServicesGet(
            @PathVariable("profileID") String profileID,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        try {
            List<GetCompareService> compareServices = profileDao.getCompareServices(profileID, userEPPN);
            return new ResponseEntity<List<GetCompareService>>(compareServices, HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }	
    }	
}
