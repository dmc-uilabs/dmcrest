package org.dmc.services.verification;

import org.dmc.services.verification.VerificationPatchDao;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.verification.VerificationPatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class VerificationPatchController {
	
	private final String logTag = VerificationPatch.class.getName();
    private VerificationPatchDao verificationPatchDao = new VerificationPatchDao(); 
    private Verification verificationTest = new Verification(); 

	
	@RequestMapping(value = "/verify", method = RequestMethod.POST, produces = { "application/json" })
    public ResponseEntity verify(@RequestBody VerificationPatch payload) {
		
    	ServiceLogger.log(logTag, "Verification Machine Patch");
        
        int httpStatusCode = HttpStatus.OK.value();
        	
        try{
        	VerificationPatch patchedURL  = verificationPatchDao.verify(payload);
            return new ResponseEntity<VerificationPatch>(patchedURL, HttpStatus.valueOf(httpStatusCode));        

        } catch(DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
	
	//CONTROLLER ONLY FOR TESTING VERIFICATION MACHINE
	@RequestMapping(value = "/verifyTest", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity testVerify()
	{        
        int httpStatusCode = HttpStatus.OK.value();
        
        try{
        	VerificationPatch back = verificationTest.verify("https://s3.amazonaws.com/dmc-uploads2/uilabs.jpeg", "verify_test", "noreply", 1, "ProfilePicture", "Profile");
            return new ResponseEntity<VerificationPatch>(back, HttpStatus.valueOf(httpStatusCode));        

        } catch(DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
	
	
	
	
	
}
