package org.dmc.services;

import java.util.ArrayList;
import org.junit.Test;

import com.jayway.restassured.response.ValidatableResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dmc.services.profile.ProfileHistory;
import org.json.JSONArray;

import static org.springframework.http.HttpStatus.OK;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class ProfileHistoryIT extends BaseIT {

    private static final String LOGTAG = ProfileHistoryIT.class.getName();

    private static final String PROFILE_HISTORY = "/profiles/{profileID}/profile_history";

    /**
     * test case for GET /profiles/{profileID}/profile_history
     */
    @Test
    public void testProfileGet_ProfileHistory() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testProfileGet_ProfileHistory");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN)
                .expect().statusCode(OK.value())
                .when().get(PROFILE_HISTORY, 102)
                .then();
        final ArrayList<ProfileHistory> list = new ArrayList<ProfileHistory>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            ProfileHistory item = mapper.readValue(jsonArray.getJSONObject(i).toString(), ProfileHistory.class);
            list.add(item);
        }
        assertTrue("expect something for fforgeadmin user", 0 < list.size());
    }

    /**
     * test case for GET /profiles/{profileID}/profile_history
     */
    @Test
    public void testProfileGet_MutualProfileHistory() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testProfileGet_MutualProfileHistory");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).params("section", "mutual")
                .expect().statusCode(OK.value())
                .when().get(PROFILE_HISTORY, 102)
                .then();
        final ArrayList<ProfileHistory> list = new ArrayList<ProfileHistory>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            ProfileHistory item = mapper.readValue(jsonArray.getJSONObject(i).toString(), ProfileHistory.class);
            list.add(item);
        }
        assertTrue("expect something for fforgeadmin user", 0 < list.size());
    }

    /**
     * test case for GET /profiles/{profileID}/profile_history
     */
    @Test
    public void testProfileGet_PublicProfileHistory() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testProfileGet_PublicProfileHistory");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).params("section", "public")
                .expect().statusCode(OK.value())
                .when().get(PROFILE_HISTORY, 102)
                .then();
        final ArrayList<ProfileHistory> list = new ArrayList<ProfileHistory>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            ProfileHistory item = mapper.readValue(jsonArray.getJSONObject(i).toString(), ProfileHistory.class);
            list.add(item);
        }
        assertTrue("expect something for fforgeadmin user", 0 < list.size());
    }

    /**
     * test case for GET /profiles/{profileID}/profile_history
     */
    @Test
    public void testProfileGet_PublicLimitProfileHistory() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testProfileGet_PublicProfileHistory");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).params("section", "public").params("_limit", 4)
                .expect().statusCode(OK.value())
                .when().get(PROFILE_HISTORY, 102)
                .then();
        final ArrayList<ProfileHistory> list = new ArrayList<ProfileHistory>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            ProfileHistory item = mapper.readValue(jsonArray.getJSONObject(i).toString(), ProfileHistory.class);
            list.add(item);
        }
        assertTrue("expect something for fforgeadmin user", 0 < list.size());
        assertTrue("count should have been <= 4 for fforgeadmin user", 4 >= list.size());
    }

}
