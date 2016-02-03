package org.dmc.services;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.*;
import org.springframework.test.*;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before; 
import org.junit.Ignore;

import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.RestAssured;

import static org.hamcrest.Matchers.*;

import java.util.Random;
import org.junit.Ignore;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.dmc.services.company.CompanyDao;
import org.dmc.services.company.Company;

public class CompanyIT extends BaseIT {
	
	private static final String COMPANY_GET_RESOURCE = "/companies/{id}";
	private static final String COMPANY_CREATE_RESOURCE = "/companies/create";
		
	@Ignore
	@Test
	public void testTaskCreateAndGet() {
		JSONObject json = createFixture();
		Integer id = given()
				.body(json.toString())
				.expect()
				.statusCode(200)
				.when()
				.post(COMPANY_CREATE_RESOURCE)
				.then()
				.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
				.extract()
				.path("id");

		expect().statusCode(200)
				.when()
				.get(COMPANY_GET_RESOURCE, id.toString())
				.then().log()
				.all()
				.body(matchesJsonSchemaInClasspath("Schemas/taskSchema.json"));
	}
	
	@Test
	public void testCompanyCreate() {
		
		JSONObject json = createFixture();
		
		given()
		.body(json.toString())
		.expect()
		.statusCode(200)
		.when()
		.post("/companies/create")
		.then()
		.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));

	}
	
	public JSONObject createFixture() {
		
		JSONObject json = new JSONObject();
		Random r = new Random(); // use to randomize data params

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
}