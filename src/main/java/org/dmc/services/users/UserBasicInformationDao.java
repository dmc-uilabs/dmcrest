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

		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();
		PreparedStatement statement = null;
		String query;
		int id = -99999;

		// we must have a valid userEPPNto continue;
		if (userEPPN.equals("")) {
			return new Id.IdBuilder(-1).build();
		}

		try {
			JSONObject json = new JSONObject(jsonStr.trim());
			String username = userEPPN;
			connection.setAutoCommit(false);

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
			JSONArray names = json.names();
			ArrayList<String> keys = new ArrayList<String>();

			query = "UPDATE users SET";
			boolean firstFieldAdded = false;
			for (int i = 0; i < names.length(); i++) {
				query += (firstFieldAdded) ? "," : "";
				String key = names.getString(i);
				String value = json.getString(names.getString(i));
				if (!value.equals("")) {
					firstFieldAdded = true;
					ServiceLogger.log(logTag, "KEY: " + key);
					ServiceLogger.log(logTag, "VALUE: <" + value + ">");
					keys.add(key);
					query += " " + key + " = ?";
				}
			}
			query += " WHERE user_name = ?";

			statement = DBConnector.prepareStatement(query, statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < keys.size(); i++) {
				statement.setString(i + 1, json.getString(keys.get(i)));
			}
			statement.setString(keys.size() + 1, username);
			ServiceLogger.log(logTag, "Prepared Statement: " + statement.toString());
			statement.executeUpdate();
			id = util.getGeneratedKey(statement, "user_id");
			connection.commit();

			connection.setAutoCommit(true);

			ServiceLogger.log(logTag, "User Basic Information updated! User ID: " + id);

			if (Config.IS_TEST == null) {
				String indexResponse = SolrUtils.invokeFulIndexingUsers();
				ServiceLogger.log(logTag, "SolR indexing triggered for user: " + id);
			}

			return new Id.IdBuilder(id).build();
			
		} catch (IOException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return new Id.IdBuilder(id).build();
		} catch (SQLException e) {
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
			return new Id.IdBuilder(id).build();
		}
	}
}
