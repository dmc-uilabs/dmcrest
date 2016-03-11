package org.dmc.services.profile;

import org.dmc.services.DBConnector;
import org.dmc.services.Config;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.UserDao;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;
import org.springframework.http.HttpStatus;

import org.dmc.solr.SolrUtils;

public class ProfileDao {

	private final String logTag = ProfileDao.class.getName();

	public Profile getProfile(int requestId) throws HTTPException {
        ServiceLogger.log(logTag, "In getProfile: user_id "+requestId);
        Profile profile = new Profile();
        profile.setId(Integer.toString(requestId));
        
        try {
            String queryProfile = "SELECT user_name, realname, title, phone, email, address, image, people_resume FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(queryProfile);
            preparedStatement.setInt(1, requestId);
			
            ResultSet resultSet = preparedStatement.executeQuery();
            String userName = null;
            
            if (resultSet.next()) {
                //id = resultSet.getString("id");
                profile.setDisplayName(resultSet.getString("realname"));
                
                // need to get company;
                profile.setCompany("unset");
                
                profile.setJobTitle(resultSet.getString("title"));
                profile.setPhone(resultSet.getString("phone"));
                profile.setEmail(resultSet.getString("email"));
                profile.setLocation(resultSet.getString("address"));
                profile.setImage(resultSet.getString("image"));
                profile.setDescription(resultSet.getString("people_resume"));
                
                // need to get skills;
                profile.setSkills(new ArrayList<String>());
                
                userName = resultSet.getString("user_name");
                
            }
            
            int user_id_lookedup = -1;
            try{
                user_id_lookedup = UserDao.getUserID(userName);
            } catch (SQLException e) {
                ServiceLogger.log(logTag, e.getMessage());
            }
            
            // check user name is equal to userEPPN
            if(user_id_lookedup != requestId) {
                throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
            }
            

        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.GATEWAY_TIMEOUT.value());
		}
        
		return profile;
	}

	public Id createProfile(String jsonStr, String userEPPN) {

		Util util = Util.getInstance();
		int userId = -9999;
		Connection connection = DBConnector.connection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			return null;
		}

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

			PreparedStatement statement = DBConnector.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
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

			if (Config.IS_TEST == null) {
				SolrUtils.invokeFulIndexingUsers();
				ServiceLogger.log(logTag, "SolR indexing triggered for User: "
						+ userId);
			}

			// create people_skill, and relational skill_inventory
			if (skills.length() != 0) {
				this.createSkills(userId, skills);
			}
			connection.commit();
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag,
							"Transaction Profile Create Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		} catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag,
							"Transaction Profile Create Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		} catch (IOException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag,
							"Transaction Profile Create Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
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
		return new Id.IdBuilder(userId).build();

	}

	public Id updateProfile(int id, String jsonStr, String userEPPN) {
		PreparedStatement statement;
		String query;

		Connection connection = DBConnector.connection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			return null;
		}

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

			// update user
			query = "UPDATE users SET "
					+ "realname = ?, title = ?, phone = ?, email = ?, address = ?, image = ?, people_resume = ? "
					+ "WHERE user_id = ? AND user_name = ?";

			statement = DBConnector.prepareStatement(query);
			statement.setString(1, displayName);
			statement.setString(2, jobTitle);
			statement.setString(3, phone);
			statement.setString(4, email);
			statement.setString(5, location);
			statement.setString(6, image);
			statement.setString(7, description);
			statement.setInt(8, id);
			statement.setString(9, userEPPN);
			statement.executeUpdate();

			if (Config.IS_TEST == null) {
				//SolrUtils.invokeFulIndexingUsers();
				ServiceLogger.log(logTag, "SolR indexing triggered for User: "
						+ id);
			}

			// delete and create skills
			if (this.deleteSkills(id) && skills.length() > 0) {
				this.createSkills(id, skills);
			}
			connection.commit();
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag,
							"Transaction Profile Update Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		} catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag,
							"Transaction Profile Update Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
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

	public Id deleteProfile(int id, String userEPPN) {
		PreparedStatement statement;
		String query;

		Connection connection = DBConnector.connection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			return null;
		}

		try {
			this.deleteSkills(id);
			query = "DELETE FROM users WHERE user_id = ? AND user_name = ?";
			statement = DBConnector.prepareStatement(query);
			statement.setInt(1, id);
			statement.setString(2, userEPPN);
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag,
							"Transaction Profile Delete Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		} catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag,
							"Transaction Profile Delete Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
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

	public boolean createSkills(int userId, JSONArray skills)
			throws SQLException {
		Util util = Util.getInstance();
		PreparedStatement statement;
		int skillId;

		if (skills.length() != 0) {
			for (int i = 0; i < skills.length(); i++) {
				String query = "INSERT INTO people_skill (name) VALUES (?)";
				statement = DBConnector.prepareStatement(query,
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, skills.getString(i));
				statement.executeUpdate();
				skillId = util.getGeneratedKey(statement, "skill_id");
				ServiceLogger.log(logTag, "PEOPLE SKILL ID: " + skillId);

				query = "INSERT INTO people_skill_inventory (user_id, skill_id) VALUES (?, ?) ";
				statement = DBConnector.prepareStatement(query);
				statement.setInt(1, userId);
				statement.setInt(2, skillId);
				statement.executeUpdate();
			}
		}

		return true;
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
}