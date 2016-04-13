package org.dmc.services;

import static com.jayway.restassured.RestAssured.*;

import org.dmc.services.member.PostFollowingMemeber;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MemberIT extends BaseIT {
	
	private String followedMemberId = "1";

	/*
	 * test case for GET /following_members
	 */
	@Test
	public void testGet_FollowingMember() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/following_members");
	}
	
	
	/*
	 * test case for POST /following_members
	 */
	@Test
	public void testPost_FollowingMember() {
		PostFollowingMemeber obj = new PostFollowingMemeber();
		ObjectMapper mapper = new ObjectMapper();
		String postedFollowingMemberJSONString = null;
		try {
			postedFollowingMemberJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", "user_EPPN").
		body(postedFollowingMemberJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		post("/following_members");
	}
	
	
	/*
	 * test case for GET /following_members/{id}
	 */
	@Test
	public void testGet_FollowingMemberById() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/following_members/" + followedMemberId);
	}
	
	
	/*
	 * test case for DELETE /following_members/{id}
	 */
	@Test
	public void testDelete_FollowingMemberById() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		delete("/following_members/" +followedMemberId);
	}
	
}
