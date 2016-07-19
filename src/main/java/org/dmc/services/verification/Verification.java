package org.dmc.services.verification;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.springframework.web.client.RestTemplate;

//A class that calls the Verfication machine with a JSON object with temp URL 


public class Verification {
	
	//Env var for our verification machine 
	private static final String targetURL = "http://localhost:3000/";//System.getenv("verifyURL");
	
	
	public String verify (int id, String url, String table, String userEPPN, String folder, String resourceType ) throws DMCServiceException {
		try{ 
			
			//Create upload object
			VerificationPatch upload = new VerificationPatch(); 
			upload.setId(id);
			upload.setTable(table);
			upload.setUrl(url);
			upload.setUserEPPN(userEPPN);
			upload.setFolder(folder);
			upload.setResourceType(resourceType);
			
			//REST API CLient class
			RestTemplate restTemplate = new RestTemplate();
			
			//URL of API, body of post, data type of response
	        String returnObject = restTemplate.postForObject(targetURL, upload, String.class);
	        return returnObject; 

		} 
		catch (Exception e){ 
			throw new DMCServiceException(DMCError.AWSError, e.getMessage());
		}

	}	 //End Verify

}