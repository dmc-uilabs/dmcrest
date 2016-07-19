package org.dmc.services;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

import org.dmc.services.event.CommunityEvent;
import org.junit.Test;
import org.springframework.http.HttpStatus;


public class EventIT extends BaseIT
{
	private static final String COMMUNITY_EVENTS_POST  = "/events";
    private static final String COMMUNITY_EVENTS_DELETE = "/events/{id}";
    private static final String COMMUNITY_EVENTS_GET= "/events";
    private static final String COMMUNITY_EVENTS_PATCH= "/events/{id}";
    
    /*
	 * test case for GET /events
	 */
	
    @Test
	public void testGet_events()
	{
    	given().
    	header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.OK.value()).
		when().
		get("/events");
	}
    
    /*
     * Test Case for POST /services/service_images
     * test case for GET /services/{serviceID}/service_images
     * and DELETE /service_images/{imageId}
     */
    @Test
    public void addAndGetAndDeleteEvents ()
    {
    	//Get a list of the current images
    	
    	ArrayList<CommunityEvent> originalEvents =
    			given().
    	    	header("Content-type", "application/json").
    	        header("AJP_eppn", userEPPN).
    			expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(COMMUNITY_EVENTS_GET).
                as(ArrayList.class);


        int eventId = addEvent();
        
        //Make sure the added image returns a valid id
        assertTrue("Event ID returned invalid", eventId != -1);

        //Get a list of the new images 
        ArrayList<CommunityEvent> newEvent =
        		given().
            	header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
        		expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(COMMUNITY_EVENTS_GET).
                as(ArrayList.class);

        int numBefore = (originalEvents != null) ? originalEvents.size() : 0;
        int numAfter  = (newEvent != null) ? newEvent.size() : 0;
        int numExpected = numBefore + 1;
        //the new list and old list should only differ by one
        assertTrue ("Adding an Event failed"  , numAfter == numExpected);

        deleteExistingEvent(eventId);

        ArrayList<CommunityEvent> afterDeleteEvent =
        given().
    	header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(COMMUNITY_EVENTS_GET).
                as(ArrayList.class);

        int numAfterDelete  = (afterDeleteEvent != null) ? afterDeleteEvent.size() : 0;
        assertTrue ("Deleting an event failed", numAfterDelete == numBefore);

    }
    
    
    public int addEvent()
    {
        CommunityEvent json = new CommunityEvent();        
        
        json.setId("0");
        json.setTitle("Fake Title");
        json.setDate("01/01/2001");
        json.setStartTime("Yesterday");
        json.setEndTime("Never");
        json.setAddress("Everywhere");
        json.setDescription("A fake event that never ends");
        
        Integer createdId  = 
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(COMMUNITY_EVENTS_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        int id = (createdId != null) ? createdId.intValue() : -1;
        assertTrue ("No Event Created / Id returned was 0 or -1", id > 0);

        return id;
    }
    
    public void deleteExistingEvent (int eventId)
    {
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                delete(COMMUNITY_EVENTS_DELETE, eventId);
    }
	
	
}
