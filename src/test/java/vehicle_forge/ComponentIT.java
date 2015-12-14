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

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class ComponentIT extends BaseIT {
	
    

	@Test
	public void testComponent1() {
	  expect().
	    statusCode(200).
	    when().
	    get("/components/1").then().
        body(matchesJsonSchemaInClasspath("Schemas/componentSchema.json")); 
	}
	
	@Test
	public void testComponentList(){
		expect().statusCode(200).when().get("/components").then().
        body(matchesJsonSchemaInClasspath("Schemas/componentListSchema.json"));
	}
	
	@Test
	public void testProjectComponents(){
		expect().statusCode(200).when().get("/projects/6/components").then().
		body(matchesJsonSchemaInClasspath("Schemas/componentListSchema.json"));
	}
	
}