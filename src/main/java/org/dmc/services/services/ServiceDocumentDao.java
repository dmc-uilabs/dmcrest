package org.dmc.services.services;

/*
 * DAO class to query DB and store presignedURL from AWS S3
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;

import org.json.JSONException;

import org.dmc.services.AWSConnector;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.verification.Verification;

public class ServiceDocumentDao {

	private final String logTag = ServiceImagesDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;
	private AWSConnector AWS = new AWSConnector();
	private Verification verify = new Verification(); 

	public Id createServiceDocument(ServiceDocument payload, String userEPPN) throws DMCServiceException {

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
			String query = "INSERT INTO service_document (service_id, service_document_id, owner_id, title, modified, size,file) VALUES (?,?,?,?,?,?,?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, Integer.parseInt(payload.getServiceId()));
			statement.setString(2, payload.getServiceDocumentId());
			statement.setInt(3, Integer.parseInt(payload.getOwnerId()));
			statement.setString(4, payload.getTitle());
			statement.setString(5, payload.getModifed());
			statement.setString(6, payload.getSize());
			statement.setString(7, payload.getFile());
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
		
		ServiceLogger.log(logTag, "Attempting to verify document");
		//Verify the document 
		String temp = verify.verify(id,payload.getFile(),"service_document", userEPPN, "Projects", "ServiceDocuments", "id", "file");
		ServiceLogger.log(logTag, "Verification Machine Response" + temp);

		ServiceLogger.log(logTag, "Returned from Verification machine");


		
		return new Id.IdBuilder(id).build();
	}//END Create


	public ArrayList<ServiceDocument> getServiceDocs(int serviceId, String sort, String order, Integer limit) throws DMCServiceException {

		ArrayList<ServiceDocument> list =new ArrayList<ServiceDocument>();
		try {
		

			String query = "SELECT * FROM service_document WHERE service_id = " + serviceId;
			query += " ORDER BY " + sort + " " + order + " LIMIT " + limit;
			resultSet = DBConnector.executeQuery(query);
			while (resultSet.next()) {
				//Collect output and push to a list
				int id = resultSet.getInt("id");
				int service_Id = resultSet.getInt("service_id");
				
				ServiceDocument doc = new ServiceDocument();
				doc.setId(Integer.toString(id));
				doc.setServiceId(Integer.toString(service_Id));
				doc.setServiceDocumentId(null);
				doc.setModifed(resultSet.getString("modified"));
				doc.setTitle(resultSet.getString("title"));
				doc.setSize(resultSet.getString("size"));
				doc.setOwnerId(Integer.toString(resultSet.getInt("owner_id")));
				doc.setOwner("Temp");
				doc.setFile(resultSet.getString("file"));
				list.add(doc);
			}
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		return list;
	}//END GET


	public boolean deleteServiceDocument(int id, String userEPPN) throws DMCServiceException {
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
			//Connect to DB
			int rows;
			connection = DBConnector.connection();
			PreparedStatement statement;

	    try {
			// delete Image
			connection.setAutoCommit(false);
			
			/*
			 * To delete from S3 bucket
			 */
            //Get the Image URL to delete 
           /* final String AWSquery = "SELECT file FROM service_document WHERE id = ?";  
            final PreparedStatement AWSstatement = DBConnector.prepareStatement(AWSquery);
            AWSstatement.setInt(1, id);
            final ResultSet url = AWSstatement.executeQuery();
            
            String URL = null;
            if(url.next()){
            	URL = url.getString(1); 
            }
            
            //Call function to delete 
            try{
            	AWS.remove(URL, userEPPN);
            } catch (DMCServiceException e) {
            	return false;
            }*/
            //End S3 delete

	        String query = "DELETE FROM service_document WHERE id = ?";
	        statement = DBConnector.prepareStatement(query);
	        statement.setInt(1, id);
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
