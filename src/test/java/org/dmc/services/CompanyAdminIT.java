package org.dmc.services;

import org.dmc.services.company.CompanyDao;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.*;
import org.junit.Test;


import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

/**
 * Created by 200005921 on 3/30/2016.
 */
public class CompanyAdminIT  extends BaseIT {

    private static final String COMPANY_CREATE_RESOURCE = "/companies/create";
    private static final String COMPANY_ADD_ADMIN_RESOURCE = "/companies/{id}/admin/{userId}";
    private static final String COMPANY_ADD_MEMBER_RESOURCE = "/companies/{id}/member/{userId}";


    private static final String logTag = CompanyAdminIT.class.getName();

    private int ownerUserId = -1;
    private String ownerEPPN;

    private int memberUserId = -1;
    private String memberEPPN;

    private int nonmemberUserId = -1;
    private String nonmemberEPPN;

    private CompanyDao companyDao = new CompanyDao();

    @Before
    public void setupData () {

        ServiceLogger.log(logTag, "starting setupData");
        String unique = TestUserUtil.generateTime();

        // set up user as owner of company
        ownerEPPN = "userEPPN" + unique;
        String ownerGivenName = "userGivenName" + unique;
        String ownerSurName= "userSurName" + unique;
        String ownerDisplayName = "userDisplayName" + unique;
        String ownerEmail = "userEmail" + unique;

        ownerUserId = createUser(ownerEPPN,ownerGivenName, ownerSurName, ownerDisplayName, ownerEmail);

        // add company
        companyId =  createCompany(ownerEPPN);

        // Create a member user
        String unique2 = TestUserUtil.generateTime();
        memberEPPN = "userEPPN" + unique2;
        String memberGivenName = "userGivenName" + unique2;
        String memberSurName= "userSurName" + unique2;
        String memberDisplayName = "userDisplayName" + unique2;
        String memberEmail = "userEmail" + unique2;

        memberUserId = createUser(memberEPPN,memberGivenName, memberSurName, memberDisplayName, memberEmail);

        companyDao.addMember(companyId, memberUserId, ownerEPPN);

        // Create a user that is not a member of the company
        String unique3 = TestUserUtil.generateTime();
        nonmemberEPPN = "userEPPN" + unique3;
        String member3GivenName = "userGivenName" + unique3;
        String member3SurName= "userSurName" + unique3;
        String member3DisplayName = "userDisplayName" + unique3;
        String member3Email = "userEmail" + unique3;

        nonmemberUserId = createUser(nonmemberEPPN,member3GivenName, member3SurName, member3DisplayName, member3Email);
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

    @Test
    public void testAddAdminSuccess () {

        // Add to ORGANIZATION_ADMIN table
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", ownerEPPN).
                expect().statusCode(200).
                when().
                post(COMPANY_ADD_ADMIN_RESOURCE, Integer.toString(companyId), Integer.toString(memberUserId));

    }

    // This test ignored until since Checks for admins/members are disabled as of 3/31/2016 until members for companies are tracked
    @Ignore
    @Test
    public void testAddAdminNonMember () {

        // Add to ORGANIZATION_ADMIN table
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", ownerEPPN).
                expect().statusCode(401).  // UNAUTHORIZED
                when().
                post(COMPANY_ADD_ADMIN_RESOURCE, Integer.toString(companyId), Integer.toString(nonmemberUserId));

    }

    @Test
    public void testAddMemberSuccess () {

        // Create a member user
        String unique2 = TestUserUtil.generateTime();
        String memberEPPN = "userEPPN" + unique2;
        String memberGivenName = "userGivenName" + unique2;
        String memberSurName= "userSurName" + unique2;
        String memberDisplayName = "userDisplayName" + unique2;
        String memberEmail = "userEmail" + unique2;

        int memberUserId = createUser(memberEPPN,memberGivenName, memberSurName, memberDisplayName, memberEmail);

        given().
                header("Content-type", "application/json").
                header("AJP_eppn", ownerEPPN).
                expect().statusCode(200).
                when().
                post(COMPANY_ADD_MEMBER_RESOURCE, Integer.toString(companyId), Integer.toString(memberUserId));

    }

    // This test ignored until since Checks for admins/members are disabled as of 3/31/2016 until members for companies are tracked
    @Ignore
    @Test
    public void testAddMemberFailNonAdmin () {

        // Create a member user
        String unique2 = TestUserUtil.generateTime();
        String memberEPPN = "userEPPN" + unique2;
        String memberGivenName = "userGivenName" + unique2;
        String memberSurName= "userSurName" + unique2;
        String memberDisplayName = "userDisplayName" + unique2;
        String memberEmail = "userEmail" + unique2;

        int memberUserId = createUser(memberEPPN,memberGivenName, memberSurName, memberDisplayName, memberEmail);

        given().
                header("Content-type", "application/json").
                header("AJP_eppn", nonmemberEPPN).
                expect().statusCode(401).  // UNAUTHORIZED
                when().
                post(COMPANY_ADD_MEMBER_RESOURCE, Integer.toString(companyId), Integer.toString(memberUserId));

    }

}
