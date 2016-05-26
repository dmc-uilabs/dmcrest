package org.dmc.services;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import org.dmc.services.services.DomeAPIDao;
import org.dmc.services.services.DomeEntity;

@Ignore
public class DomeIT extends BaseIT {

	private String domeServer = "http://localhost:8080/DOMEApiServicesV7";
	private DomeAPIDao domeAPIDao = new DomeAPIDao();

	@Test
	public void testGetChildrenWhenRootDirectory() {
		DomeEntity domeEntity = new DomeEntity();
		domeEntity.setDomeServer("http://localhost:8080/DOMEApiServicesV7");
		String resultFromDirectCommunication = new String("");

		try {
			resultFromDirectCommunication = domeAPIDao.getChildren(domeEntity);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		}

		String resultFromREST = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.param("domeServer", domeServer).expect().statusCode(HttpStatus.OK.value()).when().get("/get-children")
				.as(String.class);

		if (!resultFromDirectCommunication.equals("")) {
			assertTrue("testGetChildrenWhenRootDirectory: Result from dome server does not match result from REST API",
					(resultFromDirectCommunication.equals(resultFromREST)));
		}
	}

	@Test
	public void testGetChildrenWhenTypeIsFolder() {
		DomeEntity domeEntity = new DomeEntity();
		domeEntity.setDomeServer("http://localhost:8080/DOMEApiServicesV7");
		domeEntity.setName("Fracture-Mechanics");
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));
		domeEntity.setPath(path);
		domeEntity.setType("folder");

		String resultFromDirectCommunication = new String("");

		try {
			resultFromDirectCommunication = domeAPIDao.getChildren(domeEntity);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		}

		String resultFromREST = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.param("domeServer", domeServer).param("name", "Fracture-Mechanics").param("path", path)
				.param("type", "folder").expect().statusCode(HttpStatus.OK.value()).when().get("/get-children")
				.as(String.class);

		assertTrue("testGetChildrenWhenRootDirectory: Result from dome server does not match result from REST API",
				(resultFromDirectCommunication.equals(resultFromREST)));
	}

	@Test
	public void testGetChildrenWhenTypeIsModel() {
		DomeEntity domeEntity = new DomeEntity();
		domeEntity.setDomeServer("http://localhost:8080/DOMEApiServicesV7");
		domeEntity.setVersion("1");
		domeEntity.setModelId("aff647dc-d82f-1004-8e7b-5de38b2eeb0f");
		domeEntity.setDescription("");
		domeEntity.setDateModified("1416717627000");
		domeEntity.setName("AppliedLoad");
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));
		domeEntity.setPath(path);
		domeEntity.setType("model");

		String resultFromDirectCommunication = new String("");

		try {
			resultFromDirectCommunication = domeAPIDao.getChildren(domeEntity);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		}

		String resultFromREST = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.param("domeServer", domeServer).param("version", "1")
				.param("modelId", "aff647dc-d82f-1004-8e7b-5de38b2eeb0f").param("description", "")
				.param("dateModified", "1416717627000").param("name", "AppliedLoad").param("path", path)
				.param("type", "model").expect().statusCode(HttpStatus.OK.value()).when().get("/get-children")
				.as(String.class);

		assertTrue("testGetChildrenWhenRootDirectory: Result from dome server does not match result from REST API",
				(resultFromDirectCommunication.equals(resultFromREST)));

	}

	@Test
	public void testGetChildrenWhenOtherType() {
		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).param("domeServer", domeServer)
				.param("type", "otherType").expect().statusCode(HttpStatus.OK.value()).when().get("/get-children");
	}

}