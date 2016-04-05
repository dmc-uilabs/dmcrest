package org.dmc.services.users;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.Id;
import org.dmc.services.Config;
import org.dmc.services.sharedattributes.Util;

import org.dmc.services.company.CompanyDao;

import org.dmc.solr.SolrUtils;

import java.io.IOException;

public class UserBasicInformationDao {

	private final String logTag = UserBasicInformationDao.class.getName();

	public UserBasicInformationDao() {
	}

	public Id createUserBasicInformation(String userEPPN, String jsonStr) {

		Util util = Util.getInstance();
		PreparedStatement statement = null;
		String query;
		int id = -99999;

		Connection connection = DBConnector.connection();
		try {
			ServiceLogger.log(logTag, "setting AutoCommit = false");
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			return null;
		}

		// we must have a valid userEPPNto continue;
		if (userEPPN.equals("")) {
			return new Id.IdBuilder(-1).build();
		}

		try {
			JSONObject json = new JSONObject(jsonStr.trim());
			String username = userEPPN;

            // add user to company
            int companyId = Integer.parseInt(json.getString("company"));
            int userId = UserDao.getUserID(userEPPN);
            CompanyDao companyDao = new CompanyDao();
            companyDao.addMember(companyId, userId, userEPPN);
			// update the rest of the user fields
			ArrayList<String> setKeys = new ArrayList<String>();
			ArrayList<String> keys = new ArrayList<String>();
			keys.add("email");
			keys.add("firstName");
			keys.add("lastName");

			query = "UPDATE users SET";
			boolean firstFieldAdded = false;
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				if (json.has(key) && !json.getString(key).equals("")) {
					String value = json.getString(key);
					setKeys.add(key);
					query += (firstFieldAdded) ? "," : "";
					query += " " + key + " = ?";
					firstFieldAdded = true;
				}
			}
            query += ", accept_term_cond_time = now() ";  // set time when terms and conditions were accepted.
			query += " WHERE user_name = ?";

			//ServiceLogger.log(logTag, "update user query: " + query);
			
			statement = DBConnector.prepareStatement(query, statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < setKeys.size(); i++) {
				statement.setString(i + 1, json.getString(setKeys.get(i)));
			}
			statement.setString(setKeys.size() + 1, username);
			int countAffected = statement.executeUpdate();
			id = util.getGeneratedKey(statement, "user_id");
			connection.commit();

			ServiceLogger.log(logTag, "User Basic Information updated! User ID: " + id);

			if (Config.IS_TEST == null) {
				String indexResponse = ""; //SolrUtils.invokeFulIndexingUsers();
				ServiceLogger.log(logTag, "SolR indexing triggered for user: " + id);
			}

			return new Id.IdBuilder(id).build();
			
		} /*catch (IOException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return new Id.IdBuilder(id).build();
		} */ catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Basic Information update rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return new Id.IdBuilder(id).build();
		} catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Basic Information update rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return new Id.IdBuilder(id).build();
		} finally {
			ServiceLogger.log(logTag,  "finally block in createUserBasicInformation");
			if (null != connection) {
				try {
					ServiceLogger.log(logTag, "setting AutoCommit = true");
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					// don't really need to do anything
				}
			}
		}
	}
}
