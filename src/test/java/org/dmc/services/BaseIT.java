package org.dmc.services;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.*;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import com.jayway.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class) 
@SpringApplicationConfiguration(classes = Application.class)     
@WebAppConfiguration
@IntegrationTest("localhost:8080")

//@Ignore
public class BaseIT {
	
	@Autowired   
	//Not autowiring anything at this point
	
	@Value("${local.server.port}")
	private int serverPort;

	protected String userEPPN;
	
    @Before  
    public void setup() {  
    	userEPPN = new String("fforgeadmin");
    	
      // Use the embedded container port when testing
      // This setup() method is inheritted by all tests so all tests will use this port similarly
      // It can also be overridden
	  String baseURI = System.getProperty("baseURI", "not specified");
	  if (baseURI.equals("not specified")){
		RestAssured.port = serverPort;
		ServiceLogger.log("BaseIT::"+this.getClass().getSimpleName(), "BASE URI not specified.");
	  }
	  else{
		  ServiceLogger.log("BaseIT::"+this.getClass().getSimpleName(), "BASE URI specified, setting attributes");
		  RestAssured.baseURI = baseURI; 
		  RestAssured.port = Integer.getInteger("port", 8080).intValue();
		  RestAssured.basePath = "";
		  
		  
	  }
    } 
    
    @Test 
    public void test() {
      // inherited by all tests	
    }
}