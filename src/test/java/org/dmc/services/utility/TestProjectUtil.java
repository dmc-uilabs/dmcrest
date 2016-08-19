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
import org.json.JSONObject;

public class TestProjectUtil {
    private final static String LOGTAG = TestProjectUtil.class.getName(); 

    public static int createProjectOldMethod(String user) {
        final String unique = UUID.randomUUID().toString().substring(0, 24);
    
        final JSONObject json = new JSONObject();
        json.put("projectname", "junit" + unique);
        json.put("unixname", "junit" + unique);
        ServiceLogger.log(LOGTAG, "testProjectCreateJsonString: json = " + json.toString());
    
        final int createdId = given().header("AJP_eppn", user).body(json.toString()).expect().statusCode(OK.value()).when()
                .post("/projects/oldcreate").then().body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");
        return createdId;
    }

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

}