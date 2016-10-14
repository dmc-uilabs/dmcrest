package org.dmc.services;

import java.util.ArrayList;
import org.junit.Test;

import com.jayway.restassured.response.ValidatableResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dmc.services.data.dao.user.UserDao;
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

    /**
     * test case for GET /profiles/{profileID}/profile_history
     */
    @Test
    public void testProfileGet_DiscussionProfileHistory() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testProfileGet_PublicProfileHistory");
        String joeengineer = "joeengineer";
        int joeengineerId = UserDao.getUserID(joeengineer);
        final ValidatableResponse response = given().header("AJP_eppn", joeengineer)
                .expect().statusCode(OK.value())
                .when().get(PROFILE_HISTORY, joeengineerId)
                .then();
        final ArrayList<ProfileHistory> list = new ArrayList<ProfileHistory>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        boolean foundAnyStartedProjectDiscussionEvents = false;
        boolean foundAnyStartedPublicDiscussionEvents = false;
        boolean foundAnyProjectDiscussionCommentEvents = false;
        boolean foundAnyPublicDiscussionCommentEvents = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            ProfileHistory item = mapper.readValue(jsonArray.getJSONObject(i).toString(), ProfileHistory.class);
            if (item.getTitle().startsWith("started") && item.getTitle().contains("discussion in project")) {
                foundAnyStartedProjectDiscussionEvents = true;
            }
            if (item.getTitle().startsWith("started") && item.getTitle().contains("public discussion")) {
                foundAnyStartedPublicDiscussionEvents = true;
            }
            if (item.getTitle().startsWith("commented on") && item.getTitle().contains("project discussion")) {
                foundAnyProjectDiscussionCommentEvents = true;
            }
            if (item.getTitle().startsWith("commented on") && item.getTitle().contains("public discussion")) {
                foundAnyPublicDiscussionCommentEvents = true;
            }
            list.add(item);
        }
        assertTrue("expect at least one started discussion event for joeengineer", foundAnyStartedProjectDiscussionEvents);
        assertTrue("expect at least one started public discussion event for joeengineer", foundAnyStartedPublicDiscussionEvents);
        assertTrue("expect at least one comment on project discussion event for joeengineer", foundAnyProjectDiscussionCommentEvents);
        assertTrue("expect at least one comment on public discussion event for joeengineer", foundAnyPublicDiscussionCommentEvents);
    }

    /**
     * test case for GET /profiles/{profileID}/profile_history
     */
    @Test
    public void testProfileGet_ServiceReleaseProfileHistory() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testProfileGet_ServiceReleaseProfileHistory");
        int userid = UserDao.getUserID(userEPPN);
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN)
                .expect().statusCode(OK.value())
                .when().get(PROFILE_HISTORY, userid)
                .then();
        final ArrayList<ProfileHistory> list = new ArrayList<ProfileHistory>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        boolean foundAnyServiceReleaseEvents = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            ProfileHistory item = mapper.readValue(jsonArray.getJSONObject(i).toString(), ProfileHistory.class);
            if (item.getTitle().startsWith("service ") && item.getTitle().endsWith("released ")) {
                foundAnyServiceReleaseEvents = true;
            }
            list.add(item);
        }
        assertTrue("expect at least one service release event for " + userEPPN, foundAnyServiceReleaseEvents);
    }

    /**
     * test case for GET /profiles/{profileID}/profile_history
     */
    @Test
    public void testProfileGet_ServiceRunProfileHistory() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testProfileGet_ServiceRunProfileHistory");
        String joeengineer = "joeengineer";
        int joeengineerid = UserDao.getUserID(joeengineer);
        final ValidatableResponse response = given().header("AJP_eppn", joeengineer)
                .expect().statusCode(OK.value())
                .when().get(PROFILE_HISTORY, joeengineerid)
                .then();
        final ArrayList<ProfileHistory> list = new ArrayList<ProfileHistory>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        boolean foundAnyServiceStartEvents = false;
        boolean foundAnyServiceStopEvents = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            ProfileHistory item = mapper.readValue(jsonArray.getJSONObject(i).toString(), ProfileHistory.class);
            if (item.getTitle().startsWith("service ") && item.getTitle().contains(" run started ")) {
                foundAnyServiceStartEvents = true;
            }
            if (item.getTitle().startsWith("service ") && item.getTitle().contains(" run stopped ")) {
                foundAnyServiceStopEvents = true;
            }
            list.add(item);
        }
        assertTrue("expect at least one service started event for joeengineer", foundAnyServiceStartEvents);
        assertTrue("expect at least one service stopped event for joeengineer", foundAnyServiceStopEvents);
    }

}
