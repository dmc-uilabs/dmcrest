package org.dmc.services.company;

import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.search.SearchException;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.User;
import org.dmc.solr.SolrUtils;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

import javax.xml.ws.http.HTTPException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.dmc.services.company.CompanyUserUtil.isMemberOfCompany;

public class CompanyDao {

	private static final String logTag = CompanyDao.class.getName();
	private ResultSet resultSet;

	// Only declare here and instantiate in method where it is used
	// Instantiating here may cause NullPointer Exceptions
	private Connection connection;

    public ArrayList<Company> getCompanies(String userEPPN) throws HTTPException {
        ArrayList<Company> companies = null;
        ServiceLogger.log(logTag, "User: " + userEPPN + " asking for all companies");

        try {
            // get all organizations;
            // does the organization need to be active member?  assume no.
            resultSet = DBConnector.executeQuery("SELECT organization_id, accountid, name FROM organization");
            companies = new ArrayList<Company>();

            while (resultSet.next()) {
                int id = resultSet.getInt("organization_id");
                int accountId = resultSet.getInt("accountid");
                String name = resultSet.getString("name");

                Company company = new Company();
				company.setId(Integer.toString(id));
				company.setAccountId(Integer.toString(accountId));
				company.setName(name);
                companies.add(company);
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());  // ToDo: what error should this be?
        }
        return companies;
	}


	public Company getCompany(int id, String userEPPN) throws HTTPException {
		ServiceLogger.log(logTag, "User: " + userEPPN + " asking company " + id);

		Company company = new Company();
		try {

			String query = "SELECT * FROM organization o "
					+ "JOIN common_address a ON o.address_id = a.id "
// 					+ "JOIN common_image i ON o.feature_image = i.id "
					+ "WHERE o.organization_id = "  + id;
			//ServiceLogger.log(logTag, "getCompany query: " + query);
			resultSet = DBConnector.executeQuery(query);

			if (resultSet.next()) {
				id = resultSet.getInt("organization_id");
				int accountId = resultSet.getInt("accountId");
				String name = resultSet.getString("name");
				String location = resultSet.getString("location");
				String description = resultSet.getString("description");
				String division = resultSet.getString("division");
				String industry = resultSet.getString("industry");
				String NAICSCode = resultSet.getString("naics_code");
				String RDFocus = resultSet.getString("rd_focus");
				String customers = resultSet.getString("customers");
				String awardsReceived = resultSet.getString("awards");
				String technicalExpertise = resultSet.getString("tech_expertise");
				String toolsSoftwareEquipmentMachines = resultSet.getString("tools_software_equip_mach");
				String postCollaborations = resultSet.getString("post_collaboration");
				String collaborationInterests = resultSet.getString("collaboration_interest");
				String pastProjects = resultSet.getString("past_projects");
				String upcomingProjectInterests = resultSet.getString("upcoming_project_interests");
				String address = resultSet.getString("street_address1");
				String address2 = resultSet.getString("street_address2");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				String zipCode = resultSet.getString("zip");
				String twitter = resultSet.getString("social_media_twitter");
				String linkedIn = resultSet.getString("social_media_linkedin");
				String website = resultSet.getString("website");
				String methodCommunication = resultSet.getString("perfered_comm_method");
				String email = resultSet.getString("email");
				String phone = resultSet.getString("phone");
				int categoryTier = resultSet.getInt("category_tier");
				String dateJoined = resultSet.getString("date_joining");
				String reasonJoining = resultSet.getString("reason_joining");
				String featureImageThumb = "/uploads/company/picture/1/20160315182723000000.jpg"; //resultSet.getString("thumbnail");
				String featureImageLarge = "/uploads/company/picture/1/20160315182723000000.jpg"; //resultSet.getString("large_image");
				String logoImage = resultSet.getString("logo_image");
				boolean follow = Boolean.valueOf(resultSet.getString("follow"));
				int favoritesCount = resultSet.getInt("favorites_count");
				boolean isOwner = Boolean.valueOf(resultSet.getString("is_owner"));
				String owner = resultSet.getString("owner");

//				if (!owner.equals(userEPPN)) {
//					ServiceLogger.log(logTag, "User is not owner of requested Company/Organization");
//					throw new HTTPException(HttpStatus.FORBIDDEN.value());
//				}

				company.setId(Integer.toString(id));
				company.setAccountId(Integer.toString(accountId));
				company.setName(name);
				company.setLocation(location);
				company.setDescription(description);
				company.setDivision(division);
				company.setIndustry(industry);
				company.setNAICSCode(NAICSCode);
				company.setRDFocus(RDFocus);
				company.setCustomers(customers);
				company.setAwardsReceived(awardsReceived);
				company.setTechnicalExpertise(technicalExpertise);
				company.setToolsSoftwareEquipmentMachines(toolsSoftwareEquipmentMachines);
				company.setPastCollaborations(postCollaborations);
				company.setPastProjects(pastProjects);
				company.setUpcomingProjectInterests(upcomingProjectInterests);
				company.setCollaborationInterests(collaborationInterests);
				company.setAddress(address +  ", " + address2);
				company.setCity(city);
// Todo:				company.setState(state);
				company.setZipCode(zipCode);
				company.setTwitter(twitter);
				company.setLinkedIn(linkedIn);
				company.setWebsite(website);
				company.setMethodCommunication(methodCommunication);
				company.setEmail(email);
				company.setPhone(phone);
				company.setCategoryTier(categoryTier);
				company.setDateJoined(dateJoined);
				company.setReasonJoining(reasonJoining);
				company.setFeatureImage(new FeatureImage(featureImageThumb, featureImageLarge));
				company.setLogoImage(logoImage);
				company.setFollow(follow);
				company.setFavoritesCount(favoritesCount);
				company.setIsOwner(isOwner);
				company.setOwner(owner);
			}

		} catch (SQLException e) {
			ServiceLogger.log(logTag, "Error message " + e.getMessage());
			throw new HTTPException(HttpStatus.FORBIDDEN.value());
		}
		return company;
	}

