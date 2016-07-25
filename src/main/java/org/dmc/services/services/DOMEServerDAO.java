package org.dmc.services.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanySkillDao;
import org.json.JSONObject;

// This class provides dome server information for Service Run.
public class DOMEServerDAO {
	
	//private final String logTag = DOMEServer.class.getName();
	
	private int serverID;
	private String serverURL;
	private String name;
	private int userId;
	private int port;
	private String userName;
	private String userSpace;
	private String userPass;

	/*public static void main(String[] args)
	{ 
		try{
		int server_id = 3;
		DOMEServerDAO ds = new DOMEServerDAO(server_id);
		System.out.println("Server: " + ds.toDOMEString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}*/

	public DOMEServerDAO(int serverId) throws DMCServiceException
	{
		ResultSet rs = null;
		try{
			String query = "select * from servers where server_id = ?";
			PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
			preparedStatement.setInt(1,serverId);
			rs = preparedStatement.executeQuery();
			if (!rs.next())
			{
				throw new DMCServiceException(DMCError.OtherSQLError,"Server with server_id " + serverId + " does not exist;");
			}
			this.serverID = serverId;
			this.serverURL = rs.getString("URL");
			this.name = rs.getString("alias");
			this.port = rs.getInt("port");
			this.userName = rs.getString("local_dome_user");
			this.userPass = rs.getString("local_dome_user_password");
			this.userSpace = rs.getString("dome_user_space");
			this.userId = rs.getInt("user_id");
		}
		catch (SQLException e)
		{
			throw new DMCServiceException(DMCError.OtherSQLError,"SQLException: " + e.toString());
		}
	}
	
	// An example
	//\"server\":{\"name\":\"localhost\",\"port\":\"7795\",\"user\":\"ceed\",\"pw\":\"ceed\",\"space\":\"USER\"}
	public String toDOMEString()
	{
		//Hard coded because the url in the table is a string contains url and other information.  This localhost is just for DOME to run so localhost is fine.
		String result = printNameString("name","localhost") + "," +
		//String result = printNameString("name",this.serverURL) + "," +
				// This is hard coded because nowhere to store this info
				//printNameString("port", ""+this.port) + "," +
				printNameString("port", "7795") + "," +
				printNameString("user","ceed") + "," +
				printNameString("pw", "ceed") + "," +
				printNameString("space","USER");
	    result = "\"server\":{" + result + "}";		
		return result;
	}
	public String printNameInt(String name, int value)
	{
		return  "\""+name+"\":" + value;
	}
	public String printNameString(String name, String value)
	{
		return  "\""+name+"\":\"" + value +"\"";
	}
	public String pringNameJSON(String name, String value)
	{
		return  "\""+name+"\":{" + value +"}";
	}
	public JSONObject toJSON()
	{
		JSONObject returnJSON = new JSONObject();
		returnJSON.put("name",this.serverURL);
		returnJSON.put("port", this.port);
		returnJSON.put("user", this.userName);
		returnJSON.put("pw", this.userPass);
		returnJSON.put("space", this.userSpace);
		return returnJSON;
	}
		
	public int getServerID()
	{
		return this.serverID;
	}
	
	public String getServerURL()
	{
		return this.serverURL;
	}
	
	public int getOwnerID()
	{
		return this.userId;
	}
	
	public String getServerName()
	{
		return this.name;
	}
	
	public int getPort()
	{
		return this.port;
	}
}
