package org.dmc.services;

import org.dmc.services.data.models.ResourceAssessmentModel;
import org.dmc.services.data.models.ResourceJobModel;
import org.dmc.services.data.models.ResourceLabModel;
import org.dmc.services.data.models.ResourceProjectModel;
import org.dmc.services.data.models.ResourceCourseModel;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import static org.junit.Assert.assertTrue;

/**
 * Created by Josh Lustig on 6/29/2016.
 */

public class ResourceIT extends BaseIT {

    private String RESOURCE_ASSESSMENT_GET_POST  = "/resource/assessment";
    private String RESOURCE_ASSESSMENT_DELETE  = "/resource/assessment/{id}";
    
    private String RESOURCE_JOB_GET_POST  = "/resource/job";
    private String RESOURCE_JOB_DELETE = "/resource/job/{id}";

    private	String RESOURCE_LAB_GET_POST  = "/resource/lab";
    private String RESOURCE_LAB_DELETE = "/resource/lab/{id}";

    private String RESOURCE_PROJECT_GET_POST  = "/resource/project";
    private String RESOURCE_PROJECT_DELETE  = "/resource/project/{id}";

    private String RESOURCE_COURSE_GET_POST  = "/resource/course";
    private String RESOURCE_COURSE_DELETE = "/resource/course/{id}";

    public static final String userEPPN = "fforgeadmin";

 
    /* 
     * TEST CASES FOR RESOURCE: 
     * LAB
     * PROJECT
     * ASSESSMENT
     * COURSE
     * JOB
     * 
     * ALL TEST CASES GET ARRAY LIST, POST NEW ENTITY, COMPARE LENGTHS, DELETE THE SAME ENTITY, AND COMPARE LENGTHS AGAIN. 
     */
    @Test
    public void addAndGetAndDeleteAssessment () {
    	    
    	//Get a list of the current images
        ArrayList<ResourceAssessmentModel> original =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_ASSESSMENT_GET_POST).
                as(ArrayList.class);
        
        int id = addAssessment();
        
        //Make sure the added image returns a valid id
        assertTrue("Resource ID returned invalid", id != -1);

