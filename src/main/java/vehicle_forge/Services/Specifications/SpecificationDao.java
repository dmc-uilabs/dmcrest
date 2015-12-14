package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import org.json.JSONException;

public class SpecificationDao {

	private final String logTag = SpecificationDao.class.getName();
	private ResultSet resultSet; 

	public SpecificationDao() {}

	public Specification getSpecification(int serviceID) {
		//ServiceLogger.log(logTag, "In specification search with service id " + serviceID);
		Trial trial = null;
		int numMembers = 0, numProjects = 0, numSuccesses = 0, numFails = 0;
		Usage usage = null;
		int num_inputs = 0, num_outputs = 0;
		String description = "";
		
		String query = "SELECT (SELECT count(owner_id) FROM cem_runnables_running WHERE interface_id = "
				+ serviceID + ") AS membersCount, " + 
				"(SELECT count(group_id) AS count FROM service_subscriptions WHERE interface_id = "
				+ serviceID + ") AS projectsCount, " +
				"d.interface_data, d.description FROM dome_interfaces d WHERE d.interface_id = "
					+ serviceID;
		
		
		try {
			resultSet = DBConnector.executeQuery(query);
			while (resultSet.next()) {
				numMembers = resultSet.getInt("membersCount");
				//ServiceLogger.log(logTag, "Number of members - " + numMembers);
				numProjects = resultSet.getInt("projectsCount");
				//ServiceLogger.log(logTag, "Number of projects using service - " + numProjects);
				description = resultSet.getString("description");
				//ServiceLogger.log(logTag, "Description: " + description);
				
				try {
					JSONObject json = new JSONObject(resultSet.getString("interface_data")); 
					
					if(json.has("inParams")){
						JSONObject inputParams = json.getJSONObject("inParams");
						num_inputs = inputParams.length();
					}
					
					if(json.has("outParams")){
						JSONObject outputParams = json.getJSONObject("outParams");
						num_outputs = outputParams.length();
					}
				} catch (JSONException e) {
					ServiceLogger.log(logTag, e.getMessage());
				}
				
				//ServiceLogger.log(logTag, "Number of inputs for service - " + num_inputs);
				//ServiceLogger.log(logTag, "Number of outputs for service - " + num_outputs);
			}
			
			usage = new Usage(numMembers, numProjects);
			//ServiceLogger.log(logTag, "" + usage);
			
			
			resultSet = DBConnector.executeQuery("SELECT count(*) AS Fails FROM runnable_runtimes WHERE interface_id = "
					+ serviceID + " AND date_completed = 0");
			while (resultSet.next()) {
				numFails = resultSet.getInt("Fails");
				//ServiceLogger.log(logTag, "Number of failed tests: " + numFails);	
			}
			
			resultSet = DBConnector.executeQuery("SELECT count(*) AS Successes FROM runnable_runtimes WHERE interface_id = "
					+ serviceID + " AND date_completed != 0");
			while (resultSet.next()) {
				numSuccesses = resultSet.getInt("Successes");
				//ServiceLogger.log(logTag, "Number of passed tests: " + numSuccesses);	
			}
			
			trial = new Trial(numSuccesses, numFails);
			
			return new Specification(description, num_inputs, num_outputs, usage, trial);
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}

}