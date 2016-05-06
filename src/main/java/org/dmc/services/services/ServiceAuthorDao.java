package org.dmc.services.services;

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
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanyVideo;
import org.dmc.services.profile.Profile;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;
import org.dmc.services.users.UserOnboardingDao;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

public class ServiceAuthorDao {

	private final String logTag = ServiceAuthorDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;

	/**
	 * Create Service Author
	 * @param id
	 * @param video
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public Id createServiceAuthor(ServiceAuthor author, String userEPPN) throws DMCServiceException {
		
		Util util = Util.getInstance();
		PreparedStatement statement;
		String query;
		int authorId = -9999, userIdEPPN;

		Connection connection = DBConnector.connection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}

		try {
			userIdEPPN = UserDao.getUserID(userEPPN);
	
			//create Service Author
			query = "Insert INTO table (service_id, display_name, job_title, follow, avatar, company)"
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, 0);
			statement.setString(2, author.getServiceId());
			statement.setString(3, author.getDisplayName());
			statement.setString(3, author.getJobTitle());
			statement.setBoolean(3, author.getFollow());
			statement.setString(3, author.getAvatar());
			statement.setString(3, author.getCompany());
			statement.executeUpdate();
			
			authorId = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "Created Author Id: " + authorId);

			connection.commit();
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction createServiceAuthor Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {}
			}
		}

		return new Id.IdBuilder(authorId).build();
	}
	
	/**
	 * Get Service Authors
	 * @param serviceId
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public ArrayList<ServiceAuthor> getServiceAuthors(int serviceId, String userEPPN) throws DMCServiceException {
		ArrayList<ServiceAuthor> authors = new ArrayList<ServiceAuthor>();
		ServiceLogger.log(this.logTag, "User: " + userEPPN + " asking for all authors of the service: " + serviceId);
		try {
			String query = "SELECT * FROM table WHERE id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, serviceId);
			this.resultSet = preparedStatement.executeQuery();

			while (this.resultSet.next()) {
				int id = this.resultSet.getInt("id");
				String displayName = this.resultSet.getString("display_name");
				String jobTitle = this.resultSet.getString("job_title");
				Boolean follow = this.resultSet.getBoolean("follow");
				String avatar = this.resultSet.getString("avatar");
				String company = this.resultSet.getString("company");
				ServiceAuthor author = new ServiceAuthor();
				author.setId(String.valueOf(id));
				author.setServiceId(String.valueOf(serviceId));
				author.setJobTitle(jobTitle);
				author.setFollow(follow);
				author.setAvatar(avatar);
				author.setCompany(company);
				authors.add(author);
			}
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		
		return authors;
	}
	
	
	/**
	 * Delete Service Author
	 * @param id
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public Id deleteServiceAuthor(int id, String userEPPN) throws DMCServiceException {
		
		int authorId = -99999;
		Util util = Util.getInstance();
		PreparedStatement statement;
		String query;
		
		try {

			connection.setAutoCommit(false);
			
			query = "DELETE FROM table WHERE id = ?";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, id);
			statement.executeUpdate();
			authorId = util.getGeneratedKey(statement, "id");
			connection.commit();
			
		} catch (SQLException e) {
			try {
				if (connection != null) {
					ServiceLogger.log(logTag, "Transaction deleteServiceAuthor rolled back");
					connection.rollback();
				}
			} catch (SQLException ex) {}
			
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {}
			}
		}
		
		return new Id.IdBuilder(authorId).build();
	}

}