package org.dmc.services.company;

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

public class CompanyVideoDao {

	private final String logTag = CompanyVideoDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;

	
	/**
	 * Get comapny video
	 * @param companyID
	 * @param userEPPN
	 * @return
	 * @throws HTTPException
	 */
	public ArrayList<CompanyVideo> getCompanyVideos(int companyID, String userEPPN) throws HTTPException {
		ArrayList<CompanyVideo> videos = new ArrayList<CompanyVideo>();
		ServiceLogger.log(this.logTag, "User: " + userEPPN + " asking for all videos of the company: " + companyID);
		try {
			String query = "SELECT c.id, c.caption title, c.video_link link, ov.organization_id " + "FROM organization_video ov " + "JOIN common_video c ON c.id = ov.video_id "
					+ "WHERE ov.organization_id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, companyID);
			this.resultSet = preparedStatement.executeQuery();

			while (this.resultSet.next()) {
				int id = this.resultSet.getInt("id");
				int companyId = this.resultSet.getInt("organization_id");
				String title = this.resultSet.getString("title");
				String link = this.resultSet.getString("link");
				CompanyVideo video = new CompanyVideo(id, companyId, title, link);
				videos.add(video);
			}
		} catch (Exception e) {
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return videos;
	}

	/**
	 * Create Company Video
	 * @param video
	 * @param userEPPN
	 * @return
	 */
	public Id createCompanyVideo(CompanyVideo video, String userEPPN) {

		String query;
		int videoId = -99999, videoTypeId = -1;
		PreparedStatement statement;
		Util util = Util.getInstance();
		int companyId = video.getCompanyId();

		try {

			int userIdEPPN = UserDao.getUserID(userEPPN);
			
			// @todo - these checks are subject to revision given all the changes lately
			if (!CompanyUserUtil.isAdmin(userEPPN, companyId) && !CompanyUserUtil.isOwnerOfCompany(companyId, userIdEPPN)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN + " is not admin user of org:." + companyId);
				throw new DMCServiceException(DMCError.NotAdminUser, "User: " + userEPPN + " is not admin or owner of company:." + companyId);
			}
			
			Connection connection = DBConnector.connection();
			connection.setAutoCommit(false);

			query = "SELECT id FROM common_video_image_type WHERE type_desc = ?";
			statement = DBConnector.prepareStatement(query);
			statement.setString(1, "organization_video");
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				videoTypeId = resultSet.getInt("id");

				query = "INSERT INTO common_video  (length, caption, video_link) VALUES (?, ?, ?)";
				statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, 0);
				statement.setString(2, video.getTitle());
				statement.setString(3, video.getLink());
				statement.executeUpdate();

				videoId = util.getGeneratedKey(statement, "id");
				ServiceLogger.log(logTag, "VIDEO ID: " + videoId);

				query = "INSERT INTO organization_video (organization_id, video_type, video_id) VALUES (?, ?, ?) ";
				statement = DBConnector.prepareStatement(query);

				ServiceLogger.log(logTag, "ORganization ID: " + video.getCompanyId());

				statement.setInt(1, video.getCompanyId());
				statement.setInt(2, videoTypeId);
				statement.setInt(3, videoId);
				statement.executeUpdate();
			}

