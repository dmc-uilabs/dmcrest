package org.dmc.services.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;

import java.util.List;

public class DomeAPIDao {

	private final String logTag = DomeAPIDao.class.getName();

	private String printNameString(String name, String value) {
		return  "\""+name+"\":\"" + value +"\"";
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
			URL url = new URL(domeEntity.getDomeServer());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream out = conn.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");

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
				urlStr.append(printNameString("version", domeEntity.getVersion()));
				urlStr.append(",");
				urlStr.append(printNameString("modelId", domeEntity.getModelId()));
				urlStr.append(",");
				urlStr.append(printNameString("description", domeEntity.getDescription()));
				urlStr.append(",");
				urlStr.append(printNameString("dateModified", domeEntity.getDateModified()));
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

			writer.write(URLEncoder.encode(urlStr.toString(), "UTF-8"));

			writer.close();
			out.close();

			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}

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

		} catch (IOException e) {
			throw new DMCServiceException(DMCError.Generic, "unable to communicate with Dome Server " + domeEntity.getDomeServer());
		}

		
	}

}