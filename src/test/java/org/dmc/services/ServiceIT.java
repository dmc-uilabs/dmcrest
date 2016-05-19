package org.dmc.services;

import org.dmc.services.services.PostServiceInputPosition;
import org.dmc.services.services.PostUpdateDomeInterface;
import org.dmc.services.services.Service;
import org.dmc.services.services.ServiceDao;
import org.dmc.services.services.ServiceInputPosition;
import org.dmc.services.services.ServiceSpecifications;
import org.dmc.services.services.*;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.ValidatableResponse;

import static com.jayway.restassured.RestAssured.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

    
	
    
    
    
    private PostUpdateDomeInterface createPostUpdateDomeInterface() {
    	PostUpdateDomeInterface domeInterface = new PostUpdateDomeInterface();
    	domeInterface.setVersion(20);
		domeInterface.setModelId("1996");
		domeInterface.setInterfaceId("John Wayne");
		domeInterface.setDomeServer("1");
		domeInterface.setName("Brian");
		List<Integer> path = new ArrayList<Integer>();
		path.add(new Integer(1));
		path.add(new Integer(2));
		path.add(new Integer(3));
		path.add(new Integer(4));
		path.add(new Integer(5));
		domeInterface.setPath(path);
		domeInterface.setServiceId(1);
		domeInterface.setType("type");
		
		return domeInterface;
    }
    
    private List<BigDecimal> convertIntegerListtoBigDecimalList(List<Integer> path) {
    	ListIterator<Integer> pathListIter = path.listIterator();
    	List<BigDecimal> ret = new ArrayList<BigDecimal>();
		
		
		
		while(pathListIter.hasNext()) {

			Integer tempPathListInt = pathListIter.next();
			
			ret.add(new BigDecimal(tempPathListInt.toString()));
		}
    	
    	return ret;
    }
    
    
    
    /**
	 * test case for POST /dome-interfaces when invalid input is sent
	 */
	@Test
	public void testServicePost_DomeInterface_WhenInvalidInputIsSent(){
		
		//"domeServer is a very large number, something not in the database so a foreign key error occurs
		String jsonString = "{\"version\":23,\"modelId\":\"1996\",\"interfaceId\":\"John Wayne\",\"type\":\"type\",\"name\":\"Brian\",\"path\":null,\"serviceId\":19,\"domeServer\":\"1900\"}";
		

		
		
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(jsonString).
		expect().
			statusCode(HttpStatus.METHOD_NOT_ALLOWED.value()).
		when().post("/dome-interfaces");

	}
    
    
    
    /**
	 * test case for POST /dome-interfaces when valid info is sent
	 */
	@Test
	public void testServicePost_DomeInterface_WhenValidInfoIsSent(){
		PostUpdateDomeInterface sentDomeInterface = createPostUpdateDomeInterface();
		
		ObjectMapper mapper = new ObjectMapper();
		String postDomeInterfaceJSONString = null;
		try {
	           postDomeInterfaceJSONString = mapper.writeValueAsString(sentDomeInterface);
		} catch (JsonProcessingException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	    }
		
		
		GetDomeInterface receivedDomeInterface =
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(postDomeInterfaceJSONString).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().post("/dome-interfaces").as(GetDomeInterface.class);
		
		
		BigDecimal postUpdateVersion = new BigDecimal(Integer.toString(sentDomeInterface.getVersion()));
		BigDecimal postUpdateServiceId = new BigDecimal(Integer.toString(sentDomeInterface.getServiceId()));
		
		assertTrue("testServicePost_DomeInterface: Dome server values are not equal", (receivedDomeInterface.getDomeServer().equals(sentDomeInterface.getDomeServer())));
		assertTrue("testServicePost_DomeInterface: Version values are not equal", (receivedDomeInterface.getVersion().equals(postUpdateVersion)));
		assertTrue("testServicePost_DomeInterface: Model ID values are not equal", (receivedDomeInterface.getModelId().equals(sentDomeInterface.getModelId())));
		assertTrue("testServicePost_DomeInterface: Interface ID values are not equal", (receivedDomeInterface.getInterfaceId().equals(sentDomeInterface.getInterfaceId())));
		assertTrue("testServicePost_DomeInterface: Type values are not equal", (receivedDomeInterface.getType().equals(sentDomeInterface.getType())));
		assertTrue("testServicePost_DomeInterface: Name values are not equal", (receivedDomeInterface.getName().equals(sentDomeInterface.getName())));
		assertTrue("testServicePost_DomeInterface: Path values are not equal", (receivedDomeInterface.getPath().equals(convertIntegerListtoBigDecimalList(sentDomeInterface.getPath()))));
		assertTrue("testServicePost_DomeInterface: Dome server values are not equal", (receivedDomeInterface.getServiceId().equals(postUpdateServiceId)));

	}


	/**
     * test case for PATCH /dome-interfaces/{domeInterfaceId}
     */
    @Test
    public void testServicePatch_DomeInterface(){
    	PostUpdateDomeInterface newDomeInterface = createPostUpdateDomeInterface();
		
		ObjectMapper postMapper = new ObjectMapper();
		String postDomeInterfaceJSONString = null;
		try {
	           postDomeInterfaceJSONString = postMapper.writeValueAsString(newDomeInterface);
		} catch (JsonProcessingException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	    }
		
		
		GetDomeInterface createdDomeInterface =
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(postDomeInterfaceJSONString).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().post("/dome-interfaces").as(GetDomeInterface.class);
    	
    	
    	PostUpdateDomeInterface patchDomeInterface = new PostUpdateDomeInterface();
    	patchDomeInterface.setVersion(22);
		patchDomeInterface.setModelId("2016");
		patchDomeInterface.setInterfaceId("Marshall Mathers");
		patchDomeInterface.setDomeServer("1");
		patchDomeInterface.setName("Batman");
		List<Integer> path = new ArrayList<Integer>();
		path.add(new Integer(11));
		path.add(new Integer(12));
		path.add(new Integer(13));
		path.add(new Integer(14));
		path.add(new Integer(15));
		patchDomeInterface.setPath(path);
		patchDomeInterface.setServiceId(1);
		patchDomeInterface.setType("type2");
		
		
		ObjectMapper mapper = new ObjectMapper();
		String patchedDomeInterfaceJSONString = null;
		try {
	           patchedDomeInterfaceJSONString = mapper.writeValueAsString(patchDomeInterface);
		} catch (JsonProcessingException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	    }
	
		GetDomeInterface receivedDomeInterface =
		given().
	    	header("Content-type", "application/json").
	    	header("AJP_eppn", userEPPN).
	    	body(patchedDomeInterfaceJSONString).
	    expect().
        	statusCode(HttpStatus.OK.value()).
        when().patch("/dome-interfaces/" + createdDomeInterface.getId()).as(GetDomeInterface.class);
		
		
		BigDecimal postUpdateVersion = new BigDecimal(Integer.toString(patchDomeInterface.getVersion()));
		BigDecimal postUpdateServiceId = new BigDecimal(Integer.toString(patchDomeInterface.getServiceId()));
		
		assertTrue("testServicePost_DomeInterface: Dome server values are not equal", (receivedDomeInterface.getDomeServer().equals(patchDomeInterface.getDomeServer())));
		assertTrue("testServicePost_DomeInterface: Version values are not equal", (receivedDomeInterface.getVersion().equals(postUpdateVersion)));
		assertTrue("testServicePost_DomeInterface: Model ID values are not equal", (receivedDomeInterface.getModelId().equals(patchDomeInterface.getModelId())));
		assertTrue("testServicePost_DomeInterface: Interface ID values are not equal", (receivedDomeInterface.getInterfaceId().equals(patchDomeInterface.getInterfaceId())));
		assertTrue("testServicePost_DomeInterface: Type values are not equal", (receivedDomeInterface.getType().equals(patchDomeInterface.getType())));
		assertTrue("testServicePost_DomeInterface: Name values are not equal", (receivedDomeInterface.getName().equals(patchDomeInterface.getName())));
		assertTrue("testServicePost_DomeInterface: Path values are not equal", (receivedDomeInterface.getPath().equals(convertIntegerListtoBigDecimalList(patchDomeInterface.getPath()))));
		assertTrue("testServicePost_DomeInterface: Dome server values are not equal", (receivedDomeInterface.getServiceId().equals(postUpdateServiceId)));
    }
	
	
	/**
	 * test case for DELETE /dome-interfaces/{domeInterfaceId}
	 */
	@Test
	public void testServiceDelete_DomeInterface(){
		PostUpdateDomeInterface postDomeInterface = createPostUpdateDomeInterface();
		postDomeInterface.setInterfaceId("DELETE ME");
		postDomeInterface.setName("DELETEEEEE");
		
		ObjectMapper mapper = new ObjectMapper();
		String postDomeInterfaceJSONString = null;
		try {
	           postDomeInterfaceJSONString = mapper.writeValueAsString(postDomeInterface);
		} catch (JsonProcessingException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	    }
		
		
		GetDomeInterface postedDomeInterface =
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(postDomeInterfaceJSONString).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().post("/dome-interfaces").as(GetDomeInterface.class);
		
		
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().get("/dome-interfaces/" + postedDomeInterface.getId());
		
		
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().delete("/dome-interfaces/" + postedDomeInterface.getId());
		
		
		
		
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(HttpStatus.NOT_FOUND.value()).
		when().get("/dome-interfaces/" + postedDomeInterface.getId());
	}
	
	/**
	 * test case for GET /dome-interfaces/{domeInterfaceId}
	 */
	@Test
	public void testServiceGet_DomeInterfaceById(){
		
		PostUpdateDomeInterface postDomeInterface = createPostUpdateDomeInterface();
		
		ObjectMapper mapper = new ObjectMapper();
		String postDomeInterfaceJSONString = null;
		try {
	           postDomeInterfaceJSONString = mapper.writeValueAsString(postDomeInterface);
		} catch (JsonProcessingException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	    }
		
		
		GetDomeInterface postedDomeInterface =
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", userEPPN).
			body(postDomeInterfaceJSONString).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().post("/dome-interfaces").as(GetDomeInterface.class);
		
		
		GetDomeInterface readDomeInterface =
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().get("/dome-interfaces/" + postedDomeInterface.getId()).as(GetDomeInterface.class);
		
		
		BigDecimal postUpdateVersion = new BigDecimal(Integer.toString(postDomeInterface.getVersion()));
		BigDecimal postUpdateServiceId = new BigDecimal(Integer.toString(postDomeInterface.getServiceId()));
		
		assertTrue("testServicePost_DomeInterface: Dome server values are not equal", (readDomeInterface.getDomeServer().equals(postDomeInterface.getDomeServer())));
		assertTrue("testServicePost_DomeInterface: Version values are not equal", (readDomeInterface.getVersion().equals(postUpdateVersion)));
		assertTrue("testServicePost_DomeInterface: Model ID values are not equal", (readDomeInterface.getModelId().equals(postDomeInterface.getModelId())));
		assertTrue("testServicePost_DomeInterface: Interface ID values are not equal", (readDomeInterface.getInterfaceId().equals(postDomeInterface.getInterfaceId())));
		assertTrue("testServicePost_DomeInterface: Type values are not equal", (readDomeInterface.getType().equals(postDomeInterface.getType())));
		assertTrue("testServicePost_DomeInterface: Name values are not equal", (readDomeInterface.getName().equals(postDomeInterface.getName())));
		assertTrue("testServicePost_DomeInterface: Path values are not equal", (readDomeInterface.getPath().equals(convertIntegerListtoBigDecimalList(postDomeInterface.getPath()))));
		assertTrue("testServicePost_DomeInterface: Dome server values are not equal", (readDomeInterface.getServiceId().equals(postUpdateServiceId)));
	
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().delete("/dome-interfaces/" + postedDomeInterface.getId());
	
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
