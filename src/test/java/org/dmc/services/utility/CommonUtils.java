package org.dmc.services.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dmc.services.company.Company;
import org.dmc.services.sharedattributes.FeatureImage;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

/**
 * Created by 200005921 on 6/9/2016.
 */
public class CommonUtils {

    private static final String COMPANY_CREATE_RESOURCE = "/companies/create";
    private static final String COMPANY_ADD_MEMBER_RESOURCE = "/companies/{id}/member/{userId}";

    public static int  createCompany (String ownerEPPN ) {
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

    public static String createCompanyFixture(String ownerName) {
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

    public static boolean addMemberToCompany (int userId, int companyId, String ownerEPPN) {

        given().
                header("Content-type", "application/json").
                header("AJP_eppn", ownerEPPN).
                expect().statusCode(200).
                when().
                post(COMPANY_ADD_MEMBER_RESOURCE, Integer.toString(companyId), Integer.toString(userId));

        return true;
    }

}
