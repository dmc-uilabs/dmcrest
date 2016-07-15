package org.dmc.services.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceRunServiceInterfaceDAO {
		
	//TODO: The initial values will be used as development stumb for modelRun.
	//  The real values need to be filled in based on the database contents.
	private int interfaceId;
	private int version;
	private String modelID; 
	private String interfaceIdStr; 
	private String type;
	private String name;
	private ArrayList<InParameter> inpars;
	private ArrayList<OutParameter> outpars;
	private int serviceId;
	private int serverId;
	private DOMEServerDAO domeServer;
	private InterfacePath iPath;
	
	
	// A working string for test only.
	public String getTestDOMEInterfaceString()
	{
		String result = "{\"interFace\":{\"version\":1,\"modelId\":\"aff647da-d82f-1004-8e7b-5de38b2eeb0f\"," +
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
		return result;
	}
	
	public static int getServiceId(String interfaceIdStr) throws DMCServiceException
	{
	    int result = -9;
	    String query = "select service_id from service_interface where interface_id_str=?";
	    try {
	    PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
		preparedStatement.setString(1,interfaceIdStr);
		ResultSet rs = preparedStatement.executeQuery();		
		if (!rs.next())
			{
				throw (new DMCServiceException(DMCError.ServiceIDNotExist, "For method id:" + interfaceIdStr +", serviceId not exist"));
			}
		else
			{
				result = rs.getInt("service_id");
				return result;
			}
		}
		catch (SQLException e)
		{
			throw (new DMCServiceException(DMCError.ServiceIDNotExist, e.toString()));
		}
	}
	
	public String printNameInt(String name, int value)
	{
		return  "\""+name+"\":" + value;
	}
	public String printNameString(String name, String value)
	{
		return  "\""+name+"\":\"" + value +"\"";
	}
	public String printNameJSON(String name, String value)
	{
		return  "\""+name+"\":{" + value +"}";
	}
	public String printNameValue(String name, String value)
	{
		String result = "\""+name+"\":";
		try {
			double v = new Double(value);
			result = result + v;
		}
		catch (NumberFormatException e)
		{
			result = result +"\"" + value + "\"";
		}
		return result;
	}
	public int getInterfaceID()
	{
		return interfaceId;
	}
	public DOMEServerDAO getDOMEServer()
	{
		return domeServer;
	}
	
	// The DOME API invocation need a particular order
	public String toDOMEString()
	{
		String result="{";
		String interfaceValue = printNameInt("version", this.version) + ","
				+ printNameString("modelId", this.modelID) + ","
				+ printNameString("interfaceId", this.interfaceIdStr) +"," 
				+ printNameString("type", this.type) + ","
				+ printNameString("name", this.name) + ","
				+ this.iPath.toDOMEString();
		String interfaceString1 = printNameJSON("interFace", interfaceValue);
		//System.out.println("interfaceString1 = " + interfaceString1);
		result = result + interfaceString1 + ",";
		
		String inParValue = "";
		for (int i=0;i<this.inpars.size();i++)
		{
			inParValue=inParValue+inpars.get(i).toDomeString();
			if (i!=this.inpars.size()-1)
				inParValue = inParValue + ",";
		}
		String inParString2 = printNameJSON("inParams",inParValue);
		result = result + inParString2 + ",";
		
		String outParValue = "";
		for (int i=0;i<this.outpars.size();i++)
		{
			outParValue=outParValue+outpars.get(i).toDomeString();
			if (i!=this.outpars.size()-1)
				outParValue = outParValue + ",";
		}
		String outParString3 = printNameJSON("outParams",outParValue);
		result = result + outParString3 + ",";
				
		String modelNameString4 = printNameString("modelName", this.name);
		result = result + modelNameString4 + ",";
		
		String modelDescription5 = printNameString("modelDescription","");
		result = result + modelDescription5 + ",";
		
		String server6 = this.domeServer.toDOMEString();
		result = result + server6 + "}";
		System.out.println("result = "+result);
		
		return result;
		
	}
	
	// The DOME API invocation need a particular order
	public String toDOMEStringInPars(Map inPars)
	{
		String result="{";
		String interfaceValue = printNameInt("version", this.version) + ","
				+ printNameString("modelId", this.modelID) + ","
				+ printNameString("interfaceId", this.interfaceIdStr) +"," 
				+ printNameString("type", this.type) + ","
				+ printNameString("name", this.name) + ","
				+ this.iPath.toDOMEString();
		String interfaceString1 = printNameJSON("interFace", interfaceValue);
		//System.out.println("interfaceString1 = " + interfaceString1);
		result = result + interfaceString1 + ",";
		
		String inParValue = "";
		for (int i=0;i<this.inpars.size();i++)
		{
			String name = inpars.get(i).name;
			DomeModelParam valueObj = (DomeModelParam)inPars.get(name);
			String value = valueObj.getValue().toString();
			inParValue=inParValue+inpars.get(i).toDomeStringWithValue(value);
			if (i!=this.inpars.size()-1)
				inParValue = inParValue + ",";
		}
		String inParString2 = printNameJSON("inParams",inParValue);
		result = result + inParString2 + ",";
		
		String outParValue = "";
		for (int i=0;i<this.outpars.size();i++)
		{
			outParValue=outParValue+outpars.get(i).toDomeString();
			if (i!=this.outpars.size()-1)
				outParValue = outParValue + ",";
		}
		String outParString3 = printNameJSON("outParams",outParValue);
		result = result + outParString3 + ",";
				
		String modelNameString4 = printNameString("modelName", this.name);
		result = result + modelNameString4 + ",";
		
		String modelDescription5 = printNameString("modelDescription","");
		result = result + modelDescription5 + ",";
		
		String server6 = this.domeServer.toDOMEString();
		result = result + server6 + "}";
		System.out.println("result = "+result);
		
		return result;
		
	}
	
	public JSONObject toJSON()
	{
		JSONObject result = new JSONObject();
		JSONObject interfaceJSON = new JSONObject();
		interfaceJSON.put("version", version);
		interfaceJSON.put("modelId", modelID);
		interfaceJSON.put("interfaceId", interfaceId);
		interfaceJSON.put("type", type);
		interfaceJSON.put("name", name);
		interfaceJSON.put("path", iPath.toJSON());
		interfaceJSON.put("interfaceId",interfaceIdStr);
		result.put("interFace", interfaceJSON);
		JSONObject inParams = inParamsToJSON();
		result.put("inParams", inParams);
		JSONObject outParms = outParamsToJSON();
		result.put("outParams",outParms);
		result.put("modelName", name);
		result.put("modelDescription", "");
		result.put("server", domeServer.toJSON());
		return result;
	}
	
	public JSONObject inParamsToJSON()
	{
		JSONObject inParsJSON = new JSONObject();
		for (int i=0;i<inpars.size();i++)
		{
			InParameter in = inpars.get(i);
			JSONObject inJSON = in.toJSON();
			inParsJSON.put(inJSON.getString("name"),inJSON);
		}
		return inParsJSON;
	}
	
	public JSONObject outParamsToJSON()
	{
		JSONObject inParsJSON = new JSONObject();
		for (int i=0;i<outpars.size();i++)
		{
			OutParameter out = outpars.get(i);
			JSONObject inJSON = out.toJSON();
			inParsJSON.put(inJSON.getString("name"),inJSON);
		}
		return inParsJSON;
	}

	public InterfacePath getIPath(){
		return this.iPath;
	}
	
	public ServiceRunServiceInterfaceDAO(int sId) throws DMCServiceException
	{
		
		ResultSet rs = null;
		try{
			String query = "select * from service_interface where service_id = ?";
			PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
			preparedStatement.setInt(1,sId);
			rs = preparedStatement.executeQuery();
			if (!rs.next())
			{
				throw new DMCServiceException(DMCError.OtherSQLError,"service_id " + sId +" does not exist.");
			}
			this.serviceId = sId;
			this.version=rs.getInt("version");
			this.modelID=rs.getString("model_id");
			this.interfaceIdStr=rs.getString("interface_id_str");
			this.type=rs.getString("type");
			this.name=rs.getString("name");
			this.serverId=rs.getInt("server_id");
			this.interfaceId = rs.getInt("interface_id");
			this.getParameters();
			this.domeServer = new DOMEServerDAO(serverId);
			this.iPath = new InterfacePath(interfaceId);
		}
		catch (SQLException e)
		{
			throw new DMCServiceException(DMCError.OtherSQLError,"SQLException: " + e.toString());
		}
	}
	
	public ServiceRunServiceInterfaceDAO(String interfaceIdString) throws DMCServiceException
	{
		
		ResultSet rs = null;
		try{
			String query = "select * from service_interface where interface_id_str = ?";
			PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
			preparedStatement.setString(1,interfaceIdString);
			rs = preparedStatement.executeQuery();
			if (!rs.next())
			{
				throw new DMCServiceException(DMCError.OtherSQLError,"interface_id_str " + interfaceIdString +" does not exist.");
			}
			this.serviceId = rs.getInt("service_id");
			this.version=rs.getInt("version");
			this.modelID=rs.getString("model_id");
			this.interfaceIdStr=rs.getString("interface_id_str");
			this.type=rs.getString("type");
			this.name=rs.getString("name");
			this.serverId=rs.getInt("server_id");
			this.interfaceId = rs.getInt("interface_id");
			this.getParameters();
			this.domeServer = new DOMEServerDAO(serverId);
			this.iPath = new InterfacePath(interfaceId);
		}
		catch (SQLException e)
		{
			throw new DMCServiceException(DMCError.OtherSQLError,"SQLException: " + e.toString());
		}
	}
	
	public void getParameters() throws DMCServiceException
	{
		try{
			String query = "select * from service_interface_parameter where interface_id=?";
			PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
			preparedStatement.setInt(1,this.interfaceId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next())
			{
				boolean input_parameter = rs.getBoolean("input_parameter");
				if (input_parameter)
				{
					if (inpars==null)
						inpars = new ArrayList<InParameter>();
					InParameter inPar = new InParameter();
					inPar.set(
							rs.getInt("parameter_id"),
							this.interfaceId,
							rs.getString("name"),
							rs.getString("type"),
							rs.getString("unit"),
							rs.getString("category"),
							rs.getString("default_value"),
							rs.getString("parameter_id_txt"),
							rs.getInt("parameter_position")
							);
					inpars.add(inPar);
				}
				else
				{
					if (outpars==null)
						outpars = new ArrayList<OutParameter>();
					OutParameter outPar = new OutParameter();
					outPar.set(
							rs.getInt("parameter_id"),
							this.interfaceId,
							rs.getString("name"),
							rs.getString("type"),
							rs.getString("unit"),
							rs.getString("category"),
							rs.getString("default_value"),
							rs.getString("parameter_id_txt"),
							rs.getInt("parameter_position"),
							rs.getString("instancename")
							);	
					outpars.add(outPar);
				}
					
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new DMCServiceException(DMCError.OtherSQLError,"Error in query service_id " + this.serviceId +" parameters.");
		}
	}
	
	public int getNumInputPars(int interfaceID) throws Exception
	{
		String query = "select count(*) num from service_interface_parameter where interface_id=? and input_parameter=true";
		PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
		preparedStatement.setInt(1,interfaceID);
		ResultSet rs = preparedStatement.executeQuery();
		int result = -9999;
	    if (rs.next()) result=rs.getInt("num");
	    return result;
	}
	
	public int getNumOutputPars(int interfaceID) throws Exception
	{
		String query = "select count(*) num from service_interface_parameter where interface_id=? and input_parameter=false";
		PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
		preparedStatement.setInt(1,interfaceID);
		ResultSet rs = preparedStatement.executeQuery();
		int result = -9999;
	    if (rs.next()) result=rs.getInt("num");
	    return result;		
	}
	
	public String getName()
	{
		return this.name;
	}
	
	class InParameter
	{
		private int parameterId;
		private int interfaceId;
		private String name;
		private String type;
		private String unit;
		private String category;
		private String defaultValue;
		private String parameter_id_txt;
		private int parameter_position;
		
		
		//TODO: need to store the parameters to the database
		public void set(int paId, int iId, String n, String t, String u, String c, String dv, String pId, int pp )
		{
			this.parameterId = paId;
			this.interfaceId = iId;
			this.name = n;
			this.type = t;
			this.unit = u;
			this.category = c;
			this.defaultValue = dv;
			this.parameter_id_txt = pId;
			this.parameter_position = pp;
		}
		
		 
		public JSONObject toJSON()
		{
			JSONObject json = new JSONObject();
			json.put("name", this.name);
			json.put("type", this.type);
			json.put("unit", this.unit);
			json.put("category", this.category);
			json.put("value", this.defaultValue);
			json.put("parameterid", this.parameter_id_txt);
			return json;
		}
		
		public String toDomeString()
		{
			String value=printNameString("name", this.name) + "," +
					printNameString("type", this.type) + "," +
					printNameString("unit", this.unit) + "," +
					printNameString("category", this.category) + "," +
					printNameValue("value", this.defaultValue) + "," +
					printNameString("parameterid", this.parameter_id_txt);
			return printNameJSON(this.name,value); 
		}
		public String toDomeStringWithValue(String v)
		{
			String value=printNameString("name", this.name) + "," +
					printNameString("type", this.type) + "," +
					printNameString("unit", this.unit) + "," +
					printNameString("category", this.category) + "," +
					//printNameValue("value", this.defaultValue) + "," +
					printNameValue("value", v) + "," +
					printNameString("parameterid", this.parameter_id_txt);
			return printNameJSON(this.name,value); 
		}
	}
	class OutParameter
	{
		private int parameterId;
		private int interfaceId;
		private String name;
		private String type;
		private String unit;
		private String category;
		private String defaultValue;
		private String parameter_id_txt;
		private int parameter_position;
		private String instanceName;
		
		
		public void set(int paId, int iId, String n, String t, String u, String c, String dv, String pId, int pp, String iN )
		{
			this.parameterId = paId;
			this.interfaceId = iId;
			this.name = n;
			this.type = t;
			this.unit = u;
			this.category = c;
			this.defaultValue = dv;
			this.parameter_id_txt = pId;
			this.parameter_position = pp;
			this.instanceName = iN;
		}
		
		public String toDomeString()
		{
			String value=printNameString("name", this.name) + "," +
					printNameString("type", this.type) + "," +
					printNameString("unit", this.unit) + "," +
					printNameString("category", this.category) + "," +
					printNameValue("value", this.defaultValue) + "," +
					printNameString("parameterid", this.parameter_id_txt) + "," +
					printNameString("instancename", this.instanceName);
			return printNameJSON(this.name,value);
		}
		
		public JSONObject toJSON()
		{
			JSONObject json = new JSONObject();
			json.put("name", this.name);
			json.put("type", this.type);
			json.put("unit", this.unit);
			json.put("category", this.category);
			json.put("value", this.defaultValue);
			json.put("parameterid", this.parameter_id_txt);
			json.put("instancename", this.instanceName);
			return json;
		}
	}
	
	class InterfacePath
	{
		int interface_id;
		ArrayList<Integer> path;
		
		public InterfacePath(int interface_id) throws DMCServiceException
		{
			try{
				String query = "select * from service_interface_path where interface_id = ?";
				PreparedStatement preparedStatement = DBConnector
					.prepareStatement(query);
				preparedStatement.setInt(1,interface_id);
				ResultSet rs=preparedStatement.executeQuery();
				path = new ArrayList<Integer>();
				while (rs.next())
				{
					Integer p = rs.getInt("path");
					path.add(p);
				}
			}
			catch (SQLException e)
			{
				throw new DMCServiceException(DMCError.OtherSQLError,"SQLException: " + e.toString());
			}		
		}
		
		public String toDOMEString()
		{
			String pathValue="[";
			
			for (int i =0; i<path.size(); i++)
			{
				pathValue = pathValue+path.get(i);
				if (i==path.size()-1) pathValue = pathValue +"]";
				else pathValue = pathValue+",";
			}
			
			return "\"path\":"+pathValue;
		}
		
		public JSONArray toJSON()
		{
			JSONArray resultOjb = new JSONArray();

			for (int i=0;i<path.size();i++)
			{
				resultOjb.put(path.get(i));
			}
			return resultOjb;
		}
	}
	
/*	public static void main(String[] args)
	{
		try{
			int service_id = 3;
			ServiceRunServiceInterfaceDAO interfaceDao = new ServiceRunServiceInterfaceDAO(service_id);
			String interfaceString = interfaceDao.toDOMEString();
			System.out.println("interfaceString = " + interfaceString);
			String domeInterfacePath = interfaceDao.iPath.toDOMEString();
			System.out.println("domeInterfacePath = " + domeInterfacePath);
			interfaceDao.toDOMEString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
*/		
}
