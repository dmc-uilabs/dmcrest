package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyDao {

	private final String logTag = CompanyDao.class.getName();
	private ResultSet resultSet;

	public Company getCompany(int id) {     
         int accountId;    
         String name;   
         String location;
         String description;
         String division;
         String industry;
         String NAICSCode;
         String RDFocus;
         String customers;
         String awardsReceived;
         String technicalExpertise;
         String toolsSoftwareEquipmentMachines;
         String postCollaborations;
         String collaborationInterests;
         String pastProjects;
         String upcomingProjectInterests;
         String address;
         String address2;
         String city;
         String state;
         String zipCode;
         String twitter;
         String linkedIn;
         String website;
         String methodCommunication;
         String email;
         String phone;
         int categoryTier;
         String dateJoined;
         String reasonJoining;
         String featureImageThumb;
         String featureImageLarge;
         String logoImage;
         boolean follow;
         int favoratesCount;
         boolean isOwner;
         String owner;
        
		try {
			resultSet = DBConnector.executeQuery("SELECT * FROM organization o "
					+ "JOIN common_address a ON o.addressId = a.id "
					+ "JOIN common_image i ON o.feature_image = i.id "
					+ "WHERE o.organization_id = "  + id);
			
			if (resultSet.next()) {
				id = resultSet.getInt("id");
				accountId = resultSet.getInt("accountId");
				name = resultSet.getString("name");
				location = resultSet.getString("location");
				description = resultSet.getString("description");
				division = resultSet.getString("division");
				industry = resultSet.getString("industry");
				NAICSCode = resultSet.getString("naics_code");
				RDFocus = resultSet.getString("rd_focus");
				customers = resultSet.getString("customers");
				awardsReceived = resultSet.getString("awards");
				technicalExpertise = resultSet.getString("tech_expertise");
				toolsSoftwareEquipmentMachines = resultSet.getString("tools_software_equip_mach");
				postCollaborations = resultSet.getString("post_collaboration");
				collaborationInterests = resultSet.getString("collaboration_interest");
				upcomingProjectInterests = resultSet.getString("upcoming_project_interests");
				address = resultSet.getString("street_address1");
				address2 = resultSet.getString("street_address2");
				city = resultSet.getString("city");
				state = resultSet.getString("state");
				zipCode = resultSet.getString("zip");
				twitter = resultSet.getString("social_media_twitter");
				linkedIn = resultSet.getString("social_media_linkedin");
				website = resultSet.getString("website");
				methodCommunication = resultSet.getString("perfered_comm_method");
				email = resultSet.getString("email");
				phone = resultSet.getString("phone");
				categoryTier = resultSet.getInt("category_tier");
				dateJoined = resultSet.getString("date_joining");
				reasonJoining = resultSet.getString("reason_joining");
				featureImageThumb = resultSet.getString("thumbnail");
				featureImageLarge = resultSet.getString("large_image");
				logoImage = resultSet.getString("logo_image");
				follow = Boolean.valueOf(resultSet.getString("follow"));
				favoratesCount = resultSet.getInt("favorates_count");
				isOwner = Boolean.valueOf(resultSet.getString("is_owner"));
				owner = resultSet.getString("owner");
				
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
	
}