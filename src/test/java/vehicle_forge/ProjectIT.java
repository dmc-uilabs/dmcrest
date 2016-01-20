package vehicle_forge;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.*;
import org.springframework.test.*;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before; 
import org.junit.Ignore;

import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.RestAssured;
import static org.hamcrest.Matchers.*;
import java.util.Random;
import org.json.JSONObject;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class ProjectIT extends BaseIT {

	@Test
	public void testProject1() {
	  expect().
	    statusCode(200).
	    when().
	    get("/projects/1").then().
        body(matchesJsonSchemaInClasspath("Schemas/projectSchema.json")); 
	}
	
	@Test
	public void testProject5(){
		expect().
			statusCode(200).
		when().
			get("/projects/5").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectSchema.json"));
	}
	
	@Test
	public void testProjectList(){
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
	public void testProjectCreateJson(){
		JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);

		String input = "{\"projectname\": " + "junitTest" + unique + ", {\"comment\": \"test " + date.getTime() + "\"}";
		
		json.put("projectname", "junitTest" + unique);
		json.put("unixname", "junit" + unique);
		System.out.println("json = " + json.toString());
		given().
			body(json.toString()).
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

		String input = "{\"projectname\": " + "junitTestdup" + unique + ", {\"comment\": \"test " + date.getTime() + "\"}";
		
		json.put("projectname", "junitTestdup" + unique);
		json.put("unixname", "junitdup" + unique);

		// first time should work
		given().
			body(json.toString()).
		expect().
			statusCode(200).
		when().
			post("/projects/create").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));

		// second time should fail, because unixname is a duplicate
		given().
			body(json.toString()).
		expect().
			statusCode(200).
		when().
			post("/projects/create").
		then().
			log().all().
			body(matchesJsonSchemaInClasspath("Schemas/errorSchema.json"));
	}
}
