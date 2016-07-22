package org.dmc.services.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class DomeAPIDao {

	private final String logTag = DomeAPIDao.class.getName();
	
	/*public static void main(String[] args) {
		System.out.println("Starting...");
		DomeAPIDao inst = new DomeAPIDao();
		DomeEntity domeEntity = new DomeEntity();
		DomeModel domeModel = new DomeModel();
		
		domeEntity.setDomeServer("http://localhost:8082/DOMEApiServicesV7");
		domeModel.setDomeServer("http://localhost:8082/DOMEApiServicesV7");
		
//		domeEntity.setName("Fracture-Mechanics");
//		List<BigDecimal> path = new ArrayList<BigDecimal>();
//		path.add(new BigDecimal(30));
//		domeEntity.setPath(path);
//		domeEntity.setType("folder");
//		

//		domeEntity.setVersion(new BigDecimal(1));
//		domeEntity.setModelId("aff647dc-d82f-1004-8e7b-5de38b2eeb0f");
//		domeEntity.setDescription("");
//		domeEntity.setDateModified(new BigDecimal("1416717627000"));
//		domeEntity.setName("AppliedLoad");
//		List<BigDecimal> path = new ArrayList<BigDecimal>();
//		path.add(new BigDecimal(30));
//		domeEntity.setPath(path);
//		domeEntity.setType("model");
//		
//		System.out.println(domeEntity.toString());
//		System.out.println();
		
		
		domeModel.setVersion(new BigDecimal(1));
		domeModel.setModelId("bd85f846-d8f4-1004-8f94-37c24b788523");
		domeModel.setInterfaceId("bd85f847-d8f4-1004-8f94-37c24b788523");
		domeModel.setName("Upload+a+file+interface");
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(31));
		domeModel.setPath(path);
		domeModel.setType("interface");
		
//		domeModel.setVersion(new BigDecimal(1));
//		domeModel.setProjectId("Uploaded+File+Size");
//		domeModel.setInterfaceId("3a2f15fd-d8f6-1004-85e6-e48afddadd5b");
//		domeModel.setName("Project+Interface");
//		List<BigDecimal> path = new ArrayList<BigDecimal>();
//		path.add(new BigDecimal(31));
//		domeModel.setPath(path);
//		domeModel.setType("interface");
		
		System.out.println(domeModel.toString());
		System.out.println();
		
		String result = new String();
		

		try {
			//result = inst.getChildren(domeEntity);
			result = inst.getModel(domeModel);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}*/

	private String printNameString(String name, String value) {
		return  "\""+name+"\":\"" + value +"\"";
	}
	
	private String printNameInt(String name, BigDecimal value) {
		return  "\""+name+"\":" + value;
	}
	
	private String printNameList(String name, List<BigDecimal> value) {
		StringBuilder str = new StringBuilder();
		str.append("\"");
		str.append(name);
		str.append("\"");
		str.append(":");
		str.append("[");
		for(int i = 0; i < value.size(); i++) {
			str.append(value.get(i).toString());
			if (i != value.size()-1) {
				str.append(",");
			}
		}
		str.append("]");

		return  str.toString();
	}
	
	public String getChildren(DomeEntity domeEntity) throws DMCServiceException {

		try {
			URL url = new URL(domeEntity.getDomeServer() + "/getChildren");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream out = conn.getOutputStream();
			DataOutputStream writer =  new DataOutputStream(out);

			StringBuilder urlStr = new StringBuilder();

			// Create the form
			if (domeEntity.getType() == null) {
				// Do nothing
			} else if ((domeEntity.getType()).equals("folder")) {
				// Sample string -->
				// data={\"type\":\"folder\",\"name\":\"Fracture-Mechanics\",\"path\":[30]}
				urlStr.append("data");
				urlStr.append("=");
				urlStr.append("{");
				urlStr.append(printNameString("type", domeEntity.getType()));
				urlStr.append(",");
				urlStr.append(printNameString("name", domeEntity.getName()));
				urlStr.append(",");
				urlStr.append(printNameList("path", domeEntity.getPath()));
				urlStr.append("}");
			} else if ((domeEntity.getType()).equals("model")) {
				// Sample string -->
				// "data={"version":1,"modelId":"aff647da-d82f-1004-8e7b-5de38b2eeb0f","description":"","dateModified":1416717607000,"type":"model","name":"Alpha","path":[30]}"
				urlStr.append("data");
				urlStr.append("=");
				urlStr.append("{");
				urlStr.append(printNameInt("version", domeEntity.getVersion()));
				urlStr.append(",");
				urlStr.append(printNameString("modelId", domeEntity.getModelId()));
				urlStr.append(",");
				urlStr.append(printNameString("description", domeEntity.getDescription()));
				urlStr.append(",");
				urlStr.append(printNameInt("dateModified", domeEntity.getDateModified()));
				urlStr.append(",");
				urlStr.append(printNameString("type", domeEntity.getType()));
				urlStr.append(",");
				urlStr.append(printNameString("name", domeEntity.getName()));
				urlStr.append(",");
				urlStr.append(printNameList("path", domeEntity.getPath()));
				urlStr.append("}");
			} else {
				throw new DMCServiceException(DMCError.IncorrectType, "getChildren: unable to communicate with Dome Server " + domeEntity.getDomeServer() + " - unknown type " + domeEntity.getType());
			}
			
			writer.write(urlStr.toString().getBytes( StandardCharsets.UTF_8 ));

			writer.close();
			out.close();

			if (conn.getResponseCode() != 200) {
				throw new DMCServiceException(DMCError.CannotConnectToDome, conn.getResponseMessage());
			} else {

				// Buffer the result into a string
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				conn.disconnect();
				
				return sb.toString();
			}

		} catch (IOException e) {
			throw new DMCServiceException(DMCError.CanNotGetChildren, "getChildren: unable to communicate with Dome Server " + domeEntity.getDomeServer());
		}

	}
	
	public String getModel(DomeModel domeModel) throws DMCServiceException {

		try {
			URL url = new URL(domeModel.getDomeServer() + "/getModel");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream out = conn.getOutputStream();
			DataOutputStream writer =  new DataOutputStream(out);

			StringBuilder urlStr = new StringBuilder();

			// Create the form
			if (domeModel.getProjectId() != null) {
				// Sample string -->
				// "data={"version":1,"interfaceId":"3a2f15fd-d8f6-1004-85e6-e48afddadd5b","projectId":"Uploaded+File+Size","type":"interface","name":"Project+Interface","path":[31]}"
				urlStr.append("data");
				urlStr.append("=");
				urlStr.append("{");
				urlStr.append(printNameInt("version", domeModel.getVersion()));
				urlStr.append(",");
				urlStr.append(printNameString("interfaceId", domeModel.getInterfaceId()));
				urlStr.append(",");
				urlStr.append(printNameString("projectId", domeModel.getProjectId()));
				urlStr.append(",");
				urlStr.append(printNameString("type", domeModel.getType()));
				urlStr.append(",");
				urlStr.append(printNameString("name", domeModel.getName()));
				urlStr.append(",");
				urlStr.append(printNameList("path", domeModel.getPath()));
				urlStr.append("}");
			} else if (domeModel.getModelId() != null) {
				// Sample string -->
				// "data={"version":1,"modelId":"bd85f846-d8f4-1004-8f94-37c24b788523","interfaceId":"bd85f847-d8f4-1004-8f94-37c24b788523","type":"interface","name":"Upload+a+file+interface","path":[31]}"
				urlStr.append("data");
				urlStr.append("=");
				urlStr.append("{");
				urlStr.append(printNameInt("version", domeModel.getVersion()));
				urlStr.append(",");
				urlStr.append(printNameString("modelId", domeModel.getModelId()));
				urlStr.append(",");
				urlStr.append(printNameString("interfaceId", domeModel.getInterfaceId()));
				urlStr.append(",");
				urlStr.append(printNameString("type", domeModel.getType()));
				urlStr.append(",");
				urlStr.append(printNameString("name", domeModel.getName()));
				urlStr.append(",");
				urlStr.append(printNameList("path", domeModel.getPath()));
				urlStr.append("}");
			} else {
				throw new DMCServiceException(DMCError.IncorrectType, "getModel: unable to communicate with Dome Server " + domeModel.getDomeServer() + " - unknown type " + domeModel.getType());
			}
			
			writer.write(urlStr.toString().getBytes( StandardCharsets.UTF_8 ));
			writer.close();
			out.close();

			if (conn.getResponseCode() != 200) {
				throw new DMCServiceException(DMCError.CannotConnectToDome, conn.getResponseMessage());
			} else {

				// Buffer the result into a string
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				conn.disconnect();		
				
				return sb.toString();
			}

		} catch (IOException e) {
			throw new DMCServiceException(DMCError.CanNotGetModel, "getModel: unable to communicate with Dome Server " + domeModel.getDomeServer());
		}

	}

}