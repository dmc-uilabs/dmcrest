package org.dmc.services.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.json.JSONException;
import org.json.JSONObject;

public class CompanyDao {

	private final String logTag = CompanyDao.class.getName();
	private ResultSet resultSet;

	public Company getCompany(int id) {     
		try {
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
	
	public Id createCompany(String jsonStr) { 
		int id = -99999, commonAddressId = -9999, commonImageId = -9999;

		try {
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

	         
	        // insert into relational common_address
	        String query = "INSERT INTO common_address"
	        		+ "(street_address1, street_address2, city, state, zip) "
	        		+ "VALUES (?, ?, ?, ?, ?) ";
	        
	        PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
	        preparedStatement.setString(1, address);   
	        preparedStatement.setString(2, "");
	        preparedStatement.setString(3, city);
	        preparedStatement.setString(4, state);
	        preparedStatement.setString(5, zipCode);
	        preparedStatement.executeUpdate();
	        
			query = "SELECT currval('common_address_id_seq') as id";
			resultSet = DBConnector.executeQuery(query);
			while (resultSet.next()) {
				commonAddressId = resultSet.getInt("id");
			}
			
			ServiceLogger.log(logTag, "ASSOCIATED COMMON_ADDRESS ENTRY: " + commonAddressId);

	        // insert into relational common_image
	        query = "INSERT INTO common_image"
	        		+ "(caption, thumbnail, large_image) "
	        		+ "VALUES (?, ?, ?) ";
	         
	        preparedStatement = DBConnector.prepareStatement(query);
	        preparedStatement.setString(1, "");   
	        preparedStatement.setString(2, featureImageThumb);
	        preparedStatement.setString(3, featureImageLarge);
	        preparedStatement.executeUpdate();
	        
			query = "SELECT currval('common_image_id_seq') as id";
			resultSet = DBConnector.executeQuery(query);
			while (resultSet.next()) {
				commonImageId = resultSet.getInt("id");
			}
	        			
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
	        preparedStatement.executeUpdate();
	        
			query = "SELECT currval('organization_organization_id_seq') as id";
			resultSet = DBConnector.executeQuery(query);
			while (resultSet.next()) {
				id = resultSet.getInt("id");
			}
			
			ServiceLogger.log(logTag, "ORGANIZATION/COMPANY ID: " + id);
			
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		return new Id.IdBuilder(id)
		.build();

	}
	
	public Id updateCompany(int id, String jsonStr) {
		 
		int companyId = id, commonAddressId, commonImageId;

		try {
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
			}
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		return new Id.IdBuilder(companyId)
		.build();
	}
		
	
	public Id deleteCompany(int id) {
		
	    int organizationId = id, commonAddressId, commonImageId;
	    
	    try {
	        // retrieve relational common_address and common_image
			resultSet = DBConnector.executeQuery("SELECT a.id common_address_id, i.id common_image_id "
					+ "FROM organization o "
					+ "JOIN common_address a ON o.addressId = a.id "
					+ "JOIN common_image i ON o.feature_image = i.id "
					+ "WHERE o.organization_id = "  + id);
			
			if (resultSet.next()) {
				commonAddressId = resultSet.getInt("common_address_id");
				commonImageId = resultSet.getInt("common_image_id");
				
				// delete organization
		        String query = "DELETE FROM organization WHERE organization_id = ?";
		        PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
		        preparedStatement.setInt(1, organizationId);   
		        preparedStatement.executeUpdate();
		        
				// delete common_address
		        query = "DELETE FROM common_address WHERE id = ?";
		        preparedStatement = DBConnector.prepareStatement(query);
		        preparedStatement.setInt(1, commonAddressId);   
		        preparedStatement.executeUpdate();
		        
				// delete common_image
		        query = "DELETE FROM common_image WHERE id = ?";
		        preparedStatement = DBConnector.prepareStatement(query);
		        preparedStatement.setInt(1, commonImageId);   
		        preparedStatement.executeUpdate();
			}
	    }
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		
		return new Id.IdBuilder(id)
		.build();
	}
	
}