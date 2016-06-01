package org.dmc.services.projects;

import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;

/*
 * A class for project document upload and retrieval
 */

public class ProjectDocumentDao {
	private final String logTag = ProjectDocumentDao.class.getName();

	public ArrayList<ProjectDocument> getProjectDocuments(int projectID, int projectDocumentId, int limit,String order,String sort) throws DMCServiceException {
    	Connection connection = DBConnector.connection();
		ArrayList<ProjectDocument> docs = new ArrayList<ProjectDocument>();
		ProjectDocument doc = null; 

		try {
			connection.setAutoCommit(false);
			String projectDocsQuery = "SELECT file_id, owner_id, filename, description, modified_date, size, doc_group_id, group_id FROM doc2_files WHERE group_id = ? AND doc_group_id = ?"; 
            
			projectDocsQuery += "ORDER BY " + sort + " " + order + " LIMIT " + limit;            
            PreparedStatement statement = DBConnector.prepareStatement(projectDocsQuery);
            statement.setInt(1, projectID);
            statement.setInt(2, projectDocumentId);
            ResultSet resultSet = statement.executeQuery();



			while (resultSet.next()) {
				doc = new ProjectDocument();
				doc.setId(resultSet.getInt("file_id"));
				doc.setProjectId(resultSet.getInt("group_id"));
				doc.setProjectDocumentId(resultSet.getInt("doc_group_id"));
				doc.setOwner("Temp");
				doc.setOwnerId(resultSet.getInt("owner_id"));
				doc.setTitle(resultSet.getString("description")); 
				doc.setModifed(resultSet.getInt("modified_date"));
				doc.setSize(resultSet.getInt("size")); 
				doc.setFile(resultSet.getString("filename"));
				docs.add(doc);
			}
					
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
	
	
	
	
	
	
	

	
	