package org.dmc.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
import org.dmc.services.accounts.AccountServerIT;
import org.dmc.services.services.DomeEntity;
import org.dmc.services.services.DomeModelParam;
import org.dmc.services.services.DomeModelResponsePkg;
import org.dmc.services.services.RunDomeModelInput;
import org.dmc.services.services.ServiceRunController;
import org.dmc.services.services.RunDomeModelResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceRunIT  extends BaseIT {
 
    private static final String logTag = ServiceRunIT.class.getName();
    private static RunDomeModelResponse response = null;
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
    
/*	// Now prepare a test case
	DomeModelResponsePkg input2 = new DomeModelResponsePkg();
	// We only need to set up the interface id string, and input parameter, all other things are available in the database for this service
	DomeEntity de = new DomeEntity();
	de.setInterfaceId("aff647db-d82f-1004-8e7b-5de38b2eeb0f");
	input2.setInterface(de);
	HashMap<String, DomeModelParam> pars = new HashMap<String, DomeModelParam>();
	DomeModelParam par1 = new DomeModelParam();
	par1.setName("SpecimenWidth");
	par1.setValue(new BigDecimal("100"));
	par1.setCategory("length");
	par1.setType("Real");
	par1.setUnit("meter");
	par1.setParameterid("d9f30f3a-d800-1004-8f53-704dbfababa8");
	pars.put("SpecimenWidth", par1);
	DomeModelParam par2 = new DomeModelParam();
	par2.setName("CrackLength");
	par2.setValue(new BigDecimal("200"));
	par2.setCategory("length");
	par2.setType("Real");
	par2.setUnit("meter");
	par2.setParameterid("d9f30f37-d800-1004-8f53-704dbfababa8");
	pars.put("CrackLength", par2);
	input2.setInParams(pars);*/
	
    // Now run service
    String testUser = "testUser";
    RunDomeModelInput input = new RunDomeModelInput();
    input.setServiceId("3");
    
	Map<String, DomeModelParam> pars = new HashMap<String, DomeModelParam>();
	DomeModelParam par1 = new DomeModelParam();
	par1.setName("SpecimenWidth");
	par1.setValue(new BigDecimal("100"));
	par1.setCategory("length");
	par1.setType("Real");
	par1.setUnit("meter");
	par1.setParameterid("d9f30f3a-d800-1004-8f53-704dbfababa8");
	pars.put("SpecimenWidth", par1);
	DomeModelParam par2 = new DomeModelParam();
	par2.setName("CrackLength");
	par2.setValue(new BigDecimal("200"));
	par2.setCategory("length");
	par2.setType("Real");
	par2.setUnit("meter");
	par2.setParameterid("d9f30f37-d800-1004-8f53-704dbfababa8");
	pars.put("CrackLength", par2);
	input.setInParams(pars);
	
	Map<String, DomeModelParam> parsOut = new HashMap<String, DomeModelParam>();
	DomeModelParam par3 = new DomeModelParam();
	par3.setName("Alpha");
	par3.setValue(new BigDecimal("0.3333333333333333"));
	par3.setCategory("no unit");
	par3.setType("Real");
	par3.setUnit("no unit");
	par3.setParameterid("d9f30f3d-d800-1004-8f53-704dbfababa8");
	parsOut.put("Alpha", par3);
	input.setOutParams(parsOut);
	
    
	ObjectMapper mapper = new ObjectMapper();
	String postDomeRunString = null;
	try {
		postDomeRunString = mapper.writeValueAsString(input);
	} catch (JsonProcessingException e) {
		e.printStackTrace();
	} 
    
    response = 
   		    given()
    	         	.header("Content-type", "application/json")
    	         	.header("AJP_eppn", testUser)
    	         	.body(postDomeRunString)
    			.expect()
    	         	.statusCode(200)
    			.when()
    	         	.post("/model_run").as(RunDomeModelResponse.class); 
    	         
    serviceRunId = response.getRunId();
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
    
/*    public static void main(String[] args)
    {
    	ServiceRunIT sri = new ServiceRunIT();
    	sri.runService();
    }*/

/*
    public void testModelRunCycle()
    {
    	//1. Create a DOME server
    	AccountServerIT testServerSetup = new AccountServerIT();
    	int user_id = 111;
    	String serverName = "TestDOME";
    	String URL = "http://localhost:8080/DOMEApiServicesV7/";
    	String userName = "testUser";
    	String domeServerID = testServerSetup.testAccountPost_AnyServer(user_id, serverName, URL, userName );
    	System.out.println("domeServerID = " + domeServerID);
    	//2. Register a service
    	//3. Run service
    }*/

}
