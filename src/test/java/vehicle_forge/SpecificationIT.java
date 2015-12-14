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
public class SpecificationIT extends BaseIT {
	
	@Test
	public void testSpecification13() {
	  expect().
	    statusCode(200).
	    when().
	    get("/services/12/specifications").then().
        body(matchesJsonSchemaInClasspath("Schemas/specificationSchema.json"));
	}
	
	@Test
	public void testSpecification1() {
		expect().statusCode(200).when().get("/services/1/specifications").then().
        body(matchesJsonSchemaInClasspath("Schemas/specificationSchema.json"));
	}
}