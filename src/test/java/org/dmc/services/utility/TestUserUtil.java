package org.dmc.services.utility;

import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;

import static com.jayway.restassured.RestAssured.*;


public class TestUserUtil {
    public static String generateTime() {
        Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String unique = format.format(date);
        return unique;
    }

    public static String uniqueID() {
        UUID uniqueID = UUID.randomUUID();
        return uniqueID.toString();
    }
    
    public static String uniqueUserEPPN() {
        UUID uniqueID = UUID.randomUUID();
        return "uniqueUser" + uniqueID.toString();
    }
	
	
	public static String createNewUser() {
        String unique = TestUserUtil.generateTime();
        
        Integer id =
        given().
        header("Content-type", "text/plain").
        header("AJP_eppn", "userEPPN" + unique).
        header("AJP_givenName", "userGivenName" + unique).
        header("AJP_sn", "userSurname" + unique).
        header("AJP_displayName", "userDisplayName" + unique).
        header("AJP_mail", "userEmail" + unique).
        expect().
        statusCode(200).
		when().
        post("/users/create").
		then().
        extract().path("id");
        
        return new String("userEPPN" + unique);
    }

}