package org.dmc.services;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.math.BigDecimal;
import org.dmc.services.services.DomeAPIDao;
import org.dmc.services.services.DomeEntity;
import org.dmc.services.services.DomeModel;
import org.dmc.services.services.DomeModelResponse;
import org.dmc.services.services.DomeResponseEntity;

public class DomeIT extends BaseIT {

	private String domeServer = System.getenv("DOME_TEST_SERVER") + "/DOMEApiServicesV7";
	private DomeAPIDao domeAPIDao = new DomeAPIDao();
	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testGetChildrenWhenRootDirectory() {
		DomeResponseEntity resultFromDirectComm = new DomeResponseEntity();
		DomeEntity domeEntity = new DomeEntity();
		domeEntity.setDomeServer(domeServer);
		String resultFromDirectCommunication = new String();

		DomeResponseEntity resultFromREST = given().header("Content-type", "application/json")
				.header("AJP_eppn", userEPPN).param("domeServer", domeServer).expect().statusCode(HttpStatus.OK.value())
				.when().get("/getChildren").as(DomeResponseEntity.class);
		
		try {
			resultFromDirectCommunication = domeAPIDao.getChildren(domeEntity);
			resultFromDirectComm = mapper.readValue(resultFromDirectCommunication, DomeResponseEntity.class);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue("testGetChildrenWhenRootDirectory: Result from dome server does not match result from REST API",
				(resultFromDirectComm.equals(resultFromREST)));

	}

	@Test
	public void testGetChildrenWhenTypeIsFolder() {
		DomeResponseEntity resultFromDirectComm = new DomeResponseEntity();
		String name = "Fracture-Mechanics";
		String type = "folder";
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));

		DomeEntity domeEntity = new DomeEntity();
		domeEntity.setDomeServer(domeServer);
		domeEntity.setName(name);
		domeEntity.setPath(path);
		domeEntity.setType(type);

		String resultFromDirectCommunication = new String();

		DomeResponseEntity resultFromREST = given().header("Content-type", "application/json")
				.header("AJP_eppn", userEPPN).param("domeServer", domeServer).param("name", name).param("path", path)
				.param("type", type).expect().statusCode(HttpStatus.OK.value()).when().get("/getChildren")
				.as(DomeResponseEntity.class);
		
		try {
			resultFromDirectCommunication = domeAPIDao.getChildren(domeEntity);
			resultFromDirectComm = mapper.readValue(resultFromDirectCommunication, DomeResponseEntity.class);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue("testGetChildrenWhenTypeIsFolder: Result from dome server does not match result from REST API",
				(resultFromDirectComm.equals(resultFromREST)));
	}

	@Test
	public void testGetChildrenWhenTypeIsModel() {
		DomeResponseEntity resultFromDirectComm = new DomeResponseEntity();
		BigDecimal version = new BigDecimal(1);
		String modelId = "aff647dc-d82f-1004-8e7b-5de38b2eeb0f";
		String description = "";
		BigDecimal dateModified = new BigDecimal("1416717627000");
		String name = "AppliedLoad";
		String type = "model";
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));

		DomeEntity domeEntity = new DomeEntity();
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
			resultFromDirectComm = mapper.readValue(resultFromDirectCommunication, DomeResponseEntity.class);
		} catch (DMCServiceException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		DomeResponseEntity resultFromREST = given().header("Content-type", "application/json")
				.header("AJP_eppn", userEPPN).param("domeServer", domeServer).param("version", version)
				.param("modelId", modelId).param("description", description).param("dateModified", dateModified)
				.param("name", name).param("path", path).param("type", type).expect().statusCode(HttpStatus.OK.value())
				.when().get("/getChildren").as(DomeResponseEntity.class);

		assertTrue("testGetChildrenWhenTypeIsModel: Result from dome server does not match result from REST API",
				(resultFromDirectComm.equals(resultFromREST)));

	}

	@Test
	public void testGetChildrenWhenOtherType() {
		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).param("domeServer", domeServer)
				.param("type", "otherType").expect().statusCode(HttpStatus.BAD_REQUEST.value()).when()
				.get("/getChildren");
	}

	@Test
	public void testGetModelWhenTypeIsInterface() {
		BigDecimal version = new BigDecimal(1);
		String interfaceId = "aff647db-d82f-1004-8e7b-5de38b2eeb0f";
		String modelId = "aff647da-d82f-1004-8e7b-5de38b2eeb0f";
		String name = "Default Interface";
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(30));
		String type = "interface";

		DomeModelResponse received = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).param("domeServer", domeServer)
				.param("interfaceId", interfaceId).param("modelId", modelId).param("name", name).param("path", path)
				.param("type", type).param("version", version).expect().statusCode(HttpStatus.OK.value()).when()
				.get("/getModel").as(DomeModelResponse.class);
		
		assertTrue("testGetModelWhenTypeIsInterface: Name from dome server does not match expected result",
				(received.getPkg().getName().equals("Default Interface")));
		assertTrue("testGetModelWhenTypeIsInterface: Description from dome server does not match expected result",
				(received.getPkg().getDescription().equals("")));
		assertTrue("testGetModelWhenTypeIsInterface: ModelId from dome server does not match expected result",
				(received.getPkg().getInterface().getModelId().equals("aff647da-d82f-1004-8e7b-5de38b2eeb0f")));

	}

	@Test
	public void testGetModelWhenTypeIsProject() {
		BigDecimal version = new BigDecimal(1);
		String projectId = "Uploaded+File+Size";
		String interfaceId = "3a2f15fd-d8f6-1004-85e6-e48afddadd5b";
		String name = "Project+Interface";
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(31));
		String type = "interface";

		DomeModelResponse received = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).param("domeServer", domeServer)
				.param("interfaceId", interfaceId).param("projectId", projectId).param("name", name).param("path", path)
				.param("type", type).param("version", version).expect().statusCode(HttpStatus.OK.value()).when()
				.get("/getModel").as(DomeModelResponse.class);
		
		assertTrue("testGetModelWhenTypeIsInterface: Name from dome server does not match expected result",
				(received.getPkg().getName().equals("Project Interface")));
		assertTrue("testGetModelWhenTypeIsInterface: Description from dome server does not match expected result",
				(received.getPkg().getDescription().equals("")));
		assertTrue("testGetModelWhenTypeIsInterface: ModelId from dome server does not match expected result",
				(received.getPkg().getInterface().getInterfaceId().equals("3a2f15fd-d8f6-1004-85e6-e48afddadd5b")));

	}

	@Test
	public void testGetModelWhenTypeIsNotProjectOrInterface() {
		BigDecimal version = null;
		String interfaceId = null;
		String name = null;
		List<BigDecimal> path = new ArrayList<BigDecimal>();
		path.add(new BigDecimal(31));
		String type = "other";

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).param("domeServer", domeServer)
				.param("interfaceId", interfaceId).param("name", name).param("path", path)
				.param("type", type).param("version", version).expect().statusCode(HttpStatus.BAD_REQUEST.value()).when()
				.get("/getModel");

	}

}