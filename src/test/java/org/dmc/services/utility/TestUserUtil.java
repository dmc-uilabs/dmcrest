package org.dmc.services.utility;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jayway.restassured.response.ValidatableResponse;

import org.dmc.services.member.FollowingMember;
import org.dmc.services.security.UserPrincipal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;

import static com.jayway.restassured.RestAssured.*;

public class TestUserUtil {
	
	public static void setupSecurityContext(Integer id, String username) {
		UserPrincipal user = new UserPrincipal(id, username);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "test");
		SecurityContextHolder.getContext().setAuthentication(token);
	}
	
    public static String generateTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String unique = format.format(date);
        return unique;
    }

    public static String uniqueID() {
        UUID uniqueID = UUID.randomUUID();
        return uniqueID.toString();
    }

    public static String uniqueUserEPPN() {
        UUID uniqueID = UUID.randomUUID();
        return "uniqueUser" + uniqueID.toString();
    }

    public static String createNewUser() {
        String unique = TestUserUtil.generateTime();

        Integer id = given().header("Content-type", "text/plain").header("AJP_eppn", "userEPPN" + unique)
                .header("AJP_givenName", "userGivenName" + unique).header("AJP_sn", "userSurname" + unique)
                .header("AJP_displayName", "userDisplayName" + unique).header("AJP_mail", "userEmail" + unique).expect()
                .statusCode(HttpStatus.OK.value()).when().post("/users/create").then().extract().path("id");

        return new String("userEPPN" + unique);
    }

    public static Integer addBasicInfomationToUser(String userEPPN) {
        JSONObject json = new JSONObject();

        json.put("email", "test basic info email");
        json.put("firstName", "test basic info first name");
        json.put("lastName", "test basic info last name");
        json.put("company", "1");

        Integer id = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
                .body(json.toString()).expect().statusCode(HttpStatus.OK.value()).when().post("/user-basic-information")
                .then().extract().path("id");

        return id;
    }

    public static ArrayList<FollowingMember> readFollowingMemberResponse(ValidatableResponse response) {
        final ArrayList<FollowingMember> list = new ArrayList<FollowingMember>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject json = jsonArray.getJSONObject(i);
            final FollowingMember item = new FollowingMember();
            item.setFollower(json.getString("accountId"));
            item.setFollowed(json.getString("profileId"));
            list.add(item);
        }
        return list;
    }
}