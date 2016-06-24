package org.dmc.services.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import org.dmc.services.ServiceLogger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceRunDOMEAPI {
    private static final String LOGTAG ServiceRunDOMEAPI.class.getName();
 
	/*public static void main(String[] args)
	{
		int user_id = 111;
		int service_id = 3;
		ServiceRunDOMEAPI instance = new ServiceRunDOMEAPI();
		try 
		{
			// This will create a service call return modelRunId
			int modelRunId = instance.runModel(service_id,user_id);	
			//int modelRunId = 57;
			//instance.pollService(modelRunId, service_id);
			System.out.println("The model runID = " + modelRunId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/

	public int runModel(int service_id, int user_id) throws Exception
	{
		// TODO: Leave checking if user_id can run the model here.

		// Get DOMEInterface information
		ServiceRunServiceInterfaceDAO dInterface = new ServiceRunServiceInterfaceDAO(service_id);
		int server_id = dInterface.getDOMEServer().getServerID();
		int interface_id = dInterface.getInterfaceID();
		String domeJSonPars = dInterface.toDOMEString();
		// Get information of the DOMEServer
		DOMEServerDAO dServer = new DOMEServerDAO(server_id);
		// Get the unique queue id based on interface name and current time.
		String queue = dInterface.getName().replace(' ', '_')+"_"+System.currentTimeMillis();

		// Create database entry for this run.
		ServiceRunServiceDAO serviceRun = new ServiceRunServiceDAO();
		int modelRunID = serviceRun.createModelRunDBEntry(user_id, server_id, interface_id, queue);
			
		// Create a queue in the activeMQ server
		ServiceRunActiveMQ activeMQ = new ServiceRunActiveMQ();
		activeMQ.createQueue(queue);
			
		// Run model
		//TODO
		// Note that these information are different from port information provided in the database
		// Database information for DOME server is provided for local execution, 8080/DOMEApiServicesV7/runModel need to be parameterized later.
		String servicePath = "http://" + dServer.getServerURL() + ":" + dServer.getPort() + "/DOMEApiServicesV7/runModel";
		  URL url = new URL(servicePath);
		  HttpURLConnection conn =
		      (HttpURLConnection) url.openConnection();
		  conn.setRequestMethod("POST");
		  conn.setDoOutput(true);
		  conn.setDoInput(true);
		  conn.setUseCaches(false);
		  conn.setAllowUserInteraction(false);
		  conn.setRequestProperty("Content-Type",
		      "application/x-www-form-urlencoded");

		  // Create the form content
		  OutputStream out = conn.getOutputStream();
		  Writer writer = new OutputStreamWriter(out, "UTF-8");
		  writer.write("data");
		  writer.write("=");
		  writer.write(URLEncoder.encode(domeJSonPars, "UTF-8"));
		  writer.write("&");
		  writer.write("queue");
		  writer.write("=");
		  writer.write(URLEncoder.encode(queue, "UTF-8"));
		  
		  writer.close();
		  out.close();

		  if (conn.getResponseCode() != 200) {
		    throw new IOException(conn.getResponseMessage());
		  }

		  // Buffer the result into a string
		  BufferedReader rd = new BufferedReader(
		      new InputStreamReader(conn.getInputStream()));
		  StringBuilder sb = new StringBuilder();
		  String line;
		  while ((line = rd.readLine()) != null) {
		    sb.append(line);
		  }
		  rd.close();

		  conn.disconnect();
		  
		  return modelRunID;
	}
	
	public int runModel(String interfaceIdStr, Map inPars, int user_id) throws Exception
	{
		// TODO: Leave checking if user_id can run the model here.

		// Get DOMEInterface information
		ServiceRunServiceInterfaceDAO dInterface = new ServiceRunServiceInterfaceDAO(interfaceIdStr);
		int server_id = dInterface.getDOMEServer().getServerID();
		int interface_id = dInterface.getInterfaceID();
		
		// Print the required call string with values from the page 
		String domeJSonPars = dInterface.toDOMEStringInPars(inPars);
		// Get information of the DOMEServer
		DOMEServerDAO dServer = new DOMEServerDAO(server_id);
		// Get the unique queue id based on interface name and current time.
		String queue = dInterface.getName().replace(' ', '_')+"_"+System.currentTimeMillis();

		// Create database entry for this run.
		ServiceRunServiceDAO serviceRun = new ServiceRunServiceDAO();
		int modelRunID = serviceRun.createModelRunDBEntry(user_id, server_id, interface_id, queue);
			
		// Create a queue in the activeMQ server
		ServiceRunActiveMQ activeMQ = new ServiceRunActiveMQ();
		activeMQ.createQueue(queue);
			
		// Run model
		//TODO
		// Note that these information are different from port information provided in the database
		// Database information for DOME server is provided for local execution, 8080/DOMEApiServicesV7/runModel need to be parameterized later.
		String servicePath = "http://" + dServer.getServerURL() + ":" + dServer.getPort() + "/DOMEApiServicesV7/runModel";
		ServiceLogger.log(LOGTAG, "url to run model: " + servicePath);
		  URL url = new URL(servicePath);
		  HttpURLConnection conn =
		      (HttpURLConnection) url.openConnection();
		  conn.setRequestMethod("POST");
		  conn.setDoOutput(true);
		  conn.setDoInput(true);
		  conn.setUseCaches(false);
		  conn.setAllowUserInteraction(false);
		  conn.setRequestProperty("Content-Type",
		      "application/x-www-form-urlencoded");

		  // Create the form content
		  OutputStream out = conn.getOutputStream();
		  Writer writer = new OutputStreamWriter(out, "UTF-8");
		  writer.write("data");
		  writer.write("=");
		  writer.write(URLEncoder.encode(domeJSonPars, "UTF-8"));
		  writer.write("&");
		  writer.write("queue");
		  writer.write("=");
		  writer.write(URLEncoder.encode(queue, "UTF-8"));
		  
		  writer.close();
		  out.close();

		  if (conn.getResponseCode() != 200) {
		    ServiceLogger.log(LOGTAG, "failed to run service: code = " + conn.getResponseCode() + " : " + conn.getResponseMessage());
		    throw new IOException(conn.getResponseMessage());
		  }

		  // Buffer the result into a string
		  BufferedReader rd = new BufferedReader(
		      new InputStreamReader(conn.getInputStream()));
		  StringBuilder sb = new StringBuilder();
		  String line;
		  while ((line = rd.readLine()) != null) {
		    sb.append(line);
		  }
		  rd.close();

		  conn.disconnect();
		  
		  return modelRunID;
	}
	
	public ServiceRunResult pollService(int runId, int user_id) throws Exception
	{
		
		ServiceRunResult result = new ServiceRunResult();
		// Return: 0 -- finished; 1 -- partial finished; 2 -- no new results found from the queue
		//int result=0;
		// Query database to decide if all the output parameters are collected. If so, return 0
		ServiceRunServiceDAO serviceRun = new ServiceRunServiceDAO();
		serviceRun.getData(runId);
		int interfaceId = serviceRun.getIntefaceId();
		int service_id = serviceRun.getServiceId();
		ServiceRunServiceInterfaceDAO si = new ServiceRunServiceInterfaceDAO(service_id);
		int numOutput =  si.getNumOutputPars(interfaceId);
		int collectedOutput = serviceRun.getNumOutputPars(runId);
		if (numOutput==collectedOutput) 
			{
				result.setStatus(ServiceRunResult.COMPLETE);
				return result;
			}
		// Otherwise try to find out if there is any new messages from ActiveMQ
		String queueName = serviceRun.getQueueName();
		ServiceRunActiveMQ activeMQ = new ServiceRunActiveMQ();
		ArrayList<String> newResult = activeMQ.readMessageFromMQ(queueName);
		if (newResult.size()==0) 
			{
				result.setStatus(ServiceRunResult.RUNNING);
				return result;
			}
		// Now we need to parse the message, store them in DB, and also check if the service run finished, example messages:
		// Example input parameters -- for a simple method.
/*		Got 1. message: {"event":"class mit.cadlab.dome3.api.ParameterValueChangeEvent","param":"SpecimenWidth","id":{"idString":"d9f30f3a-d800-1004-8f53-704dbfababa8"},"old_val":[3.0],"new_val":[3.0],"occur":1462397804202}
		Got 6. message: {"event":"class mit.cadlab.dome3.api.ParameterStatusChangeEvent","param":"SpecimenWidth","id":{"idString":"d9f30f3a-d800-1004-8f53-704dbfababa8"},"old_val":"STALE","new_val":"CONSISTENT","occur":1462397804496}
 
 
		Got 2. message: {"event":"class mit.cadlab.dome3.api.ParameterValueChangeEvent","param":"CrackLength","id":{"idString":"d9f30f37-d800-1004-8f53-704dbfababa8"},"old_val":[1.0],"new_val":[1.0],"occur":1462397804209}
		Got 4. message: {"event":"class mit.cadlab.dome3.api.ParameterStatusChangeEvent","param":"CrackLength","id":{"idString":"d9f30f37-d800-1004-8f53-704dbfababa8"},"old_val":"STALE","new_val":"CONSISTENT","occur":1462397804487}

		// Output parameters
		Got 3. message: {"event":"class mit.cadlab.dome3.api.ParameterStatusChangeEvent","param":"Alpha","id":{"idString":"d9f30f3d-d800-1004-8f53-704dbfababa8"},"old_val":"INCONSISTENT","new_val":"WAITING_VALIDATION","occur":1462397804479}
		Got 5. message: {"event":"class mit.cadlab.dome3.api.ParameterStatusChangeEvent","param":"Alpha","id":{"idString":"d9f30f3d-d800-1004-8f53-704dbfababa8"},"old_val":"WAITING_VALIDATION","new_val":"CONSISTENT","occur":1462397804495}
		Got 7. message: {"event":"class mit.cadlab.dome3.api.ParameterValueChangeEvent","param":"Alpha","id":{"idString":"d9f30f3d-d800-1004-8f53-704dbfababa8"},"old_val":[0.3333333333333333],"new_val":[0.3333333333333333],"occur":1462397804495}

		// End of run
		Got 8. message: {"event":"class mit.cadlab.dome3.api.ParameterStatusChangeEvent","param":8,"id":{"idString":"end_of_run"},"old_val":"RUNNING","new_val":"SUCCESS","occur":1462397805016}

		
		// Get message with:  "event":"class mit.cadlab.dome3.api.ParameterValueChangeEvent" || "param":"Alpha" (output parameters), get the new_val into the database
		// Get message with:  "event":"class mit.cadlab.dome3.api.ParameterStatusChangeEvent"  || "param":8   (2*num of input + 3*num of output + 1), indication of finish of the run
*/
		int msgNum = newResult.size();
		ServiceRunOuts outs = new ServiceRunOuts();
		for (int i=0;i<msgNum;i++)
		{
			JSONObject msgObj = new JSONObject(newResult.get(i));
			JSONObject idObj = msgObj.getJSONObject("id");
			if ((idObj.getString("idString")).compareTo("end_of_run") == 0)
			{
				// Need to write this method to keep that the end_of_run is received
				serviceRun.setEndOfRun();
			}
			else
			{
				String event=msgObj.getString("event");
				if ((event.compareTo("class mit.cadlab.dome3.api.ParameterValueChangeEvent"))==0)
				{
					String id = msgObj.getJSONObject("id").getString("idString");
					JSONArray newValues = msgObj.getJSONArray("new_val");
					// Can we deal with string array?  :--(
					String newValueString ="";
					for (int j=0;j<newValues.length();j++)
					{
						double jth = newValues.getDouble(j);
						newValueString = newValueString + jth;
						if (!( j==(newValues.length()-1) )) 
							newValueString = newValueString + ","; 
					}
					serviceRun.addNewValue(id,newValueString);
					ServiceRunOut r = new ServiceRunOut();
					r.setParameterid(id);
					r.setValue(newValueString);
					outs.add(r);
				}
			}
		}
		if (serviceRun.getCollectedNumOutputPars()==numOutput && serviceRun.getStatus()==1)
		{
			// Delete queue from ActiveMQ
			activeMQ.deleteQueue(queueName);
			serviceRun.setEndOfRunTime();
			result.setStatus(ServiceRunResult.COMPLETE);
		}
		else result.setStatus(ServiceRunResult.RUNNING);
		result.setOuts(outs);
		return result;
	}	
}
