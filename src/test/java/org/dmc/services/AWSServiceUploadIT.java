package org.dmc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.dmc.services.company.Company;
import org.dmc.services.profile.Profile;
import org.dmc.services.services.ServiceImages;
import org.dmc.services.services.ServiceImagesDao;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.services.ServiceDocument;
import org.dmc.services.DMCServiceException;

import org.dmc.services.utility.TestUserUtil;
import org.dmc.services.verification.VerificationPatch;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import static org.junit.Assert.assertTrue;

/**Tests for all AWS Service Uploads 

    GET /services/{serviceID}/service_images
    POST /service_images    
    DELETE /service_images/{imageID}
    
    GET "/services/{serviceID}/service_documents
    POST /service_documents
    DELETE /service_documents/{docId}
 * @author jjlustig
 */
public class AWSServiceUploadIT extends BaseIT {

	private final String logTag = AWSServiceUploadIT.class.getName();


    private static final String SERVICE_IMAGES_POST  = "/service_images";
    private static final String SERVICE_IMAGES_DELETE = "/service_images/{imageID}";
    private static final String SERVICE_IMAGES_GET= "/services/{serviceID}/service_images";

    
    private static final String SERVICE_DOCUMENTS_POST  = "/service_documents";
    private static final String SERVICE_DOCUMENTS_DELETE = "/service_documents/{docId}";
    private static final String SERVICE_DOCUMENTS_GET= "/services/{serviceID}/service_documents";
    private static final String PATCH= "/verify";
    
    public static final String userEPPN = "fforgeadmin";
    public static final String userID = "102"; 

	private Connection connection = DBConnector.connection();

    
    /*
     * TEST CASES FOR SERVICE IMAGES
     */
    @Test
    public void deleteNonExistingImage () {

        int imageID = -1;
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

        int serviceID = -1;
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg"; 
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

        //Hardcoded URL
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg";
        
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
    
    
    
    @Test
    public void PatchServiceImages () throws DMCServiceException{
    	
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

        //Hardcoded URL
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg";
        
        
        //Manual Insert
		PreparedStatement statement;
		Util util = Util.getInstance();
		int serviceImageId;

        try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}

		try {
			String query = "INSERT INTO service_images (service_id, url) VALUES (?, ?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, serviceID);
			statement.setString(2, url);
			statement.executeUpdate();
			serviceImageId = util.getGeneratedKey(statement, "id");
			connection.commit();
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, "SQL EXCEPTION ----- " + e.getMessage());
			try {
				if (connection != null) {
					ServiceLogger.log(logTag, "createServiceImage transaction rolled back");
					connection.rollback();
				}
			} catch (SQLException ex) {
				ServiceLogger.log(logTag, ex.getMessage());
				throw new DMCServiceException(DMCError.OtherSQLError, ex.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
        
		ServiceLogger.log(logTag, "Manually created id is " + serviceImageId);

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

        
        
        //Call patch image through validation machine endpoint 
        VerificationPatch json = new VerificationPatch();
        json.setFolder("Test");
        json.setUrl(url);
        json.setScanLog("good");
        json.setId(serviceImageId);
        json.setTable("service_images");
        json.setUserEPPN(userEPPN);
        json.setUrlColumn("url");
        json.setResourceType("Test");
        json.setVerified(false);
        json.setIdColumn("id");
        
        ObjectMapper mapper = new ObjectMapper();
		String patchedJSONString = null;
		try {
			patchedJSONString = mapper.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		ServiceLogger.log(logTag, "Conversion to Json object");

        VerificationPatch returnPatch = given()
        	.header("Content-type", "application/json")
            .body(patchedJSONString)
            .expect()
            .statusCode(HttpStatus.OK.value())
            .when()
            .patch(PATCH).as(VerificationPatch.class); 

        //Test if patched 
        //Extract
      	String ResourceType = returnPatch.getResourceType(); 
      	assert(ResourceType == "Test");
      		
		ServiceLogger.log(logTag, "Patched worked?");

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

        ServiceImages json = new ServiceImages();
        json.setServiceId(serviceId);
        json.setUrl(url);

        Integer createdId  = 
        given().
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

        int id = (createdId != null) ? createdId.intValue() : -1;

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
    
    
    
  
    
    
    
    
    /*
     * SERVICE DOCUMENTS TESTS
     */
    
    
    
    @Test
    public void deleteNonExistingDoc () {

        int DocID = -1;
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                when().
                delete(SERVICE_DOCUMENTS_DELETE, DocID);

    }
    
    @Test
    public void addInvalidDocId () {

        String ServiceID = "-1";
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg"; 
        ServiceDocument json = new ServiceDocument();
        json.setFile(url);
        json.setServiceId(ServiceID);
        json.setOwnerId(userID);
        json.setOwner(userEPPN);
        json.setServiceDocumentId("temp");
        json.setTitle("temp");
        json.setId("-1");
        json.setSize("big");
        json.setModifed("test");
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(json).
                expect().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                when().
                post(SERVICE_DOCUMENTS_POST);
            
    }
    
    @Test
    public void addAndGetAndDeleteServiceDocs () {
    	
    	int serviceID = 2;
    
    	//Get a list of the current images
        ArrayList<ServiceDocument> originalDoc =
        given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(SERVICE_DOCUMENTS_GET, serviceID).
                as(ArrayList.class);

        //Hardcoded URL
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg";
        
        int serviceDocId = addDoc(serviceID, url);
        
        //Make sure the added image returns a valid id
        assertTrue("Doc ID returned invalid", serviceDocId != -1);

        //Get a list of the new images 
        ArrayList<ServiceDocument> newDocs =
        given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(SERVICE_DOCUMENTS_GET, serviceID).
                as(ArrayList.class);

        int numBefore = (originalDoc != null) ? originalDoc.size() : 0;
        int numAfter  = (newDocs != null) ? newDocs.size() : 0;
        int numExpected = numBefore + 1;
        //the new list and old list should only differ by one
        assertTrue ("Adding an doc failed"  , numAfter == numExpected);

        
        //Wait until scan done 
        deleteDoc(serviceDocId);

        ArrayList<ServiceDocument> afterDeleteDocs =
        given().
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(SERVICE_DOCUMENTS_GET, serviceID).
                as(ArrayList.class);

        int numAfterDelete  = (afterDeleteDocs != null) ? afterDeleteDocs.size() : 0;
        assertTrue ("Deleting an docs failed", numAfterDelete == numBefore);

    }
    
    
    public int addDoc (int serviceId, String url) {

        ServiceDocument json = new ServiceDocument();
        json.setFile(url);
        json.setServiceId(Integer.toString(serviceId));
        json.setOwnerId(userID);
        json.setOwner(userEPPN);
        json.setServiceDocumentId("temp");
        json.setTitle("temp");
        json.setId("-1");
        json.setSize("big");
        json.setModifed("test");

        Integer createdId  = 
        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(SERVICE_DOCUMENTS_POST).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        int id = (createdId != null) ? createdId.intValue() : -1;
        return id;
    }
    
    public void deleteDoc (int docID) {

        given().
                header("Content-type", "application/json").
                header("AJP_eppn", userEPPN).
                expect().
                statusCode(HttpStatus.NO_CONTENT.value()).
                when().
                delete(SERVICE_DOCUMENTS_DELETE, docID);
    }
    
    


}
