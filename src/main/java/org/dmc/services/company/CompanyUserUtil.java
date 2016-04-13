package org.dmc.services.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.dmc.services.DBConnector;


public class CompanyUserUtil {

    public static boolean isAdmin(String user,int org_id) throws SQLException
    {
    	boolean results = false;
    	String queryAdmin = 
    			"select admin.id " +
    			"from organization_admin admin, organization_user orgu, users " +
    			"where users.user_name=? " +
    				"and orgu.organization_id = ? " +
    				"and users.user_id = orgu.user_id " +
    				"and admin.organization_user_id = orgu.id " +
    				"and admin.organization_id = orgu.organization_id ";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(queryAdmin);
		preparedStatement.setString(1, user);
		preparedStatement.setInt(2,org_id);
		ResultSet r = preparedStatement.executeQuery();
		if (r.next()) results = true;
    	return results;
    }
    public static boolean isDMDIIMember(String user) throws SQLException
    {
    	boolean results = false;
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
		if (r.next()) results = true;
		return results;
    }

    public static Integer getUserId(String user) throws SQLException
    {
    	Integer results = null;
    	String queryAdmin = 
    	 "select user_id from users "+
    		"where "+
    			"users.user_name = ? ";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(queryAdmin);
		preparedStatement.setString(1, user);
		ResultSet r = preparedStatement.executeQuery();
		if (r.next()) results = r.getInt("user_id");
    	return results;
    }
    
    // Assume that a user is only in one organization to start with...
    // TODO:  add to allow user to have more than one organizations
    public static Integer getOrgId(String user) throws SQLException
    {
    	Integer results = null;
    	int user_id = getUserId(user);
    	String queryAdmin = 
    	 "select organization_id from organization_user "+
    		"where user_id=? ";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(queryAdmin);
		preparedStatement.setInt(1, user_id);
		ResultSet r = preparedStatement.executeQuery();
		if (r != null) results = r.getInt("organization_id");
    	return results;
    }
}