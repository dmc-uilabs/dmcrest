package org.dmc.services.services;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;

import org.json.JSONException;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;

public class ServiceImagesDao {

	private final String logTag = ServiceImagesDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;

	public Id createServiceImages(ServiceImages payload, String userEPPN) throws DMCServiceException {
		
		connection = DBConnector.connection();
		PreparedStatement statement;
		Util util = Util.getInstance();
		int id = -99999;
	
		/* 
		 * NEED TO PUT Get AWS URL FUNCTION
		 */
		
	
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
	}//END POST 
	
	
	public ArrayList<ServiceImages> getServiceImages(int input) throws DMCServiceException {

		ArrayList<ServiceImages> list =new ArrayList<ServiceImages>();
		//connection = DBConnector.connection();
		try {
	
			String query = "SELECT * FROM service_images WHERE service_id = " + input;
			resultSet = DBConnector.executeQuery(query);
			
	
				while (resultSet.next()) {
					//Collect output and push to a list
					int id = resultSet.getInt("id");
					int serviceId = resultSet.getInt("service_id");
					String url = resultSet.getString("url");
					ServiceImages img = new ServiceImages();
					img.setId(id); 
					img.setServiceId(serviceId); 
					img.setUrl(url); 
					list.add(img);			
				}
		   // connection.commit();
		} 
		
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		return list;

	}//END GET

	
	public boolean deleteServiceImages(int imageId, String userEPPN) throws DMCServiceException {

		//BEEF THIS UP!
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
		int rows; 
		
		//Connect to DB
		connection = DBConnector.connection();
		PreparedStatement statement;
		
		 
	    try {
	    	
	    	// Check that the user deleting the image is an administrator or the owner of the service
			/**
			 ** Checks disabled until user owners for services can be tracked
			if (!(isOwnerOfService(userId)) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not authorized to delete videos for company " + companyId);
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
			*/
			connection.setAutoCommit(false); 

	   
			// delete Image
	        String query = "DELETE FROM service_images WHERE id = ?";
	        statement = DBConnector.prepareStatement(query);
	        statement.setInt(1, imageId);   
	        rows = statement.executeUpdate();
			connection.commit();

	    }
		catch (SQLException e) {
			ServiceLogger.log(logTag, "ERROR IN DELETE Service Images-------------------" + e.getMessage());
			
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction deleteServiceImages Rolled back");
					connection.rollback();
				} 
				
				catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
					throw new DMCServiceException(DMCError.OtherSQLError, ex.getMessage());
				}
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}//Catch 
	    	    
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.Generic, e.getMessage());

		}
	
        if (rows > 0) return true;
        else return false;
	}
	
} //END DAO class
