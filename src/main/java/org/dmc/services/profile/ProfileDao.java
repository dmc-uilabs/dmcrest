package org.dmc.services.profile;

import org.dmc.services.DBConnector;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileDao {

	private final String logTag = ProfileDao.class.getName();
	private ResultSet resultSet;

	public Profile getProfile(int requestId) {
		return null;
	}
	
	public Id createProfile(String jsonStr, String userEPPN) { 
		
		Util util = Util.getInstance();
        int userId = -9999, peopleSkillId = -9999; 
        
		try {
			JSONObject json = new JSONObject(jsonStr);
			String displayName = json.getString("displayName");
			String jobTitle = json.getString("jobTitle");
			String phone = json.getString("phone");
			String email = json.getString("email");
			String location = json.getString("location");
			String image = json.getString("image");
			String description = json.getString("description");
			JSONArray skills = json.getJSONArray("skills");
			
	        // create user
	        String query = "INSERT INTO users"
	        		+ "(user_name, realname, title, phone, email, address, image, people_resume) "
	        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
	        
	        PreparedStatement statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	        statement.setString(1, userEPPN);
	        statement.setString(2, displayName);   
	        statement.setString(3, jobTitle);
	        statement.setString(4, phone);
	        statement.setString(5, email);
	        statement.setString(6, location);
	        statement.setString(7, image);
	        statement.setString(8, description);
	        statement.executeUpdate();
	        
	        userId = util.getGeneratedKey(statement, "user_id");
			ServiceLogger.log(logTag, "USER ID: " + userId);

	        // create people_skill, and relational skill_inventory
			if (skills.length() != 0) {
		        for (int i=0; i<skills.length(); i++) {
		        	query = "INSERT INTO people_skill (name) VALUES (?)";
		        	statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		        	statement.setString(1, skills.getString(i)); 
		        	statement.executeUpdate();
					peopleSkillId = util.getGeneratedKey(statement, "skill_id");
					ServiceLogger.log(logTag, "PEOPLE SKILL ID: " + peopleSkillId);
					
			        query = "INSERT INTO people_skill_inventory (user_id, skill_id) VALUES (?, ?) ";
			        statement = DBConnector.prepareStatement(query);
			        statement.setInt(1, userId);   
			        statement.setInt(2, peopleSkillId);
			        statement.executeUpdate();
		        }
			}
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		return new Id.IdBuilder(userId)
		.build();

	}
	
	public Id deleteProfile(int id) {
		PreparedStatement statement;
		String query;

		try {
	        query = "SELECT skill_id FROM people_skill_inventory WHERE user_id = " + id;
			resultSet = DBConnector.executeQuery(query);
			if (resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					query = "DELETE FROM people_skill WHERE skill_id = ?";
					statement = DBConnector.prepareStatement(query);
					statement.setInt(1, resultSet.getInt("skill_id"));
					statement.executeUpdate();
				}
				query = "DELETE FROM people_skill_inventory WHERE user_id = ?";
				statement = DBConnector.prepareStatement(query);
				statement.setInt(1, id);
				statement.executeUpdate();
			}
			
			query = "DELETE FROM users WHERE user_id = ?";
			statement = DBConnector.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		return new Id.IdBuilder(id)
		.build();
	}
}