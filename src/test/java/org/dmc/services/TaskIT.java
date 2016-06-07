package org.dmc.services;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

//@Ignore
public class TaskIT extends BaseIT {
	
	private String taskId ="1";

	@Test
	public void testTaskCreateAndGet() {
      JSONObject json = createTaskJsonSample("testTaskCreateAndGet");
	  Integer id = 
	    given()
			.header("Content-type", "application/json")
			.body(json.toString())
			.expect()
			.statusCode(200)
		.when()
			.post("/tasks/create")
		.then()
			.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
		.extract().path("id");
	  
		String newGetRequest = "/tasks/" + id.toString();
	
		// let's query the newly created task and make sure we get it
		given().
			header("Content-type", "application/json").
		expect().
			statusCode(200).
	    when().
			get(newGetRequest).
		then().
			log().all().body(matchesJsonSchemaInClasspath("Schemas/taskSchema.json"));
	}
	
	// WARNING: this test is ok as long as our test db has task with id = 1
	@Test
	public void testTask1(){
		given().
			header("Content-type", "application/json").
		expect().
			statusCode(200).
		when().
			get("/tasks/1").
		then().
			log().all().body(matchesJsonSchemaInClasspath("Schemas/taskSchema.json"));
	}
	
	@Test
	public void testTaskList(){
		expect().statusCode(200).when().get("/tasks").then().
        body(matchesJsonSchemaInClasspath("Schemas/taskListSchema.json"));
	}
	
	@Test
	public void testTaskCreate(){
		
	    JSONObject json = createTaskJsonSample("testTaskCreate");
		
		given()
			.header("Content-type", "application/json")
			.body(json.toString())
			.expect()
			.statusCode(200)
			.when()
			.post("/tasks/create")
			.then()
				.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
		
	}
	
	private JSONObject createTaskJsonSample(String testDescription)
	{
	    JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);
				
        json.put("title", "sample for test " + testDescription + " from junit on " + unique);
		json.put("description", "description for sample test  " + testDescription + " from junit on " + unique);
		json.put("priority", 0);
        json.put("dueDate", 0);      // a user ID in users table 
	    json.put("reporter", "bamboo tester");  // another user ID in users table
		json.put("assignee", 103);    // from group table
		json.put("projectId", 1);     // from group table and project_group_list, 1 is available in both
		return json;
	}
	
	
	/**
	 * test case for DELETE /tasks/{taskID}
	 */
	@Test
	public void testDelete_FollowDiscussions(){
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().delete("/tasks/" + taskId);
	}
}