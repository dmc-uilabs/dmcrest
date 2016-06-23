package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.dmc.services.tasks.Task;
import org.dmc.services.tasks.TaskProject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.ValidatableResponse;

//@Ignore
public class TaskIT extends BaseIT {
    private static final String TASKS_BASE = "/tasks";
    private static final String CREATE_TASKS = "/tasks/create";
    private static final String GET_TASKS_WITH_ORDER_SORT_STATUS = "/tasks?_order=ASC&_sort=dueDate&status_ne=Completed";

    private static final String ID_SCHEMA = "Schemas/idSchema.json";
    private static final String TASK_SCHEMA = "Schemas/taskSchema.json";
    private static final String TASKLIST_SCHEMA = "Schemas/taskListSchema.json";

    private String taskId = "1";

    @Test
    public void testTaskCreateAndGet() {
        Task task = createTaskJsonSample("testTaskCreateAndGet");
        Integer id = given().header("Content-type", APPLICATION_JSON_VALUE).body(task).expect().statusCode(OK.value())
                .when().post(CREATE_TASKS).then().body(matchesJsonSchemaInClasspath(ID_SCHEMA))
                .extract().path("id");

        String newGetRequest = TASKS_BASE + "/" + id.toString();

        // let's query the newly created task and make sure we get it
        Task retrievedTask =
            given().header("Content-type", APPLICATION_JSON_VALUE).expect().statusCode(OK.value()).when().get(newGetRequest).then()
                .log().all().body(matchesJsonSchemaInClasspath(TASK_SCHEMA)).extract().as(Task.class);
        assertEquals(id.toString(), retrievedTask.getId());
    }

    // WARNING: this test is ok as long as our test db has task with id = 1
    @Test
    public void testTask1() {
        given().header("Content-type", APPLICATION_JSON_VALUE).expect().statusCode(OK.value()).when().get(TASKS_BASE + "/1").then().log()
                .all().body(matchesJsonSchemaInClasspath(TASK_SCHEMA));
    }

    @Test
    public void testTaskList() {
        given().header("Content-type", APPLICATION_JSON_VALUE).expect().statusCode(OK.value()).when().get(TASKS_BASE).then()
                .body(matchesJsonSchemaInClasspath(TASKLIST_SCHEMA));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testTaskListWithOrderSortAndStatus() {
        ArrayList<Task> taskList =
            given().header("Content-type", APPLICATION_JSON_VALUE)
            .expect().statusCode(OK.value())
            .when().get(GET_TASKS_WITH_ORDER_SORT_STATUS)
            .as(ArrayList.class);
        assertNotNull(taskList);
    }

    @Test
    public void testTaskCreate() {

        Task task = createTaskJsonSample("testTaskCreate");

        given().header("Content-type", APPLICATION_JSON_VALUE)
               .header("AJP_eppn", userEPPN)
               .body(task)
       .expect().statusCode(OK.value())
       .when().post("/tasks/create")
       .then().body(matchesJsonSchemaInClasspath(ID_SCHEMA));

    }

    private Task createTaskJsonSample(String testDescription) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String unique = format.format(date);

        final String taskId = "not_set_yet";
        final String title  = "sample for test " + testDescription + " from junit on " + unique;
        final String additionalDetails = "description for sample test  " + testDescription + " from junit on " + unique;
        final int priority = 0;
        final long dueDate = 0L;
        final String reporter = "bamboo tester"; // a user ID in users table
        final String reporterId = "111"; // a user ID in users table
        final String assignee = "should be 103's name"; // from group table
        final String assigneeId = "103"; // from group table
        final String status = "Open";

        final Integer projectId = 1; // from group table and project_group_list, 1
                                        // is available in both
        final TaskProject taskProject = new TaskProject(projectId, "project title");
        Task json = new Task(taskId, title, taskProject, assignee, assigneeId,
            reporter, reporterId, dueDate,
            additionalDetails, status, priority);
        return json;
    }

    /**
     * test case for DELETE /tasks/{taskID}
     */
    @Test
    public void testDelete_FollowDiscussions() {
        given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).expect()
                .statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().delete(TASKS_BASE + "/" + taskId);
    }
}
