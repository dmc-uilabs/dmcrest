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
import org.dmc.services.tasks.TaskToCreate;
import org.dmc.services.tasks.TaskProject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.http.HttpStatus;

import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.ValidatableResponse;

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
        TaskToCreate task = createTaskJsonSample("testTaskCreateAndGet");
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

    @Test
    public void testTaskCreateAndGetWithNoAssignee() {
        TaskToCreate task = createTaskJsonSample("testTaskCreateAndGetWithNoAssignee");
        task.setAssignee(null);
        task.setAssigneeId(null);
        Integer id = given().header("Content-type", APPLICATION_JSON_VALUE).body(task).expect().statusCode(OK.value())
                .when().post(CREATE_TASKS).then().body(matchesJsonSchemaInClasspath(ID_SCHEMA))
                .extract().path("id");

        String newGetRequest = TASKS_BASE + "/" + id.toString();

        // let's query the newly created task and make sure we get it
        Task retrievedTask =
            given().header("Content-type", APPLICATION_JSON_VALUE).expect().statusCode(OK.value()).when().get(newGetRequest).then()
                .log().all().body(matchesJsonSchemaInClasspath(TASK_SCHEMA)).extract().as(Task.class);
        // because that should get set when created
        assertNotNull(retrievedTask.getId());
        assertNull(retrievedTask.getAssignee());
        assertNull(retrievedTask.getAssigneeId());
    }

    @Test
    public void testTaskCreateAndGetWithRealisticDate() {
        TaskToCreate task = createTaskJsonSampleWithRealisticDate("testTaskCreateAndGetWithNoId");
        task.setAssignee(null);
        task.setAssigneeId(null);
        Integer id = given().header("Content-type", APPLICATION_JSON_VALUE).body(task).expect().statusCode(OK.value())
                .when().post(CREATE_TASKS).then().body(matchesJsonSchemaInClasspath(ID_SCHEMA))
                .extract().path("id");

        String newGetRequest = TASKS_BASE + "/" + id.toString();

        // let's query the newly created task and make sure we get it
        Task retrievedTask =
            given().header("Content-type", APPLICATION_JSON_VALUE).expect().statusCode(OK.value()).when().get(newGetRequest).then()
                .log().all().body(matchesJsonSchemaInClasspath(TASK_SCHEMA)).extract().as(Task.class);
        assertEquals(id.toString(), retrievedTask.getId());
        assertNull(retrievedTask.getAssignee());
        assertNull(retrievedTask.getAssigneeId());
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

        TaskToCreate task = createTaskJsonSample("testTaskCreate");

        given().header("Content-type", APPLICATION_JSON_VALUE)
               .header("AJP_eppn", userEPPN)
               .body(task)
       .expect().statusCode(OK.value())
       .when().post("/tasks/create")
       .then().body(matchesJsonSchemaInClasspath(ID_SCHEMA));

    }

    private TaskToCreate createTaskJsonSample(String testDescription) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String unique = format.format(date);

        final String title  = "sample for test " + testDescription + " from junit on " + unique;
        final String additionalDetails = "description for sample test  " + testDescription + " from junit on " + unique;
        final int priority = 0;
        final long dueDate = 0L;
        final String reporter = "testUser"; // a user ID in users table
        final String reporterId = "111"; // a user ID in users table
        final String assignee = "berlier"; // from group table
        final String assigneeId = "103"; // from group table
        final String status = "Open";

        final String projectId = "1"; // from group table and project_group_list, 1
                                        // is available in both
        TaskToCreate json = new TaskToCreate(title, projectId, assignee, assigneeId,
            reporter, reporterId, dueDate,
            additionalDetails, status, priority);
        return json;
    }

    private TaskToCreate createTaskJsonSampleWithRealisticDate(String testDescription) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String unique = format.format(date);

        final String title  = "sample for test " + testDescription + " from junit on " + unique;
        final String additionalDetails = "description for sample test  " + testDescription + " from junit on " + unique;
        final int priority = 4;
        final long dueDate = 1468468800000L;
        final String reporter = "testUser"; // a user ID in users table
        final String reporterId = "111"; // a user ID in users table
        final String assignee = "berlier"; // from group table
        final String assigneeId = "103"; // from group table
        final String status = "Open";

        final String projectId = "1"; // from group table and project_group_list, 1
                                        // is available in both
        TaskToCreate json = new TaskToCreate(title, projectId, assignee, assigneeId,
            reporter, reporterId, dueDate,
            additionalDetails, status, priority);
        return json;
    }

    /**
     * test case for DELETE /tasks/{taskID}
     */
    @Test
    public void testDelete_FollowDiscussions() {
		
		TaskToCreate task = createTaskJsonSampleWithRealisticDate("testTaskCreateAndGet");
		
		// create a new task
		Integer id =
		given().
			header("Content-type", APPLICATION_JSON_VALUE).
			header("AJP_eppn", "berlier").
			body(task).
		expect().
			statusCode(OK.value()).
		when().
			post(CREATE_TASKS).
		then().
			body(matchesJsonSchemaInClasspath(ID_SCHEMA)).
			extract().path("id");
		
		String newGetRequest = TASKS_BASE + "/" + id.toString();
		
		// let's query the newly created task and make sure we get it
		Task retrievedTask =
		given().
			header("Content-type", APPLICATION_JSON_VALUE).
			header("AJP_eppn", "berlier").
		expect().
			statusCode(OK.value()).
		when().
			get(newGetRequest).
		then().
			log().all().body(matchesJsonSchemaInClasspath(TASK_SCHEMA)).
			extract().as(Task.class);
				
		assertTrue("Created and retrieved tasks are not equal", id.toString().equals(retrievedTask.getId()));
		
		// delete newly created task
        given().
			header("Content-type", APPLICATION_JSON_VALUE).
			header("AJP_eppn", "berlier").
		expect().
			statusCode(OK.value()).
		when().
			delete(TASKS_BASE + "/" + id.toString());
		
		// lookup deleted tasks
		given().
			header("Content-type", APPLICATION_JSON_VALUE).
			header("AJP_eppn", "berlier").
		expect().
			statusCode(INTERNAL_SERVER_ERROR.value()).
		when().
			get(newGetRequest);
    }
	
	@Test
	public void testPatchTask() {
		TaskToCreate task = createTaskJsonSampleWithRealisticDate("testTaskCreateAndGet");
		Integer id =
		given().
			header("Content-type", APPLICATION_JSON_VALUE).
			body(task).
		expect().
			statusCode(OK.value()).
		when().
			post(CREATE_TASKS).
		then().
			body(matchesJsonSchemaInClasspath(ID_SCHEMA)).
			extract().path("id");
		
		String newGetRequest = TASKS_BASE + "/" + id.toString();
		
		// let's query the newly created task and make sure we get it
		Task retrievedTask =
		given().
			header("Content-type", APPLICATION_JSON_VALUE).
		expect().
			statusCode(OK.value()).
		when().
			get(newGetRequest).
		then().
			log().all().body(matchesJsonSchemaInClasspath(TASK_SCHEMA)).
			extract().as(Task.class);
		
		assertEquals(id.toString(), retrievedTask.getId());

		Task patchedTask =
		given().
			header("Content-type", APPLICATION_JSON_VALUE).
			body(retrievedTask).
		expect().
			statusCode(OK.value()).
		when().
			patch(newGetRequest).
		then().
			log().all().body(matchesJsonSchemaInClasspath(TASK_SCHEMA)).
			extract().as(Task.class);
	}
}
