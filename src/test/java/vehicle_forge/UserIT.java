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

/*
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;
import static com.jayway.restassured.RestAssured.given;
*/
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class UserIT extends BaseIT {

	@Test
	public void testUserCreate(){
		JSONObject json = new JSONObject();
		Random r = new Random();
		
		json.put("user_name", "username " + r.nextInt());
		json.put("email", "testemail" + r.nextInt()+ "@ge.com");
		json.put("password", "pwd " + r.nextInt());
		json.put("name", "usertester " + r.nextInt());
		
		
		given().body(json.toString()).expect().statusCode(200).when().post("/users/create").then()
		.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}
	
}