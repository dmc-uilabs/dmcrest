package org.dmc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dmc.services.discussions.Discussion;
import org.dmc.services.discussions.IndividualDiscussion;
import org.dmc.services.projects.Project;
import org.dmc.services.projects.ProjectCreateRequest;
import org.dmc.services.projects.ProjectTag;
import org.dmc.services.utility.TestProjectUtil;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//
public class ProjectIT extends BaseIT {

    private static final String PROJECT_DISCUSSIONS_RESOURCE = "/projects/{projectID}/all-discussions";
    private static final String PROJECT_INDIVIDUAL_DISCUSSION_RESOURCE = "/projects/{projectID}/individual-discussion";
    private static final String PROJECT_UPDATE_RESOURCE = "/projects/{id}";
    private static final String PROJECT_GET_ALL_RESOURCE = "/projects/all";

    private static final String PROJECT_GET_TAGS_FOR_PROJECT = "/projects/{projectId}/projects_tags";
    private static final String PROJECT_TAGS = "/projects_tags";
    private static final String PROJECT_DELETE_TAG = "/projects_tags/{tagId}";

    private static final String adminUser = "fforgeadmin";
    private final String logTag = ProjectIT.class.getName();
    private DiscussionIT discussionIT = new DiscussionIT();
    private Integer createdId = null;
    
    private Integer createdTagId = null;

    private String projectId = "1";

    private String knownEPPN;
    
    @Before
    public void before() {
        if (knownEPPN == null) {
            knownEPPN = TestUserUtil.createNewUser();
        }
    }

    @Test
    public void testProject6() {
        ServiceLogger.log(logTag, "starting testProject6");
        given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when().get("/projects/6").then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectSchema.json"));
    }
    
    @Test
    public void testProject5() {
        ServiceLogger.log(logTag, "starting testProject5 (but really still 6???)");

        // TODO: need to update to another demo project
        given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when().get("/projects/6").then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectSchema.json"));
    }
    
    // this tests could do check more about what tests are returned
    @Test
    public void testProjectList() {
        ServiceLogger.log(logTag, "starting testProjectList");
        given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when().get("/projects").then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectListSchema.json"));
    }
    
    // this tests could do check more about what tests are returned
    @Test
    public void testProjectListAll() {
        ServiceLogger.log(logTag, "starting testProjectListAll");
        given().header("AJP_eppn", userEPPN).param("_order", "ASC").param("_sort", "most_recent").param("_start", 0)
                .expect().statusCode(OK.value()).when().get(PROJECT_GET_ALL_RESOURCE).then()
                .body(matchesJsonSchemaInClasspath("Schemas/projectListSchema.json"));
    }
    
    @Test
    public void testProjectCreateJsonString() {
        ServiceLogger.log(logTag, "starting testProjectCreateJsonString");
        this.createdId = TestProjectUtil.createProjectOldMethod(userEPPN);
    }
    
    // see as an example to configure the object
    // https://github.com/jayway/rest-assured/wiki/Usage#serialization
    @Test
    public void testProjectCreateJsonObject() throws IOException {
        ServiceLogger.log(logTag, "starting testProjectCreateJsonObject");
        this.createdId = TestProjectUtil.createProject(userEPPN);
    }
    
