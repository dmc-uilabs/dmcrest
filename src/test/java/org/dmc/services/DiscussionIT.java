package org.dmc.services;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;

import java.util.Random;
import java.util.Date;
import org.json.JSONObject;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class DiscussionIT extends BaseIT {
	
	@Test
	public void testDiscussion1() {
 
	  expect().
	    statusCode(200).
	    when().
	    get("/discussions/1").then().
        body(matchesJsonSchemaInClasspath("Schemas/discussionSchema.json"));
	    
	}
	
	@Test
	public void testDiscussion5(){
		expect().statusCode(200).when().get("/discussions/5").then().
        body(matchesJsonSchemaInClasspath("Schemas/discussionSchema.json"));
	}
	
	@Test
	public void testDiscussionList(){
		expect().statusCode(200).when().get("/discussions").then().
        body(matchesJsonSchemaInClasspath("Schemas/discussionListSchema.json"));
	}
	
	@Test
	
	public void testDiscussionCreate(){
		JSONObject json = new JSONObject();
		Random r = new Random();
		Date d = new Date();
		String input = "{\"projectId\": " + r.nextInt() + ", {\"comment\": \"test " + d.getTime() + "\"}";
		
		json.put("projectId", r.nextInt(100));
		json.put("id", -9999);
		json.put("text", "test " + d.getTime());
		json.put("full_name", "random " + r.nextInt());
		json.put("created_at" , d.getTime());
		json.put("avatar", "avatar " + d.getTime());
		
		
		given().body(json.toString()).expect().statusCode(200).when().post("/discussions/create").then()
		.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}
}