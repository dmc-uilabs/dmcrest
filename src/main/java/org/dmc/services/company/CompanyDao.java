package org.dmc.services.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

public class CompanyDao {

	private final String logTag = CompanyDao.class.getName();
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

                Company company = new Company.CompanyBuilder(id, accountId, name).build();
                companies.add(company);
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());  // ToDo: what error should this be?
        }
        return companies;
	}

    
	public Company getCompany(int id, String userEPPN) throws HTTPException { 
		
		try {
			if (!CompanyUserUtil.isDMDIIMember(userEPPN)) {
				ServiceLogger.log(logTag, "User Company/Organization is not DMDII Member");
				throw new HTTPException(HttpStatus.FORBIDDEN.value());
			}
			
			resultSet = DBConnector.executeQuery("SELECT * FROM organization o "
					+ "JOIN common_address a ON o.addressId = a.id "
					+ "JOIN common_image i ON o.feature_image = i.id "
					+ "WHERE o.organization_id = "  + id);
			
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
				String featureImageThumb = resultSet.getString("thumbnail");
				String featureImageLarge = resultSet.getString("large_image");
				String logoImage = resultSet.getString("logo_image");
				boolean follow = Boolean.valueOf(resultSet.getString("follow"));
				int favoratesCount = resultSet.getInt("favorates_count");
				boolean isOwner = Boolean.valueOf(resultSet.getString("is_owner"));
				String owner = resultSet.getString("owner");
				
				if (!owner.equals(userEPPN)) {
					ServiceLogger.log(logTag, "User is not owner of requested Company/Organization");
					throw new HTTPException(HttpStatus.FORBIDDEN.value());
				}
				
				return new 
					Company.CompanyBuilder(id, accountId, name)
					.location(location)
					.description(description)
					.division(division)
					.industry(industry)
					.NAICSCode(NAICSCode)
					.RDFocus(RDFocus)
					.customers(customers)
					.awardsReceived(awardsReceived)
					.technicalExpertise(technicalExpertise)
					.toolsSoftwareEquipmentMachines(toolsSoftwareEquipmentMachines)
					.postCollaborations(postCollaborations)
					.pastProjects(pastProjects)
					.upcomingProjectInterests(upcomingProjectInterests)
					.collaborationInterests(collaborationInterests)
					.address(address +  ", " + address2)
					.city(city)
					.state(state)
					.zipCode(zipCode)
					.twitter(twitter)
					.linkedIn(linkedIn)
					.website(website)
					.methodCommunication(methodCommunication)
					.email(email)
					.phone(phone)
					.categoryTier(categoryTier)
					.dateJoined(dateJoined)
					.reasonJoining(reasonJoining)
					.featureImage(new FeatureImage(featureImageThumb, featureImageLarge))
					.logoImage(logoImage)
					.follow(follow)
					.favoratesCount(favoratesCount)
					.isOwner(isOwner)
					.owner(owner)
					.build();
			}
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
	
	public Id createCompany(String jsonStr, String userEPPN) { 
		
		connection = DBConnector.connection();
		Util util = Util.getInstance();
		PreparedStatement statement;
		String query;
		long UnixTimeStamp = System.currentTimeMillis() / 1000L;
		Timestamp timestamp = new java.sql.Timestamp(UnixTimeStamp);
		Timestamp expires = new java.sql.Timestamp(UnixTimeStamp + (1000*60*60*24*365*10)); 
		int id = -99999, commonAddressId = -9999, commonImageId = -9999;
		// Default to Industry Tier 1 member for now
		int organizationMemberId = 1;

		try {
			String owner = userEPPN;

			JSONObject json = new JSONObject(jsonStr);
			int accountId = json.getInt("accountId");
			String name = json.getString("name");
			String location = json.getString("location");
			String description = json.getString("description");
			String division = json.getString("division");
			String industry = json.getString("industry");
			String NAICSCode = json.getString("NAICSCode");
			String RDFocus = json.getString("RDFocus");
			String customers = json.getString("customers");
			String awardsReceived = json.getString("awardsReceived");
			String technicalExpertise = json.getString("technicalExpertise");
			String toolsSoftwareEquipmentMachines = json.getString("toolsSoftwareEquipmentMachines");
			String postCollaborations = json.getString("postCollaborations");
			String collaborationInterests = json.getString("collaborationInterests");
			String pastProjects = json.getString("pastProjects");
			String upcomingProjectInterests = json.getString("upcomingProjectInterests");
			String address = json.getString("address");
			String city = json.getString("city");
			String state = json.getString("state");
			String zipCode = json.getString("zipCode");
			String twitter = json.getString("twitter");
			String linkedIn = json.getString("linkedIn");
			String website = json.getString("website");
			String methodCommunication = json.getString("methodCommunication");
			String email = json.getString("email");
			String phone = json.getString("phone");
			int categoryTier = json.getInt("categoryTier");
			String dateJoined = json.getString("dateJoined");
			String reasonJoining = json.getString("reasonJoining");
			String featureImageThumb = json.getString("featureImageThumb");
			String featureImageLarge = json.getString("featureImageLarge");
			String logoImage = json.getString("logoImage");
			boolean follow = json.getBoolean("follow");
			int favoratesCount = json.getInt("favoratesCount");
			boolean isOwner = json.getBoolean("isOwner");
			if (json.has("OrganizationMemberId")) {
				organizationMemberId = json.getInt("OrganiaztionMemberId");
			}

			// insert into
			connection.setAutoCommit(false);
			
			// insert into relational common_address
			query = "INSERT INTO common_address"
					+ "(street_address1, street_address2, city, state, zip) "
					+ "VALUES (?, ?, ?, ?, ?) ";

			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, address);
			statement.setString(2, "");
			statement.setString(3, city);
			statement.setString(4, state);
			statement.setString(5, zipCode);
			statement.executeUpdate();
			commonAddressId = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "ASSOCIATED COMMON_ADDRESS ENTRY: " + commonAddressId);

			// insert into relational common_image
			query = "INSERT INTO common_image"
					+ "(caption, thumbnail, large_image) "
					+ "VALUES (?, ?, ?) ";

			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, "");
			statement.setString(2, featureImageThumb);
			statement.setString(3, featureImageLarge);
			statement.executeUpdate();
			commonImageId = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "ASSOCIATED COMMON_IMAGE ENTRY: " + commonImageId);

			query = "INSERT INTO organization "
					+ "(accountid, name, location, description, division, "
					+ "industry, naics_code, rd_focus, customers, awards , "
					+ "tech_expertise, tools_software_equip_mach, post_collaboration, collaboration_interest, past_projects, "
					+ "upcoming_project_interests, addressid, email, phone, "
					+ "website, social_media_linkedin, social_media_twitter, "
					+ "perfered_comm_method, category_tier, date_joining, reason_joining, "
					+ "feature_image, logo_image, follow, favorates_count, is_owner, owner)"
					+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, accountId);
			statement.setString(2, name);
			statement.setString(3, location);
			statement.setString(4, description);
			statement.setString(5, division);
			statement.setString(6, industry);
			statement.setString(7, NAICSCode);
			statement.setString(8, RDFocus);
			statement.setString(9, customers);
			statement.setString(10, awardsReceived);
			statement.setString(11, technicalExpertise);
			statement.setString(12, toolsSoftwareEquipmentMachines);
			statement.setString(13, postCollaborations);
			statement.setString(14, collaborationInterests);
			statement.setString(15, pastProjects);
			statement.setString(16, upcomingProjectInterests);
			statement.setInt(17, commonAddressId);
			statement.setString(18, email);
			statement.setString(19, phone);
			statement.setString(20, website);
			statement.setString(21, linkedIn);
			statement.setString(22, twitter);
			statement.setString(23, methodCommunication);
			statement.setInt(24, categoryTier);
			statement.setString(25, dateJoined);
			statement.setString(26, reasonJoining);
			statement.setInt(27, commonImageId);
			statement.setString(28, logoImage);
			statement.setBoolean(29, follow);
			statement.setInt(30, favoratesCount);
			statement.setBoolean(31, isOwner);
			statement.setString(32, owner);
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
			
			connection.commit();
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
	
	public Id updateCompany(int id, /*Company company,*/ String jsonStr, String userEPPN) throws HTTPException {
		 
		connection = DBConnector.connection();
		int companyId = id, commonAddressId, commonImageId;

		try {
			
			// Ensure user is owner of company before patching
			resultSet = DBConnector.executeQuery("SELECT owner FROM organization "
					+ "WHERE organization_id = "  + id);
			
			if (resultSet.next()) {
				String owner = resultSet.getString("owner");
				if (!owner.equals(userEPPN)) {
					ServiceLogger.log(logTag, "User is not owner of Company/Organization");
					throw new HTTPException(HttpStatus.FORBIDDEN.value());
				}
			}
			
			connection.setAutoCommit(false);
			
			JSONObject json = new JSONObject(jsonStr);
	        int accountId = json.getInt("accountId");    
	        String name = json.getString("name");   
	        String location =  json.getString("location");
	        String description =  json.getString("description");
	        String division =  json.getString("division");
	        String industry =  json.getString("industry");
	        String NAICSCode =  json.getString("NAICSCode");
	        String RDFocus =  json.getString("RDFocus");
	        String customers =  json.getString("customers");
	        String awardsReceived =  json.getString("awardsReceived");
	        String technicalExpertise =  json.getString("technicalExpertise");
	        String toolsSoftwareEquipmentMachines =  json.getString("toolsSoftwareEquipmentMachines");
	        String postCollaborations =  json.getString("postCollaborations");
	        String collaborationInterests =  json.getString("collaborationInterests");
	        String pastProjects =  json.getString("pastProjects");
	        String upcomingProjectInterests =  json.getString("upcomingProjectInterests");
	        String address =  json.getString("address");
	        String city =  json.getString("city");
	        String state =  json.getString("state");
	        String zipCode =  json.getString("zipCode");
	        String twitter =  json.getString("twitter");
	        String linkedIn =  json.getString("linkedIn");
	        String website =  json.getString("website");
	        String methodCommunication =  json.getString("methodCommunication");
	        String email =  json.getString("email");
	        String phone =  json.getString("phone");
	        int categoryTier = json.getInt("categoryTier");
	        String dateJoined =  json.getString("dateJoined");
	        String reasonJoining =  json.getString("reasonJoining");
	        String featureImageThumb = json.getString("featureImageThumb");
	        String featureImageLarge = json.getString("featureImageLarge");
	        String logoImage =  json.getString("logoImage");
	        boolean follow = json.getBoolean("follow");
	        int favoratesCount = json.getInt("favoratesCount");
	        boolean isOwner = json.getBoolean("isOwner");
	        String owner =  json.getString("owner");
	        
	        // retrieve relational common_address and common_image
			resultSet = DBConnector.executeQuery("SELECT a.id common_address_id, i.id common_image_id "
					+ "FROM organization o "
					+ "JOIN common_address a ON o.addressId = a.id "
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
		        preparedStatement.setString(1, address);   
		        preparedStatement.setString(2, "");
		        preparedStatement.setString(3, city);
		        preparedStatement.setString(4, state);
		        preparedStatement.setString(5, zipCode);
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
		        preparedStatement.setString(2, featureImageThumb);
		        preparedStatement.setString(3, featureImageLarge);
		        preparedStatement.setInt(4, companyId);
		        preparedStatement.executeUpdate();
		        
		        // update the company
				query = "UPDATE organization SET "
				+ "accountid = ?, name = ?, location = ?, description = ?, division = ?, "
				+ "industry = ?, naics_code = ?, rd_focus = ?, customers = ?, awards = ?, "
			    + "tech_expertise = ?, tools_software_equip_mach = ?, post_collaboration = ?, "
			    + "collaboration_interest = ?, past_projects = ?, upcoming_project_interests = ?, "
				+ "addressid = ?, email = ?, phone = ?, "
	            + "website = ?, social_media_linkedin = ?, social_media_twitter = ?, "
				+ "perfered_comm_method = ?, category_tier = ?, date_joining = ?, reason_joining = ?, "
				+ "feature_image = ?, logo_image = ?, follow = ?, favorates_count = ?, is_owner = ?, owner = ? "
				+ "WHERE organization_id = ?";
				
				preparedStatement = DBConnector.prepareStatement(query);			      
				preparedStatement.setInt(1, accountId);    
		        preparedStatement.setString(2, name);   
		        preparedStatement.setString(3, location);
		        preparedStatement.setString(4, description);
		        preparedStatement.setString(5, division);
		        preparedStatement.setString(6, industry);
		        preparedStatement.setString(7, NAICSCode);
		        preparedStatement.setString(8, RDFocus);
		        preparedStatement.setString(9, customers);
		        preparedStatement.setString(10, awardsReceived);
		        preparedStatement.setString(11, technicalExpertise);
		        preparedStatement.setString(12, toolsSoftwareEquipmentMachines);
		        preparedStatement.setString(13, postCollaborations);
		        preparedStatement.setString(14, collaborationInterests);
		        preparedStatement.setString(15, pastProjects);
		        preparedStatement.setString(16, upcomingProjectInterests);
		        preparedStatement.setInt(17, commonAddressId);
		        preparedStatement.setString(18, email);
		        preparedStatement.setString(19, phone);
		        preparedStatement.setString(20, website);
		        preparedStatement.setString(21, linkedIn);
		        preparedStatement.setString(22, twitter);
		        preparedStatement.setString(23, methodCommunication);
		        preparedStatement.setInt(24, categoryTier);
		        preparedStatement.setString(25, dateJoined);
		        preparedStatement.setString(26, reasonJoining);
		        preparedStatement.setInt(27, commonImageId);
		        preparedStatement.setString(28, logoImage);
		        preparedStatement.setBoolean(29, follow);
		        preparedStatement.setInt(30, favoratesCount);
		        preparedStatement.setBoolean(31, isOwner);
		        preparedStatement.setString(32, owner);
		        preparedStatement.setInt(33, companyId);
		        preparedStatement.executeUpdate();
		        
		        connection.commit();
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
		
	
	public Id deleteCompany(int id) {
		
		connection = DBConnector.connection();
		PreparedStatement statement;
		String query;
	    int organizationId = id, commonAddressId, commonImageId;
	    
	    try {
	    	connection.setAutoCommit(false);
	    	
	        // retrieve relational common_address and common_image
			resultSet = DBConnector.executeQuery("SELECT a.id common_address_id, i.id common_image_id "
					+ "FROM organization o "
					+ "JOIN common_address a ON o.addressId = a.id "
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
		        
				// delete organization
		        query = "DELETE FROM organization WHERE organization_id = ?";
		        statement = DBConnector.prepareStatement(query);
		        statement.setInt(1, organizationId);   
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
			}
	    }
		catch (SQLException e) {
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction deleteCompany Rolled back");
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
		
		return new Id.IdBuilder(id)
		.build();
	}
	
    /**
	 * Retrieve the organization_id from the organization_user table for the specifyed company and user
	 * @param userId the user id
     * @return the organization_id from the organization_user record if found, -1 otherwise
     */
	public int getUserCompanyId(int userId)  {
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

}