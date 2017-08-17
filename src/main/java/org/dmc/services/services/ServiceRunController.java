package org.dmc.services.services;

import org.dmc.services.ServiceLogger;
import org.dmc.services.ServiceUsePermitService;
import org.dmc.services.company.CompanyUserUtil;
import org.dmc.services.exceptions.ServiceUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
public class ServiceRunController {

	private final String logTag = ServiceRunController.class.getName();

	private ServiceDao serviceDao = new ServiceDao();
	
	@Autowired
	private ServiceUsePermitService supService;

	@RequestMapping(value = "/model_run_file1", method = RequestMethod.POST)
	public ResponseEntity<?> handleFormUploadTest(@RequestParam("file") MultipartFile uploadfile,@RequestParam("service")String serviceID,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws IOException {
		ServiceLogger.log(logTag, "Test121 : The file name is : ");


		 try {
		      // Get the filename and build the local file path
		      String filename = uploadfile.getOriginalFilename();
		      String directory = "/tmp/";
		      String filepath = Paths.get(directory, filename).toString();

		      // Save the file locally
		      BufferedOutputStream stream =
		          new BufferedOutputStream(new FileOutputStream(new File(filepath)));
		      stream.write(uploadfile.getBytes());
		      stream.close();
		    }
		    catch (Exception e) {
		      System.out.println(e.getMessage());
		      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		    }
		    return new ResponseEntity<String>(serviceID, HttpStatus.OK);
	}

	@RequestMapping(value = "/model_run_file", method = RequestMethod.POST)
	public ResponseEntity<?> serviceRunWithFile(@RequestParam("file") MultipartFile uploadfile, @RequestParam("service")String serviceInput,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws IOException {
		ServiceLogger.log(logTag, "serviceInput :" + serviceInput.toString());
		ServiceLogger.log(logTag, "fileName :" + uploadfile.getName());
		ServiceLogger.log(logTag, "content type :" + uploadfile.getContentType());

    	int runId;

    	JSONObject inputs = new JSONObject(serviceInput);
    	String sId = inputs.getString("serviceId");
    	JSONObject inpars = inputs.getJSONObject("inParams");
    	int serviceId = new Integer(sId);
    	
    	RunDomeModelResponse response = new RunDomeModelResponse();
    	try {
    		int userId = CompanyUserUtil.getUserId(userEPPN);
    		if(!supService.checkUserServicePermit(serviceId, userId)) {
    			throw new ServiceUseException("Could not find service use permit for user: " + userId);
    		}
    		ServiceRunDOMEAPI serviceRunInstance = new ServiceRunDOMEAPI();
    		HashMap paras = new HashMap<String, DomeModelParam>();
    		//Map ins = serviceInput.getInParams();
    		Iterator it = inpars.keys();
    		while (it.hasNext()) {
    			String variableName = (String)it.next();
    			JSONObject variableValue = (JSONObject) inpars.getJSONObject(variableName);
    			DomeModelParam parValue = new DomeModelParam();

    			try {
    				parValue.setType(variableValue.getString("type"));
    				}
    				catch (Exception e)
    				{
    					parValue.setType(null);
    				}
    				try {
    					parValue.setName(variableValue.getString("name"));
    					}
    					catch (Exception e)
    					{
    						parValue.setName(null);
    					}
    				try {
    					parValue.setUnit(variableValue.getString("unit"));
    					}
    					catch (Exception e)
    					{
    						parValue.setUnit(null);
    					}
    				try {
    					parValue.setCategory(variableValue.getString("category"));
    					}
    					catch (Exception e)
    					{
    						parValue.setCategory(null);
    					}
    				try {
    					if (variableValue.getString("type").equals("File"))
    						parValue.setValue(variableName);
    					else parValue.setValue(variableValue.getString("value"));
    					}
    					catch (Exception e)
    					{
    						parValue.setValue(null);
    					}
    				try {
    					parValue.setParameterid(variableValue.getString("parameterid"));
    					}
    					catch (Exception e)
    					{
    						parValue.setParameterid(null);
    					}
    				try {
    					parValue.setInstancename(variableValue.getString("instancename"));
    					}
    					catch (Exception e)
    					{
    						parValue.setInstancename(null);
    					}
    				try {
    					parValue.setInstancename(variableValue.getString("defaultValue"));
    					}
    					catch (Exception e)
    					{
    						parValue.setInstancename(null);
    					}
    			// Need a type change here.
    				paras.put(variableName, parValue);
    		}
    		runId = serviceRunInstance.runModel(serviceId,paras,uploadfile, userId);
    		ServiceLogger.log(logTag, "Successfully called runModel, serviceIdStr: " + sId + " called by user " + userEPPN);
    		response.setRunId(runId);
        }
        catch (Exception e)
        {
        	ServiceLogger.log(logTag, "Exception in serviceRun, serviceIdStr: " + sId + " called by user " + userEPPN);
        	ServiceLogger.log(logTag, "Exception: " + e.toString());
        	return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<RunDomeModelResponse>(response, HttpStatus.OK);
	}


	@RequestMapping(value = "/model_run", method = RequestMethod.POST)
    public ResponseEntity<RunDomeModelResponse> serviceRun (@RequestBody RunDomeModelInput serviceInput, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

    	int runId;
    	String sId = serviceInput.getServiceId();
    	int serviceId = new Integer(sId);
    	RunDomeModelResponse response = new RunDomeModelResponse();
    	try {
    		int userId = CompanyUserUtil.getUserId(userEPPN);
    		if(!supService.checkUserServicePermit(serviceId, userId)) {
    			throw new ServiceUseException("Could not find service use permit for user: " + userId);
    		}
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
    		runId = serviceRunInstance.runModel(serviceId,paras,userId);
				JSONObject jsonParams = new JSONObject(paras);
    		ServiceLogger.log(logTag, "Success in serviceRun, serviceIdStr: " + serviceInput.getServiceId() + " serviceTitle: " + serviceDao.getService(Integer.parseInt(serviceInput.getServiceId()), userEPPN).getTitle() + " called by user " + userEPPN + " with params: " + jsonParams.toString());
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
