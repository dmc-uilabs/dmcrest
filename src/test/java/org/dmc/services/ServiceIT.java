package org.dmc.services;

import org.dmc.services.services.Service;
import org.dmc.services.services.ServiceDao;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static com.jayway.restassured.RestAssured.*;

import java.util.Random;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class ServiceIT extends BaseIT {
	
	private static final String SERVICE_RESOURCE = "/services/{id}";
    private ServiceDao serviceDao = new ServiceDao();
	private Service service = null;
	private Random r = new Random();
	private String serviceId = "1"; // the serviceId need to be assigned new value
	
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
	
	
	/**
	 * test case for patch /services/{serviceID}
	 */
	@Test
	public void testServicePatch_ServiceId(){
		//TBD
	}
	
	/**
	 * test case for post /services
	 */
	@Test
	public void testServicePost(){
		//TBD
	}
	
	
	/**
	 * test case for get /services/{serviceID}/service_authors
	 */
	@Test
	public void testServiceGet_ServiceAuthor(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_FOUND.value()).
		when().get("/services" + serviceId + "/service_authors");
	}
	
	/**
	 * test case for get /services/{serviceID}/service_documents
	 */
	@Test
	public void testServiceGet_ServiceDocument(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_FOUND.value()).
		when().get("/services" + serviceId + "/service_documents");
	}
	
	
	/**
	 * test case for get /services/{serviceID}/service_documents
	 */
	@Test
	public void testServiceGet_ServiceHistory(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_FOUND.value()).
		when().get("/services" + serviceId + "/service_history");
	}
	
	/**
	 * test case for get /services/{serviceID}/service_images
	 */
	@Test
	public void testServiceGet_ServiceImage(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_FOUND.value()).
		when().get("/services" + serviceId + "/service_images");
	}
	
	
	/**
	 * test case for get /services/{serviceID}/service_tags
	 */
	@Test
	public void testServiceGet_ServiceTags(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_FOUND.value()).
		when().get("/services" + serviceId + "/service_tags");
	}
	
}