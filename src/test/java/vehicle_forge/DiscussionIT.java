package vehicle_forge;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.*;
import org.springframework.test.*;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before; 
import org.junit.Ignore;

import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.RestAssured;
import static org.hamcrest.Matchers.*;
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