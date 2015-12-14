package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceDao {

	private final String logTag = ServiceDao.class.getName();
	private ResultSet resultSet;

	public Service getService(int requestId) {
		int id = 0;
		String serviceType = "", title = "", description = "", startDate = "", startTime = "";
		ArrayList<String> tags = new ArrayList<String>();
		
		try {
			
			resultSet = DBConnector.executeQuery("SELECT "
					+ "d.interface_id id, d.alias title, d.description, r.runtime, r.date_completed rundate, s.group_id, g.group_name "
					+ "FROM dome_interfaces d "
					+ "JOIN runnable_runtimes r "
					+ "ON r.interface_id = d.interface_id "
					+ "JOIN service_subscriptions s ON s.interface_id = d.interface_id "
					+ "JOIN groups g ON g.group_id = s.group_id "
					+ "WHERE d.interface_id = " 
					+ requestId);
			
			while (resultSet.next()) {
				
				id = resultSet.getInt("id");
				title = resultSet.getString("title");
				description = resultSet.getString("description");
				startDate = resultSet.getString("rundate");
				startTime = resultSet.getString("runtime");
				            
				//we still need thumbnail, and large URLs from above query to construct the featureImage()
						
						
				
			}
			return new 
						Service.ServiceBuilder(id, title, description)
						.featureImage(new FeatureImage("", ""))
						.currentStatus(new ServiceCurrentStatus(0, startDate, startTime)).serviceType(serviceType).tags(tags)
						.build();
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
}