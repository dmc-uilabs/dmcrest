package org.dmc.services.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
//import org.json.JSONObject;
import org.dmc.services.sharedattributes.Util;

//TODO: this class will provide some dummy date for Service Run to start with.
// Other functions can be implemented with rest services needed to register a DOME server
public class ServiceRunServiceDAO {
	
	private final String logTag = ServiceRunServiceDAO.class.getName();
	
	private int run_id;
	
	private int status;
	private int accountId;
	private int runBy;
	private int serviceId;
	private int interfaceId;
	private float percent_complete;
	private Date startDate;
	private String queueName;
	
	public DomeModelParam outputParam(String parId) throws DMCServiceException
	{
		DomeModelParam result=null;
		try{
			String query = "select * from service_interface_parameter where interface_id=? and parameter_id_txt=?";
			PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
			preparedStatement.setInt(1, interfaceId);
			preparedStatement.setString(2,parId);
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			if (!rs.getBoolean("input_parameter"))
			{
				result = new DomeModelParam();
				result.setName(rs.getString("name"));
				result.setParameterid(parId);
				result.setType(rs.getString("type"));
				result.setCategory(rs.getString("category"));
				result.setUnit(rs.getString("unit"));
			}
			return result;
		}
		catch (SQLException e)
		{
			ServiceLogger.log(this.logTag, "SQLException: " + e.toString());
			throw new DMCServiceException(DMCError.OtherSQLError,"SQLException: " + e.toString());
		}	
	}
	
	public void getData(int run_id) throws DMCServiceException
	{
		this.run_id = run_id;
		ResultSet rs = null;
		try{
			String query = "select * from service_run where run_id=?";
			PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
			preparedStatement.setInt(1,run_id);
			rs = preparedStatement.executeQuery();
			rs.next();
			this.status=rs.getInt("status");
			this.accountId=rs.getInt("account_id");
			this.runBy=rs.getInt("run_by");
			this.serviceId=rs.getInt("service_id");
			this.startDate=rs.getDate("start_date");	
			this.interfaceId=rs.getInt("interface_id");
			this.queueName=rs.getString("queue_name");
		}
		catch (SQLException e)
		{
			ServiceLogger.log(this.logTag, "SQLException: " + e.toString());
			throw new DMCServiceException(DMCError.OtherSQLError,"SQLException: " + e.toString());
		}
	}
	
	public int getNumOutputPars(int interfaceId) throws Exception
	{
		String query = "select count(*) num from service_run_parameter run, service_interface_parameter interface where " +
				"interface.interface_id=? and run.parameter_id=interface.parameter_id and interface.input_parameter=false";
		PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
		preparedStatement.setInt(1,interfaceId);
		ResultSet rs = preparedStatement.executeQuery();
		int result = -9999;
	    if (rs.next()) result=rs.getInt("num");
	    return result;		
	}
	
	//(user_id, server_id, interface_id)
	
	public int createModelRunDBEntry(int user_id, int server_id, int interface_id, String queueName) throws DMCServiceException
	{
		int insertID = -999;
		try {
			// Assume that if status=0 -- created and running, status=1, the run finished
			// When a service is created, the status is always 0
			// Need to get service_id based on service_interface if given interface_id
			String queryIntID = "select service_id from service_interface where interface_id=?";
			PreparedStatement preparedQueryIntID = DBConnector.prepareStatement(queryIntID);
			preparedQueryIntID.setInt(1, interface_id);
			ResultSet intID = preparedQueryIntID.executeQuery();
			if (!intID.next())
			{
				throw (new DMCServiceException(DMCError.ServiceInterfaceNotMatch, "service_id for interface_id " + interface_id + " does not exist."));
			}
			int service_id = intID.getInt("service_id");
		
			String createEntry = "insert into service_run (status,run_by,service_id, start_date, interface_id, queue_name) " +
				"values (0,?,?,?,?,?)";
			PreparedStatement preparedStatement = DBConnector
				.prepareStatement(createEntry,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1,user_id);
			preparedStatement.setInt(2, service_id);
			preparedStatement.setTimestamp(3, getCurrentTimeStamp());
			preparedStatement.setInt(4, interface_id);
			preparedStatement.setString(5,queueName);
			preparedStatement.executeUpdate();
		
			Util util = Util.getInstance();
			insertID = util.getGeneratedKey(preparedStatement,"run_id" );
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return insertID;
	}
	
	public void addNewValue(String parameter_id_txt, String newValue) throws Exception
	{
		//To add new run value to the database
		// Check if this id is already added
		String queryAllParId = "select * from service_run_parameter run_par, service_interface_parameter int_par" +
				" where run_id = ? and run_par.parameter_id=int_par.parameter_id and int_par.parameter_id_txt = ?";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(queryAllParId);
		preparedStatement.setInt(1,this.run_id);
		preparedStatement.setString(2, parameter_id_txt);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) return;
		
		else
		{
			int parameter_id = -9999;
			// Get the parameter_id of the inserted parameter_id_txt
			String queryParameter_id = "select parameter_id from service_interface_parameter where parameter_id_txt = ?";
			PreparedStatement stat = DBConnector.prepareStatement(queryParameter_id);
			stat.setString(1, parameter_id_txt);
			ResultSet rs1 = stat.executeQuery();
			if (!rs1.next()) return; 
			parameter_id = rs1.getInt("parameter_id");
			String insert = "insert into service_run_parameter (parameter_id, run_id, value) values (?,?,?)";
			PreparedStatement insertStat = DBConnector.prepareStatement(insert);
			insertStat.setInt(1,parameter_id);
			insertStat.setInt(2, run_id);
			insertStat.setString(3, newValue);
			insertStat.executeUpdate();
		}
	}
	
