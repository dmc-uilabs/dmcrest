package org.dmc.services;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.Random;

import org.json.JSONObject;

//@Ignore
public class TaskIT extends BaseIT {

	@Test
	public void testTask13() {
	  expect().
	    statusCode(200).
	    when().
	    get("/tasks/13").then().
        body(matchesJsonSchemaInClasspath("Schemas/taskSchema.json"));
	}
	
	@Test
	public void testTask1(){
		expect().statusCode(200).when().get("/tasks/1").then().
        body(matchesJsonSchemaInClasspath("Schemas/taskSchema.json"));
	}
	
	@Test
	public void testTaskList(){
		expect().statusCode(200).when().get("/tasks").then().
        body(matchesJsonSchemaInClasspath("Schemas/taskListSchema.json"));
	}
	
	@Test
	public void testTaskCreate(){
		
	    JSONObject json = new JSONObject();
		Random r = new Random();     // use to randomize data params
				
        json.put("title", "sample test task");
		json.put("description", "description for sample test task");
		json.put("priority", 0);
        json.put("dueDate", 0);      // a user ID in users table 
	    json.put("reporter", "bamboo tester");  // another user ID in users table
		json.put("assignee", 103);    // from group table
		json.put("projectId", 1);     // from group table and project_group_list, 1 is available in both
		
		given()
			.body(json.toString())
			.expect()
			.statusCode(200)
			.when()
			.post("/tasks/create")
			.then()
				.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
		
	}
	
}