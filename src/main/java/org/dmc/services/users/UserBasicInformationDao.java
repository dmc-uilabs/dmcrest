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

			// Update user company
			if (json.has("company")) {
				String company = json.getString("company");
				json.remove("company");
				if (!company.equals("")) {
					query = "UPDATE organization SET name = ? WHERE owner = ?";
					statement = DBConnector.prepareStatement(query);
					statement.setString(1, company);
					statement.setString(2, username);
					statement.executeUpdate();
				}
			}

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

			statement = DBConnector.prepareStatement(query, statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < setKeys.size(); i++) {
				statement.setString(i + 1, json.getString(setKeys.get(i)));
			}
			statement.setString(setKeys.size() + 1, username);
			statement.executeUpdate();
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
			if (null != connection) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					// don't really need to do anything
				}
			}
		}
	}
}
