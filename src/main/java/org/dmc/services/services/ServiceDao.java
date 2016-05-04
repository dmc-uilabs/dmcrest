package org.dmc.services.services;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceDao {

	private final String logTag = ServiceDao.class.getName();
	private ResultSet resultSet;

	public Service getService(int requestId) {
		int id = 0;
		String serviceType = "", title = "", description = "", startDate = "", startTime = "";
		ArrayList<String> tags = new ArrayList<String>();
		
		Service service = new Service();
		
		try {
			
			String query = "SELECT * FROM service WHERE service_id = " + requestId;

			resultSet = DBConnector.executeQuery(query);
			
			while (resultSet.next()) {
				
				service.setId(Integer.toString(requestId));
				service.setCompanyId(Integer.toString(resultSet.getInt("organization_id")));
				service.setTitle(resultSet.getString("title"));
				service.setDescription(resultSet.getString("description"));
				service.setOwner(resultSet.getString("owner_id"));
				service.setProfileId(resultSet.getString("owner_id"));  // ToDo: up date
				service.setReleaseDate(resultSet.getDate("release_date"));
				service.setType(resultSet.getString("service_type"));
				service.setTags(new ArrayList<String>()); // ToDo: up date
				service.setSpecifications(resultSet.getString("specifications"));

				service.setFeatureImage(new FeatureImage("", ""));
				service.setCurrentStatus(new ServiceCurrentStatus(0, "", ""));
				
				service.setProjectId(resultSet.getString("project_id"));
				service.setFrom(resultSet.getString("from_location"));
				service.setType(resultSet.getString("type"));
				service.setParent(resultSet.getString("parent"));
				service.setPublished(resultSet.getBoolean("published"));

				service.setAverageRun("");
								
			}
			return service;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
}