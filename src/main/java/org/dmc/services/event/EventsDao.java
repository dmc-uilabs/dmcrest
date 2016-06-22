package org.dmc.services.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.Company;
import org.dmc.services.company.CompanyUserUtil;
import org.dmc.services.services.ServiceImages;
import org.dmc.services.services.ServiceImagesDao;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

public class EventsDao 
{

	private final String logTag = EventsDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;
	
	//-------------------------------
	//Create Community Event Function
	//-------------------------------
	public Id createCommunityEvent(CommunityEvent event) throws DMCServiceException
	{

		connection = DBConnector.connection();
		PreparedStatement statement;
		Util util = Util.getInstance();
		int id = -99999;

  
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			ServiceLogger.log(logTag, ex.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}

		try {
			String query = "INSERT INTO community_event (title, date, start_time, end_time, address, description) VALUES (?, ?, ?, ?, ?, ?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, event.getTitle());
			statement.setString(2, event.getDate());
			statement.setString(3, event.getStartTime());
			statement.setString(4, event.getEndTime());
			statement.setString(5, event.getAddress());
			statement.setString(6, event.getDescription());
			statement.executeUpdate();
			id = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "Creating New Event, returning ID: " + id);
			connection.commit();
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, "SQL EXCEPTION ----- " + e.getMessage());
			try {
				if (connection != null) {
					ServiceLogger.log(logTag, "createCommunityEvent transaction rolled back");
					connection.rollback();
				}
			} catch (SQLException ex) {
				ServiceLogger.log(logTag, ex.getMessage());
				throw new DMCServiceException(DMCError.OtherSQLError, ex.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		//Always includes in a create
		finally {
			try {
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException et) {
				ServiceLogger.log(logTag, et.getMessage());
				throw new DMCServiceException(DMCError.OtherSQLError, et.getMessage());
			}
		}
		return new Id.IdBuilder(id).build();
	}
	
	//----------------------------
	//Get Community Event Function
	//----------------------------
    public ArrayList<CommunityEvent> getEvents() throws DMCServiceException {
        ArrayList<CommunityEvent> events = new ArrayList<CommunityEvent>();
        ServiceLogger.log(logTag, "In Get Events");
        
		try
		{
			String query = "SELECT * FROM community_event";
			resultSet = DBConnector.executeQuery(query);
			
			while (resultSet.next())
			{
				//Collect output and push to a list
				String id =Integer.toString(resultSet.getInt("id"));
				String title = resultSet.getString("title");
				String date = resultSet.getString("date");
				String startTime =resultSet.getString("start_time");
				String endTime = resultSet.getString("end_time");
				String address = resultSet.getString("address");
				String description = resultSet.getString("description");
				
				
				CommunityEvent event = new CommunityEvent();
				event.setId(id);
				event.setTitle(title);
				event.setDate(date);
				event.setStartTime(startTime);
				event.setEndTime(endTime);
				event.setAddress(address);
				event.setDescription(description);
				events.add(event);
			}
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		return events;
	}
	/*
    //Method is not needed and does not make sense to have. If needed, can be added. Has been tested and works.
    //-------------------------------
    //PATCH Community Event Function
    //-------------------------------
    public CommunityEvent updateEvent(int id, CommunityEvent event) throws DMCServiceException
    {
        ServiceLogger.log(logTag, "In Patch Event" + id);
        connection = DBConnector.connection();
        
        try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			ServiceLogger.log(logTag, ex.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}
        
        try
        {
        	//Update Event
        	String query = "UPDATE community_event SET title = ?, date = ?, start_time = ?, " 
        	+ "end_time = ?, address = ?, description = ? WHERE id = ?";
        
        	PreparedStatement preparedStatement = DBConnector.prepareStatement(query);

        	preparedStatement.setString(1, event.getTitle());
			preparedStatement.setString(2, event.getDate());
			preparedStatement.setString(3, event.getStartTime());
			preparedStatement.setString(4, event.getEndTime());
			preparedStatement.setString(5, event.getAddress());
			preparedStatement.setString(6, event.getDescription());
			preparedStatement.setInt(7, id);
        	preparedStatement.executeUpdate();
			connection.commit();

        	
        }
        catch (SQLException e)
        {
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction updateCompany Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			ServiceLogger.log(logTag, e.getMessage());
  			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());

        }
        //Always includes in a create
      	finally
      	{
      		try
      		{
      			if (connection != null)
      			{
      				connection.setAutoCommit(true);
      			}
      		}
      		catch (SQLException et)
      		{
      			ServiceLogger.log(logTag, et.getMessage());
      			throw new DMCServiceException(DMCError.OtherSQLError, et.getMessage());
      		}
      	}
        
        return event;
    }
    */
    
    //-------------------------------
    //Delete Community Event Function
    //-------------------------------
    public Id deleteCommunityEvent(int id) throws DMCServiceException
	{
    	//Connect to DB
		int rows;
		connection = DBConnector.connection();
		PreparedStatement statement;

	    try
	    {
			//Delete an Event
			connection.setAutoCommit(false);
	        String query = "DELETE FROM community_event WHERE id = ?";
	        statement = DBConnector.prepareStatement(query);
	        statement.setInt(1, id);
	        rows = statement.executeUpdate();
			connection.commit();
	    }
		catch (SQLException e)
	    {
			ServiceLogger.log(logTag, "ERROR IN DELETE Community Events-------------------" + e.getMessage());

			if (connection != null)
			{
				try
				{
					ServiceLogger.log(logTag, "Transaction deleteCommunityEvent Rolled back");
					connection.rollback();
				}
				catch (SQLException ex)
				{
					ServiceLogger.log(logTag, ex.getMessage());
					throw new DMCServiceException(DMCError.OtherSQLError, ex.getMessage());
				}
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
	    //Catch
		catch (JSONException e)
	    {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.Generic, e.getMessage());
		}

	    if (rows == 0)
	    {
	    	id = -1;
	    }
	    
	    return new Id.IdBuilder(id)
	    .build();
	}
}


