package org.dmc.services.profile;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.AWSConnector;
import org.dmc.services.Config;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.accounts.UserAccountServer;
import org.dmc.services.users.UserDao;
import org.dmc.services.users.UserOnboardingDao;
import org.dmc.services.verification.Verification;
import org.dmc.services.company.CompanyDao;
import org.dmc.services.services.GetCompareService;
import org.json.JSONException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.http.HTTPException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;

public class ProfileDao {

	private static final String LOGTAG = ProfileDao.class.getName();

	private AWSConnector AWS = new AWSConnector();
	private Verification verify = new Verification();

	public ArrayList<Profile> getProfiles(String userEPPN, Integer limit, String order, String sort, List<String> id)
			throws DMCServiceException {

		String whereClause = "";
		if (null != id) {
			ServiceLogger.log(LOGTAG, "getProfiles intList equal " + id.toString());

			String commaDelimitedIdList = StringUtils.collectionToDelimitedString(id, ",");
			whereClause = "WHERE user_id IN (" + commaDelimitedIdList + ")";
		}

		ArrayList<Profile> profiles = new ArrayList<Profile>();

		try {
			final String query = "SELECT user_id, user_name, realname, title, phone, email, address, image, people_resume FROM users "
					+ whereClause + " ORDER BY " + sort + " " + order + " LIMIT ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, limit);
			ServiceLogger.log(LOGTAG, preparedStatement.toString());

			preparedStatement.execute();
			ResultSet rs = preparedStatement.getResultSet();

			while (rs.next()) {
				Profile profile = new Profile();
				profile = setProfileValues(rs);
				profiles.add(profile);
			}
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}

		return profiles;
	}

	private Profile setProfileValues(ResultSet resultSet) throws SQLException {
		Profile profile = new Profile();
		String user_id_string = resultSet.getString("user_id");
		int user_id = Integer.parseInt(user_id_string);

		// get company
		final CompanyDao companyDao = new CompanyDao();
		final int companyId = companyDao.getUserCompanyId(user_id);
		profile.setCompany(Integer.toString(companyId));

		profile.setId(user_id_string);
		profile.setDisplayName(resultSet.getString("realname"));
		profile.setJobTitle(resultSet.getString("title"));
		profile.setPhone(resultSet.getString("phone"));
		profile.setEmail(resultSet.getString("email"));
		profile.setLocation(resultSet.getString("address"));
		profile.setImage(resultSet.getString("image"));
		profile.setDescription(resultSet.getString("people_resume"));

		// need to get skills;
		ArrayList<String> skills = getSkills(profile.getId());
		profile.setSkills(skills);

		return profile;
	}

	public Profile getProfile(int requestId) throws HTTPException {
		ServiceLogger.log(LOGTAG, "In getProfile: user_id " + requestId);
		Profile profile = new Profile();
		profile.setId(Integer.toString(requestId));

		try {
			final String queryProfile = "SELECT user_id, user_name, realname, title, phone, email, address, image, people_resume FROM users WHERE user_id = ?";
			final PreparedStatement preparedStatement = DBConnector.prepareStatement(queryProfile);
			preparedStatement.setInt(1, requestId);

			final ResultSet resultSet = preparedStatement.executeQuery();
			String userName = null;

			if (resultSet.next()) {
				profile = setProfileValues(resultSet);
				userName = resultSet.getString("user_name");
			}

			int user_id_lookedup = -1;
			try {
				user_id_lookedup = UserDao.getUserID(userName);
			} catch (SQLException e) {
				ServiceLogger.log(LOGTAG, e.getMessage());
			}

			// check user name is equal to userEPPN
			if (user_id_lookedup != requestId) {
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}

		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			throw new HTTPException(HttpStatus.GATEWAY_TIMEOUT.value());
		}
		return profile;
	}

	public Id createProfile(Profile profile, String userEPPN) throws DMCServiceException {
		int userId = -99999;
		try {
			userId = UserDao.getUserID(userEPPN);
		} catch (SQLException se) {
			ServiceLogger.log(LOGTAG, se.getMessage());
		}

		try {
			updateProfile(userId, profile, userEPPN);
		} catch (DMCServiceException e) {
			throw e;
		}
		return new Id.IdBuilder(userId).build();
	}

