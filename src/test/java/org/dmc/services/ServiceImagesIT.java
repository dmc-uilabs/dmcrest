package org.dmc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.dmc.services.company.Company;
import org.dmc.services.services.ServiceImages;
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
public class ServiceImagesIT extends BaseIT {

    //    GET /services/{serviceID}/service_tags
    //    POST /service_tags
    //    GET /service_tags
    //    DELETE /service_tags/{serviceTagID}

    private static final String SERVICE_IMAGES_POST  = "/service_images";
    private static final String SERVICE_IMAGES_DELETE = "/service_images/{imageID}";
    private static final String SERVICE_IMAGES_GET= "/services/{serviceID}/service_images";


    public static final String userEPPN = "fforgeadmin";

    @Test
    public void deleteNonExistingImage () {

        int imageID = 1223456789;
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                when().
                delete(SERVICE_IMAGES_DELETE, imageID);

    }
    
    @Test
    public void addInvalidId () {

        int serviceID = 1223456789;
        String url = "fake"; 
        
        ServiceImages json = new ServiceImages();
        json.setServiceId(serviceID);
        json.setUrl(url);
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(json).
                expect().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                when().
                post(SERVICE_IMAGES_POST);
            
    }
    
    
    @Test
    public void getImageUnit () {
        int serviceID = 2;
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(SERVICE_IMAGES_GET, serviceID);
    }
  
    /*
     * Test Case for POST /services/service_images
     * test case for GET /services/{serviceID}/service_images
     * and DELETE /service_images/{imageId}
     */
    @Test
    public void addAndGetAndDeleteServiceImages () {
    	
    	int serviceID = 2;
    	
    	
    	//Get a list of the current images
        ArrayList<ServiceImages> originalImages =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(HttpStatus.OK.value()).
                        when().
                        get(SERVICE_IMAGES_GET, serviceID).
                        as(ArrayList.class);


        String url = "FakeUrl";
        int serviceImageId = addImage(serviceID, url);
        
        //Make sure the added image returns a valid id
        assertTrue("Image ID returned invalid", serviceImageId != -1);

        //Get a list of the new images 
        ArrayList<ServiceImages> newImages =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(HttpStatus.OK.value()).
                        when().
                        get(SERVICE_IMAGES_GET, serviceID).
                        as(ArrayList.class);

        int numBefore = (originalImages != null) ? originalImages.size() : 0;
        int numAfter  = (newImages != null) ? newImages.size() : 0;
        int numExpected = numBefore + 1;
        //the new list and old list should only differ by one
        assertTrue ("Adding an image failed"  , numAfter == numExpected);

        deleteExistingImage(serviceImageId);

        ArrayList<ServiceImages> afterDeleteImages =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(HttpStatus.OK.value()).
                        when().
                        get(SERVICE_IMAGES_GET, serviceID).
                        as(ArrayList.class);

        int numAfterDelete  = (afterDeleteImages != null) ? afterDeleteImages.size() : 0;
        assertTrue ("Deleting an image failed", numAfterDelete == numBefore);

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
                post(SERVICE_IMAGES_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;

        return id;
    }
    
    public void deleteExistingImage (int imageID) {

        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.NO_CONTENT.value()).
                when().
                delete(SERVICE_IMAGES_DELETE, imageID);
    }
    


}
