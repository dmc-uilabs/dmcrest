package org.dmc.services;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
//import org.springframework.http.ResponseEntity;

import static com.jayway.restassured.RestAssured.given;
/*import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertTrue;*/

import org.dmc.services.ServiceLogger;
import org.dmc.services.services.DomeEntity;
import org.dmc.services.services.DomeModelParam;
import org.dmc.services.services.DomeModelResponsePkg;
import org.dmc.services.services.ServiceRunController;

public class ServiceRunIT  extends BaseIT {
 
    private static final String logTag = ServiceRunIT.class.getName();

    private int serviceRunId;


/*    @Before
    public void runService () {

        ServiceLogger.log(logTag, "starting runService");
        
        // Assume service with service_id=3 is already registered in the database.
        // Use TestServiceRun.psql or use service registration services
        String serviceRunInput = 
    			"{\"interFace\":{\"version\":1,\"modelId\":\"aff647da-d82f-1004-8e7b-5de38b2eeb0f\"," +
    			"\"interfaceId\":\"aff647db-d82f-1004-8e7b-5de38b2eeb0f\",\"type\":\"interface\"," +
    			"\"name\":\"Default Interface\",\"path\":[30]},\"inParams\":{\"SpecimenWidth\":{" +
    			"\"name\":\"SpecimenWidth\",\"type\":\"Real\",\"unit\":\"meter\",\"category\":\"length\",\"value\":3.0," +
    			"\"parameterid\":\"d9f30f3a-d800-1004-8f53-704dbfababa8\"},\"CrackLength\":{\"name\":\"CrackLength\"," +
    			"\"type\":\"Real\",\"unit\":\"meter\",\"category\":\"length\",\"value\":1,\"parameterid\":" +
    			"\"d9f30f37-d800-1004-8f53-704dbfababa8\"}},\"outParams\":{\"Alpha\":{\"name\":\"Alpha\",\"type" +
    			"\":\"Real\",\"unit\":\"no unit\",\"category\":\"no unit\",\"value\":0.3333333333333333,\"parameterid\":" +
    			"\"d9f30f3d-d800-1004-8f53-704dbfababa8\",\"instancename\":\"Alpha\"}},\"modelName\":\"Default Interface\"," +
    			"\"modelDescription\":\"\",\"server\":{\"name\":\"52.33.38.232\",\"port\":\"7795\",\"user\":\"ceed\",\"pw\":\"ceed" +
    			"\",\"space\":\"USER\"}}";
        // Now run service
        String testUser = "testUser";
        serviceRunId= 
       		    given()
        	         	.header("Content-type", "application/json")
        	         	.header("AJP_eppn", testUser)
        	         	.body(serviceRunInput)
        			.expect()
        	         	.statusCode(200)
        			.when()
        	         	.post("/model_run")
        	        .then()
        	        	.extract().path("id");;
        
        ServiceLogger.log(logTag, "Created service with id: " + serviceRunId);
    }
*/   

@Before
public void runService () {

    ServiceLogger.log(logTag, "starting runService");
    
	// Now prepare a test case
	DomeModelResponsePkg input2 = new DomeModelResponsePkg();
	// We only need to set up the interface id string, and input parameter, all other things are available in the database for this service
	DomeEntity de = new DomeEntity();
	de.setInterfaceId("aff647db-d82f-1004-8e7b-5de38b2eeb0f");
	input2.setInterface(de);
	HashMap<String, DomeModelParam> pars = new HashMap<String, DomeModelParam>();
	DomeModelParam par1 = new DomeModelParam();
	par1.setName("SpecimenWidth");
	par1.setValue("100");
	par1.setCategory("length");
	par1.setType("Real");
	par1.setUnit("meter");
	par1.setParameterid("d9f30f3a-d800-1004-8f53-704dbfababa8");
	pars.put("SpecimenWidth", par1);
	DomeModelParam par2 = new DomeModelParam();
	par2.setName("CrackLength");
	par2.setValue("200");
	par2.setCategory("length");
	par2.setType("Real");
	par2.setUnit("meter");
	par2.setParameterid("d9f30f37-d800-1004-8f53-704dbfababa8");
	pars.put("CrackLength", par2);
	input2.setInParams(pars);
	
    // Now run service
    String testUser = "testUser";
    serviceRunId= 
   		    given()
    	         	.header("Content-type", "application/json")
    	         	.header("AJP_eppn", testUser)
    	         	.body(input2)
    			.expect()
    	         	.statusCode(200)
    			.when()
    	         	.post("/model_run")
    	        .then()
    	        	.extract().path("id");;
    
    ServiceLogger.log(logTag, "Created service with id: " + serviceRunId);
}


    @Test
    public void testPollService()
    {
    	System.out.println("Start pollService");
    	ServiceLogger.log(logTag, "Poll service with id: " + serviceRunId); 
    	
    	given()
    		.header("Content-type", "application/json")
    		.header("AJP_eppn","testUser").
    	expect().
    		statusCode(200).
    	when().
    		get("/model_poll/"+serviceRunId);
    }

}
