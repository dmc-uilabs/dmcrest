package org.dmc.services.event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.services.ServiceImages;
import org.dmc.services.services.ServiceImagesDao;

import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

public class EventsDao 
{

	private final String logTag = EventsDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;
	
	public Id createCommunityEvent(CommunityEvent event) throws DMCServiceException
	{
		//connection = DBConnector.connection();
		//PreparedStatement statement;
		//Util util = Util.getInstance();
		int id = -99999;

		// NEED TO PUT Get AWS URL FUNCTION
		//Tests to see if valid user, exits function if so

	/*	try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			ServiceLogger.log(logTag, ex.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}*/
		return new Id.IdBuilder(id).build();
	}
	
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
	
    public CommunityEvent updateEvent(int id, CommunityEvent event) throws DMCServiceException {
        ServiceLogger.log(logTag, "In Patch Event" + id);
        
        return event;
	}
    
    
    public Id deleteCommunityEvent(int id) throws DMCServiceException
	{
		return new Id.IdBuilder(id).build();
	}

    
	/*
	 public Id createServiceImages(ServiceImages payload, String userEPPN) throws DMCServiceException {

		connection = DBConnector.connection();
		PreparedStatement statement;
		Util util = Util.getInstance();
		int id = -99999;

		// NEED TO PUT Get AWS URL FUNCTION
		//Tests to see if valid user, exits function if so
    try {
      int userId = UserDao.getUserID(userEPPN);
      if(userId == -1){
    			throw new DMCServiceException(DMCError.NotDMDIIMember, "User: " + userEPPN + " is not valid");
      }
    } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.NotDMDIIMember, "User: " + userEPPN + " is not valid");
			}

		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			ServiceLogger.log(logTag, ex.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}

		try {
			String query = "INSERT INTO service_images (service_id, url) VALUES (?, ?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, payload.getServiceId());
			statement.setString(2, payload.getUrl());
			statement.executeUpdate();
			id = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "Creating discussion, returning ID: " + id);
			connection.commit();
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, "SQL EXCEPTION ----- " + e.getMessage());
			try {
				if (connection != null) {
					ServiceLogger.log(logTag, "createServiceImage transaction rolled back");
					connection.rollback();
				}
			} catch (SQLException ex) {
				ServiceLogger.log(logTag, ex.getMessage());
				throw new DMCServiceException(DMCError.OtherSQLError, ex.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
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
	 */
	

        //createCommunityEvent function
        
        
        
        /*
			//For GET Event
	        try {
            // get events;
            // does the organization need to be active member?  assume no.
            resultSet = DBConnector.executeQuery("SELECT organization_id, accountid, name FROM organization");
            companies = new ArrayList<Company>();

			//filling id, title, date, startTime, endTime, address, description
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getInt("title");
                String date = resultSet.getString("date");
				String startTime = resultSet.getString("startTime");
				String  endTime = resultSet.getString("endTime");
				String  address = resultSet.getString("address");
				String  description= resultSet.getString("description");
				
				//DOES THIS NEED SOMETHING LIKE A NEW COMPANY/LIKE  NEW EVENT?
                Company company = new Company();
				company.setId(Integer.toString(id));
				company.setAccountId(Integer.toString(accountId));
				company.setName(name);
                companies.add(company);
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());  // ToDo: what error should this be?
        }
        return companies;
	}
			//For POST Event
			 * 
			//For PATCH Event
			//For DELETE Event
 */

     

        
        
        /*		
 * 
 * 
        try {
            // get all organizations;
            // does the organization need to be active member?  assume no.
            resultSet = DBConnector.executeQuery("SELECT organization_id, accountid, name FROM organization");
            events = new ArrayList<CommunityEvent>();

            while (resultSet.next()) {
                int id = resultSet.getInt("organization_id");
                int accountId = resultSet.getInt("accountid");
                String name = resultSet.getString("name");

                Event event = new event();
				event.setId(Integer.toString(id));
				event.setAccountId(Integer.toString(accountId));
				event.setName(name);
                events.add(event);
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());  // ToDo: what error should this be?
        }
        */
        
}



