package org.dmc.services;

import org.dmc.services.services.PostServiceInputPosition;
import org.dmc.services.services.PostSharedService;
import org.dmc.services.services.PostUpdateDomeInterface;
import org.dmc.services.services.Service;
import org.dmc.services.services.ServiceDao;
import org.dmc.services.services.ServiceInputPosition;
import org.dmc.services.services.ServiceSpecifications;
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
    private ServiceDao serviceDao = new ServiceDao();
    private Service service = null;
    private Random r = new Random();
    private String serviceId = "1"; // the serviceId need to be assigned new value
    private String domeInterfaceId = "1";
    private String positionInputId = "1";
    private String sharedServiceId = "1";

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
            body(matchesJsonSchemaInClasspath("Schemas/ServiceSchema.json"));
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
     * test case for POST /dome-interfaces
     */
    @Test
    public void testServicePost_DomeInterface(){
        PostUpdateDomeInterface  obj = new PostUpdateDomeInterface();
        ObjectMapper mapper = new ObjectMapper();
        String postedDomeInterfaceJSONString = null;
        try {
            postedDomeInterfaceJSONString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(postedDomeInterfaceJSONString).
        expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
        when().post("/dome-interfaces");
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

    /*
     * test case for POST /shared-services
     */
    @Test
    public void testPost_SharedService(){
        PostSharedService obj = new PostSharedService();
        ObjectMapper mapper = new ObjectMapper();
        String postedSharedServiceJSONString = null;
        
        try {
            postedSharedServiceJSONString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        given().
            header("Content-type", "application/json").
            header("AJP_eppn", userEPPN).
            body(postedSharedServiceJSONString).
        expect().
            statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
        when().
            post("/shared-services");    
    }

    /*
     * test case for GET /shared-services/{id}
     */
    @Test
    public void testGet_SharedService(){
        given().
            header("AJP_eppn", userEPPN).
        expect().
            statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
        when().get("/shared-services/" + sharedServiceId);
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