			connection.commit();

		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction createCompanyVideo Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} catch (Exception e) {
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
				}
			}
		}

		return new Id.IdBuilder(videoId).build();
	}
	

	/**
	 * Update Company Video
	 * @param id
	 * @param video
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public Id updateCompanyVideo(int id, CompanyVideo video, String userEPPN) throws DMCServiceException {
		
		PreparedStatement statement;
		String query;
		int videoId = -9999;
		int companyId = video.getCompanyId();
		int userIdEPPN;
		Util util = Util.getInstance();

		Connection connection = DBConnector.connection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}

		try {
			
			userIdEPPN = UserDao.getUserID(userEPPN);
			
			if (!CompanyUserUtil.isAdmin(userEPPN, companyId) && !CompanyUserUtil.isOwnerOfCompany(companyId, userIdEPPN)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN + " is not admin user of org:." + companyId);
				throw new DMCServiceException(DMCError.NotAdminUser, "User: " + userEPPN + " is not admin or owner of company:." + companyId);
			}
			
			//Update Video
			query = "UPDATE common_video  SET length = ?, caption = ?, video_link = ? "
					+ "WHERE id = ?";
			
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, 0);
			statement.setString(2, video.getTitle());
			statement.setString(3, video.getLink());
			statement.setInt(4, id);
			statement.executeUpdate();
			
			videoId = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "Updated Video Id: " + videoId);

			connection.commit();
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction updateCompanyVideo Rolled back");
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

		return new Id.IdBuilder(videoId).build();
	}


	/**
	 * Delete Company Video
	 * @param companyId
	 * @param videoId
	 * @param userEPPN
	 * @return
	 * @throws HTTPException
	 */
	public Id deleteCompanyVideo(int companyId, int videoId, String userEPPN) throws HTTPException {

		int id = -1;
		String query;
		connection = DBConnector.connection();
		PreparedStatement statement;
		Util util = Util.getInstance();
		Boolean resetAutoCommit = true;

		try {

			if (!connection.getAutoCommit()) {
				resetAutoCommit = false;
			}

			if (videoId != -1) {

				query = "SELECT organization_id FROM organization_video WHERE video_id = ?";
				statement = DBConnector.prepareStatement(query);
				statement.setInt(1, videoId);
				ResultSet resultSet = statement.executeQuery();

				if (resultSet.next()) {

					// Check that the user deleting the company video is an administrator or the owner
					/**
					 ** Checks disabled until members for companies are tracked
					 *
					companyId = resultSet.getInt("organization_id");
					if (!(companyDao.isOwnerOfCompany(companyId, userIdEPPN) || isAdminOfCompany(companyId, userIdEPPN))) {
						ServiceLogger.log(logTag, "User " + userEPPN + " is not authorized to add administrators for company " + companyId);
						throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
					}
					*/

					query = "DELETE FROM organization_video WHERE video_id = ?";
					statement = DBConnector.prepareStatement(query);
					statement.setInt(1, videoId);
					statement.executeUpdate();

					query = "DELETE FROM common_video WHERE id = ?";
					statement = DBConnector.prepareStatement(query);
					statement.setInt(1, videoId);
					statement.executeUpdate();
				}

			} else if (companyId != -1) {
				ArrayList<String> videoIds = new ArrayList<String>();
				query = "SELECT video_id FROM organization_video WHERE organization_id = ?";
				statement = DBConnector.prepareStatement(query);
				statement.setInt(1, companyId);

				ResultSet resultSet = statement.executeQuery();

				String queryOrg = "DELETE FROM organization_video WHERE video_id IN (";
				query = "DELETE FROM common_video WHERE id IN (";
				Boolean firstAdd = true;
				while (resultSet.next()) {
					videoIds.add(String.valueOf(resultSet.getInt("video_id")));
					query += (firstAdd) ? "?" : ", ?";
					queryOrg += (firstAdd) ? "?" : ", ?";
					firstAdd = false;
				}
				query += ")";
				queryOrg += ")";

				String ids = StringUtils.join(videoIds.iterator(), ",");

				if (videoIds.size() > 0) {
					statement = DBConnector.prepareStatement(queryOrg);
					for (int i = 0; i < videoIds.size(); i++) {
						statement.setInt(i + 1, Integer.parseInt(videoIds.get(i)));
					}
					statement.executeUpdate();

					statement = DBConnector.prepareStatement(query);
					for (int i = 0; i < videoIds.size(); i++) {
						statement.setInt(i + 1, Integer.parseInt(videoIds.get(i)));
					}
					statement.executeUpdate();
				}
			}

			connection.commit();

		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction deleteCompanyVideo Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		} finally {
			if (connection != null) {
				try {
					if (resetAutoCommit) {
						connection.setAutoCommit(true);
					}
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
		}

		return new Id.IdBuilder(id).build();
	}

}