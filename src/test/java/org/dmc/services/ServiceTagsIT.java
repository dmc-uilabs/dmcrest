package org.dmc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.dmc.services.company.Company;
import org.dmc.services.services.ServiceTag;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

/**
 * Created by 200005921 on 5/12/2016.
 */
public class ServiceTagsIT extends BaseIT {

    //    GET /services/{serviceID}/service_tags
    //    POST /service_tags
    //    GET /service_tags
    //    DELETE /service_tags/{serviceTagID}

    private static final String SERVICE_TAGS_POST              = "/service_tags";
    private static final String SERVICE_TAGS_DELETE            = "/service_tags/{serviceTagID}";
    private static final String SERVICE_TAGS_GET_ALL           = "/service_tags";
    private static final String SERVICE_TAGS_GET_BY_SERVICE_ID = "/services/{serviceID}/service_tags";

    public static final String userEPPN = "fforgeadmin";

    @Test
    public void insertServiceTag () {

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
                post(SERVICE_TAGS_POST);

    }

    @Test
    public void getServiceTagsAll () {

        given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(200).
                when().
                get(SERVICE_TAGS_GET_ALL);

    }




    @Test
    public void addAndGetAndDeleteServiceTag () {

        ArrayList<ServiceTag> originalTags =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(200).
                        when().
                        get(SERVICE_TAGS_GET_ALL).
                        as(ArrayList.class);


        String tag1 = "tag_" + TestUserUtil.generateTime();
        int serviceId = 2;
        int serviceTagId = addTag(serviceId, tag1);
        assertTrue(serviceId != -1);

        ArrayList<ServiceTag> newTags =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(200).
                        when().
                        get(SERVICE_TAGS_GET_ALL).
                        as(ArrayList.class);

        int numBefore = (originalTags != null) ? originalTags.size() : 0;
        int numAfter  = (newTags != null) ? newTags.size() : 0;
        int numExpected = numBefore + 1;
        assertTrue (numAfter == numExpected);

        deleteExistingTag(serviceTagId);

        ArrayList<ServiceTag> afterDeleteTags =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(200).
                        when().
                        get(SERVICE_TAGS_GET_ALL).
                        as(ArrayList.class);

        int numAfterDelete  = (afterDeleteTags != null) ? afterDeleteTags.size() : 0;
        assertTrue (numAfterDelete == numBefore);

    }

    @Test
    public void deleteNonExistingTag () {

        String serviceTagId = "1223456789";
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                when().
                delete("/service_tags/" + serviceTagId);
    }

    @Test
    public void getServiceTagsForService () {

        Integer serviceId = new Integer(2);

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

    public int addTag (int serviceId, String tag) {

        int id = -1;
        ServiceTag json = new ServiceTag();
        json.setServiceId(Integer.toString(serviceId));
        json.setName(tag);

        Integer createdId  = given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(SERVICE_TAGS_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;

        return id;

    }

    public void deleteExistingTag (int serviceTagId) {

        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.NO_CONTENT.value()).
                when().
                delete("/service_tags/" + serviceTagId);

    }


}
