package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import java.util.Arrays;
import java.util.Date;

public class DiscussionListDao {

	private final String logTag = DiscussionListDao.class.getName();
	private ResultSet resultSet;
	
	public DiscussionListDao(){}
	
	public ArrayList<Discussion> getDiscussionList(){
		
		ArrayList<Discussion> discussions = new ArrayList<Discussion>();
		/*String username;
		String password;
		
		byte[] byteArray = Base64.decodeBase64(encoded.getBytes());
		
		String decoded = new String(byteArray);
		
		int colonLocation = decoded.indexOf(":");
		
		ServiceLogger.log(logTag, "decoded String");
		
		if (colonLocation == -1){
			return null;
		}
		
		if (decoded.substring(0, 6) != "Basic "){
			return null;
		}
		
		ServiceLogger.log(logTag, "attempting to authenticate");
		
		username = decoded.substring(0, colonLocation);
		password = decoded.substring(colonLocation + 1, decoded.length());
		
		
		String query = "SELECT u.user_id AS user_id, u.user_pw AS pwd FROM users u WHERE u.user_name = \'" 
		+ username + "\';";  
		//ServiceLogger.log(logTag, query);
		try{
			
			int user_id = -1;
			resultSet = DBConnector.executeQuery(query);
			
			//ServiceLogger.log(logTag, "user password: " + password);
			
			while(resultSet.next()){
				user_id = resultSet.getInt("user_id");
				//ServiceLogger.log(logTag, "db listed password: " + resultSet.getString("pwd"));
				if (!password.equals(resultSet.getString("pwd"))){
					return null;
				}
			}
			
			ServiceLogger.log(logTag, "signed in");
		*/	
			/*query = "SELECT group_id FROM user_roles";// WHERE user_id = " + user_id;
			resultSet = DBConnector.executeQuery(query);
			DiscussionDao dLookup = new DiscussionDao();*/
			
		int id = 0;
		String text = "";
		String full_name = ""; 
		String avatar = "";
		int projectId = 0;
		long dateInt = 0;
		String date = "";
		
		
		
		String query = "SELECT h.comment_id AS id, "
				+ "h.comment AS text, h.time_posted AS time, u.realname AS name, "
				+ "h.ref_id AS pid, g.group_name as title FROM home_comments h JOIN "
				+ "users u ON u.user_id = h.user_id JOIN groups g ON h.ref_id = g.group_id";
		
		//ServiceLogger.log(logTag, "getDiscussion, id: " + discussionId);
		
		try {
				resultSet = DBConnector.executeQuery(query);
				while (resultSet.next()) {
					id = resultSet.getInt("id");
					text = resultSet.getString("text");
					full_name = resultSet.getString("name");
					projectId = resultSet.getInt("pid");
					
					Date releaseDate = new Date(1000L * resultSet.getLong("time"));
					
					SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
					
					date = formatter.format(releaseDate); 
					
					//ServiceLogger.log(logTag, "Release Date: " + date);
					
						discussions.add( new Discussion.DiscussionBuilder(id).avatar()
						.text(text).full_name(full_name)
						.created_at(date).projectId(projectId).build());					
				}
				
				
				
				return discussions;
				
				
				
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		  
		return null;
	
	}
	
	public ArrayList<Discussion> getDiscussionList(int pid){
		
		ArrayList<Discussion> discussions = new ArrayList<Discussion>();
		
			
		int id = 0;
		String text = "";
		String full_name = ""; 
		String avatar = "";
		int projectId = 0;
		long dateInt = 0;
		String date = "";
		
		
		
		String query = "SELECT h.comment_id AS id, "
				+ "h.comment AS text, h.time_posted AS time, u.realname AS name, "
				+ "h.ref_id AS pid, g.group_name as title FROM home_comments h JOIN "
				+ "users u ON u.user_id = h.user_id JOIN groups g ON h.ref_id = g.group_id WHERE g.group_id = " + pid;
		
		//ServiceLogger.log(logTag, "getDiscussion, id: " + discussionId);
		
		try {
				resultSet = DBConnector.executeQuery(query);
				while (resultSet.next()) {
					id = resultSet.getInt("id");
					text = resultSet.getString("text");
					full_name = resultSet.getString("name");
					projectId = resultSet.getInt("pid");
					
					Date releaseDate = new Date(1000L * resultSet.getLong("time"));
					
					SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
					
					date = formatter.format(releaseDate); 
					
					//ServiceLogger.log(logTag, "Release Date: " + date);
					
						discussions.add( new Discussion.DiscussionBuilder(id).avatar()
						.text(text).full_name(full_name)
						.created_at(date).projectId(projectId).build());					
				}
				
				
				
				return discussions;
				
				
				
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		  
		return null;
	
	}
}
