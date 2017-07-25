package org.dmc.services.users;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserBasicInformationDao;
import org.dmc.services.notification.NotificationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserBasicInformationController {

	private final String logTag = UserBasicInformationController.class.getName();
	private UserBasicInformationDao info = new UserBasicInformationDao();

	@Inject
	private NotificationService notificationService;

	@Transactional
	// Usually we wouldn't make a controller method transactional, but we need to work around this difficult to deal with code
	@RequestMapping(value = "/user-basic-information", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	public Id createUserBasicInformation(@RequestBody String payload,
    					 @RequestHeader(value="AJP_eppn", required=true) String userEPPN)
    {
		ServiceLogger.log(logTag, "User Basic Information: " + userEPPN);
	    try{
		    Integer orgId = new JSONObject(payload.trim()).getInt("company");
		    notificationService.notifyOrgAdminsOfNewUser(orgId, userEPPN);
	    } catch (JSONException ex){
		    ServiceLogger.log(UserBasicInformationController.class.getName(), "Null company id in create user basic info!");
	    } finally {
		    return info.createUserBasicInformation(userEPPN, payload);
	    }

    }

		@RequestMapping(value = "/user-accept-terms-and-conditions", method = RequestMethod.POST, headers = {"Content-type=application/json"})
		public Id userAcceptTermsAndConditions(@RequestHeader(value="AJP_eppn", required=true) String userEPPN)
		{
			return info.userAcceptTermsAndConditions(userEPPN);
		}

    // Exception handler - all exceptions not caught elsewhere will bubble to the controller
    // and can be returned to the client. We can also catch the exception elsewhere, then throw it again
    // so that it will be available to the client in a uniform fashion  via this method


    @ExceptionHandler(Exception.class)
    public ErrorMessage handleException(Exception ex) {
    	ErrorMessage result = new ErrorMessage.ErrorMessageBuilder(ex.getMessage())
    		.build();
        ServiceLogger.log(logTag, "UserBasicInformation Exception: " + ex.getMessage());
    	return result;
    }




}