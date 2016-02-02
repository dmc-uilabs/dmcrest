package org.dmc.services.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import org.json.JSONObject;
import org.json.JSONException;
import org.dmc.solr.SolrUtils;
import java.io.IOException;

public class UserDao {



	private final String logTag = UserDao.class.getName();
	private ResultSet resultSet;

	public UserDao(){}

        public Id createUser(String jsonStr, String userEPPN, String userFirstName, String userSurname, String userFullName, String userEmail){
	    if(userEPPN.equals("")) {
		// no user to create, so returning Id equal to negative 1.
		return new Id.IdBuilder(-1).build();  
	    }
                        int id = -99999;
			JSONObject json = new JSONObject(jsonStr);
		try{

		        String username = userEPPN;
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
			long millisOfDate = (new Date()).getTime();
			String email = userEmail;
			String password = "password";
			String realName = userFullName;
			String firstName = userFirstName;
			String lastName = userSurname;


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
