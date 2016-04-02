package org.dmc.services.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.DBConnector;
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

public class CompanySkillDao {

	private final String logTag = CompanySkillDao.class.getName();
	private ResultSet resultSet;
	
	// Only declare here and instantiate in method where it is used
	// Instantiating here may cause NullPointer Exceptions
	private Connection connection;
	
    public ArrayList<CompanySkill> getCompanySkills(String userEPPN, int companyID) throws HTTPException {
        ArrayList<CompanySkill> skills = new ArrayList<CompanySkill>();        
        ServiceLogger.log(this.logTag, "User: " + userEPPN + " asking for all skills of the company: " + companyID);
        try {
        	String query = "select * from organization_skill where organization_id = ?";
        	PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, companyID);
			this.resultSet = preparedStatement.executeQuery();
			

            while (this.resultSet.next()) {
            	int id = this.resultSet.getInt("id");
            	String skill = this.resultSet.getString("skill");
                CompanySkill s = new CompanySkill(id,companyID,skill);
                skills.add(s);
            }
        } catch (SQLException e) {
            ServiceLogger.log(this.logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());  // ToDo: what error should this be?
        }
        return skills;
	}
    
    public int createCompanySkills(String skills, String userEPPN)
    {
    	try {
    		//TODO: 1. Check if the user is the admin user.
    		//TODO: 2. Add log to the change of the company profile. 
    		//JSONObject json = new JSONObject(skills);
    		JSONArray ss = new JSONArray(skills);
    		int s = ss.length();
    		int result = 0;
    		for (int i=0;i<s;i++)
    			
    		{
    			JSONObject skill = ss.getJSONObject(i);
    			String query = "insert into organization_skill (organization_id, skill) values (?,?)";
    			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
    			
    			preparedStatement.setInt(1, skill.getInt("companyId"));
    			preparedStatement.setString(2, skill.getString("skill"));
        		ServiceLogger.log(this.logTag, "User: " + userEPPN + " tried to put skill set:" + skill.getString("skill") + " into:" + skill.getInt("companyId"));

    			result = preparedStatement.executeUpdate(); 
    			
    		}
    		return result;
    	}
    	catch (Exception e)
    	{
    		return -9999;
    	}
    }
    
    public int deleteCompanySkills(int skillId,String userEPPN)
    {
    	try {
    		// TODO: make sure that userEPPN is the administration user
    		// Add tracking information to organization_profile_change
    		String querq_org = "select organization_id from organization_skill where id = ?";
    		PreparedStatement preparedStatementOrg = DBConnector.prepareStatement(querq_org);
    		preparedStatementOrg.setInt(1, skillId);
    		ResultSet orgSet = preparedStatementOrg.executeQuery();
    		int org_id;
    		if (orgSet!=null)
    		{
    			org_id = orgSet.getInt("organization_id");
    		}    		
    		//The delete item does not exist.
    		else return(-100);
    		String changeQuery = "insert into organization_profile_change (organization_id, user_id, change_timestamp, change_type_id, table_or_field, new_value) ";
    		changeQuery = changeQuery + "values (?,?,?,?,?,?)";
    		PreparedStatement preparedStatementChange = DBConnector.prepareStatement(changeQuery);
    		preparedStatementChange.setInt(1, org_id);
    		int userId = getUserId(userEPPN);
    			if (userId==-9999) return -9999;
    		preparedStatementChange.setInt(2, userId);
    		preparedStatementChange.setTimestamp(3, getCurrentTimeStamp());
    		// 4: delete_item
    		preparedStatementChange.setInt(4, 4);
    		preparedStatementChange.setString(5, "organization_skill");
    		preparedStatementChange.setInt(6, skillId);
    		int r = preparedStatementChange.executeUpdate();
    		// Now, delete from organization_skill
			String query = "delete from organization_skill where id=?";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			
			preparedStatement.setInt(1, skillId);
    		ServiceLogger.log(this.logTag, "User: " + userEPPN + " tried to delete skill with id:" + skillId);
			int result = preparedStatement.executeUpdate(); 
    		return result;
    	}
    	catch (Exception e)
    	{
    		return -9999;
    	}
    }
    
    private static java.sql.Timestamp getCurrentTimeStamp() {
    	java.util.Date today = new java.util.Date();
    	return new java.sql.Timestamp(today.getTime());
    }
    private static int getUserId(String userName) {
    	String queryUser = "select user_id from users where user_name = ?";
    	int returnValue = -9999;
    	try {
		PreparedStatement prepareStatement = DBConnector.prepareStatement(queryUser);
		prepareStatement.setString(1, userName);
		ResultSet r = prepareStatement.executeQuery();	
		if (r!=null)
			r.getInt("user_id");
    	}
    	catch (Exception e)
    	{
    		//TODO: log policy?
    		return returnValue;
    	}
		return returnValue;
    }
    //TODO: implement the method
    private static int isAdmin(String userName)
    {
    	return 1;
    }
    //TODO: implement the method
    private static int isDMDII(String userName)
    {
    	return 1;
    }
}