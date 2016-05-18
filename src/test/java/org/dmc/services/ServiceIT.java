package org.dmc.services;

import org.dmc.services.services.*;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.ValidatableResponse;

import static com.jayway.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class ServiceIT extends BaseIT {
    private final String logTag = ServiceIT.class.getName();
	
	private static final String SERVICE_RESOURCE = "/services/{id}";
	private static final String SERVICE_TAGS_GET_BY_SERVICE_ID = "/services/{serviceID}/service_tags";
	private static final String SERVICE_TAGS_RESOURCE = "/service_tags";

    private ServiceDao serviceDao = new ServiceDao();
    private Service service = null;
    private Random r = new Random();
    private String serviceId = "1"; // the serviceId need to be assigned new value
    private String domeInterfaceId = "1";
    private String positionInputId = "1";

    @Test
    public void testService() {
        // perform actual GET request against the embedded container for the service we know exists
        // provide the same ID the test service above was created with
        // Expect
        expect().
          statusCode(200).
        when().
          get(SERVICE_RESOURCE, Integer.toString(2)).then().
          body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));
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
        
        //TODO: Create a service
        //Change description and use PATCH
        // what other things can be changed?
        // service type
        // Publish a service - not sure if it calls patch or something else.
        Service service = createNewServiceObjectToPost();    
        ValidatableResponse response =
            given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(service).
            expect().
                statusCode(HttpStatus.OK.value()).
            when().
                post("/services/").
            then().
                body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));;

        String jsonResponse = response.extract().asString();
        ServiceLogger.log(logTag, "response = " + jsonResponse);

        ObjectMapper mapper = new ObjectMapper();
        Service patchService = null;
        try {
            patchService = mapper.readValue(jsonResponse, Service.class);
        } catch (Exception e) {
            fail("unable to map response to Service object: " + e.getMessage());
        }

        patchService.setDescription(patchService.getDescription() + " - now patching");
        String patchUrl = "/services/" + patchService.getId();
        ServiceLogger.log(logTag, "patch url = " + patchUrl);

        given().
            header("Content-type", "application/json").
            header("AJP_eppn", userEPPN).
            body(patchService).
        expect().
            statusCode(HttpStatus.OK.value()).
        when().
                patch(patchUrl).
        then().
            body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));;
    }

    /**
     * test case for post /services
     */
    @Test
    public void testServicePost(){
        Service service = createNewServiceObjectToPost();

        given().
            header("Content-type", "application/json").
            header("AJP_eppn", userEPPN).
            body(service).
        expect().
            statusCode(HttpStatus.OK.value()).
        when().
            post("/services/").
        then().
            body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));
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

   
    /*
     * Test Case for POST /services/service_images
     * test case for GET /services/{serviceID}/service_images
     * and DELETE /service_images/{imageId}
     */
    @Test
    public void addAndGetAndDeleteServiceImages () {
    	
    	int serviceId = 2;
    	
    	
    	//Get a list of the current images
        ArrayList<ServiceImages> originalImages =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(HttpStatus.OK.value()).
                        when().
                        get(serviceId + "/service_images").
                        as(ArrayList.class);


        String url = "FakeUrl";
        int serviceImageId = addImage(serviceId, url);
        
        //Make sure the added image returns a valid id
        assertTrue(serviceImageId != -1);

        //Get a list of the new images 
        ArrayList<ServiceTag> newImages =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(HttpStatus.OK.value()).
                        when().
                        get("/services/" + serviceId + "/service_images").
                        as(ArrayList.class);

        int numBefore = (originalImages != null) ? originalImages.size() : 0;
        int numAfter  = (newImages != null) ? newImages.size() : 0;
        int numExpected = numBefore + 1;
        
        //the new list and old list should only differ by one
        assertTrue (numAfter == numExpected);

        deleteExistingImage(serviceImageId);

        ArrayList<ServiceTag> afterDeleteTags =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(HttpStatus.OK.value()).
                        when().
                        get("/services/" + serviceId + "/service_images").
                        as(ArrayList.class);

        int numAfterDelete  = (afterDeleteTags != null) ? afterDeleteTags.size() : 0;
        assertTrue (numAfterDelete == numBefore);

    }
    
    
    public int addImage (int serviceId, String url) {

        int id = -1;
        ServiceImages json = new ServiceImages();
        json.setServiceId(serviceId);
        json.setUrl(url);

        Integer createdId  = given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post("/service_images").
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;

        return id;
    }
    
    public void deleteExistingImage (int imageId) {

        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.NO_CONTENT.value()).
                when().
                delete("service_images/{imageId}", imageId);
    }
    
    @Test
    public void deleteNonExistingImage () {

        int imageId = 1223456789;
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                when().
                delete("service_images/{imageId}", imageId);
    }
    
    @Test
    public void addInvalidId () {

        int serviceId = 1223456789;
        given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        expect().
        statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
        when().
        get("/services/" + serviceId + "/service_images");
    }
    
    
    @Test
    public void getImageUnit () {
        int serviceId = 2;
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get("/services/" + serviceId + "/service_images");
    }


    
  
    
    /**
     * test case for get /services/{serviceID}/service_tags
     */
    @Test
    public void testServiceGet_ServiceTags(){

        int serviceId = 2;
        String tag1 = "tag_" + TestUserUtil.generateTime();

        ServiceTag json = new ServiceTag();
        json.setServiceId(Integer.toString(serviceId));
        json.setName(tag1);

        given().
           header("Content-type", "application/json").
           header("AJP_eppn", userEPPN).
           body(json).
        expect().
           statusCode(HttpStatus.OK.value()).
        when().
           post(SERVICE_TAGS_RESOURCE);

        ArrayList<ServiceTag> tags =
           given().
               header("AJP_eppn", userEPPN).
           expect().
               statusCode(200).
           when().
               get(SERVICE_TAGS_GET_BY_SERVICE_ID, serviceId).
               as(ArrayList.class);

        assertTrue(tags != null);
        assertTrue(tags.size() > 0);
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
     * test case for PATCH /dome-interfaces/{domeInterfaceId}
     */
    @Test
    public void testServicePatch_DomeInterface(){
	PostUpdateDomeInterface  obj = new PostUpdateDomeInterface();
	ObjectMapper mapper = new ObjectMapper();
	String patchedDomeInterfaceJSONString = null;
	try {
           patchedDomeInterfaceJSONString = mapper.writeValueAsString(obj);
	} catch (JsonProcessingException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
        }

	given().
            header("Content-type", "application/json").
            header("AJP_eppn", userEPPN).
            body(patchedDomeInterfaceJSONString).
        expect().
            statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
        when().patch("/dome-interfaces/" + domeInterfaceId);
    }
	
	
	/**
	 * test case for DELETE /dome-interfaces/{domeInterfaceId}
	 */
	@Test
	public void testServiceDelete_DomeInterface(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().delete("/dome-interfaces/" + domeInterfaceId);
	}
	
	/**
	 * test case for GET /dome-interfaces/{domeInterfaceId}
	 */
	@Test
	public void testServiceGet_DomeInterfaceById(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/dome-interfaces/" + domeInterfaceId);
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
	
	
	/**
	 * test case for POST /service_runs
	 */
	@Test
	public void testServicePost_ServiceRunId(){
		ObjectMapper mapper = new ObjectMapper();
		String postedServiceRunJSONString = null;
		try {
			postedServiceRunJSONString = mapper.writeValueAsString(service);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(postedServiceRunJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        post("/service_runs/");
	}
	
	

	
	/**
	 * test case for GET /service_runs/{id}
	 */
	@Test
	public void testServiceGet_ServiceRunId(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/service_runs/" + serviceId);
	}
	
	/**
	 * test case for PATCH /service_runs/{id}
	 */
	@Test
	public void testServicePatch_ServiceRunId(){
		ObjectMapper mapper = new ObjectMapper();
		String patchedServiceRunIdJSONString = null;
		try {
			patchedServiceRunIdJSONString = mapper.writeValueAsString(service);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(patchedServiceRunIdJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        patch("/service_runs/" + serviceId);
	}
	
	
	
	
	/*
	 * test case for DELETE /service_runs/{id}
	 */
	@Test
	public void testServiceDelete_ServiceRunId(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().delete("/service_runs/" + serviceId);
	}
	
	/*
	 * test case for POST /input-positions
	 */
	@Test
	public void testPost_InputPosition(){
		List<PostServiceInputPosition> obj = new ArrayList<PostServiceInputPosition>();
		ObjectMapper mapper = new ObjectMapper();
		String postedInputPositionJSONString = null;
		
		try {
			postedInputPositionJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(postedInputPositionJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        post("/input-positions");
		
	}
	
	
	/*
	 * test case for DELETE /input-positions/{positionInputId}
	 */
	@Test
	public void testDelete_InputPositionByPositionInputId(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().delete("/input-positions/" + positionInputId);
	}
	
	
	/*
	 * test case for PATCH /input-positions/{positionInputId}
	 */
	@Test
	public void testPatch_InputPositionByPositionInputId(){
		List<ServiceInputPosition> obj = new ArrayList<ServiceInputPosition>();
		ObjectMapper mapper = new ObjectMapper();
		String patchedServiceInputPositionJSONString = null;
		
		try {
			patchedServiceInputPositionJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(patchedServiceInputPositionJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        patch("/input-positions/" + positionInputId);
		
	}
	
	
    // create a service object to use as body in post
    private Service createNewServiceObjectToPost()
    {
        Service service = new Service();
        service.setTitle("junit service test");
        service.setDescription("junit service test");
        service.setOwner(userEPPN);
        return service;
    }
	
}
