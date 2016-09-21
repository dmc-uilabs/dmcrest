package org.dmc.services.search;

import com.jayway.restassured.response.Response;
import org.dmc.services.BaseIT;
import org.dmc.services.profile.Profile;
import org.dmc.services.users.User;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.patch;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by 200005921 on 9/1/2016.
 */
public class SearchIndexingIT extends BaseIT {

    private static final String USER_RESOURCE = "/user";
    private static final String USER_CREATE_RESOURCE = "/users/create";
    private static final String USER_INFO_RESOURCE = "/user-basic-information";

    private static final String SEARCH_MEMBERS_RESOURCE = "/searchMembers/{query}";
    private static final String SEARCH_USERS_RESOURCE   = "/searchUsers/{query}";

    private static final int DEFAULT_COMPANY_ID = 15;
    private static final String DEFAULT_USER_EPPN = "joeengineer";

    private int newUserId;
    private String newUserEPPN;

    @Before
    public void setUp () {

        newUserEPPN = TestUserUtil.createNewUser();

        // create user; will not have company
        final User newUser =
                given().
                        header("Content-type", APPLICATION_JSON_VALUE).
                        header("AJP_eppn", newUserEPPN).
                        header("AJP_givenName",newUserEPPN).
                        header("AJP_sn",newUserEPPN).
                        header("AJP_displayName",newUserEPPN).
                        header("AJP_givenName",newUserEPPN).
                        expect().
                        statusCode(OK.value()).
                        when().
                        get(USER_RESOURCE).as(User.class);

        newUserId = newUser.getAccountId();

        // check; does not have have company
        assertTrue("New user is assigned company " + newUser.getCompanyId() + " when the comapnay should be -1", newUser.getCompanyId() == -1);

        // add basis information
        final JSONObject json = new JSONObject();
        json.put("email", "test basic info email");
        json.put("firstName", "test basic info first name");
        json.put("lastName", "test basic info last name");
        json.put("company", "1");

        given()
                .header("Content-type", APPLICATION_JSON_VALUE)
                .header("AJP_eppn", newUserEPPN)
                .body(json.toString())
                .expect()
                .statusCode(OK.value())
                .when()
                .post(USER_INFO_RESOURCE);//.as(Integer.class);

        final User newUserGet =
                given().
                        header("Content-type", APPLICATION_JSON_VALUE).
                        header("AJP_eppn", newUserEPPN).
                        header("AJP_givenName",newUserEPPN).
                        header("AJP_sn",newUserEPPN).
                        header("AJP_displayName",newUserEPPN).
                        header("AJP_givenName",newUserEPPN).
                        expect().
                        statusCode(OK.value()).
                        when().
                        get(USER_RESOURCE).as(User.class);

    }

    @Test
    public void testNewUserWithCompany() {

        // If full index has been performed, then should get a result back from the search
        List<User> l = doSearchWithRetries(newUserEPPN);

        int numUsers = 1;
        Assert.assertTrue("List cannot be null", l != null);
        Assert.assertTrue("Expected " + numUsers + "  members", l.size() == numUsers);
    }



    @Test
    public void testUpdateUser () {

        User newUserGet =
                given().
                        header("Content-type", APPLICATION_JSON_VALUE).
                        header("AJP_eppn", newUserEPPN).
                        header("AJP_givenName",newUserEPPN).
                        header("AJP_sn",newUserEPPN).
                        header("AJP_displayName",newUserEPPN).
                        header("AJP_givenName",newUserEPPN).
                        expect().
                        statusCode(OK.value()).
                        when().
                        get(USER_RESOURCE).as(User.class);

        int numUsers = 1;
        Assert.assertTrue("User cannot be null", newUserGet != null);

        String originalDisplayName = newUserGet.getDisplayName();

        List<User> l1 = doSearchWithRetries(originalDisplayName);
        Assert.assertTrue("User list cannot be null", l1 != null);
        Assert.assertTrue("User list must have 1 element", l1.size() == 1);

        JSONObject userJson = getUserJson(newUserEPPN);


        // update user's display name
        String newDisplayName = "DisplayName_" + newUserGet.getAccountId();
        userJson.put("displayName", newDisplayName);
        System.out.println(userJson.toString());

        final User patchedKnownUser =
                given().
                        header("Content-type", APPLICATION_JSON_VALUE).
                        header("AJP_eppn", newUserEPPN).
                        body(userJson.toString()).
                        expect().
                        statusCode(OK.value()).
                        when().
                        patch(USER_RESOURCE).as(User.class);

        System.out.println (patchedKnownUser.getDisplayName());

        // If full index has been performed, then should get a result back from the search
        List<User> l2 = doSearchWithRetries(newDisplayName);
        Assert.assertTrue("User list cannot be null", l2 != null);
        Assert.assertTrue("User list must have 1 element", l2.size() == 1);

    }

    public ArrayList<Profile> searchMembers (String query) {

        // Note the searchMembers service returns users which are DMDII members
        ArrayList<Profile> members =
                given().
                    param("query", query).
                    header("AJP_eppn", DEFAULT_USER_EPPN).
                    expect().
                    statusCode(200).
                    when().
                    get(SEARCH_MEMBERS_RESOURCE, query).
                    andReturn().
                    as(ArrayList.class);

        return members;

    }

    public ArrayList<User> searchUsers (String query) {

        // Note the searchUsers service returns users even those that are not DMDII members
//        ArrayList<User> users =
//                given().
//                        param("query", query).
//                        header("AJP_eppn", DEFAULT_USER_EPPN).
//                        expect().
//                        statusCode(200).
//                        when().
//                        get(SEARCH_USERS_RESOURCE, query).
//                        andReturn().
//                        as(ArrayList.class);

        ArrayList<User> users =
                given().
                        param("query", query).
                        header("AJP_eppn", DEFAULT_USER_EPPN).
                        when().
                        get(SEARCH_USERS_RESOURCE, query).
                        andReturn().
                        as(ArrayList.class);

        return users;

    }

    public JSONObject getUserJson(String userEPPN) {
        final String userJsonString =
                given().
                        header("AJP_eppn", userEPPN).
                        expect().
                        statusCode(OK.value()).
                        when().
                        get(USER_RESOURCE).
                        body().asString();

        final JSONObject userJson = new JSONObject(userJsonString);

        return userJson;
    }

    public List<User> doSearchWithRetries (String query) {

        // If full index is being performed, then should get a result back from the search
        // Try search 3 times, wait 1 minute each time
        List<User> l = null;
        for (int i=0; i < 3; i++) {

            // Pause to give time for full indexing to occur
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }

            l = searchUsers(query);
            if (l != null && l.size() > 0) break;


        }
        return l;
    }
}
