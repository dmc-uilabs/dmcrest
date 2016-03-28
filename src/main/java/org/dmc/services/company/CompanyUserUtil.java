package org.dmc.services.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.dmc.services.DBConnector;
import org.dmc.services.sharedattributes.FeatureImage;

public class CompanyUserUtil {

    public static boolean isAdmin(String user)
    {
    	boolean results = false;
    	try {
    	String queryAdmin = 
    			"select admin.id " +
    			"from organization_admin admin, organization_user orgu, users " +
    			"where users.user_name=? " +
    				"and users.user_id = orgu.user_id " +
    				"and admin.organization_user_id = orgu.id " +
    				"and admin.organization_id = orgu.organization_id ";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(queryAdmin);
		preparedStatement.setString(1, user);
		ResultSet r = preparedStatement.executeQuery();
		if (r != null) results = true;
    	}
    	catch (Exception e)
    	{
    		// TODO: check what to do when there is query error
    		results = false;
    	}
    	return results;
    }
    public static boolean isDMDIIMember(String user)
    {
    	boolean results = false;
    	try {
    	String queryAdmin = 
    	 "select * from organization_dmdii_member dmdii, organization_user orgu, users "+
    		"where "+
    			"users.user_name = ? "+
    			"and users.user_id=orgu.user_id "+
    			"and orgu.organization_id = dmdii.organization_id "+
    			"and dmdii.expire_date >= now() "+
    		"order by modification_date "+
    		"limit 1 ";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(queryAdmin);
		preparedStatement.setString(1, user);
		ResultSet r = preparedStatement.executeQuery();
		if (r != null) results = true;
    	}
    	catch (Exception e)
    	{
    		// TODO: check what to do when there is query error
    		results = false;
    	}
    	return results;
    }

    public static int getUserId(String user)
    {
    	int results = -999;
    	try {
    	String queryAdmin = 
    	 "select user_id from users "+
    		"where "+
    			"users.user_name = ? ";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(queryAdmin);
		preparedStatement.setString(1, user);
		ResultSet r = preparedStatement.executeQuery();
		if (r != null) 
			{
			results = r.getInt("user_id");
			}
    	}
    	catch (Exception e)
    	{
    		// TODO: check what to do when there is query error
    		results = -999;
    	}
    	return results;
    }
}

