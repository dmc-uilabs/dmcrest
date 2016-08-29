package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.sql.SQLException;
import java.util.ArrayList;

import org.dmc.services.company.CompanyUserUtil;
import org.dmc.services.profile.Profile;
import org.dmc.services.projects.PostProjectJoinRequest;
import org.dmc.services.projects.ProjectJoinRequest;
import org.dmc.services.projects.ProjectMember;
import org.dmc.services.projects.ProjectMemberDao;
import org.dmc.services.users.UserDao;
import org.dmc.services.utility.TestProjectUtil;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.ValidatableResponse;

//
public class ProjectMembersIT extends BaseIT {

    // Member
    private static final String PROJECT_MEMBERS_RESOURCE = "/projects_members";
    private static final String MEMBER_ACCEPT_RESOURCE = "/new_members/{requestId}";
    private static final String MEMBER_RESOURCE_BY_ID = "/projects_members/{requestId}";
    private static final String MEMBERS_RESOURCE = "/members";

    private static final String adminUser = "fforgeadmin";
    private final static String LOGTAG = ProjectMembersIT.class.getName();
    private Integer createdId = null;
    
    private String projectId = "1";

    private String knownEPPN;
    
    @Before
    public void before() {
        if (knownEPPN == null) {
            knownEPPN = TestUserUtil.createNewUser();
        }
    }

