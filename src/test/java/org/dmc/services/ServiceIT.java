package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import org.dmc.services.services.DomeModelParam;
import org.dmc.services.services.GetDomeInterface;
import org.dmc.services.services.GetServiceRun;
import org.dmc.services.services.PostServiceInputPosition;
import org.dmc.services.services.PostUpdateDomeInterface;
import org.dmc.services.services.RunStats;
import org.dmc.services.services.Service;
import org.dmc.services.services.ServiceAuthor;
import org.dmc.services.services.ServiceInputPosition;
import org.dmc.services.services.ServiceSpecialSpecifications;
import org.dmc.services.services.ServiceSpecifications;
import org.dmc.services.services.ServiceTag;
import org.dmc.services.services.UsageStats;

import org.dmc.services.users.UserDao;
import org.dmc.services.projects.ProjectCreateRequest;
import org.dmc.services.services.*;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Ignore;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.ValidatableResponse;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceIT extends BaseIT {

    private final String logTag = ServiceIT.class.getName();

    private static final String SERVICE_LIST_RESOURCE = "/services";
    private static final String SERVICE_RESOURCE = "/services/{id}";
    private static final String SERVICE_TAGS_GET_BY_SERVICE_ID = "/services/{serviceID}/service_tags";
    private static final String SERVICE_TAGS_RESOURCE = "/service_tags";
    private static final String SERVICE_AUTHORS_RESOURCE = "/services/{serviceId}/service_authors";
    private static final String SERVICE_HISTORY_RESOURCE = "/services/{serviceId}/services_history";
    private static final String PROJECT_SERVICES_RESOURCE ="/projects/{projectId}/services";
    private static final String COMPONENT_SERVICES_RESOURCE ="/components/{componentId}/services";
    private static final String DOME_INTERFACES_RESOURCE = "/dome-interfaces";
    private static final String DOME_INTERFACE_BY_ID_RESOURCE = "/dome-interfaces/{interfaceId}";
    private static final String SERVICE_DOME_INTERFACES_RESOURCE = "/services/{serviceId}/dome-interfaces";
    private static final String SERVICE_INPUT_POS_RESOURCE = "/services/{serviceId}/input-positions";
    private static final String SERVICE_SPECS_RESOURCE = "/service/{serviceId}/specifications";
    private static final String ARRAY_SPECS_RESOURCE = "/array_specifications";
    private static final String SERVICE_RUN_RESOURCE = "/service_runs/{serviceRunId}";
    private static final String SERVICE_RUN_LIST_RESOURCE = "/service_runs";
    private static final String INPUT_POS_RESOURCE = "/input-positions";
    private static final String INPUT_POS_BY_ID_RESOURCE = "/input-positions/{positionId}";

    private Random r = new Random();
    private String serviceId = "1"; // the serviceId need to be assigned new value
    private String positionInputId = "1";
    
    private String knownEPPN;
    
    @Before
    public void before() {
        if (knownEPPN == null) {
            knownEPPN = TestUserUtil.createNewUser();
        }
    }

    @Test
    public void testService() {
        // perform actual GET request against the embedded container for the service we know exists
        // provide the same ID the test service above was created with
        // Expect
        given().header("AJP_eppn", knownEPPN).
        expect().
          statusCode(OK.value()).
        when().
          get(SERVICE_RESOURCE, Integer.toString(2)).then().
          body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));
    }

    @Test
    public void testServiceList(){
        given().header("AJP_eppn", knownEPPN).
        expect().statusCode(OK.value()).when().get(SERVICE_LIST_RESOURCE).then().
        body(matchesJsonSchemaInClasspath("Schemas/serviceListSchema.json"));
    }

    @Test
    public void testServiceListProject(){
        given().header("AJP_eppn", knownEPPN).
        expect().statusCode(OK.value()).when().get(PROJECT_SERVICES_RESOURCE, "6").then().
        body(matchesJsonSchemaInClasspath("Schemas/serviceListSchema.json"));
    }

    @Test
    public void testServiceListComponent(){
        Integer randomComponentId =  (r.nextInt(190) + 30);
        given().header("AJP_eppn", knownEPPN).
        expect().statusCode(OK.value()).when().get(COMPONENT_SERVICES_RESOURCE, randomComponentId.toString()).then().
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
                header("Content-type", APPLICATION_JSON_VALUE).
                header("AJP_eppn", userEPPN).
                body(service).
            expect().
                statusCode(OK.value()).
            when().
                post(SERVICE_LIST_RESOURCE).
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

        // make sure we send the patchService as the body
        // 1 - that is the object that we are changing the description on
        // 2 - that is the object that has the serviceId set
        patchService.setDescription(patchService.getDescription() + " - now patching");
        ServiceLogger.log(logTag, "testServicePatch_ServiceId: patch url = " + SERVICE_RESOURCE + " + id = " + patchService.getId());
        response =
                given().
                    header("Content-type", APPLICATION_JSON_VALUE).
                    header("AJP_eppn", userEPPN).
                    body(patchService).
                expect().
                    statusCode(OK.value()).
                when().
                    patch(SERVICE_RESOURCE, patchService.getId()).
                then().
                    body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));;

    }

    /**
     * test case for post /services
     */
    @Test
    public void testServicePost(){
        Service service = createNewServiceObjectToPost();

        Service serviceResponse =
                given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(service).
                expect().
                statusCode(OK.value()).
                when().
                post(SERVICE_LIST_RESOURCE).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json")).
                extract().as(Service.class);

        assertTrue("Title is not equal in sent and response",
                service.getTitle().equals(serviceResponse.getTitle()));
        assertTrue("Description is not equal in sent and response",
                service.getDescription().equals(serviceResponse.getDescription()));
        assertTrue("Service Type is not equal in sent and response",
                service.getServiceType().equals(serviceResponse.getServiceType()));
        assertTrue("Specifications is not equal in sent and response",
                service.getSpecifications().equals(serviceResponse.getSpecifications()));
    }

    /**
     * test case for get /services/{serviceID}/service_authors
     */
    @Test
    public void testServiceGet_ServiceAuthor(){
        ServiceAuthor[] authors =
                given().
                header("AJP_eppn", userEPPN).
                header("Content-type", APPLICATION_JSON_VALUE).
                expect().
                statusCode(OK.value()).
                when().
                get(SERVICE_AUTHORS_RESOURCE, serviceId).as(ServiceAuthor[].class);

        assertTrue("No service authors - list is empty", authors.length > 0);
        assertTrue("First service author is null", authors[0] != null);

        ServiceAuthor firstAuthor = (ServiceAuthor) authors[0];

        assertTrue("First author id "+firstAuthor.getId()+" job title is null", firstAuthor.getJobTitle() != null);
        assertTrue("First author id "+firstAuthor.getId()+" display name is null ", firstAuthor.getDisplayName() != null);
    }

    /**
     * test case for get /services/{serviceID}/service_history
     * this will get back an empty list of history since history is only created when we PATCH the serviceID and we haven't called patch for this service
     */
    @Test
    public void testService_GetEmptyServiceHistory(){
        List<ServiceHistory> history = given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(OK.value()).
                when().get(SERVICE_HISTORY_RESOURCE, serviceId).as(List.class);

        assertTrue("Got back nonempty history with unupdated service", history.size() == 0 && history.isEmpty());
    }
    /**
     * test case for get /services/{serviceID}/service_history
     *
     * to create some sample history:
     * 1) create a test project
     * 2) upload a test service to that project
     * 3) update the description of the service
     * 4) check the history - should be one entry
     */
    @Test
    public void testService_GetHistory() {
        Id projectId = createTestProject();
        Service testService = uploadTestService(projectId);
        updateService(testService);
        checkHistory(testService);
    }
    
    private Id createTestProject() {
        final Date date = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String unique = format.format(date);

        final ProjectCreateRequest json = new ProjectCreateRequest();
        json.setDescription("junit testProjectCreateJsonObject " + unique);
        json.setTitle("junitjson" + unique);

        ServiceLogger.log(logTag, "testProjectCreateJsonObject: json = " + json.toString());
        final Id pid = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(json).expect()
                .statusCode(OK.value()).when().post("/projects/create").then()
                .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).extract().as(Id.class);
        
        return pid;
    }

    private Service uploadTestService(Id projectId) {
        //Change description and use PATCH
        // what other things can be changed?
        // service type
        // Publish a service - not sure if it calls patch or something else.
        final Service service = createNewServiceObjectToPost();  
        service.setProjectId(Integer.toString(projectId.getId()));
        final ValidatableResponse response =
                given().
                header("Content-type", APPLICATION_JSON_VALUE).
                header("AJP_eppn", userEPPN).
                body(service).
                expect().
                statusCode(OK.value()).
                when().
                post(SERVICE_LIST_RESOURCE).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));

        final String jsonResponse = response.extract().asString();
        ServiceLogger.log(logTag, "response = " + jsonResponse);

        final ObjectMapper mapper = new ObjectMapper();
        Service patchService = null;
        try {
            patchService = mapper.readValue(jsonResponse, Service.class);
        } catch (Exception e) {
            fail("unable to map response to Service object: " + e.getMessage());
        }

        return patchService;
    }
    private void updateService(Service patchService) {
        patchService.setDescription(patchService.getDescription() + " - now patching");
        ServiceLogger.log(logTag, "testService_GetHistory: patch url = " + SERVICE_RESOURCE + " + id = " + patchService.getId());

        given().
            header("Content-type", APPLICATION_JSON_VALUE).
            header("AJP_eppn", userEPPN).
            body(patchService).
            expect().
            statusCode(OK.value()).
            when().
            patch(SERVICE_RESOURCE, patchService.getId()).
            then().
            body(matchesJsonSchemaInClasspath("Schemas/serviceSchema.json"));
    }
    private void checkHistory(Service patchService) {
        List<ServiceHistory> history = given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(OK.value()).
                when().get(SERVICE_HISTORY_RESOURCE, patchService.getId()).as(List.class);

        assertTrue("Expected history, but none returned", history.size() != 0);

        final ServiceHistory verify = history.get(0);
        assertTrue("IDs do not match", Integer.parseInt(verify.getServiceId()) == patchService.getId());
        assertTrue("Link is not empty string", verify.getLink().equals(""));
        assertTrue("Section is not marketplace", verify.getSection() == ServiceHistory.SectionEnum.marketplace);
        assertTrue("History period is not today", verify.getPeriod() == ServiceHistory.PeriodEnum.today);
        try {
            final int uid = UserDao.getUserID(userEPPN);
            final String uname = UserDao.getUserName(uid);
            assertTrue("User IDs do not match", verify.getUser().equals(Integer.toString(uid)));
            assertTrue("Message does not match expected", verify.getTitle().matches(uname + " updated the service on *"));
        } catch (SQLException e) {
            fail("Failed user lookup");
        }

    }

    /**
     * test case for get /services/{serviceID}/service_tags
     */
    @Test
    public void testServiceGet_ServiceTags(){

        final int serviceId = 2;
        final String tag1 = "tag_" + TestUserUtil.generateTime();

        final ServiceTag json = new ServiceTag();
        json.setServiceId(Integer.toString(serviceId));
        json.setName(tag1);

        given().
            header("Content-type", "application/json").
            header("AJP_eppn", userEPPN).
            body(json).
            expect().
            statusCode(OK.value()).
            when().
            post(SERVICE_TAGS_RESOURCE);

        final ArrayList<ServiceTag> tags =
                given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(OK.value()).
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
            statusCode(OK.value()).
            when().get("/services/" + serviceId + "/services_statistic");
    }

    /**
     * test case 1 of 3 for get /services/{serviceID}/dome-interfaces
     */
    @Test
    public void testServiceGet_DomeInterfaceWhenNoSortParametersAreGiven() {

        for (int i = 0; i < 5; i++) {
            final PostUpdateDomeInterface domeInterface = new PostUpdateDomeInterface();
            domeInterface.setVersion(20);
            domeInterface.setModelId((new Integer(i + 100)).toString());
            domeInterface.setInterfaceId("John Wayne");
            domeInterface.setDomeServer("http://ec2-52-88-73-23.us-west-2.compute.amazonaws.com:8080/DOMEApiServicesV7/");
            domeInterface.setName("FOR /services/#/dome-interfaces TEST");
            final List<Integer> path = new ArrayList<Integer>();
            path.add(new Integer(1 + i));
            path.add(new Integer(2 + i));
            path.add(new Integer(3 + i));
            path.add(new Integer(4 + i));
            path.add(new Integer(5 + i));
            domeInterface.setPath(path);
            domeInterface.setServiceId(2);
            domeInterface.setType("type");

            final DomeModelParam input = createDomeInterfaceParameter();
            final DomeModelParam input2 = createDomeInterfaceParameter();
            input.setName("First Input for Model " + (i + 100));
            input2.setName("Second Input for Model " + (i + 100));
            final DomeModelParam output = createDomeInterfaceParameter();
            final DomeModelParam output2 = createDomeInterfaceParameter();
            output.setName("First Output for Model " + (i + 100));
            output2.setName("Second Output for Model " + (i + 100));
            final Map<String, DomeModelParam> inputs = new HashMap<String, DomeModelParam>();
            inputs.put(input.getName(), input);
            inputs.put(input2.getName(), input2);
            Map<String, DomeModelParam> outputs = new HashMap<String, DomeModelParam>();
            outputs.put(output.getName(), output);
            outputs.put(output2.getName(), output2);

            domeInterface.setInParams(inputs);
            domeInterface.setOutParams(outputs);

            final ObjectMapper mapper = new ObjectMapper();
            String postDomeInterfaceJSONString = null;
            try {
                postDomeInterfaceJSONString = mapper.writeValueAsString(domeInterface);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN)
                .body(postDomeInterfaceJSONString).expect().statusCode(OK.value()).when()
                .post(DOME_INTERFACES_RESOURCE);
        }

        List<GetDomeInterface> receivedDomeInterfaces = Arrays.asList(given().header("Content-type", APPLICATION_JSON_VALUE)
                .header("AJP_eppn", userEPPN).expect()
                .statusCode(OK.value()).when().get(SERVICE_DOME_INTERFACES_RESOURCE, "2").as(GetDomeInterface[].class));

        for (int i = 0; i < receivedDomeInterfaces.size(); i++) {            
            assertTrue("testServiceGet_DomeInterfaceWhenNoSortParametersAreGiven: Service id values are not equal",
                    (receivedDomeInterfaces.get(i).getServiceId().equals(new BigDecimal(2))));
            assertTrue("testServiceGet_DomeInterfaceWhenNoSortParametersAreGiven: Did not receive expected number of inputs",
                    receivedDomeInterfaces.get(i).getInParams().size() == 2);        
            assertTrue("testServiceGet_DomeInterfaceWhenNoSortParametersAreGiven: Did not receive expected number of outputs",
                    receivedDomeInterfaces.get(i).getOutParams().size() == 2);
        }

    }

    /**
     * test case 2 of 3 for get /services/{serviceID}/dome-interfaces
     */
    @Test
    public void testServiceGet_DomeInterfaceWhenSortParametersAreGiven() {
        final String testDomeServerNum = "2";

        for (int i = 0; i < 5; i++) {
            final PostUpdateDomeInterface domeInterface = new PostUpdateDomeInterface();
            domeInterface.setVersion(20);
            domeInterface.setModelId((new Integer(i + 100)).toString());
            domeInterface.setInterfaceId("John Wayne");
            domeInterface.setDomeServer("http://ec2-52-88-73-23.us-west-2.compute.amazonaws.com:8080/DOMEApiServicesV7/");
            domeInterface.setName("FOR /services/#/dome-interfaces TEST");
            final List<Integer> path = new ArrayList<Integer>();
            path.add(new Integer(1 + i));
            path.add(new Integer(2 + i));
            path.add(new Integer(3 + i));
            path.add(new Integer(4 + i));
            path.add(new Integer(5 + i));
            domeInterface.setPath(path);
            domeInterface.setServiceId(new Integer(testDomeServerNum));
            domeInterface.setType("type");

            final DomeModelParam input = createDomeInterfaceParameter();
            final DomeModelParam input2 = createDomeInterfaceParameter();
            input.setName("First Input for Model " + (i + 100));
            input2.setName("Second Input for Model " + (i + 100));
            final DomeModelParam output = createDomeInterfaceParameter();
            final DomeModelParam output2 = createDomeInterfaceParameter();
            output.setName("First Output for Model " + (i + 100));
            output2.setName("Second Output for Model " + (i + 100));
            final Map<String, DomeModelParam> inputs = new HashMap<String, DomeModelParam>();
            inputs.put(input.getName(), input);
            inputs.put(input2.getName(), input2);
            final Map<String, DomeModelParam> outputs = new HashMap<String, DomeModelParam>();
            outputs.put(output.getName(), output);
            outputs.put(output2.getName(), output2);

            domeInterface.setInParams(inputs);
            domeInterface.setOutParams(outputs);

            final ObjectMapper mapper = new ObjectMapper();
            String postDomeInterfaceJSONString = null;
            try {
                postDomeInterfaceJSONString = mapper.writeValueAsString(domeInterface);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN)
                .body(postDomeInterfaceJSONString).expect().statusCode(OK.value()).when()
                .post(DOME_INTERFACES_RESOURCE);
        }

        final List<GetDomeInterface> receivedDomeInterfaces = Arrays
                .asList(given().header("Content-type", APPLICATION_JSON_VALUE)
                        .header("AJP_eppn", userEPPN)
                        .param("limit", 4).param("order", "DESC").param("sort", "interface_id")
                        .expect().statusCode(OK.value()).when().get(SERVICE_DOME_INTERFACES_RESOURCE, testDomeServerNum).as(GetDomeInterface[].class));

        assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: List is not correct size according to LIMIT", (receivedDomeInterfaces.size() == 4));

        for (int i = 0; i < receivedDomeInterfaces.size(); i++) {
            final GetDomeInterface tempDome = receivedDomeInterfaces.get(i);

            // Create this List to compare the tempDome path with this path
            final List<Integer> path = new ArrayList<Integer>();
            path.add(new Integer(1 + 4 - i));
            path.add(new Integer(2 + 4 - i));
            path.add(new Integer(3 + 4 - i));
            path.add(new Integer(4 + 4 - i));
            path.add(new Integer(5 + 4 - i));

            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Did not receive expected number of inputs",
                    tempDome.getInParams().size() == 2);        
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Did not receive expected number of outputs",
                    tempDome.getOutParams().size() == 2);
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Dome server values are not equal",
                    (tempDome.getDomeServer().equals("http://ec2-52-88-73-23.us-west-2.compute.amazonaws.com:8080/DOMEApiServicesV7/")));
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Version values are not equal", (tempDome.getVersion().equals(new BigDecimal(20))));
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Model ID values are not equal",
                    (tempDome.getModelId().equals((new Integer(4 - i + 100)).toString())));
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Interface ID values are not equal", (tempDome.getInterfaceId().equals("John Wayne")));
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Type values are not equal", (tempDome.getType().equals("type")));
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Name values are not equal", (tempDome.getName().equals("FOR /services/#/dome-interfaces TEST")));
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Path values are not equal", (tempDome.getPath().equals(convertIntegerListtoBigDecimalList(path))));
            assertTrue("testServiceGet_DomeInterfaceWhenSortParametersAreGiven: Service id values are not equal",
                    (tempDome.getServiceId().equals(new BigDecimal(testDomeServerNum))));

        }
    }

    /**
     * test case 3 of 3 for get /services/{serviceID}/dome-interfaces
     */
    @Test
    public void testServiceGet_DomeInterfaceWhenNoObjectsInReturnEntity() {
        final String testDomeServerNum = "2";

        final List<GetDomeInterface> receivedDomeInterfaces = Arrays.asList(given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN)
                .param("limit", 0).expect()
                .statusCode(OK.value()).when().get(SERVICE_DOME_INTERFACES_RESOURCE, testDomeServerNum).as(GetDomeInterface[].class));

        assertTrue("testServiceGet_DomeInterfaceWhenNoObjectsInReturnEntity: List is not correct size according to LIMIT", (receivedDomeInterfaces.size() == 0));
    }

    private PostUpdateDomeInterface createPostUpdateDomeInterface() {
        final PostUpdateDomeInterface domeInterface = new PostUpdateDomeInterface();
        domeInterface.setVersion(20);
        domeInterface.setModelId("1996");
        domeInterface.setInterfaceId("John Wayne");
        domeInterface.setDomeServer("http://ec2-52-88-73-23.us-west-2.compute.amazonaws.com:8080/DOMEApiServicesV7/");
        domeInterface.setName("Brian");
        final List<Integer> path = new ArrayList<Integer>();
        path.add(new Integer(1));
        path.add(new Integer(2));
        path.add(new Integer(3));
        path.add(new Integer(4));
        path.add(new Integer(5));
        domeInterface.setPath(path);
        domeInterface.setServiceId(1);
        domeInterface.setType("type");

        final DomeModelParam input = createDomeInterfaceParameter();
        final DomeModelParam input2 = createDomeInterfaceParameter();
        input.setName("Created Input for Post Dome Interface 1");
        input2.setName("Created Input for Post Dome Interface 2");
        DomeModelParam output = createDomeInterfaceParameter();
        final DomeModelParam output2 = createDomeInterfaceParameter();
        output.setName("Created Output for Post Dome Interface 1");
        output2.setName("Created Output for Post Dome Interface 2");
        final Map<String, DomeModelParam> inputs = new HashMap<String, DomeModelParam>();
        inputs.put(input.getName(), input);
        inputs.put(input2.getName(), input2);
        final Map<String, DomeModelParam> outputs = new HashMap<String, DomeModelParam>();
        outputs.put(output.getName(), output);
        outputs.put(output2.getName(), output2);

        domeInterface.setInParams(inputs);
        domeInterface.setOutParams(outputs);

        return domeInterface;
    }

    private DomeModelParam createDomeInterfaceParameter() {
        final DomeModelParam param = new DomeModelParam();
        param.setType("type");
        param.setName("name");
        param.setUnit("unit");
        param.setCategory("category");
        param.setValue("5");
        param.setParameterid("parameterId");
        param.setInstancename("instancename");

        return param;
    }

    private List<BigDecimal> convertIntegerListtoBigDecimalList(List<Integer> path) {
        final ListIterator<Integer> pathListIter = path.listIterator();
        final List<BigDecimal> ret = new ArrayList<BigDecimal>();

        while (pathListIter.hasNext()) {

            final Integer tempPathListInt = pathListIter.next();

            ret.add(new BigDecimal(tempPathListInt.toString()));
        }

        return ret;
    }

    /**
     * test case for POST /dome-interfaces when invalid input is sent
     */
    @Test
    public void testServicePost_DomeInterface_WhenInvalidInputIsSent() {

        // "domeServer is a very large number, something not in the database so
        // a foreign key error occurs
        final String jsonString = "{\"version\":23,\"modelId\":\"1996\",\"interfaceId\":\"John Wayne\",\"type\":\"type\",\"name\":\"Brian\",\"path\":null,\"serviceId\":19,\"domeServer\":\"1900\"}";

        given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(jsonString).expect().statusCode(METHOD_NOT_ALLOWED.value()).when()
        .post(DOME_INTERFACES_RESOURCE);

    }

    /**
     * test case for POST /dome-interfaces when valid info is sent
     */
    @Test
    public void testServicePost_DomeInterface_WhenValidInfoIsSent() {
        final PostUpdateDomeInterface sentDomeInterface = createPostUpdateDomeInterface();

        final ObjectMapper mapper = new ObjectMapper();
        String postDomeInterfaceJSONString = null;
        try {
            postDomeInterfaceJSONString = mapper.writeValueAsString(sentDomeInterface);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final GetDomeInterface receivedDomeInterface = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(postDomeInterfaceJSONString).expect()
                .statusCode(OK.value()).when().post(DOME_INTERFACES_RESOURCE).as(GetDomeInterface.class);

        final BigDecimal postUpdateVersion = new BigDecimal(Integer.toString(sentDomeInterface.getVersion()));
        final BigDecimal postUpdateServiceId = new BigDecimal(Integer.toString(sentDomeInterface.getServiceId()));

        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Did not receive expected number of inputs",
                receivedDomeInterface.getInParams().size() == 2);        
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Did not receive expected number of outputs",
                receivedDomeInterface.getOutParams().size() == 2);
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Dome server values are not equal",
                (receivedDomeInterface.getDomeServer().equals(sentDomeInterface.getDomeServer())));
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Version values are not equal", (receivedDomeInterface.getVersion().equals(postUpdateVersion)));
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Model ID values are not equal", (receivedDomeInterface.getModelId().equals(sentDomeInterface.getModelId())));
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Interface ID values are not equal",
                (receivedDomeInterface.getInterfaceId().equals(sentDomeInterface.getInterfaceId())));
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Type values are not equal", (receivedDomeInterface.getType().equals(sentDomeInterface.getType())));
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Name values are not equal", (receivedDomeInterface.getName().equals(sentDomeInterface.getName())));
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Path values are not equal",
                (receivedDomeInterface.getPath().equals(convertIntegerListtoBigDecimalList(sentDomeInterface.getPath()))));
        assertTrue("testServicePost_DomeInterface_WhenValidInfoIsSent: Dome server values are not equal", (receivedDomeInterface.getServiceId().equals(postUpdateServiceId)));

    }

    /**
     * test case for PATCH /dome-interfaces/{domeInterfaceId}
     */
    @Test
    public void testServicePatch_DomeInterface() {
        final PostUpdateDomeInterface newDomeInterface = createPostUpdateDomeInterface();

        final ObjectMapper postMapper = new ObjectMapper();
        String postDomeInterfaceJSONString = null;
        try {
            postDomeInterfaceJSONString = postMapper.writeValueAsString(newDomeInterface);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final GetDomeInterface createdDomeInterface = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(postDomeInterfaceJSONString).expect()
                .statusCode(OK.value()).when().post(DOME_INTERFACES_RESOURCE).as(GetDomeInterface.class);

        final PostUpdateDomeInterface patchDomeInterface = new PostUpdateDomeInterface();
        patchDomeInterface.setVersion(22);
        patchDomeInterface.setModelId("2016");
        patchDomeInterface.setInterfaceId("Marshall Mathers");
        patchDomeInterface.setDomeServer("http://ec2-52-88-73-23.us-west-2.compute.amazonaws.com:8080/DOMEApiServicesV7/");
        patchDomeInterface.setName("Batman");
        final List<Integer> path = new ArrayList<Integer>();
        path.add(new Integer(11));
        path.add(new Integer(12));
        path.add(new Integer(13));
        path.add(new Integer(14));
        path.add(new Integer(15));
        patchDomeInterface.setPath(path);
        patchDomeInterface.setServiceId(1);
        patchDomeInterface.setType("type2");

        final ObjectMapper mapper = new ObjectMapper();
        String patchedDomeInterfaceJSONString = null;
        try {
            patchedDomeInterfaceJSONString = mapper.writeValueAsString(patchDomeInterface);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final GetDomeInterface receivedDomeInterface = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(patchedDomeInterfaceJSONString).expect()
                .statusCode(OK.value()).when().patch(DOME_INTERFACE_BY_ID_RESOURCE, createdDomeInterface.getId()).as(GetDomeInterface.class);

        final BigDecimal postUpdateVersion = new BigDecimal(Integer.toString(patchDomeInterface.getVersion()));
        final BigDecimal postUpdateServiceId = new BigDecimal(Integer.toString(patchDomeInterface.getServiceId()));

        assertTrue("testServicePatch_DomeInterface: Dome server values are not equal", (receivedDomeInterface.getDomeServer().equals(patchDomeInterface.getDomeServer())));
        assertTrue("testServicePatch_DomeInterface: Version values are not equal", (receivedDomeInterface.getVersion().equals(postUpdateVersion)));
        assertTrue("testServicePatch_DomeInterface: Model ID values are not equal", (receivedDomeInterface.getModelId().equals(patchDomeInterface.getModelId())));
        assertTrue("testServicePatch_DomeInterface: Interface ID values are not equal", (receivedDomeInterface.getInterfaceId().equals(patchDomeInterface.getInterfaceId())));
        assertTrue("testServicePatch_DomeInterface: Type values are not equal", (receivedDomeInterface.getType().equals(patchDomeInterface.getType())));
        assertTrue("testServicePatch_DomeInterface: Name values are not equal", (receivedDomeInterface.getName().equals(patchDomeInterface.getName())));
        assertTrue("testServicePatch_DomeInterface: Path values are not equal",
                (receivedDomeInterface.getPath().equals(convertIntegerListtoBigDecimalList(patchDomeInterface.getPath()))));
        assertTrue("testServicePatch_DomeInterface: Dome server values are not equal", (receivedDomeInterface.getServiceId().equals(postUpdateServiceId)));
    }

    /**
     * test case for DELETE /dome-interfaces/{domeInterfaceId}
     */
    @Test
    public void testServiceDelete_DomeInterface() {
        final PostUpdateDomeInterface postDomeInterface = createPostUpdateDomeInterface();
        postDomeInterface.setInterfaceId("DELETE ME");
        postDomeInterface.setName("DELETEEEEE");

        final ObjectMapper mapper = new ObjectMapper();
        String postDomeInterfaceJSONString = null;
        try {
            postDomeInterfaceJSONString = mapper.writeValueAsString(postDomeInterface);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final GetDomeInterface postedDomeInterface = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(postDomeInterfaceJSONString).expect()
                .statusCode(OK.value()).when().post(DOME_INTERFACES_RESOURCE).as(GetDomeInterface.class);

        given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when().get(DOME_INTERFACE_BY_ID_RESOURCE, postedDomeInterface.getId());

        given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when().delete(DOME_INTERFACE_BY_ID_RESOURCE, postedDomeInterface.getId());

        given().header("AJP_eppn", userEPPN).expect().statusCode(NOT_FOUND.value()).when().get("/dome-interfaces/" + postedDomeInterface.getId());
    }

    /**
     * test case for GET /dome-interfaces/{domeInterfaceId}
     */
    @Test
    public void testServiceGet_DomeInterfaceById() {

        final PostUpdateDomeInterface postDomeInterface = createPostUpdateDomeInterface();

        final ObjectMapper mapper = new ObjectMapper();
        String postDomeInterfaceJSONString = null;
        try {
            postDomeInterfaceJSONString = mapper.writeValueAsString(postDomeInterface);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final GetDomeInterface postedDomeInterface = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(postDomeInterfaceJSONString).expect()
                .statusCode(OK.value()).when().post(DOME_INTERFACES_RESOURCE).as(GetDomeInterface.class);

        final GetDomeInterface readDomeInterface = given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when()
                .get(DOME_INTERFACE_BY_ID_RESOURCE, postedDomeInterface.getId()).as(GetDomeInterface.class);

        final BigDecimal postUpdateVersion = new BigDecimal(Integer.toString(postDomeInterface.getVersion()));
        final BigDecimal postUpdateServiceId = new BigDecimal(Integer.toString(postDomeInterface.getServiceId()));

        assertTrue("testServiceGet_DomeInterfaceById: Dome server values are not equal", (readDomeInterface.getDomeServer().equals(postDomeInterface.getDomeServer())));
        assertTrue("testServiceGet_DomeInterfaceById: Version values are not equal", (readDomeInterface.getVersion().equals(postUpdateVersion)));
        assertTrue("testServiceGet_DomeInterfaceById: Model ID values are not equal", (readDomeInterface.getModelId().equals(postDomeInterface.getModelId())));
        assertTrue("testServiceGet_DomeInterfaceById: Interface ID values are not equal", (readDomeInterface.getInterfaceId().equals(postDomeInterface.getInterfaceId())));
        assertTrue("testServiceGet_DomeInterfaceById: Type values are not equal", (readDomeInterface.getType().equals(postDomeInterface.getType())));
        assertTrue("testServiceGet_DomeInterfaceById: Name values are not equal", (readDomeInterface.getName().equals(postDomeInterface.getName())));
        assertTrue("testServiceGet_DomeInterfaceById: Path values are not equal",
                (readDomeInterface.getPath().equals(convertIntegerListtoBigDecimalList(postDomeInterface.getPath()))));
        assertTrue("testServiceGet_DomeInterfaceById: Dome server values are not equal", (readDomeInterface.getServiceId().equals(postUpdateServiceId)));

        given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when().delete(DOME_INTERFACE_BY_ID_RESOURCE, postedDomeInterface.getId());

    }

    /**
     * test case for get /services/{serviceID}/input-positions
     */
    @Test
    public void testServiceGet_InputPositions(){
        int sId = 3;
        given().
        header("AJP_eppn", userEPPN).
        expect().
        statusCode(OK.value()).
        when().get(SERVICE_INPUT_POS_RESOURCE, sId);
    }


    /**
     * test case for POST /service/{serviceID}/specifications
     */
    @Test
    public void testServicePost_Specification(){
        final String postedSpecificationJSONString = getSpecJSONString();

        //pathParam("serviceID", serviceId).
        ServiceLogger.log(logTag, "ServiceSpecifications object json = " + postedSpecificationJSONString);
        ServiceLogger.log(logTag, "posting for serviceId = " + serviceId);
        given().
            header("Content-type", APPLICATION_JSON_VALUE).
            header("AJP_eppn", userEPPN).
            body(postedSpecificationJSONString).
            expect().
            statusCode(OK.value()).
            when().
            post(SERVICE_SPECS_RESOURCE, serviceId);
    }

    /**
     * test case for POST /service/{serviceID}/specifications
     */
    @Test
    public void testServicePost_EmptySpecification(){
        final ServiceSpecifications specification = new ServiceSpecifications();
        final ObjectMapper mapper = new ObjectMapper();
        String postedSpecificationJSONString = null;
        try {
            postedSpecificationJSONString = mapper.writeValueAsString(specification);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ServiceLogger.log(logTag, "ServiceSpecifications object json = " + postedSpecificationJSONString);
        ServiceLogger.log(logTag, "posting for serviceId = " + serviceId);
        given().
            header("Content-type", APPLICATION_JSON_VALUE).
            header("AJP_eppn", userEPPN).
            body(postedSpecificationJSONString).
            expect().
            statusCode(OK.value()).
            when().
            post(SERVICE_SPECS_RESOURCE, serviceId);
    }

    /**
     * test GET /array_specifications
     */
    @Test
    public void testGet_ArraySpecification() {

        ArrayList<ServiceSpecifications> specs = new ArrayList<ServiceSpecifications>();
        int previousSpecId = 0;

        final ObjectMapper mapper = new ObjectMapper();

        final JsonNode jsonSpecs = given().
                param("_sort", "id").
                param("_order", "ASC").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(OK.value()).
                when().
                get(ARRAY_SPECS_RESOURCE).
                as(JsonNode.class);

        try {
            specs= mapper.readValue(mapper.treeAsTokens(jsonSpecs), new TypeReference<ArrayList<ServiceSpecifications>>() {
            });
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
        }

        // assert that items are ordered correctly
        for (ServiceSpecifications spec : specs) {
            final int newSpecId = Integer.parseInt(spec.getId());
            assertTrue("Items are not correctly ordered (ASC) by the id column", newSpecId > previousSpecId);
            previousSpecId = newSpecId;
        }
    }

    /**
     * test case for POST /array_specifications
     */
    @Test
    public void testPost_ArraySpecification() {
        final int numSpecs = 2;
        final String postedSpecificationJSONString = getSpecsJSONString(numSpecs);

        final ArrayList<ServiceSpecifications> specs = given().
            header("Content-type", APPLICATION_JSON_VALUE).
            header("AJP_eppn", userEPPN).
            body(postedSpecificationJSONString).
            expect().
            statusCode(OK.value()).
            when().
            post(ARRAY_SPECS_RESOURCE).
            as(ArrayList.class);

        assertTrue("Number of Specifications created is not number posted", specs.size() == numSpecs);
    }


    /**
     * test case for GET /service_runs/{id}
     */
    @Test
    public void testServiceGet_ServiceRunId(){
        final String serviceRunId = "1";

        final GetServiceRun received = given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(OK.value()).
                when().get(SERVICE_RUN_RESOURCE, serviceRunId).as(GetServiceRun.class);

        assertTrue("testServiceGet_ServiceRunId: serviceRunId values are not equal", received.getId().equals(serviceRunId));
        assertTrue("testServiceGet_ServiceRunId: serviceId values are not equal", received.getServiceId().equals("3"));
    }

    /**
     * test case for GET /service_runs
     */
    @Test
    public void testServiceGet_ServiceRunsFromListOfServiceIds() {
        final List<GetServiceRun> serviceRunsInTable = Arrays.asList(given().header("AJP_eppn", "joeengineer").param("serviceId", 1).param("serviceId", 3).expect()
                .statusCode(OK.value()).when().get(SERVICE_RUN_LIST_RESOURCE).as(GetServiceRun[].class));

        assertTrue("Expected service runs, found none", serviceRunsInTable.size() > 0);
        final GetServiceRun firstRun = serviceRunsInTable.get(0);

        assertTrue("Status value was not expected", firstRun.getStatus().equals(new BigDecimal(1)));
        assertTrue("AccountId value was not expected", firstRun.getAccountId().equals("550"));
        assertTrue("ServiceId value was not expected", firstRun.getServiceId().equals("3"));
    }

    /**
     * test case for PATCH /service_runs/{id}
     */
    @Test
    public void testServicePatch_ServiceRunId() {
        final ObjectMapper mapper = new ObjectMapper();
        final String serviceRunId = "1";
        final GetServiceRun serviceRun = new GetServiceRun();
        serviceRun.setStatus(new BigDecimal(1));
        serviceRun.setPercentCompleted(new BigDecimal(100));

        String patchedServiceRunIdJSONString = null;
        try {
            patchedServiceRunIdJSONString = mapper.writeValueAsString(serviceRun);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final GetServiceRun patched = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(patchedServiceRunIdJSONString).expect().statusCode(OK.value())
                .when().patch(SERVICE_RUN_RESOURCE, serviceRunId).as(GetServiceRun.class);

        assertTrue("testServicePatch_ServiceRunId: serviceRunId values are not equal", patched.getId().equals(serviceRunId));
        assertTrue("testServicePatch_ServiceRunId: serviceId values are not equal", patched.getServiceId().equals("3"));
        assertTrue("testServicePatch_ServiceRunId: status values are not equal", patched.getStatus().equals(new BigDecimal(1)));
        assertTrue("testServicePatch_ServiceRunId: percentage_complete values are not equal", patched.getPercentCompleted().equals(new BigDecimal(100)));

    }




    /*
     * test case for DELETE /service_runs/{id}
     */
    @Test
    public void testServiceDelete_ServiceRunId(){
        given().
        header("AJP_eppn", userEPPN).
        expect().
        statusCode(NOT_IMPLEMENTED.value()).
        when().delete(SERVICE_RUN_RESOURCE, serviceId);
    }

    public void testPost_InputPosition(){

        final ArrayList<ServiceInputPosition> positions = new ArrayList<ServiceInputPosition>();
        final ServiceInputPosition position1 = new ServiceInputPosition();
        position1.setName("SpecimenWidth");
        position1.setPosition(new BigDecimal(3.0));
        position1.setName("CrackLength");
        positions.add(position1);

        final ServiceInputPosition position2 = new ServiceInputPosition();
        position2.setPosition(new BigDecimal(4.0));
        position2.setName("Alpha");
        positions.add(position2);

        final ServiceInputPosition position3 = new ServiceInputPosition();
        position3.setPosition(new BigDecimal(5.0));
        final PostServiceInputPosition input = new PostServiceInputPosition();
        positions.add(position3);

        input.setPositions(positions);
        input.setServiceId("300");

        final ObjectMapper mapper = new ObjectMapper();
        String positionJSONString = null;

        try {
            positionJSONString = mapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        given().
            header("Content-type", APPLICATION_JSON_VALUE).
            header("AJP_eppn", userEPPN).
            body(positionJSONString).
            expect().
            statusCode(OK.value()).
            when().
            post(INPUT_POS_RESOURCE);

    }


    /*
     * test case for DELETE /input-positions/{positionInputId}
     */
    @Test
    public void testDelete_InputPositionByPositionInputId(){
        given().
        header("AJP_eppn", userEPPN).
        expect().
        statusCode(OK.value()).
        when().delete(INPUT_POS_BY_ID_RESOURCE, positionInputId);
    }


    /*
     * test case for PATCH /input-positions/{positionInputId}
     */
    @Test
    public void testPatch_InputPositionByPositionInputId(){
        final List<ServiceInputPosition> obj = new ArrayList<ServiceInputPosition>();

        final int interfaceId = 100;
        final ServiceInputPosition position1 = new ServiceInputPosition();
        position1.setName("SpecimenWidth");
        position1.setPosition(new BigDecimal(5000.0));
        obj.add(position1);
        final ObjectMapper mapper = new ObjectMapper();
        String patchedServiceInputPositionJSONString = null;
        try {
            patchedServiceInputPositionJSONString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        given().
            header("Content-type", APPLICATION_JSON_VALUE).
            header("AJP_eppn", userEPPN).
            body(patchedServiceInputPositionJSONString).
            expect().
            statusCode(OK.value()).
            when().
            patch(INPUT_POS_BY_ID_RESOURCE, interfaceId);    
    }    

    // create a service object to use as body in post
    private Service createNewServiceObjectToPost()
    {
        final Service service = new Service();
        service.setTitle("junit service test");
        service.setDescription("junit service test");
        service.setOwner(userEPPN);
        service.setServiceType("service type");
        service.setSpecifications("service specifications");
        return service;
    }

    // create a specification JSON String 
    private String getSpecJSONString() {

        final ServiceSpecifications specification = new ServiceSpecifications();
        specification.setId("16703234");
        specification.setServiceId(serviceId);
        specification.setDescription("testing with junit");
        specification.setInput(new Integer(1));
        specification.setOutput(new Integer(1));
        final ServiceSpecialSpecifications special = new ServiceSpecialSpecifications();
        special.setSpecification("stuck");
        special.setSpecificationId("morejunk");
        special.setData("this is the data");
        final ArrayList<ServiceSpecialSpecifications> specialList = new ArrayList<ServiceSpecialSpecifications>();
        specialList.add(special);
        specification.setSpecial(specialList);
        final RunStats runstats = new RunStats();
        runstats.setFail(new Integer(0));
        runstats.setSuccess(new Integer(0));
        specification.setRunStats(runstats);
        final UsageStats usagestats = new UsageStats();
        usagestats.setAdded(new Integer(0));
        usagestats.setMembers(new Integer(0));
        specification.setUsageStats(usagestats);

        final ObjectMapper mapper = new ObjectMapper();
        String postedSpecificationJSONString = null;
        try {
            postedSpecificationJSONString = mapper.writeValueAsString(specification);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return postedSpecificationJSONString;
    }

    // create multiple specifications JSON String 
    private String getSpecsJSONString(int num) {

        final ArrayList<ServiceSpecifications> specs = new ArrayList<ServiceSpecifications>();

        for  (int i=0; i<num; i++) {

            final ServiceSpecifications specification = new ServiceSpecifications();
            specification.setId("16703234" + i);
            specification.setServiceId(serviceId);
            specification.setDescription("testing with junit" + i);
            specification.setInput(new Integer(1));
            specification.setOutput(new Integer(1));
            final ServiceSpecialSpecifications special = new ServiceSpecialSpecifications();
            special.setSpecification("stuck");
            special.setSpecificationId("morejunk " + i);
            special.setData("this is the data " + i);
            final ArrayList<ServiceSpecialSpecifications> specialList = new ArrayList<ServiceSpecialSpecifications>();
            specialList.add(special);
            specification.setSpecial(specialList);
            // Need to modify after the service run is fixed.  Refer to DMC-878
            final RunStats runstats = new RunStats();
            runstats.setFail(new Integer(0));
            runstats.setSuccess(new Integer(0));
            specification.setRunStats(runstats);
            final UsageStats usagestats = new UsageStats();
            usagestats.setAdded(new Integer(0));
            usagestats.setMembers(new Integer(0));
            specification.setUsageStats(usagestats);

            specs.add(specification);
        }

        final ObjectMapper mapper = new ObjectMapper();
        String postedSpecificationJSONString = null;
        try {
            postedSpecificationJSONString = mapper.writeValueAsString(specs);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return postedSpecificationJSONString;
    }

}