	public void setEndOfRun() throws Exception
	{
		String updateStatement = "update service_run set status=1 where run_id = ?";
		PreparedStatement updateStat = DBConnector.prepareStatement(updateStatement);
		updateStat.setInt(1, run_id);
		updateStat.executeUpdate();
		this.status=1;
	}
	
	public void setEndOfRunTime() throws Exception
	{
		String updateStatement = "update service_run set stop_date=? where run_id = ?";
		PreparedStatement updateStat = DBConnector.prepareStatement(updateStatement);
		updateStat.setTimestamp(1, getCurrentTimeStamp());
		updateStat.setInt(2, run_id);
		updateStat.executeUpdate();
		this.status=1;
	}
	
	public int getCollectedNumOutputPars() throws Exception
	{
		int result = 0;
		String query = "select count(*) as num from service_run_parameter run, service_interface_parameter int where run.parameter_id = int.parameter_id and int.input_parameter=false and run.run_id=?";
		PreparedStatement queueStat = DBConnector.prepareStatement(query);
		queueStat.setInt(1, run_id);
		ResultSet rs = queueStat.executeQuery();
		if (rs.next()) result = rs.getInt("num");
		return result;
	}

	public HashMap<String, DomeModelParam> getAllOutputParams() throws Exception
	{
		HashMap<String, DomeModelParam> returnHash = new HashMap<String, DomeModelParam>();
		DomeModelParam domeModelParam;
		String paramIdTxt = "";
		String query = "select int.parameter_id_txt as paramIdTxt, int.name as name, int.parameter_id as parameter_id, int.type as type, int.category as category, int.unit as unit, run.value as value from service_run_parameter run, service_interface_parameter int where int.parameter_id = run.parameter_id and int.input_parameter=false and run.run_id=?";
		PreparedStatement queueStat = DBConnector.prepareStatement(query);
		queueStat.setInt(1, run_id);
		ResultSet rs = queueStat.executeQuery();

		while(rs.next()){
			paramIdTxt = rs.getString("paramIdTxt");

			domeModelParam = new DomeModelParam();
			domeModelParam.setName(rs.getString("name"));
			domeModelParam.setParameterid(rs.getString("parameter_id"));
			domeModelParam.setType(rs.getString("type"));
			domeModelParam.setCategory(rs.getString("category"));
			domeModelParam.setUnit(rs.getString("unit"));
			domeModelParam.setValue(rs.getString("value"));

			returnHash.put(domeModelParam.getName(), domeModelParam);
		}

		return returnHash;
	}


	public int getRun_id() {
		return run_id;
	}

	public void setRun_id(int run_id) {
		this.run_id = run_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getRunBy() {
		return runBy;
	}

	public void setRunBy(int runBy) {
		this.runBy = runBy;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public float getPercent_complete() {
		return percent_complete;
	}

	public void setPercent_complete(float percent_complete) {
		this.percent_complete = percent_complete;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getIntefaceId() {
		return interfaceId;
	}

	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
    private static java.sql.Timestamp getCurrentTimeStamp() {
    	java.util.Date today = new java.util.Date();
    	return new java.sql.Timestamp(today.getTime());
    }
}