	public Id updateProfile(int id, Profile profile, String userEPPN) throws HTTPException, DMCServiceException {
		PreparedStatement statement;
		String query;

		final Connection connection = DBConnector.connection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			return null;
		}

		try {

			// update user
			query = "UPDATE users SET "
					+ "realname = ?, title = ?, phone = ?, email = ?, address = ?, image = ?, people_resume = ? "
					+ "WHERE user_id = ? AND user_name = ?";

			statement = DBConnector.prepareStatement(query);
			statement.setString(1, profile.getDisplayName());
			statement.setString(2, profile.getJobTitle());
			statement.setString(3, profile.getPhone());
			statement.setString(4, profile.getEmail());
			statement.setString(5, profile.getLocation());
			statement.setString(6, profile.getImage());
			statement.setString(7, profile.getDescription());
			statement.setInt(8, id);
			statement.setString(9, userEPPN);
			// @TODO set company
			statement.executeUpdate();

			// update onboarding status
			final UserOnboardingDao userOnboardingDao = new UserOnboardingDao();
			userOnboardingDao.setProfile(id, true);

			if (Config.IS_TEST == null) {
				// SolrUtils.invokeFulIndexingUsers();
				ServiceLogger.log(LOGTAG, "SolR indexing triggered for User: " + id);
			}

			// delete and create skills
			if (this.deleteSkills(id) && profile.getSkills().size() > 0) {
				this.createSkills(id, profile.getSkills());
			}
			connection.commit();
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(LOGTAG, "Transaction Profile Update Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(LOGTAG, ex.getMessage());
				}
			}
			throw new HTTPException(HttpStatus.GATEWAY_TIMEOUT.value());
		} finally {
			if (null != connection) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					// don't really need to do anything
				}
			}
		}

		ServiceLogger.log(LOGTAG, "Attempting to verify document");
		// Verify the document
		try {
			String temp = verify.verify(id, profile.getImage(), "users", userEPPN, "Profiles", "ProfilePictures",
					"user_id", "image");
			ServiceLogger.log(LOGTAG, "Verification Machine Response" + temp);
			ServiceLogger.log(LOGTAG, "Returned from Verification machine");
		} catch (Exception e) {
			ServiceLogger.log(LOGTAG, "Unable to verify user profile image: " + e.getMessage());
			throw new DMCServiceException(DMCError.AWSError, e.getMessage());
		}

		return new Id.IdBuilder(id).build();
	}

	public Id deleteProfile(int id, String userEPPN) {
		PreparedStatement statement;

		final Connection connection = DBConnector.connection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			return null;
		}

		try {
			// update onboarding status
			final UserOnboardingDao userOnboardingDao = new UserOnboardingDao();
			userOnboardingDao.deleteUserOnboarding(id);

			// Get the Image URL to delete
			/*
			 * final String AWSquery =
			 * "SELECT image FROM users WHERE user_id = ? AND user_name = ?";
			 * final PreparedStatement AWSstatement =
			 * DBConnector.prepareStatement(AWSquery); AWSstatement.setInt(1,
			 * id); AWSstatement.setString(2, userEPPN); final ResultSet url =
			 * AWSstatement.executeQuery();
			 * 
			 * String URL = null; if(url.next()){ URL = url.getString(1); }
			 * 
			 * //Call function to delete try{ AWS.remove(URL, userEPPN); } catch
			 * (DMCServiceException e) { return null; }
			 */

			this.deleteSkills(id);
			final String query = "DELETE FROM users WHERE user_id = ? AND user_name = ?";
			statement = DBConnector.prepareStatement(query);
			statement.setInt(1, id);
			statement.setString(2, userEPPN);
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(LOGTAG, "Transaction Profile Delete Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(LOGTAG, ex.getMessage());
				}
			}
			return null;
		} catch (JSONException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(LOGTAG, "Transaction Profile Delete Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(LOGTAG, ex.getMessage());
				}
			}
			return null;
		} finally {
			if (null != connection) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					// don't really need to do anything
				}
			}
		}
		return new Id.IdBuilder(id).build();
	}

	public boolean createSkills(int userId, ArrayList<String> skills) throws SQLException {
		final Util util = Util.getInstance();
		PreparedStatement statement;
		int skillId;

		if (skills.size() != 0) {
			for (int i = 0; i < skills.size(); i++) {
				String query = "INSERT INTO people_skill (name) VALUES (?)";
				statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, skills.get(i));
				statement.executeUpdate();
				skillId = util.getGeneratedKey(statement, "skill_id");
				ServiceLogger.log(LOGTAG, "PEOPLE SKILL ID: " + skillId);

				query = "INSERT INTO people_skill_inventory (user_id, skill_id) VALUES (?, ?) ";
				statement = DBConnector.prepareStatement(query);
				statement.setInt(1, userId);
				statement.setInt(2, skillId);
				statement.executeUpdate();
			}
		}

		return true;
	}

	public ArrayList<String> getSkills(int userId) throws SQLException {
		ArrayList<String> skills = new ArrayList<String>();

		String query = "SELECT people_skill.name " + "FROM people_skill INNER JOIN people_skill_inventory "
				+ "ON people_skill.skill_id=people_skill_inventory.skill_id "
				+ "WHERE people_skill_inventory.user_id = ?";

		PreparedStatement statement = DBConnector.prepareStatement(query);
		statement.setInt(1, userId);
		final ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			String skill = resultSet.getString("name");
			skills.add(skill);
		}

		return skills;
	}

	public boolean deleteSkills(int userId) throws SQLException {
		PreparedStatement statement;
		String query;

		query = "DELETE FROM people_skill WHERE skill_id IN (SELECT skill_id FROM people_skill_inventory WHERE user_id = ?)";
		statement = DBConnector.prepareStatement(query);
		statement.setInt(1, userId);
		statement.executeUpdate();

		query = "DELETE FROM people_skill_inventory WHERE user_id = ?";
		statement = DBConnector.prepareStatement(query);
		statement.setInt(1, userId);
		statement.executeUpdate();

		return true;
	}

	public List<GetCompareService> getCompareServices(String profileID, String userEPPN) throws DMCServiceException {
		int user_id_lookedup = -1;
		String errorHeader = "CHECK AUTHORIZED USER ID : ";
		try {
			user_id_lookedup = UserDao.getUserID(userEPPN);
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, errorHeader + e.getMessage());
		}

		// check if user has permission to compare services
		if (user_id_lookedup != Integer.parseInt(profileID)) {
			String errMessage = errorHeader + "current user id " + user_id_lookedup + " does not match id of compare user " + profileID;
			ServiceLogger.log(LOGTAG, errMessage);
			throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errMessage);
			// current id and user id do not match
		}

		List<GetCompareService> compareServices = new ArrayList<GetCompareService>();
		final String query = "SELECT service_compare_id, service_id, profile_id FROM service_compare WHERE profile_id = ?";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
		try {
			preparedStatement.setInt(1, Integer.parseInt(profileID));
			preparedStatement.execute();
			ResultSet res = preparedStatement.getResultSet();
			while (res.next()) {
				GetCompareService service = setCompareService(res);
				compareServices.add(service);
			}

		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		} catch (Exception e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			throw new DMCServiceException(DMCError.Generic, e.getMessage());
		}
		return compareServices;
	}

	public GetCompareService setCompareService(ResultSet res) throws SQLException {
		GetCompareService compareService = new GetCompareService();
		String service_compare_id = res.getString("service_compare_id");
		String service_id = res.getString("service_id");
		String profile_id = res.getString("profile_id");

		compareService.setId(service_compare_id);
		compareService.setServiceId(service_id);
		compareService.setProfileId(profile_id);

		return compareService;
	}

	private int checkAuthorizedUser(String profileId, String userEPPN) throws DMCServiceException {
		int user_id_lookedup = -1;
		String errorHeader = "ACCOUNT SERVERS GET AUTHORIZED USER ID: ";
		try {
			user_id_lookedup = UserDao.getUserID(userEPPN);
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, errorHeader + e.getMessage()); // unknown
																								// user
		}
		// check if user has permission to update account
		if (user_id_lookedup != Integer.parseInt(profileId)) {
			String errMessage = errorHeader + "current user id " + user_id_lookedup + " does not match id of compare user " + profileId;
			ServiceLogger.log(LOGTAG, errMessage);
			throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errMessage);
			// current id and user id do not match
		}
		return user_id_lookedup;
	}

}