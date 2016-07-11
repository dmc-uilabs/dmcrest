package org.dmc.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.response.ValidatableResponse;

import org.json.JSONObject;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dmc.services.projects.ProjectDocument;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;

import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanyUserUtil;
import org.dmc.services.discussions.Discussion;
import org.dmc.services.profile.Profile;
import org.dmc.services.projects.ProjectCreateRequest;
import org.dmc.services.projects.Project;
import org.dmc.services.projects.ProjectJoinRequest;
import org.dmc.services.projects.PostProjectJoinRequest;
import org.dmc.services.projects.ProjectTag;
import org.dmc.services.users.UserDao;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//@Ignore
public class AwsProjectIT extends BaseIT {

    // Documents
    private static final String PROJECT_DOCS = "/projects/{projectID}/project_documents/";

    private static final String adminUser = "fforgeadmin";
    private final String logTag = AwsProjectIT.class.getName();
    String randomEPPN = UUID.randomUUID().toString();

    // Test for Project Documents POST and GET
    @Test
    public void testProjectDocuments() {
        int projectId = 100;

        // Adding test to get out preSignedURL
        ArrayList<ProjectDocument> oldProjectDoc = given().param("documentGroupId", 100).expect()
                .statusCode(HttpStatus.OK.value()).when().get(PROJECT_DOCS, projectId).as(ArrayList.class);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String unique = format.format(date);

        ProjectDocument json = new ProjectDocument();
        /*
         * JSONObject json = new JSONObject();
         * 
         * json.put("id", "100"); json.put("projectId", "100");
         * json.put("projectDocumentId", "100"); json.put("owner", "testing");
         * json.put("ownerId", "100"); json.put("title", "Test Doc");
         * json.put("modifed", "100"); json.put("size", "100"); json.put("file",
         * "https://s3.amazonaws.com/dmc-uploads2/uilabs.jpeg");
         */

        json.setId("100");
        json.setProjectId("100");
        json.setProjectDocumentId("100");
        json.setOwner("unixname");
        json.setOwnerId("100");
        json.setTitle("TestFile");
        json.setModifed("100");
        json.setSize("100");
        json.setFile("https://s3.amazonaws.com/dmc-uploads2/uilabs.jpeg"); // Put
                                                                           // AWS
                                                                           // Test
                                                                           // file
                                                                           // here
        // ServiceLogger.log(logTag, "testProjectCreateJsonString: json = " +
        // json.toString());

        // POST
        Integer DocId = given().header("Content-type", "application/json").body(json).expect().statusCode(200).when()
                .post(PROJECT_DOCS, projectId).then().body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract().path("id");

        // Do other GET with Added File
        ArrayList<ProjectDocument> newProjectDoc = given().param("documentGroupId", 100).expect()
                .statusCode(HttpStatus.OK.value()).when().get(PROJECT_DOCS, projectId).as(ArrayList.class);

        assertTrue("", newProjectDoc.size() == oldProjectDoc.size() + 1);

        // TESTING FOR VALID URL
        // COMMENTING OUT FOR NOW BECAUSE CANT MAP ARRAY TO PROJECT DOC CLASS
        /*
         * String preSignedURL = null;
         * 
         * if (newProjectDoc != null && !newProjectDoc.isEmpty()) {
         * ProjectDocument temp = newProjectDoc.get(newProjectDoc.size() - 1);
         * preSignedURL = temp.getFile(); }
         * 
         * //Only test if preSigned URL is obtained if(preSignedURL != null){
         * URL url = null; //Create URL object that is needed try{ url = new
         * URL(preSignedURL); }catch(Exception e){ assert(false); }
         * 
         * //Make sure that URL to resource is valid try{ HttpURLConnection
         * connection = (HttpURLConnection) url.openConnection();
         * connection.setRequestMethod("GET"); connection.connect(); }
         * catch(Exception e){ assert(false); } //Simple Url check test String
         * host = url.getHost(); assertTrue("S3 Host doesn't match",
         * host.equals("dmc-profiletest.s3.amazonaws.com")); }
         */
    }

}
