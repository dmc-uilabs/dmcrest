package org.dmc.services;

import java.util.Date;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.ProjectCreateRequest;
import org.dmc.services.projects.Project;

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

}
