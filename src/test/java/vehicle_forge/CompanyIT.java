package vehicle_forge;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.*;
import org.springframework.test.*;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

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

import java.util.Random;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class CompanyIT extends BaseIT {
	
	private static final String COMPANY_RESOURCE = "/company/{id}";
    private CompanyDao companyDao = new CompanyDao();
	private Company company = null;		
	private Random r = new Random();
	
	@Test
	public void getCompany() {		
	  company = companyDao.getCompany(1);
	  if (company != null) {	
	    expect().
	      statusCode(200).
	      when().
	      get(COMPANY_RESOURCE, company.getId()).then().
          body(matchesJsonSchemaInClasspath("Schemas/companySchema.json"));
	  }
	}
}