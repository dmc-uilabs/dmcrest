package org.dmc.services.company;

import java.sql.ResultSet;

public class CompanyDao {

	private final String logTag = CompanyDao.class.getName();
	private ResultSet resultSet;

	public Company getCompany(int id) {
		/*
		try {
	
			
			resultSet = DBConnector.executeQuery("SELECT * FROM organization o"
					+ "JOIN common_image c"
					+ "ON o.feature_image = c.id"
					+ "WHERE id = "  + id);
			
			while (resultSet.next()) {
				id = resultSet.getInt("id");
				title = resultSet.getString("title");
				description = resultSet.getString("description");
				startDate = resultSet.getString("rundate");
				startTime = resultSet.getString("runtime");
				            
				//we still need thumbnail, and large URLs from above query to construct the featureImage()
			}
			return new 
						company.CompanyBuilder(id, title, description)
						.featureImage(new FeatureImage("", ""))
						.currentStatus(new CompanyCurrentStatus(0, startDate, startTime)).CompanyType(CompanyType).tags(tags)
						.build();
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
		*/
		return null;   //remove this when done
	}
	
}