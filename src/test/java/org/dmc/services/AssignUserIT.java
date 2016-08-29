package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;

import org.dmc.services.projects.ProjectMember;
import org.dmc.services.users.AssignUser;
import org.dmc.services.utility.TestProjectUtil;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AssignUserIT extends BaseIT {

    private static final String LOGTAG = AssignUserIT.class.getName();

    private static final String ASSIGN_USERS_RESOURCE = "/assign_users/";
    private static final String ASSIGN_USERS_BY_PROJECT_RESOURCE = "/assign_users/{projectId}";
    private static final String PROJECT_MEMBERS_RESOURCE = "/projects_members";
    private static final String MEMBER_ACCEPT_RESOURCE = "/new_members/{requestId}";

    @Test
    public void testAssignUsersWithoutProject() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testAssignUsersWithoutProject");
        String uniqueUserEPPN1 = TestUserUtil.createNewUser();
        Integer int1 = TestUserUtil.addBasicInfomationToUser(uniqueUserEPPN1);
        ObjectMapper mapper1 = new ObjectMapper();

        JsonNode users1 = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", uniqueUserEPPN1)
                .header("projectId", -1).expect().statusCode(HttpStatus.OK.value()).when().get(ASSIGN_USERS_RESOURCE)
                .as(JsonNode.class);

        ArrayList<AssignUser> membersList1 = mapper1.readValue(mapper1.treeAsTokens(users1),
                new TypeReference<ArrayList<AssignUser>>() {
                });

        String uniqueUserEPPN2 = TestUserUtil.createNewUser();
        Integer int2 = TestUserUtil.addBasicInfomationToUser(uniqueUserEPPN2);
        ObjectMapper mapper2 = new ObjectMapper();

        JsonNode users2 = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", uniqueUserEPPN2)
                .header("projectId", -1).expect().statusCode(HttpStatus.OK.value()).when().get(ASSIGN_USERS_RESOURCE)
                .as(JsonNode.class);

        ArrayList<AssignUser> membersList2 = mapper2.readValue(mapper2.treeAsTokens(users2),
                new TypeReference<ArrayList<AssignUser>>() {
                });

        assertFalse("Users' EPPNs ( " + uniqueUserEPPN1 + " & " + uniqueUserEPPN2 + " ) are the same",
                uniqueUserEPPN1.equals(uniqueUserEPPN2));
        assertTrue("Second list " + membersList2.size() + " in not one larger than first list " + membersList1.size(),
                membersList2.size() == membersList1.size() + 1);
    }

    @Test
    public void testAssignUsersWithProject() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testAssignUsersWithProject");
        // create user and add basic info
        String uniqueUserEPPN1 = TestUserUtil.createNewUser();
        TestUserUtil.addBasicInfomationToUser(uniqueUserEPPN1);

        // create project for user
        int projectId = TestProjectUtil.createProject(uniqueUserEPPN1);

        ObjectMapper mapper1 = new ObjectMapper();
        // get users in a project
        JsonNode users1 = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", uniqueUserEPPN1)
                .expect().statusCode(HttpStatus.OK.value()).when().get(ASSIGN_USERS_BY_PROJECT_RESOURCE, projectId).as(JsonNode.class);

        ArrayList<AssignUser> membersList1 = mapper1.readValue(mapper1.treeAsTokens(users1),
                new TypeReference<ArrayList<AssignUser>>() {
                });

        assertTrue("Project does not have one member, has " + membersList1.size(), membersList1.size() == 1);
    }

    @Test
    public void testAssignMultipleUsersWithProject() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testAssignMultipleUsersWithProject");
        // create user and add basic info
        String projectAdminUser = TestUserUtil.createNewUser();
        int projectAdminUserId = TestUserUtil.addBasicInfomationToUser(projectAdminUser);
        String projectMemberUser = TestUserUtil.createNewUser();
        int projectMemberUserId = TestUserUtil.addBasicInfomationToUser(projectMemberUser);

        // create project for user
        Integer projectId = TestProjectUtil.createProject(projectAdminUser);

        final ProjectMember newRequestedMember = TestProjectUtil.createNewProjectMemberForRequest(projectId, projectMemberUserId, projectAdminUserId);
        
        if (projectId != null) {
            ProjectMember reply = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", projectAdminUser).body(newRequestedMember)
                    .expect().statusCode(HttpStatus.OK.value())
                    .when().post(PROJECT_MEMBERS_RESOURCE).as(ProjectMember.class);
            
            final String actualRequestId = reply.getId();
            // new member accepts invitation
            given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", projectMemberUser)
                    .expect().statusCode(OK.value())
                    .when().patch(MEMBER_ACCEPT_RESOURCE, actualRequestId);
        }

        ObjectMapper mapper1 = new ObjectMapper();
        // get users in a project
        JsonNode usersListByAdmin = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", projectAdminUser)
                .expect().statusCode(HttpStatus.OK.value()).when().get(ASSIGN_USERS_BY_PROJECT_RESOURCE, projectId).as(JsonNode.class);
        ArrayList<AssignUser> membersListByAdmin = mapper1.readValue(mapper1.treeAsTokens(usersListByAdmin),
                new TypeReference<ArrayList<AssignUser>>() {
                });
        assertTrue("Project does not have two members when queried by admin, has " + membersListByAdmin.size(), membersListByAdmin.size() == 2);

        JsonNode usersListByMember = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", projectMemberUser)
                .expect().statusCode(HttpStatus.OK.value()).when().get(ASSIGN_USERS_BY_PROJECT_RESOURCE, projectId).as(JsonNode.class);
        ArrayList<AssignUser> membersListByMember = mapper1.readValue(mapper1.treeAsTokens(usersListByAdmin),
                new TypeReference<ArrayList<AssignUser>>() {
                });
        assertTrue("Project does not have two members when queried by member, has " + membersListByMember.size(), membersListByMember.size() == 2);
    }

    @Test
    public void testAssignUsersWithNonProjectMember() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testAssignUsersWithNonProjectMember");
        // create user and add basic info
        String uniqueUserEPPN1 = TestUserUtil.createNewUser();
        TestUserUtil.addBasicInfomationToUser(uniqueUserEPPN1);

        // create project for user
        int projectId = TestProjectUtil.createProject(uniqueUserEPPN1);

        // create a NEW user and add basic info
        String uniqueUserEPPN2 = TestUserUtil.createNewUser();
        TestUserUtil.addBasicInfomationToUser(uniqueUserEPPN2);

        ObjectMapper mapper = new ObjectMapper();

        // get users in a project, using non-member of a project
        JsonNode users = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", uniqueUserEPPN2).expect()
                .statusCode(HttpStatus.OK.value()).when().get(ASSIGN_USERS_BY_PROJECT_RESOURCE, + projectId).as(JsonNode.class);

        ArrayList<AssignUser> membersList = mapper.readValue(mapper.treeAsTokens(users),
                new TypeReference<ArrayList<AssignUser>>() {
                });

        assertTrue("Returned list is empty because request was made by non project member", membersList.size() == 0);
    }

}