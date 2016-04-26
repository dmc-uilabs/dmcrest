package org.dmc.services.company;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;

import java.util.ArrayList;


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
						DMCError.NotDMDIIMember, "User: " + userEPPN
								+ " is not DMDII org member.");
			} else {
				String query = "select oc.id id, oc.organization_id companyId, oc.contact_type_id t, oc.name n, " +
							   " oc.phone_number phoneNumber, oc.title title, oc.email email " +
							   " from organization_contact oc " +
							   " where oc.organization_id = ?";
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
					String userName = this.resultSet.getString("n");
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
			throw new DMCServiceException(DMCError.OtherSQLError,
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
				throw new DMCServiceException(DMCError.NotAdminUser, "User: " + userEPPN + " is not admin user of org:." + org_id);
			}
			else
			{
			String query = "insert into organization_contact (organization_id, contact_type_id, name, phone_number, title, email) values (?,?,?,?,?,?)";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1,(new Integer(s.getCompanyId())).intValue());
			preparedStatement.setInt(2, s.getType());
			preparedStatement.setString(3,s.getName());
			preparedStatement.setString(4,s.getPhoneNumber());
			preparedStatement.setString(5,s.getTitle());
			preparedStatement.setString(6,s.getEmail());
			
			int rCreate = preparedStatement.executeUpdate();
			
			String queryId = "select max(id) max_id from organization_contact";
			PreparedStatement preparedStatement1 = DBConnector
					.prepareStatement(queryId);
			ResultSet r=preparedStatement1.executeQuery();
			r.next();
			result=r.getInt("max_id");
			
			ServiceLogger.log(
					this.logTag,
					"User: " + userEPPN + " added key contact:"
							+ s.getName() + " into:" + s.getCompanyId() + " with id:" + result);
			}
		}

		catch (SQLException e) {

			ServiceLogger.log(this.logTag, "SQLException: " + e.toString());
			throw new DMCServiceException(DMCError.OtherSQLError,"SQLException: " + e.toString());
		}
		
		try {
			CompanyProfileChange.profChangeAddItem(userEPPN, org_id, "organization_skill", "-1", s.getName());
		}
		catch (Exception e)
		{
			ServiceLogger.log(this.logTag, "Error in insert to change log: " + e.toString());
			throw new DMCServiceException(DMCError.CanNotInsertChangeLog, e.toString());
		}
		return result;
	}
	
	public int updateCompanyKeyContact(String contactID, CompanyKeyContact s, String userEPPN) throws DMCServiceException {
		int org_id = new Integer(s.getCompanyId()).intValue();
		int result=-1;
		try {			
			
			if (!CompanyUserUtil.isAdmin(userEPPN, org_id)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN + " is not admin user of org:." + org_id);
				throw new DMCServiceException(DMCError.NotAdminUser, "User: " + userEPPN + " is not admin user of org:." + org_id);
			}
			else
			{
				//result = new Integer(s.getId());
			    result = new Integer(contactID);
				
				String query = "select * from organization_contact where id = ?";
				PreparedStatement preparedStatement1 = DBConnector.prepareStatement(query);
				preparedStatement1.setInt(1,result);
				ResultSet r=preparedStatement1.executeQuery();
				// If there is no entry, create a new entry for the contact.
				if (!r.next()) return createCompanyKeyContact(s, userEPPN);
				
				String query2 = "update organization_contact set organization_id=?, "
						+ "contact_type_id = ?, name = ?,"
						+ "phone_number=?, title=?, email=? "
						+ " where id = ?";
				PreparedStatement preparedStatement2 = DBConnector.prepareStatement(query2);
				preparedStatement2.setInt(1,new Integer(s.getCompanyId()));
				preparedStatement2.setInt(2,s.getType());
				preparedStatement2.setString(3,s.getName());
				preparedStatement2.setString(4,s.getPhoneNumber());
				preparedStatement2.setString(5,s.getTitle());
				preparedStatement2.setString(6,s.getEmail());
				preparedStatement2.setInt(7,result);
				
				int rCreate = preparedStatement2.executeUpdate();
				
				ServiceLogger.log(
					this.logTag,
					"User: " + userEPPN + " updated contact id:"
							+ s.getCompanyId() + " of " + s.getCompanyId());
			}
		}
		catch (SQLException e) {
			ServiceLogger.log(this.logTag, "SQLException: " + e.toString());
			throw new DMCServiceException(DMCError.OtherSQLError,"SQLException: " + e.toString());
		}
		
		try {
			CompanyProfileChange.profChangeUpdate(userEPPN, org_id, "organization_contact", s.getCompanyId(), s.getName());
		}
		catch (Exception e)
		{
			ServiceLogger.log(this.logTag, "Error in insert to change log: " + e.toString());
			throw new DMCServiceException(DMCError.CanNotInsertChangeLog, e.toString());
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
				throw new DMCServiceException(DMCError.CompanySkillSetNotExist,"Contact id: " + cId + " does not exist.");
			} else
				org_id = orgSet.getInt("organization_id");

			if (!CompanyUserUtil.isAdmin(userEPPN, org_id)) {
				ServiceLogger.log(this.logTag, "User: " + userEPPN
						+ " is not admin user of org:." + org_id);
				throw new DMCServiceException(DMCError.NotAdminUser, "User: " + userEPPN
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
			throw new DMCServiceException(DMCError.OtherSQLError,e.getMessage());
		}
        // Insert to change log
		try {
		 CompanyProfileChange.profChangeDeleteItem(userEPPN, org_id,"organization_contact", cId, "Contact");
		}
		catch (SQLException e)
		{
			throw new DMCServiceException(DMCError.CanNotInsertChangeLog, e.getMessage());
		}
		return result;
	}

}