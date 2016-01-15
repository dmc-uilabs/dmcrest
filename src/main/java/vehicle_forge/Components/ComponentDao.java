package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

public class ComponentDao {
	
	

	private final String logTag = ComponentDao.class.getName();
	private ResultSet resultSet;
	
	public ComponentDao(){}

	public Component getComponent(int componentId) {
		
		/*TODO:
		 * 
		 * get project ID as well
		 */
		int id = componentId;
		Date releaseDate;
		String title = "", description = "", owner = "None", query;
		String thumbnail = "";
		String largeUrl = "";
		FeatureImage image = new FeatureImage(thumbnail, largeUrl);
		ArrayList<String> tags = new ArrayList<String>();
		String date = "";
		String tag = "";
		
		String determineTypeId = "SELECT activity_type_id AS type_id FROM site_activity_mapping s"
				+ " WHERE s.activity_type_string = 'project_component_add'";
		try {
			
			resultSet = DBConnector.executeQuery(determineTypeId);
			int typeId = 0;
			if (resultSet.next())
				typeId = resultSet.getInt("type_id");
		
			query = "SELECT c.name AS title, c.repo_creation_time AS releaseDate, "
				+ "t.tag_name AS tagname,"
				+ "c.description AS description, u.firstname AS firstname, u.lastname AS lastname "
				+"FROM  cem_objects c "
				+ "LEFT JOIN site_activity s ON c.cem_id = s.ref_id AND c.group_id = s.object_id AND s.type_id = "
				+ typeId + " LEFT JOIN users u ON u.user_id = s.user_id "
				+ "LEFT JOIN cem_tags_join j ON s.ref_id = j.cem_id AND s.object_id = j.group_id "
				+ " LEFT JOIN cem_tags t "
						+ "ON t.cem_tag_id = j.cem_tag_id "
				+ "WHERE c.cem_id = " + componentId;
		//ServiceLogger.log(logTag, "getComponent, Query: " + query);
		
				resultSet = DBConnector.executeQuery(query);
				while (resultSet.next()) {
					title = resultSet.getString("title");
					description = resultSet.getString("description");
					
					if (description == null)
						description = "";
					
					releaseDate = new Date(1000L * resultSet.getLong("releaseDate"));
					owner  = resultSet.getString("firstname") + " " + resultSet.getString("lastname");
					
					/*ServiceLogger.log(logTag, "\nID: " + id + "\nTitle: " + title + 
							"\nDesc: " + description + "\n"
							+  "\nOwner: " + owner);
					ServiceLogger.log(logTag, "Service Name - " + resultSet.toString());
					*/
					SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
					
					date = formatter.format(releaseDate);
					
					
					tag = resultSet.getString("tagname");
					
					if (tag != null)
						tags.add(tag);
					/*if (tag == null)
						tags.add("");
					else*/
						
					
					//ServiceLogger.log(logTag, "Release Date: " + date);
					
											
				}
				
				/*if (tags.isEmpty()){
					tags.add("");
				}*/
				
				/*query = "SELECT t.tag_name AS tagname FROM cem_tags_join j JOIN cem_tags t "
						+ "ON t.cem_tag_id = j.cem_tag_id WHERE j.cem_id = " + componentId;
				
				resultSet = DBConnector.executeQuery(query);
				while(resultSet.next()){
					tags.add(resultSet.getString("tagname"));
				}
				*/
				return new Component.ComponentBuilder(id, title, description).releaseDate(date)
						.servicesLink().image(image)
						.owner(owner).tags(tags).build();
				
				
				
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		  
		return null;
	}
}