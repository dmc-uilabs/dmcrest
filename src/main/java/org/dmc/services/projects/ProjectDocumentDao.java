package org.dmc.services.projects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.services.GetDomeInterface;
import org.dmc.services.services.ServiceImages;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;
import org.json.JSONObject;
import org.json.JSONException;


/*
 * A class for project document upload and retrieval
 */

public class ProjectDocumentDao {
	
	private final String logTag = ProjectDocumentDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;


	public ArrayList<ProjectDocument> getProjectDocuments(int projectID, int projectDocumentId, int limit,String order,String sort) throws DMCServiceException {
		
		ArrayList<ProjectDocument> docs = new ArrayList<ProjectDocument>();
		connection = DBConnector.connection();
		
		try {

			String projectDocsQuery = "SELECT * FROM doc2_files WHERE group_id = " + projectID + " AND doc_group_id = " + projectDocumentId; 
			
			if (sort == null) {
				projectDocsQuery += " ORDER BY file_id";
			} else {
				projectDocsQuery += " ORDER BY " + sort;
			}
			
			if (order == null) {
				projectDocsQuery += " ASC";
			} else if (!order.equals("ASC") && !order.equals("DESC")) {
				projectDocsQuery += " ASC";
			} else {
				projectDocsQuery += " " + order;
			}
			
			if (limit < 0) {
				projectDocsQuery += " LIMIT 0";
			} else if (limit > 0) {
				projectDocsQuery += " LIMIT " + limit;
			} else {
				projectDocsQuery += " LIMIT ALL";
			}
			
			ResultSet resultSet = DBConnector.executeQuery(projectDocsQuery);

			while (resultSet.next()) {
				ProjectDocument doc = new ProjectDocument();
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
					
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		return docs;	
	}
			
}		
	
	
	
	
	
	
	

	
	