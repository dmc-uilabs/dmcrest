package vehicle_forge;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.*;
import org.springframework.test.*;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before; 
import org.junit.Ignore;

import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.RestAssured;
import static org.hamcrest.Matchers.*;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@RunWith(SpringJUnit4ClassRunner.class) 
@SpringApplicationConfiguration(classes = Application.class)     
@WebAppConfiguration
@IntegrationTest("server.port:0")  

//@Ignore
public class BaseIT {
	
	@Autowired   
	//Not autowiring anything at this point
	
	@Value("${local.server.port}")
	private int serverPort;

    @Before  
    public void setup() {  
      // Use the embedded container port when testing
      // This setup() method is inheritted by all tests so all tests will use this port similarly
      // It can also be overridden
      RestAssured.port = serverPort;
    } 
    
    @Test 
    public void test() {
      // inherited by all tests	
    }
}