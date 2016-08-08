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
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.data.dao.user.UserDao;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

//imports needed for date conversion
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EventsDao 
{

	private final String logTag = EventsDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;
	
	//-------------------------------
	//Create Community Event Function
	//-------------------------------
	public Id createCommunityEvent(CommunityEvent event, String userEPPN) throws DMCServiceException
	{
		int userIdEPPN = -1;

		// Look up userId of userEPPN
		try
		{
			userIdEPPN = UserDao.getUserID(userEPPN);
		}
		catch (SQLException e)
		{
			if (userIdEPPN == -1)
			{
				ServiceLogger.log(logTag, "User " + userEPPN + " is not a valid user");
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
		}

		connection = DBConnector.connection();
		PreparedStatement statement;
		int id = -99999;

  
		try
		{
			connection.setAutoCommit(false);
		}
		catch (SQLException ex)
		{
			ServiceLogger.log(logTag, ex.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}

		try
		{ 
			Util util = Util.getInstance();

			String query = "INSERT INTO community_event (title, on_date, start_time, end_time, address, description) VALUES (?, ?, ?, ?, ?, ?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			String expectedPattern = "MM/dd/yyyy";
		    SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
		    Date date = formatter.parse(event.getDate());
		    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		    
			statement.setString(1, event.getTitle());
			statement.setDate(2, sqlDate);
			statement.setString(3, event.getStartTime());
			statement.setString(4, event.getEndTime());
			statement.setString(5, event.getAddress());
			statement.setString(6, event.getDescription());
			statement.executeUpdate();
			id = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "Creating New Event, returning ID: " + id);
			connection.commit();
		}
		catch (SQLException e)
		{
			ServiceLogger.log(logTag, "SQL EXCEPTION ----- " + e.getMessage());
			try
			{
				if (connection != null)
				{
					ServiceLogger.log(logTag, "createCommunityEvent transaction rolled back");
					connection.rollback();
				}
			}
			catch (SQLException ex)
			{
				ServiceLogger.log(logTag, ex.getMessage());
				throw new DMCServiceException(DMCError.OtherSQLError, ex.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		 catch (ParseException e)
	    {
	      e.printStackTrace();
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
		return new Id.IdBuilder(id).build();
	}
	
	//----------------------------
	//Get Community Event Function
	//----------------------------
    public ArrayList<CommunityEvent> getEvents(String userEPPN, String sort, String order, Integer limit) throws DMCServiceException
    {
    	int userIdEPPN = -1;

		// Look up userId of userEPPN
		try
		{
			userIdEPPN = UserDao.getUserID(userEPPN);
		}
		catch (SQLException e)
		{
			if (userIdEPPN == -1)
			{
				ServiceLogger.log(logTag, "User " + userEPPN + " is not a valid user");
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
		}
    	
        ArrayList<CommunityEvent> events = new ArrayList<CommunityEvent>();
        ServiceLogger.log(logTag, "In Get Events");
        
		try
		{			
			String query = "SELECT * FROM community_event";
			query += " ORDER BY " + sort + " " + order + " LIMIT " + limit;
			resultSet = DBConnector.executeQuery(query);
			
			while (resultSet.next())
			{
				
				
				//Collect output and push to a list
				CommunityEvent event = new CommunityEvent();
				event.setId(Integer.toString(resultSet.getInt("id")));
				event.setTitle(resultSet.getString("title"));
				
				
				//Get date from DB, convert date to string
				//Create a format for the date variable
				String expectedPattern = "MM/dd/yyyy";
				SimpleDateFormat format = new SimpleDateFormat(expectedPattern);

				//Get back on_date from table as a string
				Date StringAsDate = resultSet.getDate("on_date");

				//Parse dateAsString from type string to dateAsDate of type Date
				String finalDate = format.format(StringAsDate);
				
				event.setDate(finalDate);
				
				
				//SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
				//date = formatter.format(releaseDate);				
				//releaseDate = new Date(1000L * resultSet.getLong("releaseDate"));
				
				//from link: http://alvinalexander.com/java/simpledateformat-convert-string-to-date-formatted-parse
				/*
				String expectedPattern = "MM/dd/yyyy";
				SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
				String userInput = "09/22/2009";
				Date date = formatter.parse(userInput);
				*/
				
				
				
				event.setStartTime(resultSet.getString("start_time"));
				event.setEndTime(resultSet.getString("end_time"));
				event.setAddress(resultSet.getString("address"));
				event.setDescription(resultSet.getString("description"));
				events.add(event);
			}
		}
		catch (SQLException e)
		{
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
    public CommunityEvent updateEvent(int id, CommunityEvent event, String userEPPN) throws DMCServiceException
    {
    	int userIdEPPN = -1;

		// Look up userId of userEPPN
		try
		{
			userIdEPPN = UserDao.getUserID(userEPPN);
		}
		catch (SQLException e)
		{
			if (userIdEPPN == -1)
			{
				ServiceLogger.log(logTag, "User " + userEPPN + " is not a valid user");
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
		}
		
        ServiceLogger.log(logTag, "In Patch Event" + id);
        connection = DBConnector.connection();
        
        try
        {
			connection.setAutoCommit(false);
		}
		catch (SQLException ex)
		{
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
			if (connection != null)
			{
				try
				{
					ServiceLogger.log(logTag, "Transaction updateCompany Rolled back");
					connection.rollback();
				}
				catch (SQLException ex)
				{
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
    public Id deleteCommunityEvent(int id, String userEPPN) throws DMCServiceException
	{
    	int userIdEPPN = -1;

		// Look up userId of userEPPN
		try
		{
			userIdEPPN = UserDao.getUserID(userEPPN);
		}
		catch (SQLException e)
		{
			if (userIdEPPN == -1)
			{
				ServiceLogger.log(logTag, "User " + userEPPN + " is not a valid user");
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
		}
		
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
	        //Check the number of rows updated by delete function
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
	    	
	    	ServiceLogger.log(logTag, "User with EPPN number " + userEPPN + " has tried to remove an event that does not exist.");
			throw new DMCServiceException(DMCError.OtherSQLError, "Delete Fail");
	    }
	    
	    return new Id.IdBuilder(id)
	    .build();
	}
}

