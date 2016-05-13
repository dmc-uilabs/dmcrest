package org.dmc.services.services;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;


import javax.xml.ws.http.HTTPException;

import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;

public class ServiceImagesDao {

	private final String logTag = ServiceImagesDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;
	private Util util;

	
	public ServiceImages createServiceImages(ServiceImages payload, String userEPPN) throws HTTPException{
		
		
		String id = "TempID";
        int userId = -99999;
        String url = "This Is a Temp AWS URL"; 
        
        //Create return object
		ServiceImages serviceImage = new ServiceImages();
		
		//Fill Object Intially
		serviceImage.setServiceId(payload.getServiceId()); 
		serviceImage.setId(id); 
		serviceImage.setUrl(url); 
       
		
        //Tests to see if valid user, exits function if so
       /* try {
            userId = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return serviceImage; 
		}*/ 
	    
	    //Set up connection   
		// connection = DBConnector.connection();
		//PreparedStatement statement;
		 //util = Util.getInstance();
		/*
		//Query the DB
		try {

			//What does this do? 
			connection.setAutoCommit(false);
			
			String query = "INSERT INTO ?? (id, service_id, url) VALUES ( ?, ?, ?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, payload.getId());
			statement.setString(2, payload.getServiceId());
			statement.setString(3, payload.getUrl());
			statement.executeUpdate();

			//create a unique image key for return
			/*
			//NEED TO FIX GENERATE KEY TO RETURN A STRING ID 
			id = util.getGeneratedKey(statement, "image_id");
			url = AWS_S3.getUrl(statement); 
			*/
			/*
			
			ServiceLogger.log(logTag, "Creating Service Image, returning ID: " + id);

			//What does this do exactly? Execute the statement?
			connection.commit();
		} 
		
		catch (SQLException e) {
			ServiceLogger.log(logTag, "SQL EXCEPTION ----- " + e.getMessage());
			
			try {
				
				//What does this do..? 
				if (connection != null) {
					ServiceLogger.log(logTag, "createServiceImage transaction rolled back");
					connection.rollback();
				}
			} 
			
			catch (SQLException ex) {
				ServiceLogger.log(logTag, ex.getMessage());
			}
			
			return serviceImage;
		} 
		
		finally {
			try {
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
		}

		//Reset output 
		serviceImage.setId(id); 
		serviceImage.setUrl(url); 
		*/
		return serviceImage;
		
		
	}//END POST 
	
	
	public ArrayList<ServiceTag> getServiceImages() {
		ArrayList<ServiceTag> list =new ArrayList<ServiceTag>();
		String id = "Fake", serviceId = "Null", name = "Bob";
		/*
		try {
			//Query! 
			
			resultSet = DBConnector.executeQuery("SELECT "
					+ "d.interface_id id, d.alias title, d.description, r.runtime, r.date_completed rundate, s.group_id, g.group_name "
					+ "FROM dome_interfaces d "
					+ "JOIN runnable_runtimes r "
					+ "ON r.interface_id = d.interface_id "
					+ "JOIN service_subscriptions s ON s.interface_id = d.interface_id "
					+ "JOIN groups g ON g.group_id = s.group_id ");

			while (resultSet.next()) {
				
				
				//Collect output and push to a list
				id = resultSet.getString("id");
				serviceId = resultSet.getString("serviceId");
				name = resultSet.getString("name");
				list.add(new ServiceTag.ServiceTagBuilder(id, serviceId, name).build());
			}
			return list;
		} 
		
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}*/
		
		list.add(new ServiceTag.ServiceTagBuilder(id, serviceId, name).build());
		return list;

		//return null;
	}//END GET

	public Id deleteServiceImages(String imageId, String userEPPN) {

		/*
        //Tests to see if valid user, exits function if so
        try {
            userId = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null; 
		}*/

		//Connection connection = DBConnector.connection();
		//PreparedStatement statement;
		//String query;
		int id = -99;
		 
		
		/*
	    try {

	    	connection.setAutoCommit(false);
	    	
			// delete Image
	        query = "DELETE FROM ##### WHERE ImageId = ?";
	        statement = DBConnector.prepareStatement(query);
	        statement.setString(1, imageId);
	        statement.executeUpdate();
	        connection.commit();
	    }
		catch (SQLException e) {
			ServiceLogger.log(logTag, "ERROR IN DELETE COMPANY-------------------" + e.getMessage());
			
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction deleteCompany Rolled back");
					connection.rollback();
				} 
				
				catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		}//Catch 
	    
	    
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}//Catch 
	    
		finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
		}//finally*/

		return new Id.IdBuilder(id).build();
	}
	
} //END DAO class
