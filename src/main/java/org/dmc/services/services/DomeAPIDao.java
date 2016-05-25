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

import java.util.ArrayList;
import java.util.List;

public class DomeAPIDao {

	private final String logTag = DomeAPIDao.class.getName();
	
	public static void main(String[] args) {
		System.out.println("Starting...");
		DomeAPIDao inst = new DomeAPIDao();
		DomeEntity domeEntity = new DomeEntity();
		
		domeEntity.setDomeServer("http://localhost:8080/DOMEApiServicesV7");
		
		/*domeEntity.setName("Fracture-Mechanics");
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));
		domeEntity.setPath(path);
		domeEntity.setType("folder"); */
		
		domeEntity.setVersion("1");
		domeEntity.setModelId("aff647dc-d82f-1004-8e7b-5de38b2eeb0f");
		domeEntity.setDescription("");
		domeEntity.setDateModified("1416717627000");
		domeEntity.setName("AppliedLoad");
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));
		domeEntity.setPath(path);
		domeEntity.setType("model");
		
		String result = new String();
		
		System.out.println(domeEntity.toString());
		System.out.println();

		
		try {
			result = inst.getChildren(domeEntity);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}

	private String printNameString(String name, String value) {
		return  "\""+name+"\":\"" + value +"\"";
	}
	
	private String printNameInt(String name, String value) {
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
			
			//OutputStream out = conn.getOutputStream();
			//Writer writer = new OutputStreamWriter(out, "UTF-8");

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
				throw new DMCServiceException(DMCError.Generic, "unable to communicate with Dome Server " + domeEntity.getDomeServer() + " - unknown type " + domeEntity.getType());
			}
			
			System.out.println("REQUEST:");
			System.out.println(urlStr.toString());
			System.out.println();
			
			//writer.write(URLEncoder.encode(urlStr.toString(), "UTF-8"));
			
			writer.write(urlStr.toString().getBytes( StandardCharsets.UTF_8 ));

			writer.close();
			out.close();

			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
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
			throw new DMCServiceException(DMCError.Generic, "unable to communicate with Dome Server " + domeEntity.getDomeServer());
		}

		
	}

}