    @Test
    public void testProject6Members() {
        ServiceLogger.log(LOGTAG, "starting testProject6Members");
        given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when().get("/projects/6/projects_members").then()
                .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));
    }

    @Test
    public void testMembersForAdminProfile() {
        ServiceLogger.log(LOGTAG, "starting testMembersForAdminProfile");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).param("profileId", "102")
            .expect().statusCode(OK.value()).when()
            .get(PROJECT_MEMBERS_RESOURCE).then()
            .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));

        // based on data loaded in gforge.psql
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        assertTrue("Expecting at least two results for forgeadmin since we set up that and use it widely in testing, but found fewer", 2 <= jsonArray.length());
    }

    @Test
    public void testMembersForProjectMemberProfileByAdmin() {
        ServiceLogger.log(LOGTAG, "starting testMembersForProjectMemberProfileByAdmin");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).param("profileId", "111")
            .expect().statusCode(OK.value()).when()
            .get(PROJECT_MEMBERS_RESOURCE).then()
            .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));

        // based on data loaded in gforge.psql
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        assertTrue("Expecting two results for testUser (111) when queried by fforgeadmin (102), but found a different amount", 2 == jsonArray.length());
    }

    @Test
    public void testMembersForProjectMemberProfileBySelf() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testMembersForProjectMemberProfileBySelf");
        final int newUserId = UserDao.getUserID(knownEPPN);
        final String newUserIdAsString = Integer.toString(newUserId);
        final ValidatableResponse response = given().header("AJP_eppn", knownEPPN).param("profileId", newUserIdAsString)
            .expect().statusCode(OK.value()).when()
            .get(PROJECT_MEMBERS_RESOURCE).then()
            .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));

        // based on data loaded in gforge.psql
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        assertTrue("Expecting no results for new user because we haven't created a project yet, but found some", 0 == jsonArray.length());
    }

    @Test
    public void testProjectJoinRequests() {
        ServiceLogger.log(LOGTAG, "starting testProjectJoinRequests");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when()
                .get("/projects_join_requests").then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestListSchema.json"));

        ServiceLogger.log(LOGTAG, "testProjectJoinRequests " + response.extract().asString());
        // based on data loaded in gforge.psql
        // this is true only for a clean db
        // String expectedResponseAsString =
        // "[{\"id\":\"6-102-102\",\"projectId\":\"6\",\"profileId\":\"102\"},{\"id\":\"6-111-102\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"6-111-111\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"4-111-111\",\"projectId\":\"4\",\"profileId\":\"111\"}]";
        // assertTrue(expectedResponseAsString.equals(response.extract().asString()));

        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        final ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();

        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject json = jsonArray.getJSONObject(i);
            final ProjectJoinRequest item = new ProjectJoinRequest();
            item.setId(json.getString("id"));
            item.setProjectId(json.getString("projectId"));
            item.setProfileId(json.getString("profileId"));
            list.add(item);
        }

        final ProjectJoinRequest expected = new ProjectJoinRequest();
        expected.setId("6-102-102");
        expected.setProjectId("6");
        expected.setProfileId("102");
        assertTrue(list.contains(expected));
        expected.setId("6-111-102");
        expected.setProjectId("6");
        expected.setProfileId("111");
        assertTrue(list.contains(expected));
        expected.setId("6-111-111");
        expected.setProjectId("6");
        expected.setProfileId("111");
        assertTrue(list.contains(expected));
        expected.setId("4-111-111");
        expected.setProjectId("4");
        expected.setProfileId("111");
        assertTrue(list.contains(expected));
    }
    
    @Test
    public void testProjectJoinRequestsWithParamList() {
        ServiceLogger.log(LOGTAG, "starting testProjectJoinRequestsWithParamList");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).param("projectId", "4,6")
                .param("profileId", "145").expect().statusCode(OK.value()).when().get("/projects_join_requests").then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestListSchema.json"));

        ServiceLogger.log(LOGTAG, "testProjectJoinRequests " + response.extract().asString());
        // based on data loaded in gforge.psql
        final String expectedResponseAsString = "[]";
        assertTrue(expectedResponseAsString.equals(response.extract().asString()));

        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        assertTrue(0 == jsonArray.length());
    }
    
    @Test
    public void testProjectJoinRequestsProject6() {
        ServiceLogger.log(LOGTAG, "starting testProjectJoinRequestsProject6");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when()
                .get("/projects/6/projects_join_requests").then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestListSchema.json"));

        ServiceLogger.log(LOGTAG, "testProjectJoinRequestsProject6" + response.extract().asString());
        // based on data loaded in gforge.psql
        // this is only true for a clean db
        // String expectedResponseAsString =
        // "[{\"id\":\"6-102-102\",\"projectId\":\"6\",\"profileId\":\"102\"},{\"id\":\"6-111-102\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"6-111-111\",\"projectId\":\"6\",\"profileId\":\"111\"}]";
        // assertTrue(expectedResponseAsString.equals(response.extract().asString()));

        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        final ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();

        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject json = jsonArray.getJSONObject(i);
            final ProjectJoinRequest item = new ProjectJoinRequest();
            item.setId(json.getString("id"));
            item.setProjectId(json.getString("projectId"));
            item.setProfileId(json.getString("profileId"));
            list.add(item);
        }

        final ProjectJoinRequest expected = new ProjectJoinRequest();
        expected.setId("6-102-102");
        expected.setProjectId("6");
        expected.setProfileId("102");
        assertTrue(list.contains(expected));
        expected.setId("6-111-102");
        expected.setProjectId("6");
        expected.setProfileId("111");
        assertTrue(list.contains(expected));
        expected.setId("6-111-111");
        expected.setProjectId("6");
        expected.setProfileId("111");
        assertTrue(list.contains(expected));
        // this one should not appear because it is project 4
        expected.setId("4-111-111");
        expected.setProjectId("4");
        expected.setProfileId("111");
        assertFalse(list.contains(expected));
    }
    
    @Test
    public void testProjectJoinRequestsProfile111() {
        ServiceLogger.log(LOGTAG, "starting testProjectJoinRequestsProfile111");
        final ValidatableResponse response = given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when()
                .get("/profiles/111/projects_join_requests").then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestListSchema.json"));

        ServiceLogger.log(LOGTAG, "testProjectJoinRequestsProfile111" + response.extract().asString());
        // based on data loaded in gforge.psql
        final String expectedResponseAsString = "[{\"id\":\"6-111-102\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"6-111-111\",\"projectId\":\"6\",\"profileId\":\"111\"},{\"id\":\"4-111-111\",\"projectId\":\"4\",\"profileId\":\"111\"}]";
        assertTrue(expectedResponseAsString.equals(response.extract().asString()));

        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        final ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();

        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject json = jsonArray.getJSONObject(i);
            final ProjectJoinRequest item = new ProjectJoinRequest();
            item.setId(json.getString("id"));
            item.setProjectId(json.getString("projectId"));
            item.setProfileId(json.getString("profileId"));
            list.add(item);
        }

        final ProjectJoinRequest expected = new ProjectJoinRequest();
        expected.setId("6-102-102");
        expected.setProjectId("6");
        expected.setProfileId("102");
        // this one should not appear because it is profile 102
        assertFalse(list.contains(expected));
        expected.setId("6-111-102");
        expected.setProjectId("6");
        expected.setProfileId("111");
        assertTrue(list.contains(expected));
        expected.setId("6-111-111");
        expected.setProjectId("6");
        expected.setProfileId("111");
        assertTrue(list.contains(expected));
        expected.setId("4-111-111");
        expected.setProjectId("4");
        expected.setProfileId("111");
        assertTrue(list.contains(expected));
    }
    
    @Test
    public void testCreateAndDeleteProjectJoinRequests() {
        ServiceLogger.log(LOGTAG, "starting testCreateAndDeleteProjectJoinRequests");
        final PostProjectJoinRequest json = new PostProjectJoinRequest();
        json.setProjectId("6");
        json.setProfileId("110");

        final String id = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(json).expect()
                .statusCode(OK.value()).when().post("/projects_join_requests").then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectJoinRequestSchema.json")).extract().path("id");

        // forbidden
        final String invalid_id = "12-44-102-483";
        given().header("AJP_eppn", userEPPN).expect().statusCode(FORBIDDEN.value()).when()
                .delete("/projects_join_requests/" + invalid_id);

        // unauthorized
        given().header("AJP_eppn", "testUser").expect().statusCode(UNAUTHORIZED.value()).when().delete("/projects_join_requests/" + id);

        // should be successful
        given().header("AJP_eppn", userEPPN).expect().statusCode(NO_CONTENT.value()).when().delete("/projects_join_requests/" + id);

        // forbidden - because it is already deleted
        given().header("AJP_eppn", userEPPN).param("id", id).expect().statusCode(FORBIDDEN.value()).when()
                .delete("/projects_join_requests/" + id);

    }
    /**
     * POST /projects_members
     * PATCH /new_members/{request_id}
     */
    @Test
    public void testProjectMemberAcceptAfterProjectCreate() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testProjectMemberAcceptAfterProjectCreate");

        Integer adminId = UserDao.getUserID(userEPPN);
        String toBeAdded = TestUserUtil.createNewUser();
        Integer toBeAddedId = UserDao.getUserID(toBeAdded);

        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, toBeAddedId, adminId);
        
        if (this.createdId != null) {
            ProjectMember reply = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser).body(newRequestedMember)
                    .expect().statusCode(HttpStatus.OK.value())
                    .when().post(PROJECT_MEMBERS_RESOURCE).as(ProjectMember.class);
            
            assertTrue("admin id not equal", adminId.intValue() == Integer.parseInt(reply.getFromProfileId()));
            assertTrue("invitee not equal", reply.getProfileId().equals(toBeAddedId.toString()));
            assertTrue("not correct project", this.createdId.toString().equals(reply.getProjectId()));
            assertFalse("invitation should not have been accepted yet, but was", reply.getAccept());
            final String actualRequestId = reply.getId();
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", toBeAdded)
                    .expect().statusCode(OK.value())
                    .when().patch(MEMBER_ACCEPT_RESOURCE, actualRequestId);

            final ValidatableResponse response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser)
                .expect().statusCode(HttpStatus.OK.value())
                .when().get(MEMBER_RESOURCE_BY_ID, toBeAddedId.toString())
                .then();
            final JSONArray jsonArray = new JSONArray(response.extract().asString());
            
            assertEquals("expect one project_member result, but found a different amount", 1, jsonArray.length());
            final JSONObject json = jsonArray.getJSONObject(0);
            assertTrue("project invitation should have been accepted, but was not",json.getBoolean("accept"));
        }
    }

    /**
     * PATCH /new_members/{request_id}
     *    where accept = true, but requestId does not exist 
     */
    @Test
    public void testProjectMemberAcceptNoRequest() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testProjectMemberAcceptNoRequest");

        Integer adminId = UserDao.getUserID(userEPPN);
        String toJoinUser = TestUserUtil.createNewUser();
        Integer toJoinId = UserDao.getUserID(toJoinUser);

        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, toJoinId, adminId);
        final String requestId = newRequestedMember.getId();
        
        if (this.createdId != null) {
            final String response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", toJoinUser).expect()
                    .statusCode(NOT_FOUND.value()).when().patch(MEMBER_ACCEPT_RESOURCE, requestId).asString();

            assertTrue("No Existing Request", response.contains("no existing request to join the project"));
        }
    }

    /**
     * POST /projects_members
     *    where requester is not admin of project, so should be forbidden
     * PATCH /new_members/{request_id}
     *    where accept = true, since above failed, then this should not be found 
     */
    @Test
    public void testProjectMemberAcceptProjectAddMemberFakeAdmin() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testProjectMemberAcceptProjectAddMemberFakeAdmin");

        Integer toBeAddedId = UserDao.getUserID(knownEPPN);
        String fakeAdminEPPN = TestUserUtil.createNewUser();
        Integer fakeAdminId = UserDao.getUserID(fakeAdminEPPN);
       
        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, toBeAddedId, fakeAdminId);
        final String requestId = newRequestedMember.getId();
        
        if (this.createdId != null) {
            final String response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", fakeAdminEPPN).body(newRequestedMember)
                .expect().statusCode(HttpStatus.FORBIDDEN.value())
                .when().post(PROJECT_MEMBERS_RESOURCE).asString();
            assertTrue("Expected forbidden message", response.contains("is not allowed to invite new members"));
            // above fails, so accept request should not match
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownEPPN).expect()
                    .statusCode(FORBIDDEN.value()).when().patch(MEMBER_ACCEPT_RESOURCE, requestId);
        }
    }

    /**
     * POST /projects_members
     *    where requester is not admin of project, so should be forbidden
     * DELETE /projects_members/{request_id}
     *    where accept = true, since above failed, then this should not be found 
     */
    @Test
    public void testProjectMemberRejectProjectAddMemberFakeAdmin() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testProjectMemberRejectProjectAddMemberFakeAdmin");

        Integer toBeAddedId = UserDao.getUserID(knownEPPN);
        String fakeAdminEPPN = TestUserUtil.createNewUser();
        Integer fakeAdminId = UserDao.getUserID(fakeAdminEPPN);
       
        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, toBeAddedId, fakeAdminId);
        final String requestId = newRequestedMember.getId();
        
        if (this.createdId != null) {
            final String response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", fakeAdminEPPN).body(newRequestedMember)
                .expect().statusCode(HttpStatus.FORBIDDEN.value())
                .when().post(PROJECT_MEMBERS_RESOURCE).asString();
            assertTrue("Expected forbidden message", response.contains("is not allowed to invite new members"));
            // above fails, so accept request should not match
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownEPPN).expect()
                    .statusCode(UNPROCESSABLE_ENTITY.value()).when().delete(MEMBER_RESOURCE_BY_ID, requestId);
        }
    }

    /**
     * DELETE /projects_members/{requestId}
     */
    
    @Test
    public void testProjectMemberRejectOnlyAdmin() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testProjectMemberRejectOnlyAdmin");

        final int adminId = UserDao.getUserID(userEPPN);

        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, adminId, adminId);
        final String requestId = newRequestedMember.getId();        

        if (this.createdId != null) {
            // project membership is already requested for project creator
            final String response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser).expect()
                    .statusCode(FORBIDDEN.value()).when().delete(MEMBER_RESOURCE_BY_ID, requestId).asString();
            assertTrue("Should not allow only admin to delete their project membership, but did not give that message", response.contains("is the only Admin of project"));
        }
    }

    /**
     * POST /projects_members
     *    where requester is not admin of project, so should be forbidden
     * PATCH /new_members/{request_id}
     *    where accept = true, since above failed, then this should not be found 
     */
    @Test
    public void testProjectMemberAcceptProjectAddMemberCallerOverwritesRequesterInJsonBody() throws SQLException{
        ServiceLogger.log(LOGTAG, "starting testProjectMemberAcceptProjectAddMemberCallerOverwritesRequesterInJsonBody");

        Integer toBeAddedId = UserDao.getUserID(knownEPPN);
        
        String fakeUser = TestUserUtil.createNewUser();
        Integer fakeId = UserDao.getUserID(fakeUser);
       
        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, toBeAddedId, fakeId);
        final String requestId = newRequestedMember.getId();
        
        if (this.createdId != null) {
            final ProjectMember actualRequestedMember = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser).body(newRequestedMember)
                    .expect().statusCode(HttpStatus.OK.value())
                    .when().post(PROJECT_MEMBERS_RESOURCE).as(ProjectMember.class);
            final String actualRequestId = actualRequestedMember.getId();
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownEPPN).expect()
                    .statusCode(FORBIDDEN.value()).when().patch(MEMBER_ACCEPT_RESOURCE, requestId);
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownEPPN).expect()
                    .statusCode(OK.value()).when().patch(MEMBER_ACCEPT_RESOURCE, actualRequestId);
        }
    }

    /**
     * DELETE /projects_members/{requestId}
     */
    @Test
    public void testProjectMemberRejectProjectAddMemberCallerOverwritesRequesterInJsonBody() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testProjectMemberRejectProjectAddMemberCallerOverwritesRequesterInJsonBody");

        String unique = TestUserUtil.generateTime();
        final int toBeAddedId = UserDao.getUserID(knownEPPN);
        final int fakeAdminId = UserIT.createUserAndReturnID(unique);
       
        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, toBeAddedId, fakeAdminId);
        final String requestId = newRequestedMember.getId();        

        if (this.createdId != null) {
            final ProjectMember actualRequestedMember = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser).body(newRequestedMember)
                    .expect().statusCode(HttpStatus.OK.value())
                    .when().post(PROJECT_MEMBERS_RESOURCE).as(ProjectMember.class);
            final String actualRequestId = actualRequestedMember.getId();
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownEPPN)
                    .expect().statusCode(UNPROCESSABLE_ENTITY.value())
                    .when().delete(MEMBER_RESOURCE_BY_ID, requestId);
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownEPPN)
                    .expect().statusCode(OK.value())
                    .when().delete(MEMBER_RESOURCE_BY_ID, actualRequestId);
        }
    }

    /**
     * PATCH /projects_members/{requestId}
     */
    @Test
    public void testProjectMemberAcceptWrongUser() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testProjectMemberAcceptWrongUser");

        final int toBeAddedId = UserDao.getUserID(knownEPPN);
        final String fakeUserName = TestUserUtil.createNewUser();

        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, toBeAddedId, toBeAddedId);
        final String requestId = newRequestedMember.getId();        

        if (this.createdId != null) {
            final ProjectMember actualRequestedMember = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser).body(newRequestedMember)
                    .expect().statusCode(HttpStatus.OK.value())
                    .when().post(PROJECT_MEMBERS_RESOURCE).as(ProjectMember.class);
            final String actualRequestId = actualRequestedMember.getId();
            // Bad admin should get forbidden
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", fakeUserName).expect()
                    .statusCode(FORBIDDEN.value()).when().patch(MEMBER_ACCEPT_RESOURCE, requestId);
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", fakeUserName).expect()
            .statusCode(FORBIDDEN.value()).when().patch(MEMBER_ACCEPT_RESOURCE, actualRequestId);
            // Good admin should get forbidden because only user should be able to accept request
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser)
                    .expect().statusCode(FORBIDDEN.value())
                    .when().patch(MEMBER_ACCEPT_RESOURCE, actualRequestId);
        }
    }

    /**
     * DELETE /projects_members/{requestId}
     */
    @Test
    public void testProjectMemberRejectWrongUser() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testProjectMemberRejectWrongUser");

        final int toBeAddedId = UserDao.getUserID(knownEPPN);
        final String fakeUserName = TestUserUtil.createNewUser();

        this.createdId = TestProjectUtil.createProject(userEPPN);
        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(this.createdId, toBeAddedId, toBeAddedId);
        final String requestId = newRequestedMember.getId();        

        if (this.createdId != null) {
            final ProjectMember actualRequestedMember = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser).body(newRequestedMember)
                    .expect().statusCode(HttpStatus.OK.value())
                    .when().post(PROJECT_MEMBERS_RESOURCE).as(ProjectMember.class);
            final String actualRequestId = actualRequestedMember.getId();
            // Bad admin should get forbidden
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", fakeUserName)
                    .expect().statusCode(FORBIDDEN.value())
                    .when().delete(MEMBER_RESOURCE_BY_ID, requestId);
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", fakeUserName)
            .expect().statusCode(FORBIDDEN.value())
            .when().delete(MEMBER_RESOURCE_BY_ID, actualRequestId);
            // Good admin should be allowed to remove "bad" users
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser)
                    .expect().statusCode(OK.value())
                    .when().delete(MEMBER_RESOURCE_BY_ID, actualRequestId);
        }
    }

    /**
     * GET /members
     */
    @Test
    public void testGetMembers() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testGetMembers");
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode members = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", adminUser).expect()
                .statusCode(OK.value()).when().get(MEMBERS_RESOURCE).as(JsonNode.class);

        final ArrayList<Profile> membersList = mapper.readValue(mapper.treeAsTokens(members),
                new TypeReference<ArrayList<Profile>>() {
                });

        for (Profile profile : membersList) {
            final String userName = UserDao.getUserName(profile.getId());

            assertTrue("Member " + userName + " is DMDII Member", CompanyUserUtil.isDMDIIMember(userName));
        }
    }

    @Test
    public void testGetMembersMultipleProjects() {
        ServiceLogger.log(LOGTAG, "starting testGetMembersMultipleProjects");
        given().header("AJP_eppn", userEPPN).param("projectId", 6).param("projectId", 3)
                .expect().statusCode(OK.value()).when().get("/projects_members").then()
                .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));
    }

    @Test
    public void testGetMembersWithProjectAndProfile() throws SQLException {
        ServiceLogger.log(LOGTAG, "starting testGetMembersWithProjectAndProfile");

        // create a project, add userEPPN and knownEPPN to the project
        final Integer newProjectId = TestProjectUtil.createProject(userEPPN);
        final Integer adminId = UserDao.getUserID(userEPPN);
        final Integer memberId = UserDao.getUserID(knownEPPN);
        final ProjectMember requestProjectMember = TestProjectUtil.createNewProjectMemberForRequest(newProjectId, memberId, adminId);

        final ProjectMember actualRequestedMember = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(requestProjectMember)
                .expect().statusCode(HttpStatus.OK.value())
                .when().post(PROJECT_MEMBERS_RESOURCE).as(ProjectMember.class);
        final String actualRequestId = actualRequestedMember.getId();
        given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownEPPN)
                .expect().statusCode(OK.value())
                .when().patch(MEMBER_ACCEPT_RESOURCE, actualRequestId);

        // now check that the GET /project_members work correctly with different arguments.
        final ValidatableResponse responseAdmin = given().header("AJP_eppn", userEPPN).param("projectId", newProjectId).param("profileId", adminId)
                .expect().statusCode(OK.value()).when().get("/projects_members").then()
                .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));
        final JSONArray jsonArrayAdmin = new JSONArray(responseAdmin.extract().asString());
        assertEquals("expect one project_member result for project + profile check for admin, but found a different amount", 1, jsonArrayAdmin.length());

        final ValidatableResponse responseMember = given().header("AJP_eppn", userEPPN).param("projectId", newProjectId).param("profileId", adminId)
                .expect().statusCode(OK.value()).when().get("/projects_members").then()
                .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));
        final JSONArray jsonArrayMember = new JSONArray(responseMember.extract().asString());
        assertEquals("expect one project_member result for project + profile check for member, but found a different amount", 1, jsonArrayMember.length());

        final ValidatableResponse responseProjectOnly = given().header("AJP_eppn", userEPPN).param("projectId", newProjectId)
                .expect().statusCode(OK.value()).when().get("/projects_members").then()
                .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));
        final JSONArray jsonArrayProjectOnly = new JSONArray(responseProjectOnly.extract().asString());
        assertEquals("expect 2 for project only check, but found a different amount", 2, jsonArrayProjectOnly.length());

        // because there is some preloaded data for adminId (102), expect > 1 result.
        final ValidatableResponse responseProfileOnly = given().header("AJP_eppn", userEPPN).param("profileId", adminId)
                .expect().statusCode(OK.value()).when().get("/projects_members").then()
                .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));
        final JSONArray jsonArrayProfileOnly = new JSONArray(responseProfileOnly.extract().asString());
        assertTrue("expect >1 project_member result for profile only check, but found a different amount", 1 < jsonArrayProfileOnly.length());
    }
}
