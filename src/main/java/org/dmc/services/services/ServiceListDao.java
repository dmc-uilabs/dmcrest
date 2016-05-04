package org.dmc.services.services;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceListDao {

	private final String logTag = ServiceListDao.class.getName();
	private ResultSet resultSet;

	public ArrayList<Service> getServiceList() {
		ArrayList<Service> list=new ArrayList<Service>();
		int id = 0;
		String serviceType = "", title = "", description = "", startDate = "", startTime = "";
		ArrayList<String> tags = new ArrayList<String>();
		
		try {
			
			resultSet = DBConnector.executeQuery("SELECT * FROM service");
			
			while (resultSet.next()) {
				Service service = new Service();
				
				service.setId(resultSet.getInt("service_id"));
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

				list.add(service);
			}
			
			return list;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
	
	

	public ArrayList<Service> getServiceList(int projectId) {
		ArrayList<Service> list=new ArrayList<Service>();
		int id = 0;
		String serviceType = "", title = "", description = "", startDate = "", startTime = "";
		ArrayList<String> tags = new ArrayList<String>();
		
		try {
			
			resultSet = DBConnector.executeQuery("SELECT * FROM service WHERE project_id = " + projectId);
			
			while (resultSet.next()) {
				
				Service service = new Service();
				
				service.setId(resultSet.getInt("service_id"));
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
				
				list.add(service);
				
			}
			
			return list;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
	
	public ArrayList<Service> getServiceByComponentList(int componentId) {
		ArrayList<Service> list=new ArrayList<Service>();
		int id = 0;
		String serviceType = "", title = "", description = "", startDate = "", startTime = "";
		ArrayList<String> tags = new ArrayList<String>();
		
		try {
			//ToDo need to determine component ID
			resultSet = DBConnector.executeQuery("SELECT * FROM service WHERE project_id = " + componentId);
			
			while (resultSet.next()) {
				
				
				Service service = new Service();
				
				service.setId(resultSet.getInt("service_id"));
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
				
				list.add(service);
				
			}
			
			return list;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
	
	
}