	public Id createCompany(Company company, String userEPPN) {
		ServiceLogger.log(logTag, "In createCompany, User: " + userEPPN + " creating company " + company.getName());

		connection = DBConnector.connection();
		Util util = Util.getInstance();
		PreparedStatement statement;
		String query;
		long UnixTimeStamp = Calendar.getInstance().getTime().getTime();
		Timestamp timestamp = new java.sql.Timestamp(UnixTimeStamp);
		Timestamp expires = new java.sql.Timestamp(UnixTimeStamp + (1000*60*60*24*365*10));
		int id = -99999, commonAddressId = -9999, commonImageId = -9999;
		// Default to Industry Tier 1 member for now
		int organizationMemberId = 1;

		try {
			connection.setAutoCommit(false);

			// insert into relational common_address
			query = "INSERT INTO common_address"
					+ "(street_address1, street_address2, city, state, zip) "
					+ "VALUES (?, ?, ?, ?, ?) ";

			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, company.getAddress());
			statement.setString(2, "");
			statement.setString(3, company.getCity());
			statement.setInt(4, company.getState());
			statement.setString(5, company.getZipCode());
			statement.executeUpdate();
			commonAddressId = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "ASSOCIATED COMMON_ADDRESS ENTRY: " + commonAddressId);

			// insert into relational common_image
			query = "INSERT INTO common_image"
					+ "(caption, thumbnail, large_image) "
					+ "VALUES (?, ?, ?) ";

			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, "");
			statement.setString(2, company.getFeatureImage().getThumbnail());
			statement.setString(3, company.getFeatureImage().getLarge());
			statement.executeUpdate();
			commonImageId = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "ASSOCIATED COMMON_IMAGE ENTRY: " + commonImageId);

			query = "INSERT INTO organization "
					+ "(accountid, name, location, description, division, "
					+ "industry, naics_code, rd_focus, customers, awards , "
					+ "tech_expertise, tools_software_equip_mach, post_collaboration, collaboration_interest, past_projects, "
					+ "upcoming_project_interests, address_id, email, phone, "
					+ "website, social_media_linkedin, social_media_twitter, "
					+ "perfered_comm_method, category_tier, date_joining, reason_joining, "
					+ "feature_image, logo_image, follow, favorites_count, is_owner, owner)"
					+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, Integer.parseInt(company.getAccountId()));
			statement.setString(2, company.getName());
			statement.setString(3, company.getLocation());
			statement.setString(4, company.getDescription());
			statement.setString(5, company.getDivision());
			statement.setString(6, company.getIndustry());
			statement.setString(7, company.getNAICSCode());
			statement.setString(8, company.getRDFocus());
			statement.setString(9, company.getCustomers());
			statement.setString(10, company.getAwardsReceived());
			statement.setString(11, company.getTechnicalExpertise());
			statement.setString(12, company.getToolsSoftwareEquipmentMachines());
			statement.setString(13, company.getPastCollaborations());
			statement.setString(14, company.getCollaborationInterests());
			statement.setString(15, company.getPastProjects());
			statement.setString(16, company.getUpcomingProjectInterests());
			statement.setInt(17, commonAddressId);
			statement.setString(18, company.getEmail());
			statement.setString(19, company.getPhone());
			statement.setString(20, company.getWebsite());
			statement.setString(21, company.getLinkedIn());
			statement.setString(22, company.getTwitter());
			statement.setString(23, company.getMethodCommunication());
			statement.setInt(24, company.getCategoryTier());
			statement.setString(25, company.getDateJoined());
			statement.setString(26, company.getReasonJoining());
			statement.setInt(27, commonImageId);
			statement.setString(28, company.getLogoImage());
			statement.setBoolean(29, company.getFollow());
			statement.setInt(30, company.getFavoritesCount());
			statement.setBoolean(31, company.getIsOwner());
			statement.setString(32, company.getOwner());
			statement.executeUpdate();
			id = util.getGeneratedKey(statement, "organization_id");
			ServiceLogger.log(logTag, "ORGANIZATION/COMPANY ID: " + id);

			// insert into organization_dmdii_member
			query = "INSERT INTO organization_dmdii_member (organization_id, dmdii_type_id, modification_date, start_date, expire_date)"
					+ "VALUES (?, ?, ?, ?, ?)";

			statement = DBConnector.prepareStatement(query);
			statement.setInt(1, id);
			statement.setInt(2, organizationMemberId);
			statement.setTimestamp(3, timestamp);
			statement.setTimestamp(4, timestamp);
			statement.setTimestamp(5, expires);
			statement.executeUpdate();


			int userIdEPPN = UserDao.getUserID(userEPPN);

			// insert into organization_admin
			query = "INSERT INTO organization_user (user_id, organization_id)"
			+ "VALUES (?, ?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, userIdEPPN);
			statement.setInt(2, id);
			statement.executeUpdate();
			int organization_user_id = util.getGeneratedKey(statement, "id");

			// insert into organization_admin
			query = "INSERT INTO organization_admin (organization_user_id, organization_id)"
				+ "VALUES (?, ?)";
			statement = DBConnector.prepareStatement(query);
			statement.setInt(1, organization_user_id);
			statement.setInt(2, id);
			statement.executeUpdate();

			connection.commit();

			try {
				SolrUtils.triggerFullIndexing(SolrUtils.CORE_GFORGE_COMPANIES);
			} catch (SearchException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction createCompany Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
		}
		return new Id.IdBuilder(id)
		.build();
	}

	public Id updateCompany(int id, Company company, String userEPPN) throws HTTPException {

		connection = DBConnector.connection();
		int companyId = id, commonAddressId, commonImageId;

		try {

			// Ensure user is owner of company before patching
			resultSet = DBConnector.executeQuery("SELECT owner FROM organization "
					+ "WHERE organization_id = "  + id);

			if (resultSet.next()) {
				String owner = resultSet.getString("owner");
				if (!CompanyUserUtil.isAdmin(userEPPN, companyId)){
//				if (!owner.equals(userEPPN)) {
					ServiceLogger.log(logTag, "User " + userEPPN + " is not owner of Company/Organization");
					throw new HTTPException(HttpStatus.FORBIDDEN.value());
				}
			}

			connection.setAutoCommit(false);

	        // retrieve relational common_address and common_image
			resultSet = DBConnector.executeQuery("SELECT a.id common_address_id, i.id common_image_id "
					+ "FROM organization o "
					+ "JOIN common_address a ON o.address_id = a.id "
					+ "JOIN common_image i ON o.feature_image = i.id "
					+ "WHERE o.organization_id = "  + companyId);

			if (resultSet.next()) {
				commonAddressId = resultSet.getInt("common_address_id");
				commonImageId = resultSet.getInt("common_image_id");

				// update common_address
		        String query = "UPDATE common_address SET "
	        		+ "street_address1 = ?, "
	        		+ "street_address2 = ?, "
	        		+ "city = ?, "
	        		+ "state = ?, "
	        		+ "zip = ? "
	        		+ "WHERE id = ?";

		        PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
				preparedStatement.setString(1, company.getAddress());
				preparedStatement.setString(2, "");
				preparedStatement.setString(3, company.getCity());
				preparedStatement.setInt(4, company.getState());
				preparedStatement.setString(5, company.getZipCode());
		        preparedStatement.setInt(6, companyId);
		        preparedStatement.executeUpdate();

		        // update common_image
		        query = "UPDATE common_image SET "
		        		+ "caption = ?, "
		        		+ "thumbnail = ?, "
		        		+ "large_image = ? "
		        		+ "WHERE id = ? ";

		        preparedStatement = DBConnector.prepareStatement(query);
				preparedStatement.setString(1, "");
				preparedStatement.setString(2, company.getFeatureImage().getThumbnail());
				preparedStatement.setString(3, company.getFeatureImage().getLarge());
		        preparedStatement.setInt(4, companyId);
		        preparedStatement.executeUpdate();

		        // update the company
				query = "UPDATE organization SET "
				+ "accountid = ?, name = ?, location = ?, description = ?, division = ?, "
				+ "industry = ?, naics_code = ?, rd_focus = ?, customers = ?, awards = ?, "
			    + "tech_expertise = ?, tools_software_equip_mach = ?, post_collaboration = ?, "
			    + "collaboration_interest = ?, past_projects = ?, upcoming_project_interests = ?, "
				+ "address_id = ?, email = ?, phone = ?, "
	            + "website = ?, social_media_linkedin = ?, social_media_twitter = ?, "
				+ "perfered_comm_method = ?, category_tier = ?, date_joining = ?, reason_joining = ?, "
				+ "feature_image = ?, logo_image = ?, follow = ?, favorites_count = ?, is_owner = ?, owner = ? "
				+ "WHERE organization_id = ?";

				preparedStatement = DBConnector.prepareStatement(query);
				preparedStatement.setInt(1, Integer.parseInt(company.getAccountId()));
				preparedStatement.setString(2, company.getName());
				preparedStatement.setString(3, company.getLocation());
				preparedStatement.setString(4, company.getDescription());
				preparedStatement.setString(5, company.getDivision());
				preparedStatement.setString(6, company.getIndustry());
				preparedStatement.setString(7, company.getNAICSCode());
				preparedStatement.setString(8, company.getRDFocus());
				preparedStatement.setString(9, company.getCustomers());
				preparedStatement.setString(10, company.getAwardsReceived());
				preparedStatement.setString(11, company.getTechnicalExpertise());
				preparedStatement.setString(12, company.getToolsSoftwareEquipmentMachines());
				preparedStatement.setString(13, company.getPastCollaborations());
				preparedStatement.setString(14, company.getCollaborationInterests());
				preparedStatement.setString(15, company.getPastProjects());
				preparedStatement.setString(16, company.getUpcomingProjectInterests());
				preparedStatement.setInt(17, commonAddressId);
				preparedStatement.setString(18, company.getEmail());
				preparedStatement.setString(19, company.getPhone());
				preparedStatement.setString(20, company.getWebsite());
				preparedStatement.setString(21, company.getLinkedIn());
				preparedStatement.setString(22, company.getTwitter());
				preparedStatement.setString(23, company.getMethodCommunication());
				preparedStatement.setInt(24, company.getCategoryTier());
				preparedStatement.setString(25, company.getDateJoined());
				preparedStatement.setString(26, company.getReasonJoining());
				preparedStatement.setInt(27, commonImageId);
				preparedStatement.setString(28, company.getLogoImage());
				preparedStatement.setBoolean(29, company.getFollow());
				preparedStatement.setInt(30, company.getFavoritesCount());
				preparedStatement.setBoolean(31, company.getIsOwner());
				preparedStatement.setString(32, company.getOwner());
		        preparedStatement.setInt(33, companyId);
		        preparedStatement.executeUpdate();

		        connection.commit();

				if (Config.IS_TEST == null) {
					//ServiceLogger.log(LOGTAG, "SolR indexing turned off");
					// Trigger solr indexing
					try {
						SolrUtils.triggerFullIndexing(SolrUtils.CORE_GFORGE_COMPANIES);
					} catch (SearchException e) {
						ServiceLogger.log(logTag, e.getMessage());
					}
				}
			}
		}
		catch (SQLException e) {
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction updateCompany Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
		}
		return new Id.IdBuilder(companyId)
		.build();
	}


	public Id deleteCompany(int id, String userEPPN) {

		CompanyVideoDao videoDao = new CompanyVideoDao();
		connection = DBConnector.connection();
		PreparedStatement statement;
		String query;
	    int companyId = id, commonAddressId, commonImageId;

	    try {

			// Check that the user deleting the company video is an administrator or the owner of the company
			/**
			 ** Checks disabled until members for companies are tracked
			if (!(isOwnerOfCompany(companyId, userIdEPPN) || isAdminOfCompany(companyId, userIdEPPN))) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not authorized to delete videos for company " + companyId);
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
			*/

	    	connection.setAutoCommit(false);

	        // retrieve relational common_address and common_image
			resultSet = DBConnector.executeQuery("SELECT a.id common_address_id, i.id common_image_id "
					+ "FROM organization o "
					+ "JOIN common_address a ON o.address_id = a.id "
					+ "JOIN common_image i ON o.feature_image = i.id "
					+ "WHERE o.organization_id = "  + id);

			if (resultSet.next()) {
				commonAddressId = resultSet.getInt("common_address_id");
				commonImageId = resultSet.getInt("common_image_id");

		        // delete organization_dmdii_member
		        query = "DELETE FROM organization_dmdii_member WHERE organization_id = ?";
		        statement = DBConnector.prepareStatement(query);
		        statement.setInt(1, id);
		        statement.executeUpdate();

				// delete organization_video
				videoDao.deleteCompanyVideo(companyId, -1, userEPPN);

				// delete organization_admin
				query = "DELETE FROM organization_admin WHERE organization_id = ?";
				statement = DBConnector.prepareStatement(query);
				statement.setInt(1, id);
				int numAdminsDeleted = statement.executeUpdate();
				ServiceLogger.log(logTag, "Deleted " + numAdminsDeleted + " admins for organization_id=" + id);

				// delete organization_user
				query = "DELETE FROM organization_user WHERE organization_id = ?";
				statement = DBConnector.prepareStatement(query);
				statement.setInt(1, id);
				int numUsersDeleted = statement.executeUpdate();
				ServiceLogger.log(logTag, "Deleted " + numUsersDeleted + " users for organization_id=" + id);

				// delete organization
		        query = "DELETE FROM organization WHERE organization_id = ?";
		        statement = DBConnector.prepareStatement(query);
		        statement.setInt(1, companyId);
		        statement.executeUpdate();

				// delete common_address
		        query = "DELETE FROM common_address WHERE id = ?";
		        statement = DBConnector.prepareStatement(query);
		        statement.setInt(1, commonAddressId);
		        statement.executeUpdate();

				// delete common_image
		        query = "DELETE FROM common_image WHERE id = ?";
		        statement = DBConnector.prepareStatement(query);
		        statement.setInt(1, commonImageId);
		        statement.executeUpdate();

		        connection.commit();

				try {
					SolrUtils.triggerFullIndexing(SolrUtils.CORE_GFORGE_COMPANIES);
				} catch (SearchException e) {
					ServiceLogger.log(logTag, e.getMessage());
				}
			}
	    }
		catch (SQLException e) {
			ServiceLogger.log(logTag, "ERROR IN DELETE COMPANY-------------------" + e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction deleteCompany Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
		}

		return new Id.IdBuilder(id)
		.build();
	}

    /**
	 * Retrieve the organization_id from the organization_user table for the specifyed company and user
	 * @param userId the user id
     * @return the organization_id from the organization_user record if found, -1 otherwise
     */
	static public int getUserCompanyId(int userId)  {
		int id = -1;
		String query = "SELECT organization_id FROM organization_user WHERE user_id = ?";
        try {
            PreparedStatement statement = DBConnector.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				id = rs.getInt(1);
			}

		} catch (SQLException sqlEx) {
			ServiceLogger.log(logTag, sqlEx.toString());
		}

		return id;
	}

	/**
	 * Retrieve the primary key of the organization_user table for the specifyed company and user
	 * @param companyId the company id
	 * @param userId the user id
     * @return the id from the organization_user record if found, -1 otherwise
     */
	public Id getUserOrganizationId (int companyId, int userId)  {

		int id = -1;
		String query = "SELECT id FROM organization_user WHERE organization_id = " + companyId + " AND user_id = " + userId;
		ResultSet rs = DBConnector.executeQuery(query);
		try {
			if (rs.next()) {
				id = rs.getInt(1);
			}

		} catch (SQLException sqlEx) {
			ServiceLogger.log(logTag, sqlEx.toString());
		}

		return new Id.IdBuilder(id).build();
	}

	/**
	 * Add an administrator for a company
	 * @param companyId the company id
	 * @param userId the user id
	 * @param userEPPN user name of the requestor
	 * @return the id of the new organization_admin record
	 * @throws HTTPException
     */
	public Id addAdministrator (int companyId, int userId, String userEPPN) throws HTTPException {

		connection = DBConnector.connection();

		int organizationAdminId = -9999;
		int userIdEPPN = -1;
		try {

			connection.setAutoCommit(false);

			// Look up userId of userEPPN
			userIdEPPN = UserDao.getUserID(userEPPN);
			if (userIdEPPN == -1) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not a valid user");
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}

			// Check that the user adding the administrator is an administrator or the owner
			/**
			 ** Checks disabled as of 3/31/2016 until members for companies are tracked
			 **
			if (!(CompanyUserUtil.isOwnerOfCompany(companyId, userIdEPPN) || CompanyUserUtil.isAdminOfCompany(companyId, userIdEPPN))) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not authorized to add administrators for company " + companyId);
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
			 */

			// Check that the user being added as an administrator is a member of the company
			/**
			 ** Checks disabled as of 3/31/2016 until members for companies are tracked
			 **
			Id userOrganizationId =  this.getUserOrganizationId(companyId, userId);

			if (userOrganizationId == null || userOrganizationId.getId() == -1) {
				ServiceLogger.log(logTag, "User " + userId + " is not a member of company " + companyId);
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
			 */

			// Until checks are implemented, must ensure there is a record of user in OEGANIZATION_USER
			// so that foreign key on ORGANIZATION_ADMIN is satisifed
			Id userOrganizationId =  this.getUserOrganizationId(companyId, userId);
			if (userOrganizationId == null || userOrganizationId.getId() == -1) {
				userOrganizationId = addMember(companyId, userId, userEPPN);
			}

			connection.setAutoCommit(false);

			// Now add the user to the ORGANIZATION_ADMIN table
			Util util = Util.getInstance();

			String insertSQL = "INSERT INTO organization_admin (organization_user_id, organization_id)  VALUES (?,?)";
			PreparedStatement pstmt = DBConnector.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, userOrganizationId.getId());
			pstmt.setInt(2, companyId);
			pstmt.executeUpdate();
			organizationAdminId = util.getGeneratedKey(pstmt, "id");
			ServiceLogger.log(logTag, "ASSOCIATED ORGANIZATION_ADMIN ENTRY: " + organizationAdminId);

			connection.commit();

		}
		catch (SQLException sqlEx) {
			throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
		}
		finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
		}
		return new Id.IdBuilder(organizationAdminId).build();
	}


	/**
	 * Add a member for a company
	 * @param companyId the company id
	 * @param userId the user id
	 * @param userEPPN user name of the requestor
	 * @return the id of the new organization_user record
	 * @throws HTTPException
	 */
	public Id addMember (int companyId, int userId, String userEPPN) throws HTTPException {

		connection = DBConnector.connection();
		boolean resetAutoCommit = true;

		int organizationUserId = -9999;
		int userIdEPPN = -1;
		try {

			// check if we are already in a transaction
			if (!connection.getAutoCommit()) {
				resetAutoCommit = false;
			}
			connection.setAutoCommit(false);

			// Look up userId of userEPPN
			userIdEPPN = UserDao.getUserID(userEPPN);
			if (userIdEPPN == -1) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not a valid user");
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}

			/**
			 /**
			 ** Checks disabled as of 3/31/2016 until members for companies are tracked
			 **
			// Check that the user adding the administrator is an administrator or owner
			if (!(CompanyUserUtil.isOwnerOfCompany(companyId, userIdEPPN) || CompanyUserUtil.isAdminOfCompany(companyId, userIdEPPN))) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not authorized to add administrators for company " + companyId);
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}

			// Check that the user being added as a member is not already a member of the company
			if (CompanyUserUtil.isMemberOfCompany(companyId, userId)) {
				ServiceLogger.log(logTag, "User " + userId + " is already a member of comapny " + companyId);
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
			 */

			// Now add the user to the ORGANIZATION_USER table
			Util util = Util.getInstance();

			String insertSQL = "INSERT INTO organization_user (user_id, organization_id)  VALUES (?,?)";
			PreparedStatement pstmt = DBConnector.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, companyId);
			pstmt.executeUpdate();
			organizationUserId = util.getGeneratedKey(pstmt, "id");
			ServiceLogger.log(logTag, "ASSOCIATED ORGANIZATION_USER ENTRY: " + organizationUserId);

			connection.commit();;

		}
		catch (SQLException sqlEx) {
			ServiceLogger.log(logTag, "User " + userEPPN + " generated SQL exception");
			throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
		}
		finally {
			if (connection != null) {
				try {
					// only reset if we were not already inside a transaction
					if (resetAutoCommit) {
						connection.setAutoCommit(true);
					}
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
		}
		return new Id.IdBuilder(organizationUserId).build();
	}

	public List<Integer> getCompanyMemberIds(int companyId)  throws SQLException  {
		List<Integer> memberIds = null;

		String query = "SELECT user_id FROM organization_user WHERE organization_id = ?";
		try {
			PreparedStatement statement = DBConnector.prepareStatement(query);
			statement.setInt(1, companyId);
			ResultSet rs = statement.executeQuery();

			memberIds = new ArrayList<Integer>();

			while (rs.next()) {
				int user_id = rs.getInt(1);
				memberIds.add (new Integer(user_id));
			}

		} catch (SQLException sqlEx) {
			ServiceLogger.log(logTag, sqlEx.toString());
			throw sqlEx;
		}

		return memberIds;
	}

	public static String createINClause (List<Integer> ids) {

		// IN ( 1, 2, 3, 4 )
		StringBuffer sb = new StringBuffer();
		sb.append("IN (");
		if (ids != null ) {
			for (int i=0; i < ids.size(); i++) {
				if (i != 0) sb.append (",");
				sb.append(ids.get(i));
			}
		}
		sb.append(")");
		return sb.toString();
	}

	public List<User> getCompanyMembers (int companyId, String userEPPN) throws HTTPException {

		List<User> members = null;

		int userIdEPPN = -1;

		// Look up userId of userEPPN
		try {
			userIdEPPN = UserDao.getUserID(userEPPN);
		} catch (SQLException e) {
			if (userIdEPPN == -1) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not a valid user");
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
		}

		// Check that the user is a member of the company or a superadmin
		try {
			if (!isMemberOfCompany(companyId, userIdEPPN) && !PermissionEvaluationHelper.userHasRole(SecurityRoles.SUPERADMIN, 0)) {
				ServiceLogger.log(logTag, "User " + userIdEPPN + " is not a member of comapny " + companyId);
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
		} catch (SQLException e) {
			if (userIdEPPN == -1) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not a valid user");
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
		}

		try {
			List<Integer> memberIds = getCompanyMemberIds(companyId);
			members = new ArrayList<User>();

			//
			String userIdIN = createINClause(memberIds);
			String query = "select user_id, user_name, realname, accept_term_cond_time from users where user_id " + userIdIN;
			ServiceLogger.log(logTag, "getCompanyMembers query:" + query);
			ResultSet resultSet = DBConnector.executeQuery(query);

			if (resultSet != null) {
				while (resultSet.next()) {
					int userId = resultSet.getInt("user_id");
					String displayName = resultSet.getString("realname");
					String userName = resultSet.getString("user_name");
					Timestamp termsAndConditionsTimeStamp = resultSet.getTimestamp("accept_term_cond_time");
					boolean termsAndConditions = false;
					if(termsAndConditionsTimeStamp != null) {
						termsAndConditions = true;
					}

					User user = new User (userId, userName,displayName,  termsAndConditions, companyId);
					members.add(user);
				}
			}


		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new HTTPException(HttpStatus.FORBIDDEN.value());
		}

		return members;
	}

}