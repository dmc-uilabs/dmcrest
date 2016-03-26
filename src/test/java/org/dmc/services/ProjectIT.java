package org.dmc.services;

import java.util.Date;
import java.util.ArrayList;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.response.ValidatableResponse;

import org.json.JSONObject;
import org.json.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.ProjectCreateRequest;
import org.dmc.services.users.User;
import org.dmc.services.projects.Project;
import org.dmc.services.projects.ProjectJoinRequest;
import org.dmc.services.projects.PostProjectJoinRequest;

//@Ignore
public class ProjectIT extends BaseIT {

    private final String logTag = ProjectIT.class.getName();

    @Test
	public void testProject6() {
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects/6").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectSchema.json"));
	}
	
	@Test
	public void testProject5(){
		
		//TODO: need to update to another demo project
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects/6").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectSchema.json"));		
	}
	
	@Test
	public void testProjectList(){
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectListSchema.json"));
	}


	// leaving this as an example of how to work with parameters to URL
	// instead of json, but json is probably preferable
	@Test
	public void testProjectCreateParameter(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);
		given().
			header("AJP_eppn", userEPPN).
			param("projectname", "junitTestParam" + unique).
			param("unixname", "junitParam" + unique).
		expect().
			statusCode(200).
		when().
			post("/projects/createWithParameter").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}
	

	@Test
	public void testProjectCreateJsonString(){
		JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);

		json.put("projectname", "junit" + unique);
		json.put("unixname", "junit" + unique);
        ServiceLogger.log(logTag, "testProjectCreateJsonString: json = " + json.toString());
		given().
			header("AJP_eppn", userEPPN).
			body(json.toString()).
		expect().
			statusCode(200).
		when().
			post("/projects/oldcreate").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}

	// see as an example to configure the object https://github.com/jayway/rest-assured/wiki/Usage#serialization
	@Test
	public void testProjectCreateJsonObject()
	  throws IOException {
		//JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);
		//json.put("description", "junit testProjectCreateJsonObject " + unique);
		//json.put("name", "junitjson" + unique);

		ProjectCreateRequest json = new ProjectCreateRequest();
		json.setDescription("junit testProjectCreateJsonObject " + unique);
		json.setTitle("junitjson" + unique);

		ServiceLogger.log(logTag, "testProjectCreateJsonObject: json = " + json.toString());
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(json).
		expect().
			statusCode(200).
		when().
			post("/projects/create").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}

	@Test
	public void testProjectCreateFailOnDuplicate(){
		JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);

		json.put("projectname", "junitTestdup" + unique);
		json.put("unixname", "junitdup" + unique);
        ServiceLogger.log(logTag, "testProjectCreateFailOnDuplicate: json = " + json.toString());

		// first time should work
		given().
			header("Content-type", "text/plain").
			header("AJP_eppn", userEPPN).
			body(json.toString()).
		expect().
			statusCode(200).
		when().
			post("/projects/oldcreate").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));

        ServiceLogger.log(logTag, "testProjectCreateFailOnDuplicate: try to create again");
		// second time should fail, because unixname is a duplicate
		given().
			header("Content-type", "text/plain").
			header("AJP_eppn", userEPPN).
			body(json.toString()).
		expect().
			statusCode(200).
		when().
			post("/projects/oldcreate").
		then().
			log().all().
			body(matchesJsonSchemaInClasspath("Schemas/errorSchema.json"));
	}

	@Test
	public void testProjectCreateFailOnDuplicateJson()
	  throws IOException {
		//JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);

		//json.put("name", "junitTestJsondup" + unique);
		//json.put("description", "junitdup json testing " + unique);
		ProjectCreateRequest json = new ProjectCreateRequest();
		json.setDescription("junitdup json testing " + unique);
		json.setTitle("junitTestJsondup" + unique);

        ServiceLogger.log(logTag, "testProjectCreateFailOnDuplicateJson: json = " + json.toString());

		// first time should work
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(json).
		expect().
			statusCode(200).
		when().
			post("/projects/create").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));

        ServiceLogger.log(logTag, "testProjectCreateFailOnDuplicateJson: try to create again");
		// second time should fail, because unixname is a duplicate
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(json).
		expect().
			statusCode(200).
		when().
			post("/projects/create").
		then().
			log().all().
			body(matchesJsonSchemaInClasspath("Schemas/errorSchema.json"));
	}

	// example of @JsonProperty from http://www.baeldung.com/jackson-annotations
	@Test
	public void whenUsingJsonProperty_thenCorrect()
	  throws IOException {
		ProjectCreateRequest bean = new ProjectCreateRequest();
		bean.setTitle("projectname");
		bean.setDescription("project description");

	    String result = new ObjectMapper().writeValueAsString(bean);

	    assertThat(result, containsString("projectname"));
	    assertThat(result, containsString("project description"));

	    ProjectCreateRequest resultBean = new ObjectMapper().reader(ProjectCreateRequest.class)
	                                          .readValue(result);
	    assertEquals("projectname", resultBean.getTitle());
	}

	@Test
	public void testProjectTypeChecks(){
		assertEquals(1, Project.IsPublic("Public"));
		assertEquals(0, Project.IsPublic("PRIVATE"));
	}

    @Test
	public void testGetProject6Tags() {
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects/6/projects_tags").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectTagListSchema.json"));
	}
	
	@Test
	public void testProject6Members(){
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects/6/projects_members").
		then().
			log().all().
			body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));		
		
	}

	@Test
	public void testProjectJoinRequests(){
		ValidatableResponse response =  
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects_join_requests").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestListSchema.json"));
		
		ServiceLogger.log(logTag, "testProjectJoinRequests " + response.extract().asString());
		// based on data loaded in gforge.psql
		String expectedResponseAsString = "[{\"id\":\"6-102-102\",\"projectId\":\"6\",\"profileId\":\"102\"},{\"id\":\"6-111-102\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"6-111-111\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"4-111-111\",\"projectId\":\"4\",\"profileId\":\"111\"}]";
		assertTrue(expectedResponseAsString.equals(response.extract().asString()));

		JSONArray jsonArray = new JSONArray(response.extract().asString());
	    ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();

	    for (int i = 0; i < jsonArray.length(); i++) {
	    	JSONObject json = jsonArray.getJSONObject(i);
	    	ProjectJoinRequest item = new ProjectJoinRequest();
	    	item.setId(json.getString("id"));
	    	item.setProjectId(json.getString("projectId"));
	    	item.setProfileId(json.getString("profileId"));
	    	list.add(item);
	    }

		ProjectJoinRequest expected = new ProjectJoinRequest();
		expected.setId("6-102-102");
		expected.setProjectId("6");
		expected.setProfileId("102");
		assertTrue(list.contains(expected));
		expected.setId("6-111-102");
		expected.setProjectId("6");
		expected.setProfileId("111");
		assertTrue(list.contains(expected));
		expected.setId("6-111-111");
		expected.setProjectId("6");
		expected.setProfileId("111");
		assertTrue(list.contains(expected));
		expected.setId("4-111-111");
		expected.setProjectId("4");
		expected.setProfileId("111");
		assertTrue(list.contains(expected));
	}		

	@Test
	public void testProjectJoinRequestsWithParamList(){
		ValidatableResponse response =  
		given().
			header("AJP_eppn", userEPPN).
			param("projectId", "4,6").
			param("profileId", "145").
		expect().
			statusCode(200).
		when().
			get("/projects_join_requests").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestListSchema.json"));
		
		ServiceLogger.log(logTag, "testProjectJoinRequests " + response.extract().asString());
		// based on data loaded in gforge.psql
		String expectedResponseAsString = "[]";
		assertTrue(expectedResponseAsString.equals(response.extract().asString()));

		JSONArray jsonArray = new JSONArray(response.extract().asString());
		assertTrue(0 == jsonArray.length());
	}

	@Test
	public void testProjectJoinRequestsProject6(){
		ValidatableResponse response =  
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects/6/projects_join_requests").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestListSchema.json"));
		
		ServiceLogger.log(logTag, "testProjectJoinRequestsProject6" + response.extract().asString());
		// based on data loaded in gforge.psql
		String expectedResponseAsString = "[{\"id\":\"6-102-102\",\"projectId\":\"6\",\"profileId\":\"102\"},{\"id\":\"6-111-102\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"6-111-111\",\"projectId\":\"6\",\"profileId\":\"111\"}]";
		assertTrue(expectedResponseAsString.equals(response.extract().asString()));

		JSONArray jsonArray = new JSONArray(response.extract().asString());
	    ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();

	    for (int i = 0; i < jsonArray.length(); i++) {
	    	JSONObject json = jsonArray.getJSONObject(i);
	    	ProjectJoinRequest item = new ProjectJoinRequest();
	    	item.setId(json.getString("id"));
	    	item.setProjectId(json.getString("projectId"));
	    	item.setProfileId(json.getString("profileId"));
	    	list.add(item);
	    }

		ProjectJoinRequest expected = new ProjectJoinRequest();
		expected.setId("6-102-102");
		expected.setProjectId("6");
		expected.setProfileId("102");
		assertTrue(list.contains(expected));
		expected.setId("6-111-102");
		expected.setProjectId("6");
		expected.setProfileId("111");
		assertTrue(list.contains(expected));
		expected.setId("6-111-111");
		expected.setProjectId("6");
		expected.setProfileId("111");
		assertTrue(list.contains(expected));
		// this one should not appear because it is project 4
		expected.setId("4-111-111");
		expected.setProjectId("4");
		expected.setProfileId("111");
		assertFalse(list.contains(expected));
	}

	@Test
	public void testProjectJoinRequestsProfile111(){
		ValidatableResponse response =  
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/profiles/111/projects_join_requests").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestListSchema.json"));
		
		ServiceLogger.log(logTag, "testProjectJoinRequestsProfile111" + response.extract().asString());
		// based on data loaded in gforge.psql
		String expectedResponseAsString = "[{\"id\":\"6-111-102\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"6-111-111\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"4-111-111\",\"projectId\":\"4\",\"profileId\":\"111\"}]";
		assertTrue(expectedResponseAsString.equals(response.extract().asString()));

		JSONArray jsonArray = new JSONArray(response.extract().asString());
	    ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();

	    for (int i = 0; i < jsonArray.length(); i++) {
	    	JSONObject json = jsonArray.getJSONObject(i);
	    	ProjectJoinRequest item = new ProjectJoinRequest();
	    	item.setId(json.getString("id"));
	    	item.setProjectId(json.getString("projectId"));
	    	item.setProfileId(json.getString("profileId"));
	    	list.add(item);
	    }

		ProjectJoinRequest expected = new ProjectJoinRequest();
		expected.setId("6-102-102");
		expected.setProjectId("6");
		expected.setProfileId("102");
		// this one should not appear because it is profile 102
		assertFalse(list.contains(expected));
		expected.setId("6-111-102");
		expected.setProjectId("6");
		expected.setProfileId("111");
		assertTrue(list.contains(expected));
		expected.setId("6-111-111");
		expected.setProjectId("6");
		expected.setProfileId("111");
		assertTrue(list.contains(expected));
		expected.setId("4-111-111");
		expected.setProjectId("4");
		expected.setProfileId("111");
		assertTrue(list.contains(expected));
	}
	
	@Test
	public void testCreateAndDeleteProjectJoinRequests(){
		PostProjectJoinRequest json = new PostProjectJoinRequest();
		json.setProjectId("4");
		json.setProfileId("110");

		String id = 
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(json).
		expect().
			statusCode(200).
		when().
			post("/projects_join_requests").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestSchema.json")).
			extract().path("id");

		// forbidden
		String invalid_id = "12-44-102-483";
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(403).
		when().
			delete("/projects_join_requests/" + invalid_id);

		// unauthorized
		given().
			header("AJP_eppn", "testUser").
		expect().
			statusCode(401).
		when().
			delete("/projects_join_requests/" + id);

		// should be successful
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(204).
		when().
			delete("/projects_join_requests/" + id);

		// forbidden - because it is already deleted
		given().
			header("AJP_eppn", userEPPN).
			param("id", id).
		expect().
			statusCode(403).
		when().
			delete("/projects_join_requests/" + id);

	}

	
}
