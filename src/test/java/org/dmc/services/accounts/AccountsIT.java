package org.dmc.services;

import org.junit.Test;

import java.util.Date;
import java.text.SimpleDateFormat;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.*;

import org.json.JSONObject;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.dmc.services.utility.TestUserUtil;

public class AccountsIT extends BaseIT {


    /**
     Tests for <code> get /accounts/{accountID} </code>
     **/
    
    @Test
	public void testAccountGet_UnknownUser(){
        String unknownUserEPPN = TestUserUtil.uniqueUserEPPN();
        String unknownUserID = Integer.toString(Integer.MAX_VALUE);
        
		given().
        header("AJP_eppn", unknownUserEPPN).
		expect().
        statusCode(401).
		when().
        get("/accounts/" + unknownUserID);

        // not json is returned on 401.  If it is, then the check below should be perfromed.
        //		.then().
        //        body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
        // check resulting json
	}
    
    @Test
	public void testAccountGet_KnownUser(){
        String knownUserEPPN = "fforgeadmin";
        String knownUserID = "102";
        
		given().
        header("AJP_eppn", knownUserEPPN).
		expect().
        statusCode(200).
		when().
        get("/accounts/" + knownUserID).
		then().
        body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
        
        // check results
//        assertTrue(false);
	}

    @Test
	public void testAccountGet_MismatchedUser(){
        String knownUser = "fforgeadmin";
        
		given().
        header("AJP_eppn", knownUser).
		expect().
        statusCode(200).
		when().
        get("/accounts/123").
		then().
        body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
        
        // check results
        assertTrue(false);
    }
    
    /**
     Tests for <code> patch /accounts/{accountID} </code>
     **/
    
    @Test
	public void testAccountUpdate(){
        // check results
        assertTrue(false);
    }
    
    @Test
    public void testAccountUpdate_MismatchedUser(){
        // check results
        assertTrue(false);
    }
    
    @Test
	public void testAccountUpdateIncorrectInvocation(){
        // check results
        assertTrue(false);
	}
    

    @Test
	public void testAccountUpdateOfNewlyCreatedUser(){
        // check results
        assertTrue(false);
	}

}