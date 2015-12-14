package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import org.json.JSONObject;
import org.json.JSONException;

public class DiscussionDao {
	
	

	private final String logTag = DiscussionDao.class.getName();
	private ResultSet resultSet;
	
	public DiscussionDao(){}
	
	public Id createDiscussion(String jsonStr){
			int id = -99999;
			
		try{
			JSONObject json = new JSONObject(jsonStr);
			String text = json.getString("text");
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YYYY hh:mm:ss");
			long millisOfDate = (new Date()).getTime();
			int projectId = json.getInt("projectId");
			
			//ServiceLogger.log(logTag, "INPUTS: " + text + ", " + millisOfDate + ", " + projectId);
		

			String query = "INSERT INTO home_comments(comment, time_posted, ref_id) VALUES ( ?, ?, ? )";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setString(1, text);
			preparedStatement.setLong(2, 0);
			preparedStatement.setInt(3, projectId);
			preparedStatement.executeUpdate();
			
			
			ServiceLogger.log(logTag, "Done updating!");

			query = "SELECT currval('home_comments_comment_id_seq') AS id";
			resultSet = DBConnector.executeQuery(query);
			while(resultSet.next()){
				id = resultSet.getInt("id");
			}
			
			ServiceLogger.log(logTag, "Creating discussion, returning ID: " + id);
			
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

	public Discussion getDiscussion(int discussionId) {
		int id = discussionId;
		String text = "";
		String full_name = ""; 
		String avatar = "";
		int projectId = 0;
		long dateInt = 0;
		String date = "";
		
		
		
		String query = "SELECT h.comment AS text, h.time_posted AS time, u.realname AS name, "
				+ "h.ref_id AS pid, g.group_name as title FROM home_comments h JOIN "
				+ "users u ON u.user_id = h.user_id JOIN groups g ON h.ref_id = g.group_id "
				+ "WHERE h.comment_id = " + discussionId;
		ServiceLogger.log(logTag, "getDiscussion, id: " + discussionId);
		try {
				resultSet = DBConnector.executeQuery(query);
				while (resultSet.next()) {
					text = resultSet.getString("text");
					full_name = resultSet.getString("name");
					projectId = resultSet.getInt("pid");
					
					Date releaseDate = new Date(1000L * resultSet.getLong("time"));
					
					/*ServiceLogger.log(logTag, "\nID: " + id + "\nTitle: " + title + 
							"\nDesc: " + description + "\n"
							+  "\nOwner: " + owner);
					ServiceLogger.log(logTag, "Service Name - " + resultSet.toString());
					*/
					SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YYYY hh:mm:ss");
					
					date = formatter.format(releaseDate);
					
					//ServiceLogger.log(logTag, "Release Date: " + date);
					
											
				}
				
				
				
				return new Discussion.DiscussionBuilder(id).avatar()
						.text(text).full_name(full_name)
						.created_at(date).projectId(projectId).build();
				
				
				
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		  
		return null;
	}
}
