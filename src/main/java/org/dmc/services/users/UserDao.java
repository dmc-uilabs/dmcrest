package org.dmc.services.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.json.JSONObject;
import org.json.JSONException;
import org.dmc.solr.SolrUtils;
import java.io.IOException;

public class UserDao {



	private final String logTag = UserDao.class.getName();
	private ResultSet resultSet;

	public UserDao(){}

	public Id createUser(String jsonStr){
			int id = -99999;
			JSONObject json = new JSONObject(jsonStr);
		try{

			String username = json.getString("user_name");
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YYYY hh:mm:ss");
			long millisOfDate = (new Date()).getTime();
			String email = json.getString("email");
			String password = json.getString("password");
			String realName = json.getString("name");
			String firstName = realName.split(" ", 2)[0];
			String lastName = realName.split(" ", 2)[1];


			String query = "INSERT INTO users(user_name, email, user_pw, realname, add_date, firstname, lastname) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ? )";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			preparedStatement.setString(4, realName);
			preparedStatement.setInt(5, 0);
			preparedStatement.setString(6, firstName);
			preparedStatement.setString(7, lastName);
			preparedStatement.executeUpdate();


			ServiceLogger.log(logTag, "Done updating!");

			query = "SELECT currval('users_pk_seq') AS id";
			resultSet = DBConnector.executeQuery(query);
			while(resultSet.next()){
				id = resultSet.getInt("id");
			}

			ServiceLogger.log(logTag, "User added: " + id);
			
			if (Config.IS_TEST == null){
				String indexResponse = SolrUtils.invokeFulIndexingUsers();
				ServiceLogger.log(logTag, "SolR indexing triggered for user: " + id);
				}
        	

			ServiceLogger.log(logTag, "Creating User, returning ID: " + id);

			 return new Id.IdBuilder(id).build();

		}
		catch(IOException e){
			ServiceLogger.log(logTag, e.getMessage());
			return new Id.IdBuilder(id).build();
		}
		catch(SQLException e){
			ServiceLogger.log(logTag, e.getMessage());
			return new Id.IdBuilder(id).build();
		}
		catch(JSONException j){
			ServiceLogger.log(logTag, j.getMessage());
			return new Id.IdBuilder(id).build();
		}

		catch(Exception ee){
			ServiceLogger.log(logTag, ee.getMessage());
			return new Id.IdBuilder(id).build();
		}


	}


}
