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

public class CompanyKeyContactDao {

	private final String logTag = CompanyKeyContactDao.class.getName();
	private ResultSet resultSet;

	public ArrayList<CompanyKeyContact> getCompanyKeyContacts(String userEPPN,
			int companyID) throws DMCServiceException {
		ArrayList<CompanyKeyContact> contacts = new ArrayList<CompanyKeyContact>();
		try {
			if (!CompanyUserUtil.isDMDIIMember(userEPPN)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN
						+ " is not DMDII org member.");
				throw new DMCServiceException(
						DMCServiceException.NotDMDIIMember, "User: " + userEPPN
								+ " is not DMDII org member.");
			} else {
				String query = "select oc.id id, oc.organization_id companyId, oc.contact_type_id t, u.user_name n, " +
							   " u.phone phoneNumber, u.title title, u.email email " +
							   " from users u, organization_contact oc " +
							   " where oc.organization_id = ? and " +
							   " u.user_id = oc.user_id";
				PreparedStatement preparedStatement = DBConnector
						.prepareStatement(query);
				preparedStatement.setInt(1, companyID);
				this.resultSet = preparedStatement.executeQuery();
				while (this.resultSet.next()) {
					String id = (new Integer(this.resultSet.getInt("id")))
							.toString();
					String company_id = (new Integer(
							this.resultSet.getInt("companyId")))
							.toString();
					int typeOfContact = this.resultSet.getInt("t");
					String userName = this.resultSet.getString("u");
					String phone = this.resultSet.getString("phoneNumber");
					String title = this.resultSet.getString("title");
					String email = this.resultSet.getString("email");
					CompanyKeyContact s = new CompanyKeyContact();
					s.setId(id);
					s.setCompanyId(company_id);
					s.setName(userName);
					s.setType(typeOfContact);
					s.setPhoneNumber(phone);
					s.setTitle(title);
					s.setEmail(email);
					contacts.add(s);
				}
			}
		} catch (SQLException e) {
			ServiceLogger.log(this.logTag, e.getMessage());
			throw new DMCServiceException(DMCServiceException.OtherSQLError,
					"SQLException: \n" + e.getMessage());
		}
		return contacts;
	}

	public int createCompanyKeyContact(CompanyKeyContact s, String userEPPN) throws DMCServiceException {
		int org_id = new Integer(s.getCompanyId()).intValue();
		int result=-1;
		try {			
			
			if (!CompanyUserUtil.isAdmin(userEPPN, org_id)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN + " is not admin user of org:." + org_id);
				throw new DMCServiceException(DMCServiceException.NotAdminUser, "User: " + userEPPN + " is not admin user of org:." + org_id);
			}
			else
			{
			int uid = CompanyUserUtil.getUserId(userEPPN);
			String query = "insert into organization_contact (organization_id, contact_type_id, user_id) values (?,?,?)";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);

			preparedStatement.setInt(1,
					(new Integer(s.getCompanyId())).intValue());
			preparedStatement.setInt(2, s.getType());
			preparedStatement.setInt(3,uid);
			
			int rCreate = preparedStatement.executeUpdate();
			
			String queryId = "select max(id) max_id from organization_skill";
			PreparedStatement preparedStatement1 = DBConnector
					.prepareStatement(queryId);
			ResultSet r=preparedStatement1.executeQuery();
			r.next();
			result=r.getInt("max_id");
			
			ServiceLogger.log(
					this.logTag,
					"User: " + userEPPN + " added skill set:"
							+ s.getName() + " into:" + s.getCompanyId() + " with id:" + result);
			}
		}

		catch (SQLException e) {
			ServiceLogger.log(this.logTag, "SQLException: " + e.toString());
			throw new DMCServiceException(DMCServiceException.OtherSQLError,"SQLException: " + e.toString());
		}
		
		try {
			//TODO: for insert return the index as the results.
			CompanyProfileChange.profChangeAddItem(userEPPN, org_id, "organization_skill", "-1", s.getName());
		}
		catch (Exception e)
		{
			ServiceLogger.log(this.logTag, "Error in insert to change log: " + e.toString());
			throw new DMCServiceException(DMCServiceException.CanNotInsertChangeLog, e.toString());
		}
		return result;
	}
	
	public int updateCompanyKeyContact(CompanyKeyContact s, String userEPPN) throws DMCServiceException {
		int org_id = new Integer(s.getCompanyId()).intValue();
		int result=-1;
		try {			
			
			if (!CompanyUserUtil.isAdmin(userEPPN, org_id)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN + " is not admin user of org:." + org_id);
				throw new DMCServiceException(DMCServiceException.NotAdminUser, "User: " + userEPPN + " is not admin user of org:." + org_id);
			}
			else
			{
				result = new Integer(s.getId());
				
				String query = "select * from organization_contact where id = ?";
				PreparedStatement preparedStatement1 = DBConnector.prepareStatement(query);
				preparedStatement1.setInt(1,result);
				ResultSet r=preparedStatement1.executeQuery();
				// If there is no entry, create a new entry for the contact.
				if (!r.next()) return createCompanyKeyContact(s, userEPPN);
				
				int uid = CompanyUserUtil.getUserId(userEPPN);
				String query2 = "update organization_contact set contact_type_id = ?, user_id = ? where id = ?";
				PreparedStatement preparedStatement2 = DBConnector.prepareStatement(query2);
				preparedStatement2.setInt(1,s.getType());
				preparedStatement2.setInt(2,uid);
				preparedStatement2.setInt(3,result);
				int rCreate = preparedStatement2.executeUpdate();
				
				ServiceLogger.log(
					this.logTag,
					"User: " + userEPPN + " updated contact id:"
							+ result + " of " + s.getCompanyId());
			}
		}
		catch (SQLException e) {
			ServiceLogger.log(this.logTag, "SQLException: " + e.toString());
			throw new DMCServiceException(DMCServiceException.OtherSQLError,"SQLException: " + e.toString());
		}
		
		try {
			CompanyProfileChange.profChangeUpdate(userEPPN, org_id, "organization_contact", new Integer(result).toString(), s.getName());
		}
		catch (Exception e)
		{
			ServiceLogger.log(this.logTag, "Error in insert to change log: " + e.toString());
			throw new DMCServiceException(DMCServiceException.CanNotInsertChangeLog, e.toString());
		}
		return result;
	}


	public int deleteCompanyKeyContact(String cId, String userEPPN)
			throws DMCServiceException {
		int contactId = new Integer(cId).intValue();
		int org_id;
		int result;
		try {
			
			String querq_org = "select organization_id from organization_contact where id = ?";
			PreparedStatement preparedStatementOrg = DBConnector
					.prepareStatement(querq_org);
			preparedStatementOrg.setInt(1, contactId);
			ResultSet orgSet = preparedStatementOrg.executeQuery();

			if (!orgSet.next()) {
				ServiceLogger.log(this.logTag, "Contact id: " + cId
						+ " does not exist.");
				throw new DMCServiceException(5,"Contact id: " + cId + " does not exist.");
			} else
				org_id = orgSet.getInt("organization_id");

			if (!CompanyUserUtil.isAdmin(userEPPN, org_id)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN
						+ " is not admin user of org:." + org_id);
				throw new DMCServiceException(DMCServiceException.NotAdminUser, "User: " + userEPPN
						+ " is not admin user of org:." + org_id);
			}

			String query = "delete from organization_contact where id=?";
			PreparedStatement preparedStatement = DBConnector
					.prepareStatement(query);
			preparedStatement.setInt(1, contactId);
			ServiceLogger.log(this.logTag, "User: " + userEPPN
					+ " tried to delete skill with id:" + contactId);
			result = preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			ServiceLogger.log(this.logTag, e.getMessage());
			throw new DMCServiceException(DMCServiceException.OtherSQLError,e.getMessage());
		}
        // Insert to change log
		try {
		 CompanyProfileChange.profChangeDeleteItem(userEPPN, org_id,"organization_contact", cId, "Contact");
		}
		catch (SQLException e)
		{
			throw new DMCServiceException(DMCServiceException.CanNotInsertChangeLog, e.getMessage());
		}
		return result;
	}

}