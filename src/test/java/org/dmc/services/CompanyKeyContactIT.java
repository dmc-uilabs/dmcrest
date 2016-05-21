package org.dmc.services;

import org.dmc.services.company.CompanyDao;
import org.dmc.services.company.Company;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.*;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertTrue;


public class CompanyKeyContactIT  extends BaseIT {

    private static final String COMPANY_CREATE_RESOURCE = "/companies/create";
    private static final String COMPANY_ADD_ADMIN_RESOURCE = "/companies/{id}/admin/{userId}";
    private static final String COMPANY_ADD_MEMBER_RESOURCE = "/companies/{id}/member/{userId}";


    private static final String logTag = CompanyKeyContactIT.class.getName();

    private int ownerUserId = -1;
    private String ownerEPPN;

    private int memberUserId = -1;
    private String memberEPPN;
    
    private int adminMemberUserId = -1;
    private String adminMemberEPPN;

    private int nonmemberUserId = -1;
    private String nonmemberEPPN;
    
    private int contactId=-1;
    
    private CompanyDao companyDao = new CompanyDao();

    @Before
    public void setupDataAndCreateSkill () {

        ServiceLogger.log(logTag, "starting setupData");
        String unique = TestUserUtil.generateTime();
        
        // set up user as owner of company
        this.ownerEPPN = "userEPPN" + unique;
        String ownerGivenName = "userGivenName" + unique;
        String ownerSurName= "userSurName" + unique;
        String ownerDisplayName = "userDisplayName" + unique;
        String ownerEmail = "userEmail" + unique;

        this.ownerUserId = createUser(this.ownerEPPN,ownerGivenName, ownerSurName, ownerDisplayName, ownerEmail);
        System.out.println("User created: " + this.ownerEPPN);

        // add company
        this.companyId =  createCompany(this.ownerEPPN);
        System.out.println("Company created: " + this.companyId);
        
        // Create a member user
        String unique2 = TestUserUtil.generateTime();
        this.memberEPPN = "userEPPN" + unique2;
        String memberGivenName = "userGivenName" + unique2;
        String memberSurName= "userSurName" + unique2;
        String memberDisplayName = "userDisplayName" + unique2;
        String memberEmail = "userEmail" + unique2;

        this.memberUserId = createUser(this.memberEPPN,memberGivenName, memberSurName, memberDisplayName, memberEmail);

        this.companyDao.addMember(this.companyId, this.memberUserId, this.ownerEPPN);
        System.out.println("Member user created: " + this.ownerEPPN);
        
        // Create an admin user
        String unique4 = TestUserUtil.generateTime();
        this.adminMemberEPPN = "userEPPN" + unique4;
        String member4GivenName = "userGivenName" + unique4;
        String member4SurName= "userSurName" + unique4;
        String member4DisplayName = "userDisplayName" + unique4;
        String member4Email = "userEmail" + unique4;
        this.adminMemberUserId = createUser(this.adminMemberEPPN,member4GivenName, member4SurName, member4DisplayName, member4Email);
        this.companyDao.addMember(this.companyId, this.adminMemberUserId, this.ownerEPPN);
        this.companyDao.addAdministrator(this.companyId, this.adminMemberUserId, this.ownerEPPN);
        
        //Make sure the company is a DMDII member
        addToDMDII(this.companyId);
        System.out.println("Org admin created: " + this.companyId);

        // Create a user that is not a member of the company
        String unique3 = TestUserUtil.generateTime();
        this.nonmemberEPPN = "userEPPN" + unique3;
        String member3GivenName = "userGivenName" + unique3;
        String member3SurName= "userSurName" + unique3;
        String member3DisplayName = "userDisplayName" + unique3;
        String member3Email = "userEmail" + unique3;

        this.nonmemberUserId = createUser(this.nonmemberEPPN,member3GivenName, member3SurName, member3DisplayName, member3Email);
        System.out.println("Nonmember user created: " + this.nonmemberUserId); 
        
        // After all this create a skill
        JSONObject json = createFixture("1","email1@test.com");
        // Now create a skill for testing
        contactId=
       		    given()
        	         	.header("Content-type", "application/json")
        	         	.header("AJP_eppn", this.adminMemberEPPN)
        	         	.body(json.toString())
        			.expect()
        	         	.statusCode(200)
        			.when()
        	         	.post("/company_key_contacts")
        	       .then()
        	         	.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
        	         .extract()
        	         	.path("id");
        ServiceLogger.log(logTag, "Created contact with id: " + contactId);
    }
    
    @Test
    public void testGetContact()
    {
    	given().
    		header("AJP_eppn",this.memberEPPN).
    	expect().
    		statusCode(200).
    	when().
    		get("/companies/"+this.companyId+"/company_key_contacts");
    }

    @Test
    public void testGetKeyContactNonMember()
    {
    	given().
    		header("AJP_eppn",this.nonmemberEPPN).
    	expect().
    		statusCode(500).
    	when().
    		get("/companies/"+this.companyId+"/company_key_contacts");
    }

