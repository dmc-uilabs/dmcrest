package org.dmc.services;

import org.dmc.services.services.Service;
import org.dmc.services.services.ServiceDao;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;

import java.util.Random;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class ServiceIT extends BaseIT {
	
	private static final String SERVICE_RESOURCE = "/services/{id}";
    private ServiceDao serviceDao = new ServiceDao();
	private Service service = null;
	private Random r = new Random();
	
	@Test
	public void getService() {	
	  // Get a random service to test againts	
	  service = serviceDao.getService(2);
	  // perform actual GET request against the embedded container for the service we know exists
	  // provide the same ID the test service above was created with
      // Expect 
	  if (service != null) {	
	    expect().
	      statusCode(200).
	      when().
	      get(SERVICE_RESOURCE, service.getId()).then().
          body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));
	  }
	}
	
	@Test
	public void testServiceList(){
		expect().statusCode(200).when().get("/services").then().
        body(matchesJsonSchemaInClasspath("Schemas/serviceListSchema.json"));
	}
	
	@Test
	public void testServiceListProject(){
		expect().statusCode(200).when().get("/projects/6/services").then().
        body(matchesJsonSchemaInClasspath("Schemas/serviceListSchema.json"));
	}
	
	@Test
	public void testServiceListComponent(){
		
		expect().statusCode(200).when().get("/components/" + (r.nextInt(190) + 30) + "/services").then().
        body(matchesJsonSchemaInClasspath("Schemas/serviceListSchema.json"));
	}
	
}