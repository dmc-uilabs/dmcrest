package org.dmc.services.verification;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.AWSConnector;
import org.dmc.services.ServiceLogger;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.dmc.services.verification.VerificationPatch;



public class VerificationPatchDao {

    private static final String logTag = VerificationPatchDao.class.getName();
    private PreparedStatement statement; 
    private AWSConnector AWS = new AWSConnector();
    private final Connection connection = DBConnector.connection();


    public VerificationPatch verify(VerificationPatch payload) throws DMCServiceException {
    	String finalURL = " "; 
    	
    	ServiceLogger.log(logTag, "Verification Machine DAO");


        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
			throw new DMCServiceException(DMCError.AWSError, ex.getMessage());
        }

        try {
        	
        	//Tests to see if verification function returns true 
        	if (payload.isVerified()){
        		//Copy and paste to secure bucket
        		finalURL = AWS.upload(payload.getUrl(), payload.getFolder(), payload.getUserEPPN(), payload.getResourceType());
        	}
        
            // update correct table entity return finalURL;
            String query = "UPDATE " + payload.getTable() + " SET " + payload.getUrlColumn() + " = ?, verified = ? " + "WHERE " + payload.getIdColumn() + " = ?";

            statement = DBConnector.prepareStatement(query);
            statement.setString(1, finalURL);
            statement.setBoolean(2, payload.isVerified());
            statement.setInt(3, payload.getId());          
            statement.executeUpdate();
            connection.commit();
            
            payload.setUrl(finalURL);
            return payload;

            
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            if (connection != null) {
                try {
                    ServiceLogger.log(logTag, "Transaction Verification Update Rolled back");
                    connection.rollback();
                } catch (SQLException ex) {
                    ServiceLogger.log(logTag, ex.getMessage());
                }
            }
			throw new DMCServiceException(DMCError.AWSError, e.getMessage());
        } finally {
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
        			throw new DMCServiceException(DMCError.AWSError, ex.getMessage());
                }
            }
        }

    }

   }
