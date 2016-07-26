package org.dmc.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import static com.jayway.restassured.RestAssured.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dmc.services.projects.ProjectDocument;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.verification.VerificationPatch;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//@Ignore
public class AWSProjectUploadIT extends BaseIT {

    // Documents
    private static final String GET_PROJECT_DOCS = "/projects/{projectID}/project_documents";
    private static final String POST_PROJECT_DOCS = "/project_documents";
    private static final String DELETE_PROJECT_DOCS = "/project_documents/{id}";

    private static final String PATCH= "/verify";

    private static final String adminUser = "fforgeadmin";
    private final String logTag = AWSProjectUploadIT.class.getName();
    
    public static final String userEPPN = "fforgeadmin";
    public static final String userID = "102"; 
	private Connection connection = DBConnector.connection();
	
    @Test
    public void addAndGetAndDeleteServiceImages () {
    	
    	int ProjectID = 1;
    
    	//Get a list of the current images
        ArrayList<ProjectDocument> original =
        given().
                header("AJP_eppn", userEPPN).
        		param("documentGroupId", "1").
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(GET_PROJECT_DOCS, ProjectID).
                as(ArrayList.class);

        //Hardcoded URL
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg";
        
        int projectDocID = addDoc(ProjectID, url);
        
        //Make sure the added image returns a valid id
        assertTrue("Project Doc ID returned invalid", projectDocID != -1);

        //Get a list of the new images 
        ArrayList<ProjectDocument> newList =
        given().
                header("AJP_eppn", userEPPN).
        		param("documentGroupId", "1").
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(GET_PROJECT_DOCS, ProjectID).
                as(ArrayList.class);

        int numBefore = (original != null) ? original.size() : 0;
        int numAfter  = (newList != null) ? newList.size() : 0;
        int numExpected = numBefore + 1;
        //the new list and old list should only differ by one
        assertTrue ("Adding an doc failed"  , numAfter == numExpected);

        deleteDoc(projectDocID);

        ArrayList<ProjectDocument> afterDelete=
        given().
                header("AJP_eppn", userEPPN).
        		param("documentGroupId", "1").
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(GET_PROJECT_DOCS, ProjectID).
                as(ArrayList.class);

        int numAfterDelete  = (afterDelete != null) ? afterDelete.size() : 0;
        assertTrue ("Deleting an doc failed", numAfterDelete == numBefore);

    }
    
    
    
    @Test
    public void PatchServiceImages () throws DMCServiceException{
    	
    	int ProjectID = 2;
    
    	//Get a list of the current images
        ArrayList<ProjectDocument> original =
        given().
                header("AJP_eppn", userEPPN).
        		param("documentGroupId", "1").
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(GET_PROJECT_DOCS, ProjectID).
                as(ArrayList.class);

        //Hardcoded URL
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg";
        
        
        //Manual Insert
		PreparedStatement statement;
		Util util = Util.getInstance();
		int DocId;
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		Timestamp expires = new Timestamp(now.getTime());
		long duration = 1000 * 60 * 60; // Add 1 hour.
		expires.setTime(expires.getTime() + duration);
		
		try {
			connection.setAutoCommit(false);
		} catch (SQLException ex) {
			ServiceLogger.log(logTag, ex.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "An SQL exception has occured");
		}
		
		try {
			
			String query = "INSERT INTO doc2_files (owner, owner_id, filename, description, "
			+ "modified_date, size, doc_group_id, group_id,resource_path, expiration_date) VALUES (?,?,?,?,?,?,?,?,?,?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, userEPPN);
			statement.setInt(2, Integer.parseInt(userID));
			statement.setString(3,url);
			statement.setString(4,"test");
			statement.setInt(5,1);
			statement.setInt(6, 1);
			statement.setInt(7, 1);
			statement.setInt(8, ProjectID);
			statement.setString(9, "temp_path");
			statement.setTimestamp(10, expires);
			statement.executeUpdate();
			DocId = util.getGeneratedKey(statement, "file_id");
			ServiceLogger.log(logTag, "Creating ProjectDoc, returning ID: " + DocId);
			connection.commit();
			}
			catch (SQLException e) {
				ServiceLogger.log(logTag, "SQL EXCEPTION ----- " + e.getMessage());
				try {
					if (connection != null) {
						ServiceLogger.log(logTag, "createProjectDoc transaction rolled back");
						connection.rollback();
					}
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
					throw new DMCServiceException(DMCError.OtherSQLError, ex.getMessage());
				}
				throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
			}
        
		ServiceLogger.log(logTag, "Manually created id is " + DocId);

        //Make sure the added image returns a valid id
        assertTrue("Doc ID returned invalid", DocId != -1);

        //Get a list of the new images 
        ArrayList<ProjectDocument> newList =
        given().
                header("AJP_eppn", userEPPN).
        		param("documentGroupId", "1").
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(GET_PROJECT_DOCS, ProjectID).
                as(ArrayList.class);

        int numBefore = (original != null) ? original.size() : 0;
        int numAfter  = (newList != null) ? newList.size() : 0;
        int numExpected = numBefore + 1;
        //the new list and old list should only differ by one
        assertTrue ("Adding an doc failed"  , numAfter == numExpected);

        
        
        //Call patch image through validation machine endpoint 
        VerificationPatch json = new VerificationPatch();
        json.setFolder("Test");
        json.setUrl(url);
        json.setScanLog("good");
        json.setId(DocId);
        json.setTable("doc2_files");
        json.setUserEPPN(userEPPN);
        json.setUrlColumn("filename");
        json.setResourceType("Test");
        json.setVerified(false);
        json.setIdColumn("file_id");
        
        ObjectMapper mapper = new ObjectMapper();
		String patchedJSONString = null;
		try {
			patchedJSONString = mapper.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		ServiceLogger.log(logTag, "Conversion to Json object");

        VerificationPatch returnPatch = 
		given()
        	.header("Content-type", "application/json")
            .body(patchedJSONString)
            .expect()
            .statusCode(HttpStatus.OK.value())
            .when()
            .post(PATCH).as(VerificationPatch.class); 

        //Test if patched 
        //Extract
      	String ResourceType = returnPatch.getResourceType(); 
		ServiceLogger.log(logTag, "Resource Type " + ResourceType);
		ServiceLogger.log(logTag, "Table  " + returnPatch.getTable());


      	assert(returnPatch.getId() == DocId);
      		
		ServiceLogger.log(logTag, "Patched worked?");

		deleteDoc(DocId);

        ArrayList<ProjectDocument> afterDelete =
        given().
                header("AJP_eppn", userEPPN).
        		param("documentGroupId", "1").
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get(GET_PROJECT_DOCS, ProjectID).
                as(ArrayList.class);

        int numAfterDelete  = (afterDelete != null) ? afterDelete.size() : 0;
        assertTrue ("Deleting an doc failed", numAfterDelete == numBefore);

    }
    
    public int addDoc(int ProjectID, String url) {

        ProjectDocument json = new ProjectDocument();
        json.setFile(url);
        json.setId("1");
        json.setProjectId(Integer.toString(ProjectID));
        json.setOwner(userEPPN);
        json.setOwnerId(userID);
        json.setProjectDocumentId("1");
        json.setSize("1");
        json.setModifed("1");
        json.setTitle("hello");

        Integer createdId  = 
        given().
                header("Content-type", "application/json").
                body(json).
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                post(POST_PROJECT_DOCS).
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
                statusCode(HttpStatus.OK.value()).
                when().
                delete(DELETE_PROJECT_DOCS, docID);
    }

}
