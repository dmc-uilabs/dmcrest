package org.dmc.services.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.dmc.services.DBConnector;
import org.dmc.services.sharedattributes.FeatureImage;

public class CompanyProfileChange {

	// Change type 1
    public int profChangeCreateCompany(String user, int organization_id)
    {
    	int result=0;
    	try {	
    	int user_id = CompanyUserUtil.getUserId(user);
    	String updateQuery = 
    			"insert into orgnization_profile_change (organization_id,user_id,change_timestamp,"
    			+ "	change_type_id) values (?,?,?,1)";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(updateQuery);
		preparedStatement.setInt(1,organization_id);
		preparedStatement.setInt(2,user_id);
		preparedStatement.setTimestamp(3, getCurrentTimeStamp());
		result = preparedStatement.executeUpdate();
    	}
    	catch (Exception e)
    	{
    		// TODO: check what to do when there is query error
    		result = -999;
    	}
    	return result;
    }
    
	// Change type 2
    public int profChangeUpdate(String user, int organization_id, String fieldName, String newValue, String oldValue)
    {
    	int result=0;
    	try {	
    	int user_id = CompanyUserUtil.getUserId(user);
    	String updateQuery = 
    			"insert into orgnization_profile_change (organization_id,user_id,change_timestamp,"
    			+ "	change_type_id, table_or_field, new_value, old_value) values (?,?,?,2,?,?,?)";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(updateQuery);
		preparedStatement.setInt(1,organization_id);
		preparedStatement.setInt(2,user_id);
		preparedStatement.setTimestamp(3, getCurrentTimeStamp());
		preparedStatement.setString(4, fieldName);
		preparedStatement.setString(5, newValue);
		preparedStatement.setString(6, oldValue);
		result = preparedStatement.executeUpdate();
    	}
    	catch (Exception e)
    	{
    		// TODO: check what to do when there is query error
    		result = -999;
    	}
    	return result;
    }

	// Change type 3
    public int profChangeAddItem(String user, int organization_id, String tableName, String entryIndex, String valueToStore)
    {
    	int result=0;
    	try {	
    	int user_id = CompanyUserUtil.getUserId(user);
    	String updateQuery = 
    			"insert into orgnization_profile_change (organization_id,user_id,change_timestamp,"
    			+ "	change_type_id, table_or_field, new_value, old_value) values (?,?,?,3,?,?,?)";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(updateQuery);
		preparedStatement.setInt(1,organization_id);
		preparedStatement.setInt(2,user_id);
		preparedStatement.setTimestamp(3, getCurrentTimeStamp());
		preparedStatement.setString(4, tableName);
		preparedStatement.setString(5, entryIndex);
		preparedStatement.setString(6, valueToStore);
		result = preparedStatement.executeUpdate();
    	}
    	catch (Exception e)
    	{
    		// TODO: check what to do when there is query error
    		result = -999;
    	}
    	return result;
    }

	// Change type 4
    public int profChangeDeleteItem(String user, int organization_id, String tableName, String entryIndex, String valueToDelete)
    {
    	int result=0;
    	try {	
    	int user_id = CompanyUserUtil.getUserId(user);
    	String updateQuery = 
    			"insert into orgnization_profile_change (organization_id,user_id,change_timestamp,"
    			+ "	change_type_id, table_or_field, new_value, old_value) values (?,?,?,4,?,?,?)";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(updateQuery);
		preparedStatement.setInt(1,organization_id);
		preparedStatement.setInt(2,user_id);
		preparedStatement.setTimestamp(3, getCurrentTimeStamp());
		preparedStatement.setString(4, tableName);
		preparedStatement.setString(5, entryIndex);
		preparedStatement.setString(6, valueToDelete);
		result = preparedStatement.executeUpdate();
    	}
    	catch (Exception e)
    	{
    		// TODO: check what to do when there is query error
    		result = -999;
    	}
    	return result;
    }

    
    
    
    private static java.sql.Timestamp getCurrentTimeStamp() {
    	java.util.Date today = new java.util.Date();
    	return new java.sql.Timestamp(today.getTime());
    }
}
