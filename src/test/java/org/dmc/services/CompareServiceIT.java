package org.dmc.services;

import org.dmc.services.services.CompareServicesDao;
import org.dmc.services.services.GetCompareService;
import org.dmc.services.services.PostCompareService;
import org.springframework.http.HttpStatus;
import org.junit.Test;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.*;

import java.util.List;


public class CompareServiceIT extends BaseIT {

	private static final String PROFILE_COMPARE_SERVICE = "/profiles/{profileID}/compare_services";
	private static final String PROFILE_POST_COMPARE_SERVICE = "/compare_services";
	private static final String PROFILE_DELETE_COMPARE_SERVICE = "/compare_services/{id}";
	public static final String testUser = "testUser"; // 111

	@Test
	public void postAndDeleteCompareService() {
		int serviceId = 3;
		int profileId = 102;
		PostCompareService json = new PostCompareService();
		json.setServiceId(Integer.toString(serviceId));
		json.setProfileId(Integer.toString(profileId));

		GetCompareService compareService = given().header("Content-type", APPLICATION_JSON_VALUE)
				.header("AJP_eppn", userEPPN).body(json).expect().statusCode(HttpStatus.OK.value()).when()
				.post(PROFILE_POST_COMPARE_SERVICE).as(GetCompareService.class);
		int posted_service_id = Integer.parseInt(compareService.getServiceId());
		int posted_profile_id = Integer.parseInt(compareService.getProfileId());
		assertTrue(posted_service_id == serviceId && posted_profile_id == profileId);
		int id = Integer.parseInt(compareService.getId());
		deleteCompareService(id);
		CompareServicesDao test = new CompareServicesDao();
		GetCompareService deletedService = test.getCompareService(id, userEPPN);
		assertTrue(deletedService == null);
	}

	public void deleteCompareService(int id) {
		given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).expect()
				.statusCode(HttpStatus.OK.value()).when().delete(PROFILE_DELETE_COMPARE_SERVICE, id);
	}

	@Test
	public void deleteNonExistingCompareService() {
		int id = 1111121211;
		given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", testUser).expect()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).when().delete(PROFILE_DELETE_COMPARE_SERVICE, id);

	}

	@Test
	public void getCompareServices() {
		int profileId = 111;
		List<GetCompareService> compareServices = given().header("AJP_eppn", testUser).expect().statusCode(HttpStatus.OK.value()).when()
				.get(PROFILE_COMPARE_SERVICE, profileId).as(List.class);
		assertTrue(compareServices.size() == 3);
	}

	@Test
	public void getCompareServicesWithInvalidProfileId() {
		int profileId = 102;
		given().header("AJP_eppn", testUser).expect().statusCode(HttpStatus.UNAUTHORIZED.value()).when()
				.get(PROFILE_COMPARE_SERVICE, profileId);
	}

}
