package org.dmc.services;


import java.util.UUID;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before; 
import org.junit.After;
import org.junit.Ignore;

import static com.jayway.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CompanyIT extends BaseIT {
	
	private static final String COMPANY_GET_RESOURCE = "/companies/{id}";
	private static final String COMPANY_CREATE_RESOURCE = "/companies/create";
	private static final String COMPANY_UPDATE_RESOURCE = "/companies/{id}/update";
	private static final String COMPANY_DELETE_RESOURCE = "/companies/{id}/delete";
	
	private Integer createdId = null;
	String randomEPPN = UUID.randomUUID().toString();
		
	// Setup test data
	@Before
	public void testCompanyCreate() {
		JSONObject json = createFixture();
		this.createdId = given()
				.body(json.toString())
				.header("AJP_eppn", randomEPPN)
				.expect()
				.statusCode(200)
				.when()
				.post(COMPANY_CREATE_RESOURCE)
				.then()
				.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
				.extract()
				.path("id");
	}
	
	@Test
	public void testCompanyGet() {
		if (this.createdId != null) {
			given().
            header("Content-type", "application/json").
            header("AJP_eppn", randomEPPN).
            expect().statusCode(200).
            when().
            get(COMPANY_GET_RESOURCE, this.createdId.toString()).
    		then().
            body(matchesJsonSchemaInClasspath("Schemas/companySchema.json")).
            body("id", equalTo(this.createdId));	
		}
	}
	
	@Test
	public void testCompanyGetNotOwner() {
		if (this.createdId != null) {
			given().
            header("Content-type", "application/json").
            header("AJP_eppn", randomEPPN + "random").
            expect().statusCode(403).
            when().
            get(COMPANY_GET_RESOURCE, this.createdId.toString());
		}
	}
	
	@Test
	public void testCompanyUpdate() {
		JSONObject json = updateFixture();
		given()
		.body(json.toString())
		.expect()
		.statusCode(200)
		.when()
		.post(COMPANY_UPDATE_RESOURCE, this.createdId.toString())
		.then()
		.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}
	
	// Cleanup
	@After  
	public void testCompanyDelete() {
		expect().statusCode(200)
		.when()
		.get(COMPANY_DELETE_RESOURCE, this.createdId.toString())
		.then()
		.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}
	
	public JSONObject createFixture() {
		
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
		json.put("owner", "test owner");
        
		return json;
	}

	public JSONObject updateFixture() {
		
		JSONObject json = new JSONObject();

		json.put("accountId", 1001);
		json.put("name", "test updated name");
		json.put("location", "test updated location");
		json.put("description", "test updated description");
		json.put("division", "test updated division");
		json.put("industry", "test updated industry");
		json.put("NAICSCode", "test updated NAICSCode");
		json.put("RDFocus", "test updated RDFocus");
		json.put("customers", "test updated customers");
		json.put("awardsReceived", "test updated awardsReceived");
		json.put("technicalExpertise", "test updated technicalExpertise");
		json.put("toolsSoftwareEquipmentMachines", "test updated toolsSoftwareEquipmentMachines");
		json.put("postCollaborations", "test updated postCollaborations");
		json.put("collaborationInterests", "test updated collaborationInterests");
		json.put("pastProjects", "test updated pastProjects");
		json.put("upcomingProjectInterests", "test updated upcomingProjectInterests");
		json.put("address", "test updated address");
		json.put("city", "test updated city");
		json.put("state", "test updated state");
		json.put("zipCode", "test updated zipCode");
		json.put("twitter", "test updated twitter");
		json.put("linkedIn", "test updated linkedIn");
		json.put("website", "test updated website");
		json.put("methodCommunication", "test updated methodCommunication");
		json.put("email", "test updated email");
		json.put("phone", "test updated phone");
		json.put("categoryTier", 30);
		json.put("dateJoined", "test updated dateJoined");
		json.put("reasonJoining", "test updated reasonJoining");
		json.put("featureImageThumb", "feature_image_thumb.jpg");
		json.put("featureImageLarge", "feature_image_large.jpg");
		json.put("logoImage", "test updated logoImage");
		json.put("follow", true);
		json.put("favoratesCount", 1002);
		json.put("isOwner", false);
		json.put("owner", "test updated owner");
        
		return json;
	}
	
}