    @Test
    public void ftestProjectCreateFailOnDuplicate() {
        ServiceLogger.log(logTag, "starting ftestProjectCreateFailOnDuplicate");
        final JSONObject json = new JSONObject();
        final Date date = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String unique = format.format(date);

        json.put("projectname", "junitTestdup" + unique);
        json.put("unixname", "junitdup" + unique);
        ServiceLogger.log(logTag, "testProjectCreateFailOnDuplicate: json = " + json.toString());

        // first time should work
        given().header("Content-type", "text/plain").header("AJP_eppn", userEPPN).body(json.toString()).expect()
                .statusCode(OK.value()).when().post("/projects/oldcreate").then()
                .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));

        ServiceLogger.log(logTag, "testProjectCreateFailOnDuplicate: try to create again");
        // second time should fail, because unixname is a duplicate
        given().header("Content-type", "text/plain").header("AJP_eppn", userEPPN).body(json.toString()).expect()
                .statusCode(OK.value()).when().post("/projects/oldcreate").then().log().all()
                .body(matchesJsonSchemaInClasspath("Schemas/errorSchema.json"));
    }
    
    @Test
    public void testProjectCreateFailOnDuplicateJson() throws IOException {
        ServiceLogger.log(logTag, "starting testProjectCreateFailOnDuplicateJson");
        final Date date = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String unique = format.format(date);

        final ProjectCreateRequest json = new ProjectCreateRequest();
        json.setDescription("junitdup json testing " + unique);
        json.setTitle("junitTestJsondup" + unique);

        ServiceLogger.log(logTag, "testProjectCreateFailOnDuplicateJson: json = " + json.toString());

        // first time should work
        given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(json).expect()
                .statusCode(OK.value()).when().post("/projects/create").then()
                .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));

        ServiceLogger.log(logTag, "testProjectCreateFailOnDuplicateJson: try to create again");

        // second time should fail, because unixname is a duplicate
        given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).body(json).expect()
                .statusCode(OK.value()).when().post("/projects/create").then().log().all()
                .body(matchesJsonSchemaInClasspath("Schemas/errorSchema.json"));

    }
    
    @Test
    public void testProjectTypeChecks() {
        ServiceLogger.log(logTag, "starting testProjectTypeChecks");
        assertEquals(1, Project.IsPublic("Public"));
        assertEquals(0, Project.IsPublic("PRIVATE"));
    }
    
    @Test
    public void testAllProjectDiscussions() {
        ServiceLogger.log(logTag, "starting testAllProjectDiscussions");

        final ObjectMapper mapper = new ObjectMapper();
        this.createdId = TestProjectUtil.createProject(userEPPN);

        if (this.createdId != null) {
            Integer discussionId = createDiscussion(this.createdId);
            if (discussionId != null) {
                JsonNode discussions = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownEPPN)
                        .expect().statusCode(200).when().get(PROJECT_DISCUSSIONS_RESOURCE, this.createdId)
                        .as(JsonNode.class);

                try {
                    final ArrayList<Discussion> discussionList = mapper.readValue(mapper.treeAsTokens(discussions),
                            new TypeReference<ArrayList<Discussion>>() {
                            });

                    assertEquals("should  only be one discussion that we created, found a different amount", 1,
                            discussionList.size());
                    for (Discussion discussion : discussionList) {
                        assertTrue("Discussion belongs to project",
                                discussion.getProjectId().equals(this.createdId.toString()));
                    }

                } catch (Exception e) {
                    ServiceLogger.log(logTag, e.getMessage());
                }
            }
        }
    }
    

    private Integer createDiscussion(Integer projectId) {
        String projId = (projectId != null) ? projectId.toString() : "123";
        JSONObject json = new JSONObject();
        json.put("title", "test discussion title");
        json.put("message", "test discussion message");
        json.put("createdBy", "test-disc-created-by");
        json.put("createdAt", 1232000);
        json.put("accountId", "123");
        json.put("projectId", projId);
        
        return given()
                .header("Content-type", APPLICATION_JSON_VALUE)
                .header("AJP_eppn", knownEPPN)
                .body(json.toString())
                .expect()
                .statusCode(200)
                .when()
                .post("/discussions/create")
                .then()
                .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract()
                .path("id");
    }


    @Test
    public void testGetProject1Tags() {
        ServiceLogger.log(logTag, "starting testGetProject1Tags");

        final int projectId = 1;
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonSpecs = given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when()
                .get(PROJECT_GET_TAGS_FOR_PROJECT, projectId).as(JsonNode.class);

        try {
            final ArrayList<ProjectTag> tags = mapper.readValue(mapper.treeAsTokens(jsonSpecs), new TypeReference<ArrayList<ProjectTag>>() {
            });
            // assert that we've received tags for the requested project ID
            assertTrue("no tags found for project " + projectId, tags.size() > 0);
            for (ProjectTag tag : tags) {
                assertTrue("Project Tag is for the project ID requested",
                        Integer.parseInt(tag.getProjectId()) == projectId);
            }
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.IncorrectType, "unable to parse tag array list: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetAllTags() {
        ServiceLogger.log(logTag, "starting testGetAllTags");
        final ArrayList<ProjectTag> tags = getAllTagsHelper();
        // assert that we've received tags for the requested project ID
        assertTrue("no tags found", tags.size() > 0);
        int countProjects = 0;
        int firstProject = -1;
        for (ProjectTag tag : tags) {
            if (0 == countProjects) {
                firstProject = Integer.parseInt(tag.getProjectId());
                countProjects++;
            } else {
                if (firstProject != Integer.parseInt(tag.getProjectId())) {
                    countProjects++;
                    break;
                }
            }
        }
        assertTrue("Only tags for one project returned", countProjects > 1);
    }

    private ArrayList<ProjectTag> getAllTagsHelper() {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonSpecs = given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when()
                .get(PROJECT_TAGS).as(JsonNode.class);
        try {
            final ArrayList<ProjectTag> tags = mapper.readValue(mapper.treeAsTokens(jsonSpecs), new TypeReference<ArrayList<ProjectTag>>() {
            });
            return tags;
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.IncorrectType, "error parsing tags array list: " + e.getMessage());
        }
    }
    
    @Test
    public void testProject6Members() {
        ServiceLogger.log(logTag, "starting testProject6Members");
        given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when().get("/projects/6/projects_members").then()
                .log().all().body(matchesJsonSchemaInClasspath("Schemas/projectMemberListSchema.json"));

    }

    /**
     * test case for post /projects_tags
     */
    @Test
    public void testProjectPost_ProjectTag() {
        ServiceLogger.log(logTag, "starting testProjectPost_ProjectTag");

        final String tagName = "Test-tag-name " + UUID.randomUUID().toString().substring(0, 24);;
        this.createdId = TestProjectUtil.createProject(userEPPN);

        if (this.createdId != null) {
            final String projectId = Integer.toString(this.createdId);
            final ProjectTag obj = new ProjectTag();
            obj.setProjectId(projectId);
            obj.setName(tagName);

            final ObjectMapper mapper = new ObjectMapper();

            String postedProjectTagsJSONString = null;

            try {
                postedProjectTagsJSONString = mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            final ProjectTag tag = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN)
                    .body(postedProjectTagsJSONString).expect().statusCode(OK.value()).when()
                    .post(PROJECT_TAGS).as(ProjectTag.class);
            createdTagId = Integer.parseInt(tag.getId());
            // assert some attributes on the created tag
            assertTrue("Created Project Tag name is as requested", tag.getName().equals(tagName));
            assertTrue("Created Project Project ID is as requested", tag.getProjectId().equals(projectId));
        }

    }
    
    /**
     * test case for DELETE /projects_tags/{projectTagid}
     */
    @Test
    public void testDelete_ProjectTag() {
        ServiceLogger.log(logTag, "starting testDelete_ProjectTag");
        // create a tag to delete
        testProjectPost_ProjectTag();
        final int tagCountBeforeDelete = getAllTagsHelper().size();
        final int deletedTagId = given().header("AJP_eppn", userEPPN).expect().statusCode(OK.value()).when()
                .delete(PROJECT_DELETE_TAG, createdTagId).then().body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).extract().path("id");
        final int tagCountAfterDelete = getAllTagsHelper().size();
        assertEquals(createdTagId, new Integer(deletedTagId));
        assertTrue("tag count should change by 1 after deleting", tagCountBeforeDelete == tagCountAfterDelete + 1);
    }

    /**
     * test case for GET /projects/{projectID}/following_discussions
     */
    
    @Test
    public void testProject_FollowingDiscussion() {
        ServiceLogger.log(logTag, "starting testProject_FollowingDiscussion");
        List<IndividualDiscussion>  results = given().header("Content-type", "application/json").header("AJP_eppn", "testUser").expect().statusCode(OK.value()).when()
                .get("/projects/2/following_discussions").as(List.class);
     
    }

    /**
     * PATCH /projects/{projectId}
     */
    @Test
    public void testProjectPatch() {
        ServiceLogger.log(logTag, "starting testProjectPatch");

        this.createdId = TestProjectUtil.createProject(userEPPN);
        if (this.createdId != null) {
            final JSONObject json = new JSONObject();
            json.put("title", "test project title update");
            json.put("description", "test project description update");
            json.put("dueDate", 0);

            final int updatedId = given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN)
                    .body(json.toString()).expect().statusCode(200).when()
                    .patch(PROJECT_UPDATE_RESOURCE, this.createdId).then()
                    .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).extract().path("id");

            assertTrue("Updated project is the one identified in URL param", updatedId == this.createdId);
        }
    }

}
