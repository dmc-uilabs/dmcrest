package org.dmc.services.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.UserDao;
import org.dmc.services.profile.ProfileDao;

public class CompareServicesDao {
	private static final String LOGTAG = CompareServicesDao.class.getName();
	private ProfileDao profileDao = new ProfileDao();
	private ResultSet resultSet = null;

	public GetCompareService createCompareService(PostCompareService body, String userEPPN) throws DMCServiceException{
		Connection connection = DBConnector.connection();
		String errorHeader = "CHECK AUTHORIZED USER ID : ";

		int userId = -1;
		try {
			userId = UserDao.getUserID(userEPPN);
		} catch (SQLException e2) {
			ServiceLogger.log(LOGTAG, e2.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, errorHeader + e2.getMessage());
		}
		
		String profileId = body.getProfileId();
		int profileID = Integer.parseInt(profileId);
		
		
		if(profileID != userId) {
			//throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, "The profileId: " + profileID + " does not match to current userId: " + userId);
			ServiceLogger.log(LOGTAG,
					"Current user id " + userId + " does not match id of compare user " + profileID);
			throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errorHeader + "current user id "
					+ userId + " does not match id of compare user " + profileID);
		}

		try {
			connection.setAutoCommit(false);
			String query = "insert into service_compare (service_id, profile_id) values (?, ?)";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(body.getServiceId()));
			preparedStatement.setInt(2, profileID);
			preparedStatement.executeUpdate();
			
			String queryCompareService = "select currval('service_compare_service_compare_id_seq') as id";
			resultSet = DBConnector.executeQuery(queryCompareService);
			
			int id = -1; 
			if(resultSet.next()){
				id = resultSet.getInt(1);
			}
			resultSet.close();
			
			GetCompareService compareService = getCompareService(id, userEPPN);
			return compareService;
			
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			if(null != connection) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to rollback: " + e1.getMessage());
				}
			}
			throw new DMCServiceException(DMCError.OtherSQLError, "unable to create new compare service: " + e.getMessage());
		} finally {
			if(null != connection) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private GetCompareService getCompareService(int id, String userEPPN) throws DMCServiceException {
		String query = "SELECT * FROM service_compare WHERE service_compare_id = " + id;
		GetCompareService compareService = new GetCompareService();
		resultSet = DBConnector.executeQuery(query);
		try {
			while(resultSet.next()){
				compareService = profileDao.setCompareService(resultSet);
			}
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "unable to get compare service " + id + ":" + e.getMessage());
		}
		
		return compareService;
	}

}
