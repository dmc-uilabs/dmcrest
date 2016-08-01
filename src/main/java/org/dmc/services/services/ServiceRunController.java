package org.dmc.services.services;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanyUserUtil;
import org.dmc.services.projects.ProjectController;
import org.dmc.services.projects.ProjectCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class ServiceRunController {

    private final String logTag = ServiceRunController.class.getName();

    //@RequestMapping(value = "/model_run", method = RequestMethod.POST)
    //public ResponseEntity<Integer> serviceRun (@RequestBody int serviceId, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
/*  public ResponseEntity<Id> serviceRun (@RequestBody String inputJson, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
    	int serviceId=-9;
    	int runId;
    	try {
    		JSONObject in = new JSONObject(inputJson);
    		String intStr = in.getJSONObject("interFace").getString("interfaceId");
    		serviceId = ServiceRunServiceInterfaceDAO.getServiceId(intStr);        
    		int userId = CompanyUserUtil.getUserId(userEPPN);
    		ServiceRunDOMEAPI serviceRunInstance = new ServiceRunDOMEAPI();
    		runId = serviceRunInstance.runModel(serviceId, userId);
    		
        }
        catch (Exception e)
        {
        	ServiceLogger.log(logTag, "Exception in serviceRun, serviceId: " + serviceId + " called by user " + userEPPN);
        	return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Id>(new Id.IdBuilder(runId).build(), HttpStatus.OK);
    }*/
    @RequestMapping(value = "/model_run", method = RequestMethod.POST)
    public ResponseEntity<RunDomeModelResponse> serviceRun (@RequestBody RunDomeModelInput serviceInput, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
    	
    	int runId;
    	String sId = serviceInput.getServiceId();
    	RunDomeModelResponse response = new RunDomeModelResponse();
    	try {      
    		int userId = CompanyUserUtil.getUserId(userEPPN);
    		ServiceRunDOMEAPI serviceRunInstance = new ServiceRunDOMEAPI();
    		HashMap paras = new HashMap();
    		Map ins = serviceInput.getInParams();
    		Iterator it = ins.entrySet().iterator();
    		while (it.hasNext()) {
    			Map.Entry pair = (Map.Entry)it.next();
    			paras.put(pair.getKey(), pair.getValue());
    		}
/*    		Map outs = serviceInput.getOutParams();
    		Iterator ut = outs.entrySet().iterator();
    		while (ut.hasNext()) {
    			Map.Entry pair = (Map.Entry)ut.next();
    			paras.put(pair.getKey(), pair.getValue());
    		}*/
    		int serviceId = new Integer(sId);
    		runId = serviceRunInstance.runModel(serviceId,paras,userId);
    		ServiceLogger.log(logTag, "Success in serviceRun, serviceIdStr: " + serviceInput.getServiceId() + " called by user " + userEPPN);
    		response.setRunId(runId);
        }
        catch (Exception e)
        {
        	ServiceLogger.log(logTag, "Exception in serviceRun, serviceIdStr: " + serviceInput.getServiceId() + " called by user " + userEPPN);
        	return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<RunDomeModelResponse>(response, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/model_poll/{serviceRunID}", method = RequestMethod.GET, produces = { "application/json"})
    public ResponseEntity<ServiceRunResult> servicePoll (@PathVariable("serviceRunID") int serviceRunID, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(logTag, "In servicePoll, runId: " + serviceRunID + " by user " + userEPPN);
        ServiceRunResult result=null;
        try {
        	ServiceRunDOMEAPI serviceRunInstance = new ServiceRunDOMEAPI();
        	int userId = CompanyUserUtil.getUserId(userEPPN);
        	result = serviceRunInstance.pollService(serviceRunID, userId);
        }
        catch (Exception ex) {
        	ServiceLogger.log(logTag, "Exception in servicePoll, runId: " + serviceRunID + " called by user " + userEPPN + ex.toString());
            return new ResponseEntity<ServiceRunResult>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<ServiceRunResult>(result, HttpStatus.OK);
    }
    
/*    public static void main(String[] args)
    {
    	String input = 
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
    	
    	
    	RunDomeModelInput inputs = new RunDomeModelInput();
    	inputs.setServiceId("3");
    	HashMap<String, DomeModelParam> pars = new HashMap<String, DomeModelParam>();
    	DomeModelParam par1 = new DomeModelParam();
    	par1.setName("SpecimenWidth");
    	par1.setValue(new BigDecimal("1999"));
    	par1.setCategory("length");
    	par1.setType("Real");
    	par1.setUnit("meter");
    	par1.setParameterid("d9f30f3a-d800-1004-8f53-704dbfababa8");
    	pars.put("SpecimenWidth", par1);
    	DomeModelParam par2 = new DomeModelParam();
    	par2.setName("CrackLength");
    	par2.setValue(new BigDecimal("2999"));
    	par2.setCategory("length");
    	par2.setType("Real");
    	par2.setUnit("meter");
    	par2.setParameterid("d9f30f37-d800-1004-8f53-704dbfababa8");
    	pars.put("CrackLength", par2);
    	inputs.setInParams(pars);
    	
    	// Run service
    	ServiceRunController c = new ServiceRunController();
   	    ResponseEntity<RunDomeModelResponse> result = c.serviceRun(inputs,"testUser"); 
//    	ResponseEntity<ServiceRunResult> result = c.servicePoll(9,"testUser");
    	System.out.println("result: " + result.getBody().getRunId());
    }*/

}
