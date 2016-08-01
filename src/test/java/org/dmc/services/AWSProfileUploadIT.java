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
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.utility.TestUserUtil;
import org.dmc.services.verification.VerificationPatch;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//@Ignore
public class AWSProfileUploadIT extends BaseIT {

    // Documents
	private static final String PROFILE_DELETE_RESOURCE = "/profiles/{id}/delete";
    private static final String PATCH= "/verify";
    public static final String userEPPN = "fforgeadmin";
    public static final String userID = "102";
    private String unique = TestUserUtil.generateTime();
    private final String logTag = AWSProfileUploadIT.class.getName();
	private Connection connection = DBConnector.connection();
    
    @Test
    public void PatchProfiles () throws DMCServiceException{
    	
    	int ProjectID = 2;


        //Hardcoded URL
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg";
        
        
        //Manual Insert
		PreparedStatement statement;
		Util util = Util.getInstance();
		int userId;
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
			
			String query = "INSERT INTO users (user_name, email, user_pw, realname, add_date, firstname, lastname, image) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, unique);
			preparedStatement.setString(2, "Fake");
			preparedStatement.setString(3, "Dont");
			preparedStatement.setString(4, "Care");
			preparedStatement.setInt(5, 10);
			preparedStatement.setString(6, "Josh");
			preparedStatement.setString(7, "Momma");
			preparedStatement.setString(8, "test");
			preparedStatement.executeUpdate();
			userId = util.getGeneratedKey(preparedStatement, "user_id");
			ServiceLogger.log(logTag, "Creating User, returning ID: " + userId);
			connection.commit();
			}
			catch (SQLException e) {
				ServiceLogger.log(logTag, "SQL EXCEPTION ----- " + e.getMessage());
				try {
					if (connection != null) {
						ServiceLogger.log(logTag, "createUser transaction rolled back");
						connection.rollback();
					}
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
					throw new DMCServiceException(DMCError.OtherSQLError, ex.getMessage());
				}
				throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
			}
        
		ServiceLogger.log(logTag, "Manually created id is " + userId);

        //Make sure the added image returns a valid id
        assertTrue("User ID returned invalid", userId != -1);
        
        //Call patch image through validation machine endpoint 
        VerificationPatch json = new VerificationPatch();
        json.setFolder("Test");
        json.setUrl(url);
        json.setScanLog("good");
        json.setId(userId);
        json.setTable("users");
        json.setUserEPPN(unique);
        json.setUrlColumn("image");
        json.setResourceType("Test");
        json.setVerified(false);
        json.setIdColumn("user_id");
        
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
        	.header("AJP_eppn", userEPPN)
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


      	assert(returnPatch.getId() == userId);
      		
		ServiceLogger.log(logTag, "Patched worked!");

		deleteUser(userId);


    }
   
    public void deleteUser (int id) {
    	given()
		.header("AJP_eppn", unique)
		.expect().statusCode(200)
		.when()
		.get(PROFILE_DELETE_RESOURCE, id)
		.then()
		.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));	
    }

}
