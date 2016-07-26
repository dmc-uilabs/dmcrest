package org.dmc.services;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.utility.TestUserUtil;
import org.dmc.services.verification.VerificationPatch;
import org.json.JSONException;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class AWSProfileIT {
    private static final String verification = "/verify";
	private static final String PROFILE_DELETE_RESOURCE = "/profiles/{id}/delete";
	public static final String userEPPN = "Tester";
    private static final String logTag = AWSProfileIT.class.getName();
    private String unique = TestUserUtil.generateTime();
	private Integer id;
	private Util util = Util.getInstance();
    private Connection connection = DBConnector.connection();

    
    @Test
    public void PatchProfile () throws DMCServiceException {
    	
        //Hardcoded URL
        String url = "https://s3-us-west-2.amazonaws.com/test-temp-verify/test.jpeg";
        
        //Manual Insert
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
			ServiceLogger.log(logTag, "Done INSERT INTO users!");
			id = util.getGeneratedKey(preparedStatement, "user_id");
        } 
        
    	catch(SQLException e){
			ServiceLogger.log(logTag, e.getMessage());
		}
		catch(JSONException j){
			ServiceLogger.log(logTag, j.getMessage());
		}
		catch(Exception ee){
			ServiceLogger.log(logTag, ee.getMessage());
		}
        
		ServiceLogger.log(logTag, "Manually created id is " + id);

        //Make sure the added image returns a valid id
        assertTrue("User ID returned invalid", id != -1);
        int intID  = id.intValue();
        
        
        //Call patch image through validation machine endpoint 
        VerificationPatch json = new VerificationPatch();
        json.setFolder("test");
        json.setUrl(url);
        json.setScanLog("good");
        json.setId(intID);
        json.setTable("users");
        json.setUserEPPN(userEPPN);
        json.setUrlColumn("image");
        json.setResourceType("Test");
        json.setVerified(false);
        json.setIdColumn("user_id");
        
   
		ServiceLogger.log(logTag, "Conversion to Json object");

        //VerificationPatch obj = 
		given()
        	.header("Content-type", "application/json")
            .body(json.toString())
            .expect()
            .statusCode(HttpStatus.OK.value())
            .when()
            .post("/verify");//.as(VerificationPatch.class); 

        //Test if patched 
      	//String ResourceType = obj.getResourceType(); 
		//ServiceLogger.log(LOGTAG, "Resource Type " + ResourceType);
		//ServiceLogger.log(LOGTAG, "Table  " + obj.getTable());

      //	assert(obj.getId() == id);
		ServiceLogger.log(logTag, "Patched worked?");
        deleteUser(id);
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
