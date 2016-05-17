package org.dmc.services;

import org.dmc.services.services.PostSharedService;
import org.dmc.services.services.SharedService;
import org.dmc.services.services.SharedServicesController;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

/**
 * Created by 200005921 on 5/13/2016.
 */
public class SharedServiceIT  extends BaseIT{

    private final String logTag = SharedServiceIT.class.getName();

    public static final String SHARED_SERVICE_ENDPOINT = "/shared-services";
    public static final String SHARED_SERVICE_GET_BY_ID = "/shared-services/{id}";

    @Test
    public void testGetSharedServices () {

        given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(200).
                when().
                get(SHARED_SERVICE_ENDPOINT);
    }

    @Test
    public void testInsertSharedService () {

        // Services from sample data: service_id, owner_id, user_name
        //        1;102;"fforgeadmin"
        //        2;102;"fforgeadmin"

        ArrayList<SharedService> sharedServicesBefore =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(200).
                        when().
                        get(SHARED_SERVICE_ENDPOINT).
                        as(ArrayList.class);

        int serviceId = 1;
        int userId = 102;
        int idSharedService = addSharedService(serviceId, userId);
        assertTrue(idSharedService != -1);
        ServiceLogger.log(logTag, "inserted shared servie, id =" + idSharedService);

        ArrayList<SharedService> sharedServicesAfter =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(200).
                        when().
                        get(SHARED_SERVICE_ENDPOINT).
                        as(ArrayList.class);

        int numBefore = (sharedServicesBefore != null) ? sharedServicesBefore.size() : 0;
        int numAfter  = (sharedServicesAfter != null) ? sharedServicesAfter.size() : 0;
        int numExpected = numBefore + 1;
        assertTrue (numAfter == numExpected);

        ArrayList<SharedService> sharedServiceLookup =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(200).
                        when().
                        get(SHARED_SERVICE_GET_BY_ID, idSharedService).
                        as(ArrayList.class);
        int numLookup = (sharedServiceLookup != null) ? sharedServiceLookup.size() : 0;
        ServiceLogger.log(logTag, "get shared service by id returns list of size =" + numLookup);
        assertTrue (numLookup == 1);

    }

    public int addSharedService (int serviceId, int userId) {

        int id = -1;
        PostSharedService sharedService = new PostSharedService();
        sharedService.setServiceId(Integer.toString(serviceId));
        sharedService.setAccountId(Integer.toString(userId));
        sharedService.setProfileId(Integer.toString(userId));

        Integer createdId  = given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(sharedService).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(SHARED_SERVICE_ENDPOINT).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;

        return id;

    }
}