    @Test
    public void testPostKeyContactNonAdminMember()
    {
        JSONObject json = createFixture("2","email2@test.com");
        // Now create a skill for testing
       		    given()
        	         	.header("Content-type", "application/json")
        	         	.header("AJP_eppn", this.nonmemberEPPN)
        	         	.body(json.toString())
        			.expect()
        	         	.statusCode(400)
        			.when()
        	         	.post("/company_key_contacts").getStatusCode();
    }
    
    @Test
    public void testPatchKeyContactNonAdminMember()
    {
        JSONObject json = createFixture(new Integer(contactId).toString(),"email3@test.com");
        // Now create a skill for testing
       		    given()
        	         	.header("Content-type", "application/json")
        	         	.header("AJP_eppn", this.adminMemberEPPN)
        	         	.body(json.toString())
        			.expect()
        	         	.statusCode(200)
        			.when()
        	         	.patch("/company_key_contacts/"+new Integer(contactId).toString()).getStatusCode();
    }
    
    @After
    public void testDeleteKeyContact()
    {
    	given().
			header("AJP_eppn",this.adminMemberEPPN).
		expect().
			statusCode(200).
		when().
			delete("/company_key_contacts/"+contactId);
    }
    


    public static int createUser(String userEPPN, String userGivenName, String userSurName, String userDisplayName, String userEmail){

        Integer id =
                given().
                        header("Content-type", "text/plain").
                        header("AJP_eppn", userEPPN).
                        header("AJP_givenName", userGivenName).
                        header("AJP_sn", userSurName).
                        header("AJP_displayName", userDisplayName).
                        header("AJP_mail", userEmail).
                        expect().
                        statusCode(200).
                        when().
                        post("/users/create").
                        then().
                        body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
                        extract().path("id");

        // check return value > 0
        assertTrue("Added user: " + "userEPPN" + userEPPN + " Valid user ID must be greater then zero.  Value is " + id + ".", id > 0);
        // could also check email syntax

        return id.intValue();
    }

    private int companyId = -1;

    public int  createCompany (String ownerEPPN ) {
        String json = createCompanyFixture(ownerEPPN);
        int companyId = given()
                .body(json)
                .header("AJP_eppn", ownerEPPN)
				.header("Content-type", "application/json")
                .expect()
                .statusCode(200)
                .when()
                .post(COMPANY_CREATE_RESOURCE)
                .then()
                .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract()
                .path("id");

        return companyId;
    }

    public String createCompanyFixture(String ownerName) {
		Company company = new Company();
		
		//		company.setId(Integer.toString(id));
		company.setAccountId(Integer.toString(1001));
		company.setName("test name");
		company.setLocation("test location");
		company.setDescription("test description");
		company.setDivision("test division");
		company.setIndustry("test industry");
		company.setNAICSCode("test NAICSCode");
		company.setRDFocus("test RDFocus");
		company.setCustomers("test customers");
		company.setAwardsReceived("test awardsReceived");
		company.setTechnicalExpertise("test technicalExpertise");
		company.setToolsSoftwareEquipmentMachines("test toolsSoftwareEquipmentMachines");
		company.setPastCollaborations("test postCollaborations");
		company.setPastProjects("test pastProjects");
		company.setUpcomingProjectInterests("test upcomingProjectInterests");
		company.setCollaborationInterests("test collaborationInterests");
		company.setAddress("test address , test address");
		company.setCity("test city");
		
		// Todo:				company.setState(state);
		// 		json.put("state", "test state");
		
		company.setZipCode("test zipCode");
		company.setTwitter("test twitter");
		company.setLinkedIn("test linkedIn");
		company.setWebsite("test website");
		company.setMethodCommunication("test methodCommunication");
		company.setEmail("test email");
		company.setPhone("test phone");
		company.setCategoryTier(30);
		company.setDateJoined("test dateJoined");
		company.setReasonJoining("test reasonJoining");
		company.setFeatureImage(new FeatureImage("feature_image_thumb.jpg", "feature_image_large.jpg"));
		company.setLogoImage("test logoImage");
		company.setFollow(true);
		company.setFavoritesCount(1002);
		company.setIsOwner(false);
		company.setOwner(ownerName);
		
		ObjectMapper mapper = new ObjectMapper();
		String companyJSONString = null;
		try {
			companyJSONString = mapper.writeValueAsString(company);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return companyJSONString;
    }

        
    public void addToDMDII(int companyID)  {
    	String update = "insert into organization_dmdii_member " +
    			"(organization_id,dmdii_type_id,modification_date,start_date,expire_date) values "+
    			" (?,1,now(),now(),date(now())+integer'1')";
    	try {
		PreparedStatement preparedStatementOrg = DBConnector.prepareStatement(update);
		preparedStatementOrg.setInt(1, companyID);
		int orgSet = preparedStatementOrg.executeUpdate();
    	}
    	catch (SQLException e)
    	{
			ServiceLogger.log(this.logTag, "SQL Error occurs: "+e.getMessage());
    	}
    }
    

    private JSONObject createFixture(String id, String email)
    {
    	JSONObject json = new JSONObject();
    	json.put("id", "1");
    	json.put("companyId", this.companyId);
    	json.put("email", email);
    	json.put("name", "Testuser");
    	json.put("phoneNumber", "1234567");
    	json.put("title", "Director");
    	json.put("type", 1);
    	
    	return json;
    }
}
