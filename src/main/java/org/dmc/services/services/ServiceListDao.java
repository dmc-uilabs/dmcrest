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
			
			resultSet = DBConnector.executeQuery("SELECT "
					+ "d.interface_id id, d.alias title, d.description, r.runtime, r.date_completed rundate, s.group_id, g.group_name "
					+ "FROM dome_interfaces d "
					+ "JOIN runnable_runtimes r "
					+ "ON r.interface_id = d.interface_id "
					+ "JOIN service_subscriptions s ON s.interface_id = d.interface_id "
					+ "JOIN groups g ON g.group_id = s.group_id ");
			
			while (resultSet.next()) {
				
				id = resultSet.getInt("id");
				title = resultSet.getString("title");
				description = resultSet.getString("description");
				startDate = resultSet.getString("rundate");
				startTime = resultSet.getString("runtime");
				list.add( new 
						Service.ServiceBuilder(id, title, description)
						.featureImage(new FeatureImage("", "")).tags(tags)
						.currentStatus(new ServiceCurrentStatus(0, startDate, startTime)).serviceType(serviceType)
						.build());            
				//we still need thumbnail, and large URLs from above query to construct the featureImage()
						
						
				
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
			
			resultSet = DBConnector.executeQuery("SELECT "
					+ "d.interface_id id, d.alias title, d.description, r.runtime, r.date_completed rundate, s.group_id, g.group_name "
					+ "FROM dome_interfaces d "
					+ "JOIN runnable_runtimes r "
					+ "ON r.interface_id = d.interface_id "
					+ "JOIN service_subscriptions s ON s.interface_id = d.interface_id "
					+ "JOIN groups g ON g.group_id = s.group_id WHERE s.group_id = " + projectId);
			
			while (resultSet.next()) {
				
				id = resultSet.getInt("id");
				title = resultSet.getString("title");
				description = resultSet.getString("description");
				startDate = resultSet.getString("rundate");
				startTime = resultSet.getString("runtime");
				list.add( new 
						Service.ServiceBuilder(id, title, description)
						.featureImage(new FeatureImage("", "")).tags(tags)
						.currentStatus(new ServiceCurrentStatus(0, startDate, startTime)).serviceType(serviceType)
						.build());            
				//we still need thumbnail, and large URLs from above query to construct the featureImage()
						
						
				
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
			
			resultSet = DBConnector.executeQuery("SELECT "
					+ "d.interface_id id, d.alias title, d.description, r.runtime, r.date_completed rundate, s.group_id, g.group_name "
					+ "FROM dome_interfaces d "
					+ "JOIN runnable_runtimes r "
					+ "ON r.interface_id = d.interface_id "
					+ "JOIN service_subscriptions s ON s.interface_id = d.interface_id "
					+ "JOIN groups g ON g.group_id = s.group_id WHERE d.cem_id = " + componentId);
			
			while (resultSet.next()) {
				
				id = resultSet.getInt("id");
				title = resultSet.getString("title");
				description = resultSet.getString("description");
				startDate = resultSet.getString("rundate");
				startTime = resultSet.getString("runtime");
				list.add( new 
						Service.ServiceBuilder(id, title, description)
						.featureImage(new FeatureImage("", "")).tags(tags)
						.currentStatus(new ServiceCurrentStatus(0, startDate, startTime)).serviceType(serviceType)
						.build());            
				//we still need thumbnail, and large URLs from above query to construct the featureImage()
						
						
				
			}
			
			return list;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
	
	
}