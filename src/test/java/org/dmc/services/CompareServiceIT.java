package org.dmc.services;

import org.dmc.services.services.GetCompareService;
import org.dmc.services.services.PostCompareService;
import org.springframework.http.HttpStatus;
import org.junit.Test;
import static com.jayway.restassured.RestAssured.given;

public class CompareServiceIT extends BaseIT{
	
    private static final String PROFILE_COMPARE_SERVICE = "/profiles/{profileID}/compare_services";
    private static final String PROFILE_POST_COMPARE_SERVICE = "/compare_services";
    private static final String PROFILE_DELETE_COMPARE_SERVICE = "/compare_services/{id}";
    public static final String userEPPN = "testUser"; //111
    public static final String forgeAdmin = "fforgeadmin"; //102
    
    @Test
    public void postAndDeleteCompareService(){
    	int serviceId = 3;
    	int profileId = 102;
    	PostCompareService json = new PostCompareService();
    	json.setServiceId(Integer.toString(serviceId));
    	json.setProfileId(Integer.toString(profileId));
    	
    	GetCompareService compareService = 
		given().
				header("Content-type", "application/json").header("AJP_eppn", forgeAdmin).body(json).expect()
				.statusCode(HttpStatus.OK.value()).when().post(PROFILE_POST_COMPARE_SERVICE).as(GetCompareService.class);
    	int id = Integer.parseInt(compareService.getId());
    	deleteCompareService(id);
	}
    
    //@Test
    public void deleteCompareService(int id){
    	given().
    			header("Content-type", "application/json").header("AJP_eppn", forgeAdmin).expect().statusCode(HttpStatus.OK.value()).when().delete(PROFILE_DELETE_COMPARE_SERVICE, id);
    	
    }
    
    @Test
    public void deleteNonExistingCompareService(){
    	int id = 1111121211;
    	given().
    			header("Content-type", "application/json").header("AJP_eppn", forgeAdmin).expect().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).when().delete(PROFILE_DELETE_COMPARE_SERVICE, id);
    	
    }
    
    @Test
    public void getCompareServices(){
    	int profileId = 111;
    	given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.OK.value()).when().get(PROFILE_COMPARE_SERVICE, profileId);	
    }
    
    @Test
    public void getCompareServicesWithInvalidProfileId(){
    	int profileId = 102;
    	given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.UNAUTHORIZED.value()).when().get(PROFILE_COMPARE_SERVICE, profileId);	
    }
	

}
