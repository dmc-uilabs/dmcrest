package org.dmc.services.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCServiceException;
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

	private Connection connection;

	public ArrayList<CompanySkill> getCompanySkills(String userEPPN, int companyID) throws Exception {
		ArrayList<CompanySkill> skills = new ArrayList<CompanySkill>();
		try {
			if (!CompanyUserUtil.isDMDIIMember(userEPPN)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN + " is not DMDII org member.");
				throw new Exception("User: " + userEPPN + " is not DMDII org member.");
			}
			String query = "select * from organization_skill where organization_id = ?";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, companyID);
			this.resultSet = preparedStatement.executeQuery();
			while (this.resultSet.next()) {
				String id = (new Integer(this.resultSet.getInt("id"))).toString();
				String company_id = (new Integer(this.resultSet.getInt("organization_id"))).toString();
				String skill = this.resultSet.getString("skill");
				CompanySkill s = new CompanySkill();
				s.setId(id);
				s.setCompanyId(company_id);
				s.setName(skill);
				skills.add(s);
			}
		} catch (SQLException e) {
			ServiceLogger.log(this.logTag, e.getMessage());
			throw new Exception("SQLException: \n" + e.getMessage());
		}
		return skills;
	}

	/*
	 * public int createCompanySkills(CompanySkill skills, String userEPPN) {
	 * try { //TODO: 1. Check if the user is the admin user. //TODO: 2. Add log
	 * to the change of the company profile. //JSONObject json = new
	 * JSONObject(skills); JSONArray ss = new JSONArray(skills); int s =
	 * ss.length(); int result = 0; for (int i=0;i<s;i++)
	 * 
	 * { JSONObject skill = ss.getJSONObject(i); String query =
	 * "insert into organization_skill (organization_id, skill) values (?,?)";
	 * PreparedStatement preparedStatement =
	 * DBConnector.prepareStatement(query);
	 * 
	 * preparedStatement.setInt(1, skill.getInt("companyId"));
	 * preparedStatement.setString(2, skill.getString("skill"));
	 * ServiceLogger.log(this.logTag, "User: " + userEPPN +
	 * " tried to put skill set:" + skill.getString("skill") + " into:" +
	 * skill.getInt("companyId"));
	 * 
	 * result = preparedStatement.executeUpdate();
	 * 
	 * } return result; } catch (Exception e) { return -9999; } }
	 */

	public CompanySkill createCompanySkill(CompanySkill s, String userEPPN) throws Exception{
		try {
			// TODO: Add log to the change of the company profile.
			

			int org_id = new Integer(s.getCompanyId()).intValue();
			
			ServiceLogger.log(this.logTag, "Begning of createCompanySkill. org_id:" + org_id + "userEPPN:" + userEPPN);

			if (! CompanyUserUtil.isAdmin(userEPPN, org_id)) {

				ServiceLogger.log(this.logTag, "User: " + userEPPN + " is not admin user of org:." + org_id);
				throw new DMCServiceException(DMCServiceException.NotAdminUser, "User: " + userEPPN + " is not admin user of org:." + org_id);
			}
			ServiceLogger.log(this.logTag, "End  of isAdmin check. org_id:" + org_id + "userEPPN:" + userEPPN);

			String query = "insert into organization_skill (organization_id, skill) values (?,?)";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);

			preparedStatement.setInt(1, (new Integer(s.getCompanyId())).intValue());
			preparedStatement.setString(2, s.getName());

			ServiceLogger.log(this.logTag,
					"User: " + userEPPN + " tried to put skill set:" + s.getName() + " into:" + s.getCompanyId());
			int result = preparedStatement.executeUpdate();

			return s;
		} catch (SQLException e) {
			ServiceLogger.log(this.logTag, "There is a SQLException: " + e.toString());
			throw new Exception("SQLException: " + e.toString());
		}
	}

	public int deleteCompanySkills(String sId, String userEPPN) throws Exception {
		int skillId = new Integer(sId).intValue();
		try {
			ServiceLogger.log(this.logTag, "Begning of deteleCompanySkills.");

			// Add tracking information to organization_profile_change
			String querq_org = "select organization_id from organization_skill where id = ?";
			PreparedStatement preparedStatementOrg = DBConnector.prepareStatement(querq_org);
			preparedStatementOrg.setInt(1, skillId);
			ResultSet orgSet = preparedStatementOrg.executeQuery();
			int org_id;
			if (!orgSet.next()) {
				ServiceLogger.log(this.logTag, "Skill id: " + sId + " does not exist.");
				throw new Exception("Skill id: " + sId + " does not exist.");
			} else
				org_id = orgSet.getInt("organization_id");

			ServiceLogger.log(this.logTag, "Organization_id: " + org_id);

			if (!CompanyUserUtil.isAdmin(userEPPN, org_id)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN + " is not admin user of org:." + org_id);
				throw new Exception("User: " + userEPPN + " is not admin user of org:." + org_id);
			}

			ServiceLogger.log(this.logTag, "User: " + userEPPN + " chagne organization:" + org_id);
			// public int profChangeDeleteItem(String user, int organization_id,
			// String tableName, String entryIndex, String valueToDelete)
/*			if (CompanyProfileChange.profChangeDeleteItem(userEPPN, org_id, "organization_skill", sId,
					"Skills") == -999) {
				ServiceLogger.log(this.logTag, "Cannot insert to the log table");
				throw new Exception("Can not insert to log table");
			}
			;*/

			// Now, delete from organization_skill
			String query = "delete from organization_skill where id=?";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, skillId);
			ServiceLogger.log(this.logTag, "User: " + userEPPN + " tried to delete skill with id:" + skillId);
			int result = preparedStatement.executeUpdate();
			return result;
		} catch (Exception e) {
			ServiceLogger.log(this.logTag, e.getMessage());
			throw e;
			// throw new Exception("User: " + userEPPN + " cannot delete skill
			// with id:" + skillId);
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
			if (r != null)
				r.getInt("user_id");
		} catch (Exception e) {
			// TODO: log policy?
			return returnValue;
		}
		return returnValue;
	}

	// TODO: implement the method
	private static int isAdmin(String userName) {
		return 1;
	}

	// TODO: implement the method
	private static int isDMDII(String userName) {
		return 1;
	}
}