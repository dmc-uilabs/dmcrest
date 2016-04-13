package org.dmc.services.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.dmc.services.DBConnector;

import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.ServiceLogger;

public class CompanyUserUtil {
	private final static String logTag = CompanyUserUtil.class.getName();

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
    			"and dmdii.expire_date >= now() ";
    	//ServiceLogger.log(logTag, "isDMDIIMember query: " + queryAdmin + " for user " + user);
		PreparedStatement preparedStatement = DBConnector.prepareStatement(queryAdmin);
		preparedStatement.setString(1, user);
		ResultSet r = preparedStatement.executeQuery();
		if (r.next()) results = true;
		return results;
    }

	/**
	 *
	 * @param companyId - Organization/Company for which to check memebership
	 * @param owner - The logged-in user's username/EPPN
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean isDMDIIMemberXX(int companyId, String owner) throws SQLException {
		String query = "SELECT m.id FROM organization_dmdii_member m "
				+ "JOIN organization o ON o.organization_id = m.organization_id "
				+ "WHERE o.owner = ?"
				+ "AND o.organization_id = ?";
		PreparedStatement statement = DBConnector.prepareStatement(query);
		statement.setString(1, owner);
		statement.setInt(2, companyId);
		ResultSet result = statement.executeQuery();
		return result.isBeforeFirst();
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

	/**
	 * Check that user is a member of a company
	 * @param companyId
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	static public boolean isMemberOfCompany (int companyId, int userId) throws SQLException {

		// Check ORGANIZATION_USER table to see if user is member of the company
		String query = "SELECT id FROM organization_user WHERE organization_id = " + companyId + " AND user_id = " + userId;
		ResultSet rs = DBConnector.executeQuery(query);
		return rs.isBeforeFirst();
	}

	/**
	 * Check that user is an admin of a company
	 * @param companyId
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	static public boolean isAdminOfCompany (int companyId, int userId) throws SQLException {

		// Check ORGANIZATION_ADMIN table to see if user is member of the company
		String query = "SELECT id FROM organization_admin WHERE organization_id = " + companyId + " AND organization_user_id = " + userId;
		ResultSet rs = DBConnector.executeQuery(query);
		return rs.isBeforeFirst();
	}

	/**
	 * Check that user is an owner of a company
	 * @param companyId the company id
	 * @param userId the user id of the user
	 * @return true if the user is the owner of a company, false otherwise
	 * @throws SQLException
	 */
	static public boolean isOwnerOfCompany (int companyId, int userId) throws SQLException {

		String query = "SELECT u.user_id FROM users u LEFT JOIN organization o ON o.owner = u.user_name WHERE o.organization_id = " + companyId;

		boolean isOwner = false;
		int ownerUserId = -1;
		ResultSet rs = DBConnector.executeQuery(query);
		if (rs.next()) {

			ownerUserId = rs.getInt(1);
			isOwner = ownerUserId == userId;
		}
		return isOwner;
	}


}