        //Get a list of the new images 
        ArrayList<ResourceAssessmentModel> newList =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_ASSESSMENT_GET_POST).
                as(ArrayList.class);

        int numBefore = (original != null) ? original.size() : 0;
        int numAfter  = (newList != null) ? newList.size() : 0;
        int numExpected = numBefore + 1;
        //the new list and old list should only differ by one
        assertTrue ("Adding resource failed"  , numAfter == numExpected);

        delete(id, RESOURCE_ASSESSMENT_DELETE);

        ArrayList<ResourceAssessmentModel> after =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_ASSESSMENT_GET_POST).
                as(ArrayList.class);

        int numAfterDelete  = (after != null) ? after.size() : 0;
        assertTrue ("Deleting resource failed", numAfterDelete == numBefore);

    }
    
    @Test
    public void addAndGetAndDeleteJob() {
	    
    	//Get a list of the current images
        ArrayList<ResourceJobModel> original =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_JOB_GET_POST).
                as(ArrayList.class);
        
        int id = addJob();
        
        //Make sure the added image returns a valid id
        assertTrue("Resource ID returned invalid", id != -1);

        //Get a list of the new images 
        ArrayList<ResourceJobModel> newList =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_JOB_GET_POST).
                as(ArrayList.class);

        int numBefore = (original != null) ? original.size() : 0;
        int numAfter  = (newList != null) ? newList.size() : 0;
        int numExpected = numBefore + 1;
        
        //the new list and old list should only differ by one
        assertTrue ("Adding resource failed"  , numAfter == numExpected);

        delete(id, RESOURCE_JOB_DELETE);

        ArrayList<ResourceJobModel> after =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_JOB_GET_POST).
                as(ArrayList.class);

        int numAfterDelete  = (after != null) ? after.size() : 0;
        assertTrue ("Deleting resource failed", numAfterDelete == numBefore);

    }
    
    
    @Test
    public void addAndGetAndDeleteCourse() {
	    
    	//Get a list of the current images
        ArrayList<ResourceCourseModel> original =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_COURSE_GET_POST).
                as(ArrayList.class);
        
        int id = addCourse();
        
        //Make sure the added image returns a valid id
        assertTrue("Resource ID returned invalid", id != -1);

        //Get a list of the new images 
        ArrayList<ResourceCourseModel> newList =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_COURSE_GET_POST).
                as(ArrayList.class);

        int numBefore = (original != null) ? original.size() : 0;
        int numAfter  = (newList != null) ? newList.size() : 0;
        int numExpected = numBefore + 1;
        
        //the new list and old list should only differ by one
        assertTrue ("Adding resource failed"  , numAfter == numExpected);

        delete(id, RESOURCE_COURSE_DELETE);

        ArrayList<ResourceCourseModel> after =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_COURSE_GET_POST).
                as(ArrayList.class);

        int numAfterDelete  = (after != null) ? after.size() : 0;
        assertTrue ("Deleting resource failed", numAfterDelete == numBefore);

    }
    
    
    @Test
    public void addAndGetAndDeleteProject() {
	    
    	//Get a list of the current images
        ArrayList<ResourceProjectModel> original =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_PROJECT_GET_POST).
                as(ArrayList.class);
        
        int id = addProject();
        
        //Make sure the added image returns a valid id
        assertTrue("Resource ID returned invalid", id != -1);

        //Get a list of the new images 
        ArrayList<ResourceProjectModel> newList =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_PROJECT_GET_POST).
                as(ArrayList.class);

        int numBefore = (original != null) ? original.size() : 0;
        int numAfter  = (newList != null) ? newList.size() : 0;
        int numExpected = numBefore + 1;
        
        //the new list and old list should only differ by one
        assertTrue ("Adding resource failed"  , numAfter == numExpected);

        delete(id, RESOURCE_PROJECT_DELETE);

        ArrayList<ResourceProjectModel> after =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_PROJECT_GET_POST).
                as(ArrayList.class);

        int numAfterDelete  = (after != null) ? after.size() : 0;
        assertTrue ("Deleting resource failed", numAfterDelete == numBefore);

    }
    
    
    
    @Test
    public void addAndGetAndDeleteLab() {
	    
    	//Get a list of the current images
        ArrayList<ResourceLabModel> original =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_LAB_GET_POST).
                as(ArrayList.class);
        
        int id = addProject();
        
        //Make sure the added image returns a valid id
        assertTrue("Resource ID returned invalid", id != -1);

        //Get a list of the new images 
        ArrayList<ResourceLabModel> newList =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_LAB_GET_POST).
                as(ArrayList.class);

        int numBefore = (original != null) ? original.size() : 0;
        int numAfter  = (newList != null) ? newList.size() : 0;
        int numExpected = numBefore + 1;
        
        //the new list and old list should only differ by one
        assertTrue ("Adding resource failed"  , numAfter == numExpected);

        delete(id, RESOURCE_LAB_DELETE);

        ArrayList<ResourceLabModel> after =
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(RESOURCE_LAB_GET_POST).
                as(ArrayList.class);

        int numAfterDelete  = (after != null) ? after.size() : 0;
        assertTrue ("Deleting resource failed", numAfterDelete == numBefore);

    }
    
    
    /*
     * Helper Functions
     */
    
    public int addAssessment() {

        int id = -1;
        ResourceAssessmentModel json = new ResourceAssessmentModel();
        json.setId(1);
        json.setTitle("Title");
        json.setImage("Image"); 
        json.setDescription("Description");
        json.setDateCreated("Date"); 
        json.setLink("Link");
        json.setContact("Contact"); 
        json.setHighlighted(true);
        
        Integer createdId  = 
        given().
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(RESOURCE_ASSESSMENT_GET_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;
        return id;
    }
    
    public int addJob() {

        int id = -1;
        ResourceJobModel json = new ResourceJobModel();
        json.setId(1);
        json.setTitle("Title");
        json.setImage("Image"); 
        json.setDescription("Description");
        json.setDateCreated("Date"); 
        json.setLink("Link");
        json.setContact("Contact"); 
        json.setHighlighted(true);
        
        Integer createdId  = 
        given().
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(RESOURCE_JOB_GET_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;
        return id;
    }
    
    public int addCourse() {

        int id = -1;
        ResourceCourseModel json = new ResourceCourseModel();
        json.setId(1);
        json.setTitle("Title");
        json.setImage("Image"); 
        json.setDescription("Description");
        json.setDateCreated("Date"); 
        json.setLink("Link");
        json.setContact("Contact"); 
        json.setHighlighted(true);
        
        Integer createdId  = 
        given().
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(RESOURCE_COURSE_GET_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;
        return id;
    }
    
    public int addLab() {

        int id = -1;
        ResourceLabModel json = new ResourceLabModel();
        json.setId(1);
        json.setTitle("Title");
        json.setImage("Image"); 
        json.setDescription("Description");
        json.setDateCreated("Date"); 
        json.setLink("Link");
        json.setContact("Contact"); 
        json.setHighlighted(true);
        
        Integer createdId  = 
        given().
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(RESOURCE_LAB_GET_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;
        return id;
    }
    
    public int addProject() {

        int id = -1;
        ResourceProjectModel json = new ResourceProjectModel();
        json.setId(1);
        json.setTitle("Title");
        json.setImage("Image"); 
        json.setDescription("Description");
        json.setDateCreated("Date"); 
        json.setLink("Link");
        json.setContact("Contact"); 
        json.setHighlighted(true);
        
        Integer createdId  = 
        given().
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(RESOURCE_PROJECT_GET_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        id = (createdId != null) ? createdId.intValue() : -1;
        return id;
    }
    
    public void delete (int id, String endpoint) {
        given().
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                delete(endpoint, id);
    }
    


}
