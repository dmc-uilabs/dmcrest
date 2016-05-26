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
import org.dmc.services.services.DomeFolderEntity;
import org.dmc.services.services.DomeModelEntity;

@Ignore
public class DomeIT extends BaseIT {

	private String domeServer = "http://localhost:8082/DOMEApiServicesV7";
	private DomeAPIDao domeAPIDao = new DomeAPIDao();

	@Test
	public void testGetChildrenWhenRootDirectory() {
		DomeEntity domeEntity = new DomeEntity();
		domeEntity.setDomeServer(domeServer);
		String resultFromDirectCommunication = new String();

		try {
			resultFromDirectCommunication = domeAPIDao.getChildren(domeEntity);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		}

		String resultFromREST = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.param("domeServer", domeServer).expect().statusCode(HttpStatus.OK.value()).when().get("/get-children")
				.as(String.class);

		assertTrue("testGetChildrenWhenRootDirectory: Result from dome server does not match result from REST API",
				(resultFromDirectCommunication.equals(resultFromREST)));

	}

	@Ignore
	@Test
	public void testGetChildrenWhenTypeIsFolder() {
		String name = "Fracture-Mechanics";
		String type = "folder";
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));

		DomeFolderEntity domeEntity = new DomeFolderEntity();
		domeEntity.setDomeServer(domeServer);
		domeEntity.setName(name);
		domeEntity.setPath(path);
		domeEntity.setType(type);

		String resultFromDirectCommunication = new String();

		try {
			resultFromDirectCommunication = domeAPIDao.getChildren(domeEntity);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		}

		String resultFromREST = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.param("domeServer", domeServer).param("name", name).param("path", path).param("type", type).expect()
				.statusCode(HttpStatus.OK.value()).when().get("/get-children").as(String.class);

		assertTrue("testGetChildrenWhenRootDirectory: Result from dome server does not match result from REST API",
				(resultFromDirectCommunication.equals(resultFromREST)));
	}

	@Ignore
	@Test
	public void testGetChildrenWhenTypeIsModel() {
		BigDecimal version = new BigDecimal(1);
		String modelId = "aff647dc-d82f-1004-8e7b-5de38b2eeb0f";
		String description = "";
		BigDecimal dateModified = new BigDecimal("1416717627000");
		String name = "AppliedLoad";
		String type = "model";
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));

		DomeModelEntity domeEntity = new DomeModelEntity();
		domeEntity.setDomeServer(domeServer);
		domeEntity.setVersion(version);
		domeEntity.setModelId(modelId);
		domeEntity.setDescription(description);
		domeEntity.setDateModified(dateModified);
		domeEntity.setName(name);
		domeEntity.setPath(path);
		domeEntity.setType(type);

		String resultFromDirectCommunication = new String();

		try {
			resultFromDirectCommunication = domeAPIDao.getChildren(domeEntity);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		}

		String resultFromREST = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.param("domeServer", domeServer).param("version", version).param("modelId", modelId)
				.param("description", description).param("dateModified", dateModified).param("name", name)
				.param("path", path).param("type", type).expect().statusCode(HttpStatus.OK.value()).when()
				.get("/get-children").as(String.class);

		assertTrue("testGetChildrenWhenRootDirectory: Result from dome server does not match result from REST API",
				(resultFromDirectCommunication.equals(resultFromREST)));

	}

	@Ignore
	@Test
	public void testGetChildrenWhenOtherType() {
		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).param("domeServer", domeServer)
				.param("type", "otherType").expect().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).when().get("/get-children");
	}

}