package org.dmc.services.projects;

import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;
import java.util.Calendar;


import org.dmc.services.AWSConnector;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;


/*
 * A class for project document upload and retrieval
 */

public class ProjectDocumentDao {
	private final String logTag = ProjectDocumentDao.class.getName();
	Util util = Util.getInstance();
	private AWSConnector AWS = new AWSConnector();


	public Id postProjectDocuments (ProjectDocument payload) throws DMCServiceException{ 
    	Connection connection = DBConnector.connection();
		PreparedStatement statement;
		int id = -999;
		
		// create a java calendar instance
		Calendar calendar = Calendar.getInstance();
		 
		//  get a java.util.Date from the calendar instance.
		java.util.Date now = calendar.getTime();
		 
		// a java current time (now) instance
		java.sql.Timestamp expires = new java.sql.Timestamp(now.getTime());
		//Timestamp expires = new java.sql.Timestamp(UnixTimeStamp + (1000*60*60*24*365*10)); 
		
		//Add an hour 
		long duration = 1000 * 60 * 60; // Add 1 hour.
		expires.setTime(expires.getTime() + duration);
		
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			ServiceLogger.log(logTag, ex.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}
		
		try {
			
			//AWS Profile Picture Upload
			String signedURL = "temp";
			signedURL = AWS.upload(payload.getFile(),"Projects", payload.getId(), "Documents");
			String path = AWS.createPath(signedURL);
			
			
			String query = "INSERT INTO doc2_files (owner, owner_id, filename, description, "
			+ "modified_date, size, doc_group_id, group_id,resource_path, expiration_date) VALUES (?,?,?,?,?,?,?,?,?,?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, payload.getOwner());
			statement.setInt(2, Integer.parseInt(payload.getOwnerId()));
			statement.setString(3,signedURL);
			statement.setString(4,payload.getTitle());
			statement.setInt(5,Integer.parseInt(payload.getModifed()));
			statement.setInt(6, Integer.parseInt(payload.getSize()));
			statement.setInt(7, Integer.parseInt(payload.getProjectDocumentId()));
			statement.setInt(8, Integer.parseInt(payload.getProjectId()));
			statement.setString(9, path);
			statement.setTimestamp(10, expires);
			statement.executeUpdate();
			id = util.getGeneratedKey(statement, "file_id");
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

	public ArrayList<ProjectDocument> getProjectDocuments(int projectID, int projectDocumentId, int limit,String order,String sort) throws DMCServiceException {
    	Connection connection = DBConnector.connection();
		ArrayList<ProjectDocument> docs = new ArrayList<ProjectDocument>();
		ProjectDocument doc = null; 

		try {
			connection.setAutoCommit(false);
			String projectDocsQuery = "SELECT * FROM doc2_files WHERE group_id = ? AND doc_group_id = ?"; 
            
			projectDocsQuery += "ORDER BY " + sort + " " + order + " LIMIT " + limit;            
            PreparedStatement statement = DBConnector.prepareStatement(projectDocsQuery);
            statement.setInt(1, projectID);
            statement.setInt(2, projectDocumentId);
            ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				doc = new ProjectDocument();
				doc.setId(Integer.toString(resultSet.getInt("file_id")));
				doc.setProjectId(Integer.toString(resultSet.getInt("group_id")));
				doc.setProjectDocumentId(Integer.toString(resultSet.getInt("doc_group_id")));
				doc.setOwner(resultSet.getString("owner"));
				doc.setOwnerId(Integer.toString(resultSet.getInt("owner_id")));
				doc.setTitle(resultSet.getString("description")); 
				doc.setModifed(Integer.toString(resultSet.getInt("modified_date")));
				doc.setSize(Integer.toString(resultSet.getInt("size"))); 
				String filename = resultSet.getString("filename"); 
				
				//Refresh Check 
				if(AWS.isTimeStampExpired(resultSet.getTimestamp("expiration_date"))){
					//Refresh URL
					filename =  AWS.refreshURL(resultSet.getString("resource_path"));
			
		            //update the project
		            String query = "UPDATE doc2_files SET "
			        		+ "filename = ?, "
			        		+ "expiration_date = ? "
			        		+ "WHERE file_id = ? ";
			         
		        	// create a java timestamp instance
		            //NEED TO MAKE SURE THAT THE PRESIGNED URL EXPIRATION TIME MATCHES THIS 
		        	// create a java calendar instance
		    		Calendar calendar = Calendar.getInstance();
		    		 
		    		//  get a java.util.Date from the calendar instance.
		    		java.util.Date now = calendar.getTime();
		    		 
		    		// a java current time (now) instance
		    		java.sql.Timestamp expires = new java.sql.Timestamp(now.getTime()); 
		    		
		    		//Add an hour 
		    		long duration = 1000 * 60 * 60; // Add 1 hour.
		    		expires.setTime(expires.getTime() + duration);
		    		
		            PreparedStatement update = DBConnector.prepareStatement(query);
		            update.setString(1, filename);
		            update.setTimestamp(2, expires);
		            update.setInt(3, resultSet.getInt("file_id"));
		            update.executeUpdate();
			    } //end refresh check 
				
				doc.setFile(filename);
				docs.add(doc);
			}
            connection.commit();		
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
			
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}

		}
		return docs;	
	}
			
}		
	
	
	
	
	
	
	

	
	