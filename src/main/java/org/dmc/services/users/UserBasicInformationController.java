package org.dmc.services.users;

import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.ErrorMessage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestController
public class UserBasicInformationController {

	private final String logTag = UserBasicInformationController.class.getName();
	private UserBasicInformationDao  info = new UserBasicInformationDao(); 
    
	@RequestMapping(value = "/user-basic-information", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	public Id createUserBasicInformation(@RequestBody String payload,
    					 @RequestHeader(value="AJP_eppn", required=true) String userEPPN)
    {
		ServiceLogger.log(logTag, "User Basic Information: " + userEPPN);
    	return info.createUserBasicInformation(userEPPN, payload);
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