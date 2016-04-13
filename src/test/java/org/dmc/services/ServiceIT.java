package org.dmc.services;

import org.dmc.services.services.Service;
import org.dmc.services.services.ServiceDao;
import org.dmc.services.services.ServiceSpecifications;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		ObjectMapper mapper = new ObjectMapper();
		String patchedServiceJSONString = null;
		try {
			patchedServiceJSONString = mapper.writeValueAsString(service);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(patchedServiceJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        patch("/services/" + serviceId);
	}
	
	/**
	 * test case for post /services
	 */
	@Test
	public void testServicePost(){
		ObjectMapper mapper = new ObjectMapper();
		String postedServiceJSONString = null;
		try {
			postedServiceJSONString = mapper.writeValueAsString(service);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(postedServiceJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        post("/services/");
	}
	
	
	/**
	 * test case for get /services/{serviceID}/service_authors
	 */
	@Test
	public void testServiceGet_ServiceAuthor(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/services/" + serviceId + "/service_authors");
	}
	
	/**
	 * test case for get /services/{serviceID}/service_documents
	 */
	@Test
	public void testServiceGet_ServiceDocument(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/services/" + serviceId + "/service_documents");
	}
	
	
	/**
	 * test case for get /services/{serviceID}/service_documents
	 */
	@Test
	public void testServiceGet_ServiceHistory(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/services/" + serviceId + "/service_history");
	}
	
	
	/**
	 * test case for get /services/{serviceID}/service_images
	 */
	@Test
	public void testServiceGet_ServiceImage(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/services/" + serviceId + "/service_images");
	}
	
	
	/**
	 * test case for get /services/{serviceID}/service_tags
	 */
	@Test
	public void testServiceGet_ServiceTags(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/services/" + serviceId + "/service_tags");
	}
	
	/**
	 * test case for get /services/{serviceID}/services_statistic
	 */
	@Test
	public void testServiceGet_ServiceStatistic(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/services/" + serviceId + "/services_statistic");
	}
	

	/**
	 * test case for get /services/{serviceID}/dome-interfaces
	 */
	@Test
	public void testServiceGet_DomeInterface(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/services/" + serviceId + "/dome-interfaces");
	}
	
	
	/**
	 * test case for get /services/{serviceID}/input-positions
	 */
	@Test
	public void testServiceGet_InputPositions(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/services/" + serviceId + "/input-positions");
	}
	
	
	/**
	 * test case for POST /service/{serviceID}/specifications
	 */
	@Test
	public void testServicePost_Specification(){
		ServiceSpecifications specification = new ServiceSpecifications();
		ObjectMapper mapper = new ObjectMapper();
		String postedSpecificationJSONString = null;
		try {
			postedSpecificationJSONString = mapper.writeValueAsString(specification);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(postedSpecificationJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        post("/service/" + serviceId + "/specifications");
	}
}