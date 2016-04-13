package org.dmc.services;

import org.dmc.services.company.CompanyDao;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.*;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;


public class CompanySkillIT  extends BaseIT {

    private static final String COMPANY_CREATE_RESOURCE = "/companies/create";
    private static final String COMPANY_ADD_ADMIN_RESOURCE = "/companies/{id}/admin/{userId}";
    private static final String COMPANY_ADD_MEMBER_RESOURCE = "/companies/{id}/member/{userId}";


    private static final String logTag = CompanySkillIT.class.getName();

    private int ownerUserId = -1;
    private String ownerEPPN;

    private int memberUserId = -1;
    private String memberEPPN;
    
    private int adminMemberUserId = -1;
    private String adminMemberEPPN;

    private int nonmemberUserId = -1;
    private String nonmemberEPPN;
    
    private int skillId=-1;
    
    private CompanyDao companyDao = new CompanyDao();

    @Before
    public void setupDataAndCreateSkill () {

        ServiceLogger.log(logTag, "starting setupData");
        String unique = TestUserUtil.generateTime();

        // set up org1: non dmdii
        // set up org2: dmdii
        // set up user1: org1
        // set up user2: org2, regular
        // set up user3: org2, admin
        
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
        JSONObject json = createFixture("create");
        // Now create a skill for testing
        skillId=
       		    given()
        	         	.header("Content-type", "application/json")
        	         	.header("AJP_eppn", this.adminMemberEPPN)
        	         	.body(json.toString())
        			.expect()
        	         	.statusCode(200)
        			.when()
        	         	.post("/company_skills")
        	       .then()
        	         	.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
        	         .extract()
        	         	.path("id");
        ServiceLogger.log(logTag, "Created skill with id: " + skillId);
    }
    
    //@Test
    @Test
    public void testGetSkill()
    {
    	given().
    		header("AJP_eppn",this.memberEPPN).
    	expect().
    		statusCode(200).
    	when().
    		get("/companies/"+this.companyId+"/company_skills");
    }
    @Test
    public void testGetSkillNonMember()
    {
    	given().
    		header("AJP_eppn",this.nonmemberEPPN).
    	expect().
    		statusCode(500).
    	when().
    		get("/companies/"+this.companyId+"/company_skills");
    }
    @Test
    public void testPostSkillNonAdminMember()
    {
        JSONObject json = createFixture("create");
        // Now create a skill for testing
       		    given()
        	         	.header("Content-type", "application/json")
        	         	.header("AJP_eppn", this.nonmemberEPPN)
        	         	.body(json.toString())
        			.expect()
        	         	.statusCode(400)
        			.when()
        	         	.post("/company_skills").getStatusCode();
    }
    @After
    public void testDeleteSkillAndDelete()
    {
    	given().
			header("AJP_eppn",this.adminMemberEPPN).
		expect().
			statusCode(200).
		when().
			delete("/company_skills/"+skillId);
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
        JSONObject json = createCompanyFixture(ownerEPPN);
        int companyId = given()
                .body(json.toString())
                .header("AJP_eppn", ownerEPPN)
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

    public JSONObject createCompanyFixture(String ownerName) {

        JSONObject json = new JSONObject();

        json.put("accountId", 1001);
        json.put("name", "test name");
        json.put("location", "test location");
        json.put("description", "test description");
        json.put("division", "test division");
        json.put("industry", "test industry");
        json.put("NAICSCode", "test NAICSCode");
        json.put("RDFocus", "test RDFocus");
        json.put("customers", "test customers");
        json.put("awardsReceived", "test awardsReceived");
        json.put("technicalExpertise", "test technicalExpertise");
        json.put("toolsSoftwareEquipmentMachines", "test toolsSoftwareEquipmentMachines");
        json.put("postCollaborations", "test postCollaborations");
        json.put("collaborationInterests", "test collaborationInterests");
        json.put("pastProjects", "test pastProjects");
        json.put("upcomingProjectInterests", "test upcomingProjectInterests");
        json.put("address", "test address");
        json.put("city", "test city");
        json.put("state", "test state");
        json.put("zipCode", "test zipCode");
        json.put("twitter", "test twitter");
        json.put("linkedIn", "test linkedIn");
        json.put("website", "test website");
        json.put("methodCommunication", "test methodCommunication");
        json.put("email", "test email");
        json.put("phone", "test phone");
        json.put("categoryTier", 30);
        json.put("dateJoined", "test dateJoined");
        json.put("reasonJoining", "test reasonJoining");
        json.put("featureImageThumb", "feature_image_thumb.jpg");
        json.put("featureImageLarge", "feature_image_large.jpg");
        json.put("logoImage", "test logoImage");
        json.put("follow", true);
        json.put("favoratesCount", 1002);
        json.put("isOwner", false);
        json.put("owner", ownerName);

        return json;
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
    

    private JSONObject createFixture(String s)
    {
    	JSONObject json = new JSONObject();
    	json.put("id", "1");
    	json.put("companyId", this.companyId);
    	json.put("name", "Test skill from Unit test");
    	return json;
    }
}
