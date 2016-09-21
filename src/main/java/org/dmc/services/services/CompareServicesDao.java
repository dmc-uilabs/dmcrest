package org.dmc.services.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.profile.ProfileDao;

public class CompareServicesDao {
	private static final String LOGTAG = CompareServicesDao.class.getName();
	private ProfileDao profileDao = new ProfileDao();
	private ResultSet resultSet = null;

	public GetCompareService createCompareService(PostCompareService body, String userEPPN) throws DMCServiceException {
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
		String errMessage = errorHeader + "current user id " + userId + " does not match id of compare user " + profileID;
		if (profileID != userId) {
			ServiceLogger.log(LOGTAG, errMessage);
			throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errMessage);
		}

		try {
			connection.setAutoCommit(false);
			final String query = "insert into service_compare (service_id, profile_id) values (?, ?)";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(body.getServiceId()));
			preparedStatement.setInt(2, profileID);
			preparedStatement.executeUpdate();

			final String queryCompareService = "select currval('service_compare_service_compare_id_seq') as id";
			resultSet = DBConnector.executeQuery(queryCompareService);

			int id = -1;
			if (resultSet.next()) {
				id = resultSet.getInt(1);
			}
			resultSet.close();

			GetCompareService compareService = getCompareService(id, userEPPN);
			return compareService;

		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			if(e.getMessage().contains("duplicate key value")){
				throw new DMCServiceException(DMCError.OtherSQLError, "The service with the same ID has already been added into compare list");
			}
			if (null != connection) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to rollback: " + e1.getMessage());
				}
			}
			throw new DMCServiceException(DMCError.OtherSQLError,
					"unable to create new compare service: " + e.getMessage());
		} finally {
			if (null != connection) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public GetCompareService getCompareService(int id, String userEPPN) throws DMCServiceException {
		final String query = "SELECT * FROM service_compare WHERE service_compare_id = " + id;
		GetCompareService compareService = null;
		resultSet = DBConnector.executeQuery(query);
		try {
			while (resultSet.next()) {
				compareService = profileDao.setCompareService(resultSet);
			}
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError,
					"unable to get compare service " + id + ":" + e.getMessage());
		}

		return compareService;
	}

	public boolean deleteCompareService(String id, String userEPPN) throws DMCServiceException {
		ServiceLogger.log(LOGTAG,
				"In deleteCompareService for userEPPN: " + userEPPN + " and compare_service_id " + id);
		final String query = "DELETE from service_compare WHERE service_compare_id = ? AND profile_id = ? ";
		String errorHeader = "DELETE COMPARE_SERVICE_ID: ";

		int userId = -1;

		try {
			userId = UserDao.getUserID(userEPPN);
		} catch (SQLException e1) {
			ServiceLogger.log(LOGTAG, e1.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, errorHeader + e1.getMessage());
		}

		PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
		try {
			preparedStatement.setInt(1, Integer.parseInt(id));
			preparedStatement.setInt(2, userId);
			int change = preparedStatement.executeUpdate();
			if (change != 1) {
				throw new DMCServiceException(DMCError.OtherSQLError,
						errorHeader + " cannot delete compare_service_id:" + id + " for userEPPN: " + userEPPN);
			}
			return true;
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, errorHeader + e.getMessage());
		}

	}

}
