package org.dmc.services.utility;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.ProjectCreateRequest;
import org.dmc.services.projects.ProjectMember;
import org.json.JSONObject;

public class TestProjectUtil {
    private final static String LOGTAG = TestProjectUtil.class.getName(); 

    public static int createProject(String user) {
        final Date date = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        final String unique = format.format(date);

        final ProjectCreateRequest json = new ProjectCreateRequest();
        json.setDescription("junit testProjectCreateJsonObject " + unique);
        json.setTitle("junitjson" + unique);

        ServiceLogger.log(LOGTAG, "testProjectCreateJsonObject: json = " + json.toString());
        final int createdId = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", user).body(json).expect()
                .statusCode(OK.value()).when().post("/projects/create").then()
                .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");
        return createdId;
    }

    public static ProjectMember createNewProjectMemberForRequest(Integer projectId, Integer idToAdd, Integer requesterId) {
        final ProjectMember newRequestedMember = new ProjectMember();
        newRequestedMember.setAccept(false);
        newRequestedMember.setFrom(requesterId.toString());
        newRequestedMember.setProfileId(idToAdd.toString());
        
        newRequestedMember.setFromProfileId(requesterId.toString());
        newRequestedMember.setProjectId(projectId.toString());
        newRequestedMember.setDate(System.currentTimeMillis());
        return newRequestedMember;
    }

}