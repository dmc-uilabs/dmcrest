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
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;

public class ServiceImagesDao {

	private final String logTag = ServiceImagesDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;

	public Id createServiceImages(ServiceImages payload, String userEPPN) throws SQLException {
		
		int userId = -99999;
		connection = DBConnector.connection();
		PreparedStatement statement;
		Util util = Util.getInstance();
		int id = -99999;
	
		/* 
		 * NEED TO PUT Get AWS URL FUNCTION
		 */
		
	
		  //Tests to see if valid user, exits function if so
        try {
            userId = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw e;
		} 
	  
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			ServiceLogger.log(logTag, ex.getMessage());
			throw ex;
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
				throw ex;
			}
			throw e;
		} 
		finally {
			try {
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException et) {
				ServiceLogger.log(logTag, et.getMessage());
				throw et;
			}
		}

		return new Id.IdBuilder(id).build();			
	}//END POST 
	
	
	public ArrayList<ServiceImages> getServiceImages(int input) throws SQLException {

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
			throw e;
		}
		return list;

	}//END GET

	
	public boolean deleteServiceImages(int imageId, String userEPPN) throws SQLException {

		//BEEF THIS UP!
        //Tests to see if valid user, exits function if so
        try {
            int userId = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw e; 
		}
		int rows; 
		
		//Connect to DB
		connection = DBConnector.connection();
		PreparedStatement statement;
		Boolean resetAutoCommit = true;
		
		 
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
					throw ex;
				}
			}
			throw e;
		}//Catch 
	    
	    
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw e;
		}
	

        if (rows > 0) return true;
        else return false;
        
        
	}
	
} //END DAO class
